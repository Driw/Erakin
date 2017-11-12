package com.erakin.textures.png;

import static com.erakin.api.resources.texture.PixelFormat.FORMAT_RGBA;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.lang.ByteUtil;
import org.diverproject.util.lang.IntUtil;

import com.erakin.api.buffer.Buffer;
import com.erakin.api.resources.texture.PixelFormat;
import com.erakin.api.resources.texture.TextureReaderDefault;
import com.erakin.api.resources.texture.TextureRuntimeException;

/**
 * <h1>Leitor de Textura em PNG</h1>
 *
 * <p>Esse leitor permite ler arquivos PNG e a partir da leitura criar um buffer apenas com os bytes dos pixels.
 * O formato padrão para armazenamento desses dados é em RGBA como a própria biblioteca LWJGL/OpenGL trabalha.
 * Serão lidos apenas os dados que realmente forem necessários para a conversão, ou seja alguns campos são ignorados.</p>
 *
 * <p>Utiliza procedimentos da classe PNG para que seja possível tratar os bytes lidos adequadamente em pixels.
 * Esse tratamento é feito somente após efetuar a leitura dos dados necessários do arquivo, como cabeçalhos e paletas.
 * Assim, será possível fazer o tratamento adequado conforme os dados dos pixels foram armazenados no arquivo.</p>
 *
 * @see PNGUtil
 * @see Buffer
 * @see TextureReaderDefault
 *
 * @author Andrew Mello
 */

public class TextureReaderPNG extends TextureReaderDefault
{
	/**
	 * Extensão dos arquivos que utilizarão este reader.
	 */
	public static final String FILE_EXTENSION = "png";

	/**
	 * Assinatura PNG garante que o arquivo independente da extensão seja PNG.
	 */
	private static final byte SIGNATURE[] = new byte[] { (byte) 137, 80, 78, 71, 13, 10, 26, 10 };


	/**
	 * Código para identificação de uma chunk Image Header (IHDR).
	 */
	private static final int IHDR = 0x49484452;

	/**
	 * Código para identificação de uma chunk Palette (PLTE).
	 */
	private static final int PLTE = 0x504C5445;

	/**
	 * Código para identificação de uma chunk Transparency (tRNS).
	 */
	private static final int tRNS = 0x74524E53;

	/**
	 * Código para identificação de uma chunk Image Data (IDAT).
	 */
	private static final int IDAT = 0x49444154;


	/**
	 * Código para cores do tipo escala cinza sem propriedade alpha.
	 */
	private static final byte COLOR_GREYSCALE = 0;

	/**
	 * Código para cores do tipo simples: vermelho, verde e azul, sem propriedade alpha.
	 */
	private static final byte COLOR_TRUECOLOR = 2;

	/**
	 * Código para cores do tipo indexada: uso de paleta.
	 */
	private static final byte COLOR_INDEXED = 3;

	/**
	 * Código para cores do tipo escala cinza com propriedade alpha.
	 */
	private static final byte COLOR_GREYALPHA = 4;

	/**
	 * Código para cores do tipo simples: vermelho, verde e azul com propriedade alpha.
	 */
	private static final byte COLOR_TRUEALPHA = 6;


	/**
	 * CRC que deve garantir integridade nos dados de uma chunk.
	 */
	private final CRC32 crc;

	/**
	 * Quantos bits cada pixel deverá possuir.
	 */
	private int depth;

	/**
	 * Tamanho da largura da imagem em pixels.
	 */
	private int width;

	/**
	 * Tamanho da altura da imagem em pixels.
	 */
	private int height;

	/**
	 * Tipo de cor que será usada pelos pixels.
	 */
	private int colorType;

	/**
	 * Quantidades de bytes por pixel.
	 */
	private int bpp;

	/**
	 * Bytes respectivos a paleta de cores.
	 */
	private byte palette[];

	/**
	 * Bytes respectivos a paleta de cores indexadas.
	 */
	private byte paletteA[];

	/**
	 * Bytes respectivos a transparência.
	 */
	private byte transPixel[];


	/**
	 * Localização do ponteiro ao ler a última chunk.
	 */
	private int lastChunkOffset;

