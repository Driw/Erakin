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
 * O formato padrão para armazenamento desses dados é em RGBA como a própria biblioteca LWJGL/OpenGL trabalha.
 * Todos os dados serão lidos se estiver no tipo BMP padrão que possui a assinatura BM logo no inicio do arquivo.</p>
 *
 * <p>Utiliza procedimentos da classe BMP para que seja possível tratar os bytes lidos adequadamente em pixels.
 * Esse tratamento é feito somente após efetuar a leitura dos dados necessários do arquivo, como cabeçalhos e paletas.
 * Assim, será possível fazer o tratamento adequado conforme os dados dos pixels foram armazenados no arquivo.</p>
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
	 * Código de compressão RGB.
	 */
	public static final int COMPRESSION_RGB = 0;

	/**
	 * Código de compressão RLE8.
	 */
	public static final int COMPRESSION_RLE8 = 1;

	/**
	 * Código de compressão RLE4.
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
	 * Informações do cabeçalho BMP.
	 */
	private Header header;

	/**
	 * ~Informações do cabeçalho da imagem.
	 */
	private InfoHeader infoHeader;

	/**
	 * Dados diretos dos bytes armazenados.
	 */
	private RasterData rasterData;

	/**
	 * Paleta de cores que será usada se necessário.
	 */
	private Palette palette;

	/**
	 * Cria um novo leitor para texturas armazenadas em imagens no formato Bitmap (BMP).
	 * Determina o seu padrão de formato dos pixels como RGB, o mais comuns em imagens BMP.
	 * Inicializa alguns objetos que irão representar algumas partes da estrutura do arquivo.
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
	 * Analisa o inicio do arquivo para fazer a leitura das informações do arquivo BMP.
	 * @param buffer referência do buffer usado na leitura do arquivo em questão.
	 * @return objeto contendo as informações do cabeçalho BMP lido do buffer.
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
	 * Analisa a segunda parte do arquivo que representa as informações da imagem.
	 * @param buffer referência do buffer usado na leitura do arquivo em questão.
	 * @return objeto contendo as informações do cabeçalho da imagem lida do buffer.
	 */

	private InfoHeader parseInfoHeader(Buffer buffer)
	{
		infoHeader.size = buffer.getInt();

		if (infoHeader.size != 40)
			throw new TextureRuntimeException("cabeçalho com formato não aceito");

		infoHeader.width = buffer.getInt();
		infoHeader.height = buffer.getInt();

		if (infoHeader.width < 1 || infoHeader.height < 1)
			throw new TextureRuntimeException("tamanho da imagem inválida");

		infoHeader.planes = buffer.getShort();
		infoHeader.bitcount = buffer.getShort();

		if (!isBitcount(infoHeader.bitcount))
			throw new TextureRuntimeException("bitcount não aceito");

		infoHeader.compression = buffer.getInt();

		if (!isCompression(infoHeader.compression))
			throw new TextureRuntimeException("tipo de compressão inválida");

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
	 * @return true se for válido ou false caso não seja uma quantidade de bits inválida.
	 */

	private boolean isBitcount(int bitcount)
	{
		return	bitcount != BITCOUNT_PALETTE ||
				bitcount != BITCOUNT_RGB ||
				bitcount != BITCOUNT_RGBA;
	}

	/**
	 * Verifica se um determinado tipo de compressão existe para essa conversor de dados BMP.
	 * @param compression código do tipo de conversão do qual deverá ser analisado.
	 * @return true se for válido ou false caso não seja uma compressão válida.
	 */

	private boolean isCompression(int compression)
	{
		return	compression != COMPRESSION_RGB ||
				compression != COMPRESSION_RLE4 ||
				compression != COMPRESSION_RLE8;
	}

	/**
	 * Analisa a quarta parte do arquivo que representa as cores dos pixels da imagem.
	 * @param buffer referência do buffer usado na leitura do arquivo em questão.
	 * @return objeto contendo as informações das cores de cada pixel.
	 */

	private RasterData parseRasterData(Buffer buffer)
	{
		if (buffer.space() < infoHeader.imageSize)
			throw new TextureRuntimeException("não há dados para o raster");

		RasterData rasterData = new RasterData();
		rasterData.bytes = buffer.read(infoHeader.imageSize);

		return rasterData;
	}

	/**
	 * Analisa a quinta e última parte do arquivo que representa a paleta de cores.
	 * A pela só é usada quando a quantidade de bytes por pixel for igual a um.
	 * Onde cada byte representa um índice da paleta que terá os dados da cor.
	 * @param buffer referência do buffer usado na leitura do arquivo em questão.
	 * @return objeto contendo as informações da paleta de cores lida do buffer.
	 */

	private Palette parsePalette(Buffer buffer)
	{
		if (buffer.space() < 1024)
			throw new TextureRuntimeException("não há dados para a paleta de cores");

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
			throw new TextureRuntimeException("compressão '%d' não aceita", infoHeader.compression);
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
	 * <h1>Cabeçalho de Arquivo Bitmap</h1>
	 *
	 * <p>Esse bloco de bytes é onde inicia o arquivo e é usado para identificar o arquivo.
	 * Esse primeiro bloco é para garantir que o arquivo é realmente um BMP e não está danificado.</p>
	 *
	 * @author Andrew Mello
	 */

	@SuppressWarnings("unused")
	private class Header
	{
		/**
		 * Usado para identificar o BMP e arquivos DIB são 0x42 0x4D em hexadecimal, mesmo que BM em ASCII.
		 */
		public String signature;

		/**
		 * Tamanho do arquivo BMP em bytes.
		 */
		public int fileSize;

		/**
		 * Reservado, valor atual depende da aplicação que criou a imagem.
		 */
		public byte[] reserved1;

		/**
		 * Reservado, valor atual depende da aplicação que criou a imagem.
		 */
		public byte[] reserved2;

		/**
		 * Endereço de inicialização, dos bytes onde os dados da imagem bitmap (vetor de pixels) podem ser encontrados.
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
	 * <h1>Cabeçalho de Informações Bitmap</h1>
	 *
	 * <p>Esse cabeçalho é especifico sobre as informações diretas da imagem armazenada no bitmap.
	 * Aqui será definido qual o tamanho deste cabeçalho, tamanho da largura e altura da imagem.
	 * Como ainda também algumas especificações que irão indicar como os dados são armazenados.</p>
	 *
	 * @author Andrew Mello
	 */

	@SuppressWarnings("unused")
	private class InfoHeader
	{
		/**
		 * Número de bytes no cabeçalho.
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
		 * Número de planos de cores utilizadas.
		 */
		public short planes;

		/**
		 * Número de bits por pixel.
		 */
		public short bitcount;

		/**
		 * Tipo de compressão usada para armazenar os pixels.
		 */
		public int compression;

		/**
		 * Tamanho em bytes dos dados dos pixels incluindo espaçamento.
		 */
		public int imageSize;

		/**
		 * Tamanho da resolução da imagem.
		 */
		public float xPixelsPerMeter;

		/**
		 * Tamanho da resolução da imagem.
		 */
		public float yPixelsPerMeter;

		/**
		 * Número de cores na paleta.
		 */
		public int colorUsed;

		/**
		 * Número de cores importantes.
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
	 * <p>Os bits que irão representar os pixels do bitmap estarão embalados em linhas consecutivas.
	 * Para cada linha será considerado que ao final desta exista um espaçamento entre ela e a próxima.
	 * Cada pixel também deverá estar armazenado em 4 bytes para RGBA ou menos dependendo da compressão.</p>
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
