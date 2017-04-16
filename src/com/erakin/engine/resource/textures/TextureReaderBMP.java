package com.erakin.engine.resource.textures;

import static com.erakin.engine.resource.textures.pixel.PixelFormat.FORMAT_LUMINANCE;
import static com.erakin.engine.resource.textures.pixel.PixelFormat.FORMAT_RGB;
import static com.erakin.engine.resource.textures.pixel.PixelFormat.FORMAT_RGBA;

import java.nio.ByteBuffer;

import org.diverproject.util.ObjectDescription;

import com.erakin.common.buffer.Buffer;
import com.erakin.engine.resource.textures.pixel.PixelFormat;
import com.erakin.file.images.BMP.BMPUtil;

/**
 * <h1>Leitor de Textura em BMP</h1>
 *
 * <p>Esse leitor permite ler arquivos BMP e a partir da leitura criar um buffer apenas com os bytes dos pixels.
 * O formato padr�o para armazenamento desses dados � em RGBA como a pr�pria biblioteca LWJGL/OpenGL trabalha.
 * Todos os dados ser�o lidos se estiver no tipo BMP padr�o que possui a assinatura BM logo no inicio do arquivo.</p>
 *
 * <p>Utiliza procedimentos da classe BMP para que seja poss�vel tratar os bytes lidos adequadamente em pixels.
 * Esse tratamento � feito somente ap�s efetuar a leitura dos dados necess�rios do arquivo, como cabe�alhos e paletas.
 * Assim, ser� poss�vel fazer o tratamento adequado conforme os dados dos pixels foram armazenados no arquivo.</p>
 *
 * @see BMPUtil
 * @see Buffer
 * @see TextureReaderDefault
 *
 * @author Andrew Mello
 */

public class TextureReaderBMP extends TextureReaderDefault
{
	/**
	 * C�digo de compress�o RGB.
	 */
	public static final int COMPRESSION_RGB = 0;

	/**
	 * C�digo de compress�o RLE8.
	 */
	public static final int COMPRESSION_RLE8 = 1;

	/**
	 * C�digo de compress�o RLE4.
	 */
	public static final int COMPRESSION_RLE4 = 2;

	/**
	 * Quantidade de bits para imagens BMP com uso de paleta da cores.
	 */
	public static final int BITCOUNT_PALETTE = 8;

	/**
	 * Quantidade de bits para imagens BMP com uso cores RGB.
	 */
	public static final int BITCOUNT_RGB = 24;

	/**
	 * Quantidade de bits para imagens BMP com uso cores RGBA.
	 */
	public static final int BITCOUNT_RGBA = 32;


	/**
	 * Informa��es do cabe�alho BMP.
	 */
	private Header header;

	/**
	 * ~Informa��es do cabe�alho da imagem.
	 */
	private InfoHeader infoHeader;

	/**
	 * Dados diretos dos bytes armazenados.
	 */
	private RasterData rasterData;

	/**
	 * Paleta de cores que ser� usada se necess�rio.
	 */
	private Palette palette;

	/**
	 * Cria um novo leitor para texturas armazenadas em imagens no formato Bitmap (BMP).
	 * Determina o seu padr�o de formato dos pixels como RGB, o mais comuns em imagens BMP.
	 * Inicializa alguns objetos que ir�o representar algumas partes da estrutura do arquivo.
	 */

	public TextureReaderBMP()
	{
		super(FORMAT_RGB);

		header = new Header();
		infoHeader = new InfoHeader();
		rasterData = new RasterData();
		palette = new Palette();
	}

	@Override
	protected int getDepth()
	{
		return getOutputFormat() != null ? getOutputFormat().BYTES * 8 : infoHeader.bitcount;
	}

	@Override
	protected int getWidth()
	{
		return infoHeader.width;
	}

	@Override
	protected int getHeight()
	{
		return infoHeader.height;
	}

	@Override
	protected void parseBuffer(Buffer buffer)
	{
		buffer.invert(true);

		header = parseHeader(buffer);
		infoHeader = parseInfoHeader(buffer);
		palette = parsePalette(buffer);
		rasterData = parseRasterData(buffer);

		if (buffer.space() != 0)
			throw new TextureRuntimeException("nem todos os dados foram lidos");

		buffer.close();
	}

	/**
	 * Analisa o inicio do arquivo para fazer a leitura das informa��es do arquivo BMP.
	 * @param buffer refer�ncia do buffer usado na leitura do arquivo em quest�o.
	 * @return objeto contendo as informa��es do cabe�alho BMP lido do buffer.
	 */

	private Header parseHeader(Buffer buffer)
	{
		header.signature = new String(new byte[] { buffer.read(), buffer.read() });
		header.fileSize = buffer.getInt();
		header.reserved1 = buffer.read(2);
		header.reserved2 = buffer.read(2);
		header.dataOffset = buffer.getInt();

		if (!header.signature.equals("BM"))
			throw new TextureRuntimeException("aceito apenas bmp windows");

		if (header.fileSize != buffer.space() + 14)
			throw new TextureRuntimeException("arquivo corrompido, faltam dados");

		return header;
	}

