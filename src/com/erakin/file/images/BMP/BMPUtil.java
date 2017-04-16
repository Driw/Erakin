package com.erakin.file.images.BMP;

import static com.erakin.engine.resource.textures.pixel.PixelFormat.FORMAT_ALPHA;
import static com.erakin.engine.resource.textures.pixel.PixelFormat.FORMAT_LUMINANCE;
import static com.erakin.engine.resource.textures.pixel.PixelFormat.FORMAT_LUMINANCE_ALPHA;
import static com.erakin.engine.resource.textures.pixel.PixelFormat.FORMAT_RGB;
import static com.erakin.engine.resource.textures.pixel.PixelFormat.FORMAT_RGBA;
import static com.erakin.engine.resource.textures.pixel.PixelReadDirect.CONVERT_LUMINANCE_RGB;
import static com.erakin.engine.resource.textures.pixel.PixelReadDirect.CONVERT_RGB_ALPHA;
import static com.erakin.engine.resource.textures.pixel.PixelReadDirect.CONVERT_RGB_LUMINANCE;
import static com.erakin.engine.resource.textures.pixel.PixelReadDirect.CONVERT_RGB_LUMINANCEALPHA;
import static com.erakin.engine.resource.textures.pixel.PixelReadDirect.CONVERT_RGB_RGBA;
import static com.erakin.engine.resource.textures.pixel.PixelReadPalette.CONVERT_PALETTE_ALPHA;
import static com.erakin.engine.resource.textures.pixel.PixelReadPalette.CONVERT_PALETTE_LUMINANCE;
import static com.erakin.engine.resource.textures.pixel.PixelReadPalette.CONVERT_PALETTE_LUMINANCE_ALPHA;
import static com.erakin.engine.resource.textures.pixel.PixelReadPalette.CONVERT_PALETTE_RGB;
import static com.erakin.engine.resource.textures.pixel.PixelReadPalette.CONVERT_PALETTE_RGBA;

import java.nio.ByteBuffer;

import com.erakin.engine.resource.textures.pixel.PixelFormat;
import com.erakin.engine.resource.textures.pixel.PixelRead;
import com.erakin.file.FileRuntimeException;

/**
 * <h1>BMP</h1>
 *
 * <p>Classe utilitária usada para realizar operações que seguem o padrão de dados dos arquivos BMP.
 * Deve permitir fazer algumas operações a fim de converter os dados de um raster obtido do arquivo.
 * Além disso deverá permitir outras operações como trabalhar com operações de RLE comum para BMP.</p>
 *
 * @author Andrew Mello
 */

public class BMPUtil
{
	/**
	 * Construtor privado para evitar instâncias desnecessárias dessa classe utilitária.
	 */

	private BMPUtil()
	{
		
	}

	/**
	 * Converte os dados de um arquivo BMP denominado raster para os dados dos pixels em bytes.
	 * Considera a possibilidade de utilização da paleta de cores se assim for necessário.
	 * Caso a quantidade de BPP seja igual a um significa que será usada caso contrário não.
	 * Se configurado BPP como 1 irá usar apenas a paleta em quanto o 3 considera alpha 0.
	 * @param buffer referência do buffer que será usado para armazenar os pixels convertidos.
	 * @param width tamanho da largura da imagem em pixels para definir o tamanho do vetor.
	 * @param height tamanho da altura da imagem em pixels para definir o tamanho do vetor.
	 * @param output após a decodificação como os dados dos pixels serão armazenados.
	 * @param raster vetor contendo os dados que foram lidos diretamente do arquivo.
	 * @param palette paleta de cores contendo informações RGBA, apenas pra BPP igual a 1.
	 */

	public static void decodePalette(ByteBuffer buffer, int width, int height, PixelFormat output, byte[] raster, byte[] palette)
	{
		int bpp = output.BYTES;
		int space = buffer.limit() - buffer.position();
		int need = width * height * bpp;

		if (space < need)
			throw new FileRuntimeException("buffer com espaço insuficiente (%d de %d)", space, need);

		if (output == FORMAT_RGB)
			directDecodePalette(buffer, width, height, raster, palette, CONVERT_PALETTE_RGB);

		else if (output == FORMAT_RGBA)
			directDecodePalette(buffer, width, height, raster, palette, CONVERT_PALETTE_RGBA);

		else if (output == FORMAT_ALPHA)
			directDecodePalette(buffer, width, height, raster, palette, CONVERT_PALETTE_ALPHA);

		else if (output == FORMAT_LUMINANCE)
			directDecodePalette(buffer, width, height, raster, palette, CONVERT_PALETTE_LUMINANCE);

		else if (output == FORMAT_LUMINANCE_ALPHA)
			directDecodePalette(buffer, width, height, raster, palette, CONVERT_PALETTE_LUMINANCE_ALPHA);

		else
			throw new FileRuntimeException("tipo de saída não aceita (bpp: %d, alpha: %s)", output.BYTES, output.ALPHA);
	}

