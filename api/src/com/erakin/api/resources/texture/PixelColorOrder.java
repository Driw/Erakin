package com.erakin.api.resources.texture;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Ordem das Cores do Pixel</h1>
 *
 * <p>Objeto utilizado para determinar qual a ordem das cores de um pixel.
 * Tem como finalidade determinar como será ordenado os dados em bytes
 * de cada tonalidade de cor quando necessário ler/gravar um pixel.</p>
 *
 * <p>A ordenação deverá ser feita considerado as 3 tonalidades de cores,
 * RGB (red|green|blue, vermelho|verde|azul) com uma "quarta tonalidade",
 * denominada alfa que terá como finalidade determinar transparência.</p>
 *
 * @author Andrew
 */

public class PixelColorOrder
{
	/**
	 * Pixel com cores ordenadas como:
	 * Alpha (Alfa).
	 */
	public static final PixelColorOrder COLOR_ORDER_ALPHA = new PixelColorOrder(-1, -1, -1, 0);

	/**
	 * Pixel com cores no tom de preto e branco sem transparência.
	 * Red[Black/White] (Vermelho como Preto/Branco).
	 */
	public static final PixelColorOrder COLOR_ORDER_LUMINANCE = new PixelColorOrder(0, 0, 0, 0);

	/**
	 * Pixel com cores no tom de preto e branco com transparência.
	 * Red[Black/White] e Alpha (Vermelho como Preto/Branco e Alfa).
	 */
	public static final PixelColorOrder COLOR_ORDER_LUMINANCE_ALPHA = new PixelColorOrder(0, 0, 0, 1);

	/**
	 * Pixel com cores ordenadas como:
	 * Red, Green e Blue (Vermelho, Verde e Azul).
	 */
	public static final PixelColorOrder COLOR_ORDER_RGB = new PixelColorOrder(0, 1, 2, -1);

	/**
	 * Pixel com cores ordenadas como:
	 * Red, Green, Blue e Alpha (Vermelho, Verde, Azul e Alfa).
	 */
	public static final PixelColorOrder COLOR_ORDER_RGBA = new PixelColorOrder(0, 1, 2, 3);

	/**
	 * Pixel com cores ordenadas como:
	 * Alpha, Blue, Green e Red (Alfa, Azul, Verde e Vermelho).
	 */
	public static final PixelColorOrder COLOR_ORDER_ABGR = new PixelColorOrder(4, 3, 2, 1);

	/**
	 * Pixel com cores ordenadas como:
	 * Blue, Green, Red e Alpha (Azul, Verde, Vermelho e Alfa).
	 */
	public static final PixelColorOrder COLOR_ORDER_BGRA = new PixelColorOrder(2, 1, 0, 3);


	/**
	 * Ordem para da tonalidade vermelha no pixel (0 a 3).
	 */
	public final int RED;

	/**
	 * Ordem para da tonalidade vermelha no pixel (0 a 3).
	 */

	public final int GREEN;
	/**
	 * Ordem para da tonalidade vermelha no pixel (0 a 3).
	 */

	public final int BLUE;

	/**
	 * Ordem para da tonalidade vermelha no pixel (-1 se não usado).
	 */
	public final int ALPHA;

	/**
	 * Quantidade de bytes por pixel.
	 */
	public final int BPP;

	/**
	 * Constrói uma nova ordenação para as tonalidades da cor de um pixel.
	 * @param red índice da tonalidade vermelha na ordem das cores.
	 * @param green índice da tonalidade verde na ordem das cores.
	 * @param blue índice da tonalidade azul na ordem das cores.
	 * @param alpha índice da tonalidade alfa na ordem das cores.
	 */

	public PixelColorOrder(int red, int green, int blue, int alpha)
	{
		RED = red;
		GREEN = green;
		BLUE = blue;
		ALPHA = alpha;

		int bpp = 0;
		bpp += RED >= 0   ? 1 : 0;
		bpp += GREEN >= 0 ? 1 : 0;
		bpp += BLUE >= 0  ? 1 : 0;
		bpp += ALPHA >= 0 ? 1 : 0;

		BPP = bpp;
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("bpp", BPP);
		description.append("red", RED);
		description.append("green", GREEN);
		description.append("blue", BLUE);
		description.append("alpha", ALPHA);

		return description.toString();
	}
}