	/**
	 * Analisa a segunda parte do arquivo que representa as informa��es da imagem.
	 * @param buffer refer�ncia do buffer usado na leitura do arquivo em quest�o.
	 * @return objeto contendo as informa��es do cabe�alho da imagem lida do buffer.
	 */

	private InfoHeader parseInfoHeader(Buffer buffer)
	{
		infoHeader.size = buffer.getInt();

		if (infoHeader.size != 40)
			throw new TextureRuntimeException("cabe�alho com formato n�o aceito");

		infoHeader.width = buffer.getInt();
		infoHeader.height = buffer.getInt();

		if (infoHeader.width < 1 || infoHeader.height < 1)
			throw new TextureRuntimeException("tamanho da imagem inv�lida");

		infoHeader.planes = buffer.getShort();
		infoHeader.bitcount = buffer.getShort();

		if (!isBitcount(infoHeader.bitcount))
			throw new TextureRuntimeException("bitcount n�o aceito");

		infoHeader.compression = buffer.getInt();

		if (!isCompression(infoHeader.compression))
			throw new TextureRuntimeException("tipo de compress�o inv�lida");

		switch (infoHeader.bitcount)
		{
			case 8:
				setInputFormat(infoHeader.compression == COMPRESSION_RLE8 ? FORMAT_LUMINANCE : FORMAT_RGB);
				break;

			case 24:
				setInputFormat(FORMAT_RGB);
				break;

			case 32:
				setInputFormat(FORMAT_RGBA);
				break;
		}

		infoHeader.imageSize = buffer.getInt();
		infoHeader.xPixelsPerMeter = buffer.getFloat();
		infoHeader.yPixelsPerMeter = buffer.getFloat();
		infoHeader.colorUsed = buffer.getInt();
		infoHeader.importantColors = buffer.getInt();

		return infoHeader;
	}

	/**
	 * Verifica se um determinado tipo de bitcount existe para essa conversor de dados BMP.
	 * @param bitcount quantidade de bits usados para cada pixel para verificar a validez.
	 * @return true se for v�lido ou false caso n�o seja uma quantidade de bits inv�lida.
	 */

	private boolean isBitcount(int bitcount)
	{
		return	bitcount != BITCOUNT_PALETTE ||
				bitcount != BITCOUNT_RGB ||
				bitcount != BITCOUNT_RGBA;
	}

	/**
	 * Verifica se um determinado tipo de compress�o existe para essa conversor de dados BMP.
	 * @param compression c�digo do tipo de convers�o do qual dever� ser analisado.
	 * @return true se for v�lido ou false caso n�o seja uma compress�o v�lida.
	 */

	private boolean isCompression(int compression)
	{
		return	compression != COMPRESSION_RGB ||
				compression != COMPRESSION_RLE4 ||
				compression != COMPRESSION_RLE8;
	}

	/**
	 * Analisa a quarta parte do arquivo que representa as cores dos pixels da imagem.
	 * @param buffer refer�ncia do buffer usado na leitura do arquivo em quest�o.
	 * @return objeto contendo as informa��es das cores de cada pixel.
	 */

	private RasterData parseRasterData(Buffer buffer)
	{
		if (buffer.space() < infoHeader.imageSize)
			throw new TextureRuntimeException("n�o h� dados para o raster");

		RasterData rasterData = new RasterData();
		rasterData.bytes = buffer.read(infoHeader.imageSize);

		return rasterData;
	}

	/**
	 * Analisa a quinta e �ltima parte do arquivo que representa a paleta de cores.
	 * A pela s� � usada quando a quantidade de bytes por pixel for igual a um.
	 * Onde cada byte representa um �ndice da paleta que ter� os dados da cor.
	 * @param buffer refer�ncia do buffer usado na leitura do arquivo em quest�o.
	 * @return objeto contendo as informa��es da paleta de cores lida do buffer.
	 */

	private Palette parsePalette(Buffer buffer)
	{
		if (buffer.space() < 1024)
			throw new TextureRuntimeException("n�o h� dados para a paleta de cores");

		Palette palette = new Palette();
		palette.bytes = buffer.read(1024);

		return palette;
	}

	@Override
	protected void parsePixels(ByteBuffer buffer, PixelFormat output)
	{
		int width = infoHeader.width;
		int height = infoHeader.height;

		if (getInputFormat() == FORMAT_RGB)
		{
			if (infoHeader.bitcount == BITCOUNT_PALETTE)
				BMPUtil.decodePalette(buffer, width, height, output, rasterData.bytes, palette.bytes);
			else
				BMPUtil.decodeRGB(buffer, width, height, output, rasterData.bytes);
		}

		else if (getInputFormat() == FORMAT_RGBA)
			buffer.put(rasterData.bytes);

		else if (getInputFormat() == FORMAT_LUMINANCE)
			BMPUtil.decodeGray(buffer, width, height, output, rasterData.bytes);

		else
			throw new TextureRuntimeException("compress�o '%d' n�o aceita", infoHeader.compression);
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("signature", header == null ? null : header.signature);
		description.append("offset", header == null ? 0 : header.dataOffset);
		description.append("width", infoHeader == null ? 0 : infoHeader.width);
		description.append("height", infoHeader == null ? 0 : infoHeader.height);
		description.append("size", infoHeader == null ? 0 : infoHeader.imageSize);
		description.append("bitcount", infoHeader == null ? 0 : infoHeader.bitcount);
		description.append("compression", infoHeader == null ? 0 : infoHeader.compression);
		description.append("paletta", palette == null ? 0 : palette.bytes.length);

		return description.toString();
	}

