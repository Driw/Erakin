package com.erakin.engine.resource.textures.pixel;

import static com.erakin.engine.resource.textures.pixel.PixelWriteImpl.WRITE_ABGR;
import static com.erakin.engine.resource.textures.pixel.PixelWriteImpl.WRITE_BGRA;
import static com.erakin.engine.resource.textures.pixel.PixelWriteImpl.WRITE_RGBA;

import java.nio.ByteBuffer;

/**
 * <h1>Implementa��o para Ajuste de Pixels por Paleta</h1>
 *
 * <p>Essa classe ir� implementar todas as funcionalidades para que seja poss�vel
 * ser feito o ajuste dos dados dos pixels de acordo com uma paleta de cores usada.
 * A paleta de cores considerada ser� uma com as cores normais e uma de transpar�ncia.</p>
 *
 * <p>Paleta de cores funciona como uma lista de cores simples, determinado que a cada
 * N bytes ser� armazenado as informa��es das tonalidades de um cor que um pixel pode
 * assumir se utilizar o �ndice dessa cor na paleta, normalmente usando 3 ou 4 bytes.</p>
 *
 * <p>Quando utilizado 3 bytes determina informa��es em RGB e quando em 4 bytes em RGBA.
 * A ordem das tonalidades ou propriedade alfa pode variar de acordo com o tipo de
 * colora��o que foi definido para o salvamento dos dados dessa imagem em arquivo.</p>
 *
 * @see PixelWrite
 * @see PixelPaletteCast
 *
 * @author Andrew
 */

public class PixelPaletteCastImpl implements PixelPaletteCast
{
	/**
	 * Ajuste de pixels com paleta para cores armazenadas em ABGR.
	 */
	public static final PixelPaletteCast CONVERT_PAL_ABGR = new PixelPaletteCastImpl(WRITE_ABGR);

	/**
	 * Ajuste de pixels com paleta para cores armazenadas em RGBA.
	 */
	public static final PixelPaletteCast CONVERT_PAL_RGBA = new PixelPaletteCastImpl(WRITE_RGBA);

	/**
	 * Ajuste de pixels com paleta para cores armazenadas em BGRA.
	 */
	public static final PixelPaletteCast CONVERT_PAL_BGRA = new PixelPaletteCastImpl(WRITE_BGRA);


	/**
	 * Refer�ncia do escritor de pixels que ser� usado para garantir ordem das tonalidades.
	 */
	private final PixelWrite PAST_PIXEL;

	/**
	 * Constr�i um novo ajustador de pixels atrav�s de paleta de cores.
	 * @param pastPixel como os pixels dever�o ser escritos no buffer.
	 */

	public PixelPaletteCastImpl(PixelWrite pastPixel)
	{
		PAST_PIXEL = pastPixel;
	}

	@Override
	public void cast(ByteBuffer buffer, byte[] line, byte[] palette, byte[] paletteA)
	{
		if (paletteA != null)
			for (int i = 1; i < line.length; i++)
			{
				int idx = line[i] & 255;

				byte red	= palette[(idx * 3) + 0];
				byte green	= palette[(idx * 3) + 1];
				byte blue	= palette[(idx * 3) + 2];
				byte alpha	= palette[idx];

				PAST_PIXEL.write(buffer, red, blue, green, alpha);
			}

		else
			for (int i = 1; i < line.length; i++)
			{
				int idx = line[i] & 255;

				byte red	= palette[(idx * 3) + 0];
				byte green	= palette[(idx * 3) + 1];
				byte blue	= palette[(idx * 3) + 2];
				byte alpha	= (byte) 0xFF;

				PAST_PIXEL.write(buffer, red, blue, green, alpha);
			}
	}
}
