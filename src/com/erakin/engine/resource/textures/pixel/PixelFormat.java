package com.erakin.engine.resource.textures.pixel;

import org.diverproject.util.ObjectDescription;

/**
 * Formato de Pixels
 *
 * Essa classe tem como finalidade determinar dos atributos de um pixel.
 * Primeiramente quantos bytes cada pixel dever� possuir, sendo sempre
 * respectivos as cores RGB (red/vermelho, green/verde e blue/azul).
 * Em quanto o outro atributo ir� determinar se h� propriedade alpha.
 *
 * @author Andrew
 */

public class PixelFormat
{
	/**
	 * Formato de pixel alfa, cont�m apenas pixels apenas com a propriedade alpha.
	 */
	public static final PixelFormat FORMAT_ALPHA = new PixelFormat(1, true);

	/**
	 * Formato de pixel lumin�ncia, cont�m pixels preto e branco cada sem propriedade alpha.
	 */
	public static final PixelFormat FORMAT_LUMINANCE = new PixelFormat(1, false);

	/**
	 * Formato de pixel lumin�ncia, cont�m pixels preto e branco cada com propriedade alpha.
	 */
	public static final PixelFormat FORMAT_LUMINANCE_ALPHA = new PixelFormat(2, true);

	/**
	 * Formado de pixel para tonalidades de: vermelha, verde e azul.
	 * Tendo 1 byte para cada na sequ�ncia e sem propriedade alpha.
	 */
	public static final PixelFormat FORMAT_RGB = new PixelFormat(3, false);

	/**
	 * Formado de pixel para tonalidades de: vermelho, verde e azul.
	 * Tendo 1 byte para cada na sequ�ncia e com propriedade alpha.
	 */
	public static final PixelFormat FORMAT_RGBA = new PixelFormat(4, true);

	/**
	 * Formado de pixel para tonalidades de: azul, verde e vermelho.
	 * Tendo 1 byte para cada na sequ�ncia e com propriedade alpha.
	 */
	public static final PixelFormat FORMAT_BGRA = new PixelFormat(4, true);

	/**
	 * Formado de pixel para tonalidades de: azul, verde e vermelho.
	 * Tendo 1 byte para cada na sequ�ncia e com propriedade alpha.
	 * Nesse caso a propriedade alpha vem antes das tonalidades.
	 */
	public static final PixelFormat FORMAT_ABGR = new PixelFormat(4, true);


	/**
	 * Quantidade de bytes que esse formado de pixel ocupa.
	 */
	public final int BYTES;

	/**
	 * Propriedade alpha habilitada para esse pixel (byte inclu�do em BYTES).
	 */
	public final boolean ALPHA;

	/**
	 * Constr�i um novo formado para pixel sendo necess�rio definir:
	 * @param bytes quantidade de bytes que esse tipo de pixel ocupa.
	 * @param alpha propriedade alpha habilitada para esse formato.
	 */

	public PixelFormat(int bytes, boolean alpha)
	{
		BYTES = bytes;
		ALPHA = alpha;
	}

	@Override
	protected Object clone()
	{
		PixelFormat format = new PixelFormat(BYTES, ALPHA);

		return format;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof PixelFormat))
			return false;

		PixelFormat format = (PixelFormat) obj;

		return format.ALPHA == ALPHA && format.BYTES == BYTES;
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("bytes", BYTES);
		description.append("alpha", ALPHA);

		return description.toString();
	}
}