	/**
	 * <h1>Cabe�alho de Arquivo Bitmap</h1>
	 *
	 * <p>Esse bloco de bytes � onde inicia o arquivo e � usado para identificar o arquivo.
	 * Esse primeiro bloco � para garantir que o arquivo � realmente um BMP e n�o est� danificado.</p>
	 *
	 * @author Andrew Mello
	 */

	@SuppressWarnings("unused")
	private class Header
	{
		/**
		 * Usado para identificar o BMP e arquivos DIB s�o 0x42 0x4D em hexadecimal, mesmo que BM em ASCII.
		 */
		public String signature;

		/**
		 * Tamanho do arquivo BMP em bytes.
		 */
		public int fileSize;

		/**
		 * Reservado, valor atual depende da aplica��o que criou a imagem.
		 */
		public byte[] reserved1;

		/**
		 * Reservado, valor atual depende da aplica��o que criou a imagem.
		 */
		public byte[] reserved2;

		/**
		 * Endere�o de inicializa��o, dos bytes onde os dados da imagem bitmap (vetor de pixels) podem ser encontrados.
		 */
		public int dataOffset;

		@Override
		public String toString()
		{
			ObjectDescription description = new ObjectDescription(getClass());

			description.append("signature", signature);
			description.append("size", fileSize);
			description.append("offset", dataOffset);

			return description.toString();
		}
	}

	/**
	 * <h1>Cabe�alho de Informa��es Bitmap</h1>
	 *
	 * <p>Esse cabe�alho � especifico sobre as informa��es diretas da imagem armazenada no bitmap.
	 * Aqui ser� definido qual o tamanho deste cabe�alho, tamanho da largura e altura da imagem.
	 * Como ainda tamb�m algumas especifica��es que ir�o indicar como os dados s�o armazenados.</p>
	 *
	 * @author Andrew Mello
	 */

	@SuppressWarnings("unused")
	private class InfoHeader
	{
		/**
		 * N�mero de bytes no cabe�alho.
		 */
		public int size;

		/**
		 * Largura do bitmap em pixels.
		 */
		public int width;

		/**
		 * Altura do bitmap em pixels.
		 */
		public int height;

		/**
		 * N�mero de planos de cores utilizadas.
		 */
		public short planes;

		/**
		 * N�mero de bits por pixel.
		 */
		public short bitcount;

		/**
		 * Tipo de compress�o usada para armazenar os pixels.
		 */
		public int compression;

		/**
		 * Tamanho em bytes dos dados dos pixels incluindo espa�amento.
		 */
		public int imageSize;

		/**
		 * Tamanho da resolu��o da imagem.
		 */
		public float xPixelsPerMeter;

		/**
		 * Tamanho da resolu��o da imagem.
		 */
		public float yPixelsPerMeter;

		/**
		 * N�mero de cores na paleta.
		 */
		public int colorUsed;

		/**
		 * N�mero de cores importantes.
		 */
		public int importantColors;

		@Override
		public String toString()
		{
			ObjectDescription description = new ObjectDescription(getClass());

			description.append("size", size);
			description.append("width", width);
			description.append("height", height);
			description.append("bitcount", bitcount);
			description.append("compression", compression);
			description.append("size", imageSize);
			description.append("used", colorUsed);
			description.append("important", importantColors);

			return description.toString();
		}
	}

	/**
	 * <h1>Armazenador de Pixels</h1>
	 *
	 * <p>Os bits que ir�o representar os pixels do bitmap estar�o embalados em linhas consecutivas.
	 * Para cada linha ser� considerado que ao final desta exista um espa�amento entre ela e a pr�xima.
	 * Cada pixel tamb�m dever� estar armazenado em 4 bytes para RGBA ou menos dependendo da compress�o.</p>
	 *
	 * @author Andrew Mello
	 */

	private class RasterData
	{
		/**
		 * Vetor contendo os dados de todos os pixels da imagem.
		 */
		public byte[] bytes;

		@Override
		public String toString()
		{
			ObjectDescription description = new ObjectDescription(getClass());

			description.append("bytes", bytes == null ? 0 : bytes.length);

			return description.toString();
		}
	}

	/**
	 * <h1></h1>
	 *
	 * <p></p>
	 *
	 * @author Andrew Mello
	 */

	private class Palette
	{
		/**
		 * 
		 */
		public byte[] bytes;

		@Override
		public String toString()
		{
			ObjectDescription description = new ObjectDescription(getClass());

			description.append("initate", bytes != null);

			return description.toString();
		}
	}
}