	/**
	 * Quantos bytes tem a chunk que será lida.
	 */
	private int lastChunkLength;

	/**
	 * Quantos bytes ainda podem ser lidos dos dados da chunk.
	 */
	private int lastChunkRemaing;

	/**
	 * Código de identificação da última chunk aberta.
	 */
	private int lastChunkCode;

	/**
	 * Buffer contendo os dados da última chunk aberta.
	 */
	private Buffer buffer;

	/**
	 * Constrói um novo decodificador de imagem iniciando a leitura dos dados.
	 */

	public TextureReaderPNG()
	{
		super(FORMAT_RGBA);

		this.crc = new CRC32();
	}

	@Override
	protected int getDepth()
	{
		return bpp * 8;
	}

	@Override
	protected int getWidth()
	{
		return width;
	}

	@Override
	protected int getHeight()
	{
		return height;
	}

	@Override
	protected void parseBuffer(Buffer buffer)
	{
		this.buffer = buffer;

		if (!checkSignature(buffer))
			throw new TextureRuntimeException("não é um arquivo PNG válido");

		openChunk(IHDR);
		readIHDR();
		closeChunk();

		searchIDAT: for (;;)
		{
			openChunk();

			switch (lastChunkCode)
			{
				case IDAT:				break searchIDAT;
				case PLTE:	readPLTE();	break;
				case tRNS:	readtRNS(); break;

				default:
					buffer.skip(lastChunkLength);
			}

			closeChunk();
		}

		if (colorType == COLOR_INDEXED && palette == null)
			throw new TextureRuntimeException("PLTE chunk não encontrada");
	}

	/**
	 * Chamado sempre que o buffer tiver terminado de ler uma chunk.
	 * Irá garantir que o CRC não seja verificado para chunks que foram puladas.
	 */

	private void afterReadBuffer()
	{
		lastChunkRemaing -= buffer.offset() - (lastChunkOffset + 8);
	}

	/**
	 * Deve verificar se um determinado buffer contem a assinatura PNG.
	 * A assinatura deve se encontrar no inicio do arquivo como também
	 * deverá possuir exatamente os mesmos bytes pré-definidos.
	 * @param buffer referência do buffer que verificará a assinatura.
	 * @return true se o buffer possuir a assinatura ou false caso contrário.
	 */

	private boolean checkSignature(Buffer buffer)
	{
		for (int i = 0; i < SIGNATURE.length; i++)
			if (buffer.read() != SIGNATURE[i])
				return false;

		return true;
	}

	/**
	 * Procedimento que deverá fazer a leitura do cabeçalho da imagem PNG.
	 * @throws TextureRuntimeException ocorre apenas quando algum dos dados lidos
	 * forem inválidos de acordo com os padrões definidos para essa chunk.
	 */

	private void readIHDR() throws TextureRuntimeException
	{
		buffer.save(lastChunkLength);

		width = buffer.getInt();
		height = buffer.getInt();
		depth = buffer.read();
		colorType = buffer.read();

		switch (colorType)
		{
			case COLOR_GREYSCALE:
				readColorType(1);
				break;

			case COLOR_GREYALPHA:
				readColorType(2);
				break;

			case COLOR_TRUECOLOR:
				readColorType(3);
				break;

			case COLOR_TRUEALPHA:
				readColorType(4);
				break;

			case COLOR_INDEXED:
				readColorTypeIndexed();
				break;

			default:
				throw new TextureRuntimeException("formato de cor não suportado (format: %d)", colorType);
		}

		if (buffer.read() != 0)
			throw new TextureRuntimeException("método de compressão não suportado");

		if (buffer.read() != 0)
			throw new TextureRuntimeException("método de filtragem não suportado");

		if (buffer.read() != 0)
			throw new TextureRuntimeException("método de entrelaçar não suportado");

		afterReadBuffer();
	}

	/**
	 * Procedimento que irá fazer a validação do tipo de cor utilizado.
	 * @param bytePerPixel quantidade de bytes por pixel necessários.
	 * @throws TextureRuntimeException apenas se depth for inválido para tal.
	 */

