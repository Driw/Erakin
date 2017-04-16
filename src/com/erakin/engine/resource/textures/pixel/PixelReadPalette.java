package com.erakin.engine.resource.textures.pixel;

import static com.erakin.engine.resource.textures.pixel.PixelWriteImpl.WRITE_ABGR;
import static com.erakin.engine.resource.textures.pixel.PixelWriteImpl.WRITE_ALPHA;
import static com.erakin.engine.resource.textures.pixel.PixelWriteImpl.WRITE_BGRA;
import static com.erakin.engine.resource.textures.pixel.PixelWriteImpl.WRITE_LUMINANCE;
import static com.erakin.engine.resource.textures.pixel.PixelWriteImpl.WRITE_LUMINANCE_ALPHA;
import static com.erakin.engine.resource.textures.pixel.PixelWriteImpl.WRITE_RGB;
import static com.erakin.engine.resource.textures.pixel.PixelWriteImpl.WRITE_RGBA;

import java.nio.ByteBuffer;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.lang.IntUtil;

/**
 * <h1>Implementação par Leitura de Pixels</h1>
 *
 * <p>Essa classe possui implementado alguns padrões para leitura de pixels.
 * Os objetos respectivos podem ser acessados através das variáveis CONVERT_???.
 * Onde ??? é o formado em que o pixel se encontra para ser convertido.</p>
 *
 * <p>Para a especificação de RGBA é respectivo apenas ao significado de cada letra:
 * R para red/vermelho, G para green/verde, B para blue/azul e A para alpha/alfa.</p>
 *
 * @see PixelRead
 *
 * @author Andrew
 */

public class PixelReadPalette implements PixelRead
{
	/**
	 * Conversor de cores em paletas para RGB.
	 */
	public static final PixelRead CONVERT_PALETTE_RGB = new PixelReadPalette(WRITE_RGB);

	/**
	 * Conversor de cores em paletas para RGBA.
	 */
	public static final PixelRead CONVERT_PALETTE_RGBA = new PixelReadPalette(WRITE_RGBA);

	/**
	 * Conversor de cores em paletas para ABGR.
	 */
	public static final PixelRead CONVERT_PALETTE_ABGR = new PixelReadPalette(WRITE_ABGR);

	/**
	 * Conversor de cores em paletas para BGRA.
	 */
	public static final PixelRead CONVERT_PALETTE_BGRA = new PixelReadPalette(WRITE_BGRA);

	/**
	 * Conversor de cores em paletas para Alpha.
	 */
	public static final PixelRead CONVERT_PALETTE_ALPHA = new PixelReadPalette(WRITE_ALPHA);

	/**
	 * Conversor de cores em paletas para Luminance.
	 */
	public static final PixelRead CONVERT_PALETTE_LUMINANCE = new PixelReadPalette(WRITE_LUMINANCE);

	/**
	 * Conversor de cores em paletas para Luminance Alpha.
	 */
	public static final PixelRead CONVERT_PALETTE_LUMINANCE_ALPHA = new PixelReadPalette(WRITE_LUMINANCE_ALPHA);


	/**
	 * Como é feita a escrita das cores do pixel.
	 */
	private final PixelWrite PIXEL_WRITE;

	/**
	 * Constrói um nodo modelo para converter dados de uma linha de pixels.
	 * @param pixelWrite como será feita a escrita dos pixels.
	 */

	public PixelReadPalette(PixelWrite pixelWrite)
	{
		PIXEL_WRITE = pixelWrite;
	}

	@Override
	public void read(ByteBuffer buffer, byte[] line, byte[] palette, int offset, int length)
	{
		for (int i = 0; i < length; i++)
		{
			int index = IntUtil.parseByte(line[offset + i]);
			int indexOffset = index * 4;

			byte red = palette[indexOffset + 0];
			byte green = palette[indexOffset + 1];
			byte blue = palette[indexOffset + 2];
			byte alpha = palette[indexOffset + 3];

			PIXEL_WRITE.write(buffer, red, green, blue, alpha);
		}
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		return description.toString();
	}
}