	/**
	 * Decodifica uma quantidade de bytes específicos que estejam usando a pelta de cores.
	 * @param buffer referência do buffer que irá armazenar os pixels decodificados em bytes.
	 * @param width quantos pixels a imagem a ser convertida possui na vertical.
	 * @param height quantos pixels a imagem a ser convertida possui na horizontal.
	 * @param raster referência do vetor contendo os bytes codificados da imagem.
	 * @param palette paleta que pode ser usada ou não dependendo da decodificação.
	 * @param output formato em que os pixels deverão ser escritos após a decodificação.
	 */

	private static void directDecodePalette(ByteBuffer buffer, int width, int height, byte[] raster, byte[] palette, PixelRead output)
	{
		for (int y = 0; y < height; y++)
			output.read(buffer, raster, palette, y * width, width);
	}

	/**
	 * Decodifica uma quantidade de bytes específicos que estejam no formato RGB.
	 * @param buffer referência do buffer que irá armazenar os pixels decodificados em bytes.
	 * @param width quantos pixels a imagem a ser convertida possui na vertical.
	 * @param height quantos pixels a imagem a ser convertida possui na horizontal.
	 * @param output formato em que os pixels deverão ser escritos após a decodificação.
	 * @param raster referência do vetor contendo os bytes codificados da imagem.
	 */

	public static void decodeRGB(ByteBuffer buffer, int width, int height, PixelFormat output, byte[] raster)
	{
		int bpp = output.BYTES;
		int space = buffer.limit() - buffer.position();
		int need = width * height * bpp;

		if (space < need)
			throw new FileRuntimeException("buffer com espaço insuficiente (%d de %d)", space, need);

		if (output == FORMAT_RGB)
			buffer.put(raster);

		else if (output == FORMAT_RGBA)
			directDecodeRGB(buffer, width, height, raster, CONVERT_RGB_RGBA);

		else if (output == FORMAT_ALPHA)
			directDecodeRGB(buffer, width, height, raster, CONVERT_RGB_ALPHA);

		else if (output == FORMAT_LUMINANCE)
			directDecodeRGB(buffer, width, height, raster, CONVERT_RGB_LUMINANCE);

		else if (output == FORMAT_LUMINANCE_ALPHA)
			directDecodeRGB(buffer, width, height, raster, CONVERT_RGB_LUMINANCEALPHA);

		throw new FileRuntimeException("tipo de saída não aceita (bpp: %d, alpha: %s)", output.BYTES, output.ALPHA);
	}

	/**
	 * Decodifica uma quantidade de bytes específicos que estejam no formato RGB.
	 * @param buffer referência do buffer que irá armazenar os pixels decodificados em bytes.
	 * @param width quantos pixels a imagem a ser convertida possui na vertical.
	 * @param height quantos pixels a imagem a ser convertida possui na horizontal.
	 * @param raster referência do vetor contendo os bytes codificados da imagem.
	 */

	private static void directDecodeRGB(ByteBuffer buffer, int width, int height, byte[] raster, PixelRead read)
	{
		for (int y = 0; y < height; y++)
			read.read(buffer, raster, null, y * width, width);		
	}

	/**
	 * Decodifica uma quantidade de bytes específicos que estejam no formato Escala Cinza.
	 * @param buffer referência do buffer que irá armazenar os pixels decodificados em bytes.
	 * @param width quantos pixels a imagem a ser convertida possui na vertical.
	 * @param height quantos pixels a imagem a ser convertida possui na horizontal.
	 * @param output formato em que os pixels deverão ser escritos após a decodificação.
	 * @param raster referência do vetor contendo os bytes codificados da imagem.
	 */

	public static void decodeGray(ByteBuffer buffer, int width, int height, PixelFormat output, byte[] raster)
	{
		int bpp = output.BYTES;
		int space = buffer.limit() - buffer.position();
		int need = width * height * bpp;

		if (space < need)
			throw new FileRuntimeException("buffer com espaço insuficiente (%d de %d)", space, need);

		for (int y = 0; y < height; y++)
			CONVERT_LUMINANCE_RGB.read(buffer, raster, null, y * height, width);
	}
}