	private void readColorType(int bytePerPixel) throws TextureRuntimeException
	{
		if (depth != 8)
			throw new TextureRuntimeException("bit depth não suportado");

		bpp = bytePerPixel;		
	}

	/**
	 * Procedimento que irá fazer a validação quando for cor do tipo INDEXED.
	 * @throws TextureRuntimeException apenas se depth for inválido para tal.
	 */

	private void readColorTypeIndexed() throws TextureRuntimeException
	{
		switch (depth)
		{
			case 8:
			case 4:
			case 2:
			case 1:
				bpp = 1;
				return;
		}

		throw new TextureRuntimeException("bit depth não suportado");
	}

	/**
	 * Procedimento que deverá fazer a leitura do da paleta de cores.
	 * @throws TextureRuntimeException ocorre apenas quando algum dos dados lidos
	 * forem inválidos de acordo com os padrões definidos para essa chunk.
	 */

	private void readPLTE() throws TextureRuntimeException
	{
		buffer.save(lastChunkLength);

		if (lastChunkLength % 3 != 0)
			throw new TextureRuntimeException("PLTE corrompido");

		int entries = lastChunkLength / 3;

		if (!IntUtil.interval(entries, 1, 255))
			throw new TextureRuntimeException("PLT com tamanho inválido");

		palette = new byte[lastChunkLength];
		buffer.read(palette);

		afterReadBuffer();
	}

	/**
	 * Procedimento que deverá fazer a leitura da paleta de transparência.
	 * @throws TextureRuntimeException ocorre apenas quando algum dos dados lidos
	 * forem inválidos de acordo com os padrões definidos para essa chunk.
	 */

	private void readtRNS() throws TextureRuntimeException
	{
		buffer.save(lastChunkLength);

		switch (colorType)
		{
			case COLOR_GREYSCALE:
				transPixel = new byte[2];
				buffer.read(transPixel);
				break;

			case COLOR_TRUECOLOR:
				transPixel = new byte[6];
				buffer.read(transPixel);
				break;

			case COLOR_INDEXED:
				if (palette == null)
					throw new TextureRuntimeException("chunk tRNS sem chunk PLTE");
				paletteA = new byte[palette.length / 3];
				buffer.read(paletteA);
		}

		afterReadBuffer();
	}

	/**
	 * Deve verificar se o buffer contendo os dados da imagem pode prosseguir
	 * lendo uma nova chunk especificada de acordo com as informações abaixo.
	 * @param code código da chunk do qual deve ser a próxima a ser lida.
	 * @param lenght quantos bytes essa chunk deve possuir, menor que zero
	 * determina que o tamanho dela não será verificado se é correspondente.
	 * @return buffer referência do buffer contendo apenas os dados da chunk.
	 * @throws TextureRuntimeException ocorre por tamanho da chunk inválido, tipo
	 * da chunk inesperado ou ainda então por dados insuficientes.
	 */

	private void openChunk() throws TextureRuntimeException
	{
		buffer.save(8);

		lastChunkOffset = buffer.offset();
		lastChunkLength = buffer.getInt();
		lastChunkCode = buffer.getInt();
		lastChunkRemaing = lastChunkLength;

		crc.reset();
		crc.update(buffer.getSaved(), 4, 4);
	}

	/**
	 * Deve verificar se o buffer contendo os dados da imagem pode prosseguir
	 * lendo uma nova chunk especificada de acordo com as informações abaixo.
	 * @param code código da chunk do qual deve ser a próxima a ser lida.
	 * @param lenght quantos bytes essa chunk deve possuir, menor que zero
	 * determina que o tamanho dela não será verificado se é correspondente.
	 * @return buffer referência do buffer contendo apenas os dados da chunk.
	 * @throws TextureRuntimeException ocorre por tamanho da chunk inválido, tipo
	 * da chunk inesperado ou ainda então por dados insuficientes.
	 */

	private void openChunk(int code) throws TextureRuntimeException
	{
		openChunk();

		if (code != lastChunkCode)
			throw new TextureRuntimeException("chunk inesperada (chunk: %s)", new String(ByteUtil.parseInt(code)));
	}

	/**
	 * Procedimento que deve ser chamado sempre uma chunk terminar de ser lida.
	 * @throws TextureRuntimeException apenas se não houver integridade no CRC da chunk.
	 */

