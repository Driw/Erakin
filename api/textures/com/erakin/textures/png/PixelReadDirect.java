package com.erakin.textures.png;

import static com.erakin.api.ErakinAPIUtil.objectString;
import static com.erakin.api.resources.texture.PixelColorOrder.COLOR_ORDER_LUMINANCE;
import static com.erakin.api.resources.texture.PixelColorOrder.COLOR_ORDER_LUMINANCE_ALPHA;
import static com.erakin.api.resources.texture.PixelColorOrder.COLOR_ORDER_RGB;
import static com.erakin.api.resources.texture.PixelColorOrder.COLOR_ORDER_RGBA;
import static com.erakin.api.resources.texture.PixelWriteImpl.WRITE_ABGR;
import static com.erakin.api.resources.texture.PixelWriteImpl.WRITE_ALPHA;
import static com.erakin.api.resources.texture.PixelWriteImpl.WRITE_BGRA;
import static com.erakin.api.resources.texture.PixelWriteImpl.WRITE_LUMINANCE;
import static com.erakin.api.resources.texture.PixelWriteImpl.WRITE_LUMINANCE_ALPHA;
import static com.erakin.api.resources.texture.PixelWriteImpl.WRITE_RGB;
import static com.erakin.api.resources.texture.PixelWriteImpl.WRITE_RGBA;

import java.nio.ByteBuffer;

import org.diverproject.util.ObjectDescription;

import com.erakin.api.resources.texture.PixelColorOrder;
import com.erakin.api.resources.texture.PixelRead;
import com.erakin.api.resources.texture.PixelWrite;

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

public class PixelReadDirect implements PixelRead
{
	/**
	 * Conversor de cores em Luminance para RGB.
	 */
	public static final PixelRead CONVERT_LUMINANCE_RGB = new PixelReadDirect(COLOR_ORDER_LUMINANCE, WRITE_RGB);

	/**
	 * Conversor de cores em Luminance para RGBA.
	 */
	public static final PixelRead CONVERT_LUMINANCE_RGBA = new PixelReadDirect(COLOR_ORDER_LUMINANCE, WRITE_RGBA);

	/**
	 * Conversor de cores em Luminance Alpha para RGB.
	 */
	public static final PixelRead CONVERT_LUMALPHA_RGB = new PixelReadDirect(COLOR_ORDER_LUMINANCE_ALPHA, WRITE_RGB);

	/**
	 * Conversor de cores em Luminance Alpha para RGBA.
	 */
	public static final PixelRead CONVERT_LUMALPHA_RGBA = new PixelReadDirect(COLOR_ORDER_LUMINANCE_ALPHA, WRITE_RGBA);


	/**
	 * Conversor de cores em RGB para RGB.
	 */
	public static final PixelRead CONVERT_RGB = new PixelReadDirect(COLOR_ORDER_RGB, WRITE_RGB);

	/**
	 * Conversor de cores em RGB para RGBA.
	 */
	public static final PixelRead CONVERT_RGB_RGBA = new PixelReadDirect(COLOR_ORDER_RGB, WRITE_RGBA);

	/**
	 * Conversor de cores em RGB para ABGR.
	 */
	public static final PixelRead CONVERT_RGB_ABGR = new PixelReadDirect(COLOR_ORDER_RGB, WRITE_ABGR);

	/**
	 * Conversor de cores em RGB para BGRA.
	 */
	public static final PixelRead CONVERT_RGB_BGRA = new PixelReadDirect(COLOR_ORDER_RGB, WRITE_BGRA);

	/**
	 * Conversor de cores em RGB para Alpha.
	 */
	public static final PixelRead CONVERT_RGB_ALPHA = new PixelReadDirect(COLOR_ORDER_RGB, WRITE_ALPHA);

	/**
	 * Conversor de cores em RGB para Luminance.
	 */
	public static final PixelRead CONVERT_RGB_LUMINANCE = new PixelReadDirect(COLOR_ORDER_RGB, WRITE_LUMINANCE);

	/**
	 * Conversor de cores em RGB para Luminance Alpha.
	 */
	public static final PixelRead CONVERT_RGB_LUMINANCEALPHA = new PixelReadDirect(COLOR_ORDER_RGB, WRITE_LUMINANCE_ALPHA);


	/**
	 * Conversor de cores em RGBA para RGB.
	 */
	public static final PixelRead CONVERT_RGBA_RGB = new PixelReadDirect(COLOR_ORDER_RGBA, WRITE_RGB);

	/**
	 * Conversor de cores em RGBA para RGBA.
	 */
	public static final PixelRead CONVERT_RGBA_RGBA = new PixelReadDirect(COLOR_ORDER_RGBA, WRITE_RGBA);

	/**
	 * Conversor de cores em RGBA para ABGR.
	 */
	public static final PixelRead CONVERT_RGBA_ABGR = new PixelReadDirect(COLOR_ORDER_RGBA, WRITE_ABGR);

	/**
	 * Conversor de cores em RGBA para BGRA.
	 */
	public static final PixelRead CONVERT_RGBA_BGRA = new PixelReadDirect(COLOR_ORDER_RGBA, WRITE_BGRA);


	/**
	 * Como é feita a escrita das cores do pixel.
	 */
	private final PixelWrite PIXEL_WRITE;

	/**
	 * Ordem em que os pixels deverão ser lidos.
	 */
	private final PixelColorOrder COLOR_ORDER;

	/**
	 * Constrói um nodo modelo para converter dados de uma linha de pixels.
	 * @param pixelWrite como será feita a escrita dos pixels.
	 * @param colorOrder ordem em que os bytes estarão sendo lidos.
	 */

	public PixelReadDirect(PixelColorOrder colorOrder, PixelWrite pixelWrite)
	{
		PIXEL_WRITE = pixelWrite;
		COLOR_ORDER = colorOrder;
	}

	@Override
	public void read(ByteBuffer buffer, byte[] line, byte[] transparency, int offset, int length)
	{
		for (int i = offset; i < length; i += COLOR_ORDER.BPP)
		{
			byte red = COLOR_ORDER.RED == -1 ? 0 : line[i + COLOR_ORDER.RED];
			byte green = COLOR_ORDER.GREEN == -1 ? 0 : line[i + COLOR_ORDER.GREEN];
			byte blue = COLOR_ORDER.BLUE == -1 ? 0 : line[i + COLOR_ORDER.BLUE];
			byte alpha = COLOR_ORDER.ALPHA == -1 ? 0 : line[i + COLOR_ORDER.ALPHA];

			if (transparency != null && red == transparency[1] && green == transparency[3] && blue == transparency[5])
				alpha = 0;

			PIXEL_WRITE.write(buffer, red, green, blue, alpha);
		}
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("reader", objectString(COLOR_ORDER));

		return description.toString();
	}
}