	private void closeChunk() throws TextureRuntimeException
	{
		if (lastChunkRemaing > 0)
		{
			lastChunkCode = 0;
			lastChunkLength = 0;
			lastChunkRemaing = 0;

			buffer.skip(4);

			return;
		}

		crc.update(buffer.getSaved());

		int expected = buffer.getInt();
		int computed = (int) crc.getValue();

		if (computed != expected)
			throw new TextureRuntimeException("CRC inválido (chunk: %s)", new String(ByteUtil.parseInt(lastChunkCode)));
	}

	@Override
	protected void parsePixels(ByteBuffer buffer, PixelFormat output)
	{
		final int lineSize = ((width * depth + 7) / 8) * bpp;

		byte current[] = new byte[lineSize + 1];
		byte previous[] = new byte[lineSize + 1];
		byte palLine[] = (depth < 8) ? new byte[width + 1] : null;

		final Inflater inflater = new Inflater();

		try {

			for (int y = 0; y < height; y++)
			{
				readChunkUnzip(inflater, current);
				PNGUtil.unfilter(bpp, current, previous);

				switch (colorType)
				{
					case COLOR_TRUECOLOR:
						PNGUtil.decodeColorTrueColor(buffer, output, current, transPixel);
						break;

					case COLOR_TRUEALPHA:
						PNGUtil.decodeColorTrueAlpha(buffer, output, current, transPixel);
						break;

					case COLOR_GREYSCALE:
						PNGUtil.decodeColorGrayScale(buffer, output, current);
						break;

					case COLOR_GREYALPHA:
						PNGUtil.decodeColorGrayAlpha(buffer, output, current);
						break;

					case COLOR_INDEXED:
						PNGUtil.decodeColorIndexed(buffer, output, depth, current, palLine);
						PNGUtil.decodeColorIndexedSub(buffer, output, palLine, palette, paletteA);
						break;

					default:
						throw new TextureRuntimeException("tipo de cor não suportado");
				}

				byte[] temp = current;
				current = previous;
				previous = temp;
			}

		} finally {
			inflater.end();
		}
	}

	/**
	 * Irá fazer a leitura de uma chunk do qual tem seus dados dados como compactados.
	 * Procedimento utilizado para descompactar os dados dos pixels da imagem (IDAT).
	 * @param inflater referência do objeto que irá descompactar dados em ZIP.
	 * @param buffer referência do buffer que será armazenados os dados dos pixels.
	 * @throws TextureRuntimeException apenas se houver falha no carregamento da chunk.
	 */

	private void readChunkUnzip(Inflater inflater, byte[] buffer) throws TextureRuntimeException
	{
		try {

			int length = buffer.length;

			do {

				int read = inflater.inflate(buffer);

				if (read > 0)
					length -= read;

				else
				{
					if (inflater.finished())
						throw new TextureRuntimeException("fim do arquivo");

					if (inflater.needsInput())
						refillInflater(inflater);
					else
						throw new TextureRuntimeException("não pode descompactar %d bytes", length);
				}

			} while (length > 0);

		} catch (DataFormatException e) {
			throw new TextureRuntimeException(e, "falha ao descompactar");
		}
	}

	/**
	 * Reabastecer Inflater para descompactar os dados em ZIP de uma chunk.
	 * @param inflater referência do objeto que irá descompactar os dados.
	 * @throws TextureRuntimeException apenas se houver falha na obtenção dos
	 * dados seguintes, tal como chunk incorreta ou fim da chunk anterior.
	 */

	private void refillInflater(Inflater inflater) throws TextureRuntimeException
	{
		if (lastChunkRemaing == 0)
		{
			do {

				closeChunk();
				openChunk();

			} while (lastChunkCode != IDAT);
		}

		buffer.save(lastChunkRemaing);
		buffer.skip(lastChunkRemaing);

		inflater.setInput(buffer.getSaved());

		afterReadBuffer();
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("colorType", colorType);
		description.append("bpp", bpp);
		description.append("palette", palette != null);
		description.append("transparency", transPixel != null);

		return description.toString();
	}
}
