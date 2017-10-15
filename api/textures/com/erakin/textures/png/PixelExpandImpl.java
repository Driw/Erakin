package com.erakin.textures.png;

/**
 * <h1>Implementação para Expansão de Pixels</h1>
 *
 * <p>Classe que permite fazer a expansão de pixels contendo ainda variáveis estáticas.
 * Essas variáveis estáticas são objetos que permite a expansão de pixels em formatos
 * padrões utilizados em algumas partes do sistema, podendo ser util por fora.</p>
 *
 * @see PixelExpand
 *
 * @author Andrew
 */

public abstract class PixelExpandImpl implements PixelExpand
{
	/**
	 * Expansor de pixels armazenados em 4 bits.
	 */
	public static final PixelExpand EXPAND4 = new PixelExpand4();

	/**
	 * Expansor de pixels armazenados em 2 bits.
	 */
	public static final PixelExpand EXPAND2 = new PixelExpand2();

	/**
	 * Expansor de pixels armazenados em 1 bit.
	 */
	public static final PixelExpand EXPAND1 = new PixelExpand1();


	/**
	 * Quantos bytes serão analisado por loop de analise dos pixels.
	 */
	private final int BIT_INCREASE;

	/**
	 * Qual será a margem para exponenciação do valor dos pixels.
	 */
	private final int BIT_OFFSET;

	/**
	 * Constrói um novo expansor de pixels através de bytes.
	 * @param increase quantos bytes serão analisado por pixel.
	 * @param offset margem para exponenciação do valor dos pixels.
	 */

	public PixelExpandImpl(int increase, int offset)
	{
		BIT_INCREASE = increase;
		BIT_OFFSET = offset;
	}

	@Override
	public void parse(byte[] source, byte[] destin)
	{
		for (int i = 1; i < destin.length; i += BIT_INCREASE)
		{
			int value = source[1 + (i >> BIT_OFFSET) & 255];
			int index = destin.length - i;

			for (int j = 0; j < BIT_INCREASE; j++)
				destin[j] = parse(index, value);
		}
	}

	/**
	 * <h1>Expansor de Pixels N4</h1>
	 *
	 * Esse expansor de pixels de N4, onde cada pixel da imagem terá 4bits.
	 * TODO como é feito a "compactação".
	 *
	 * @see PixelExpandImpl
	 *
	 * @author Andrew
	 */

	private static class PixelExpand4 extends PixelExpandImpl
	{
		/**
		 * Constrói um novo expansor de pixels PE4.
		 */

		public PixelExpand4()
		{
			super(2, 1);
		}

		@Override
		public byte parse(int index, int value)
		{
			switch (index)
			{
				case 1:		return (byte) (value & 15);
				default:	return (byte) (value >> 4);
			}
		}
	}

	/**
	 * <h1>Expansor de Pixels N24</h1>
	 *
	 * Esse expansor de pixels de N2, onde cada pixel da imagem terá 2bits.
	 * TODO como é feito a "compactação".
	 *
	 * @see PixelExpandImpl
	 *
	 * @author Andrew
	 */

	private static class PixelExpand2 extends PixelExpandImpl
	{
		/**
		 * Constrói um novo expansor de pixels PE2.
		 */

		public PixelExpand2()
		{
			super(4, 2);
		}

		@Override
		public byte parse(int index, int value)
		{
			switch (index)
			{
				case 1:		return (byte) ((value >> 6)	   );
				case 2:		return (byte) ((value >> 4)	& 3);
				case 3:		return (byte) ((value >> 2)	& 3);
				default:	return (byte) ((value)		& 3);
			}
		}
	}

	/**
	 * <h1>Expansor de Pixels N1</h1>
	 *
	 * Esse expansor de pixels de N1, onde cada pixel da imagem terá 1bit.
	 * TODO como é feito a "compactação".
	 *
	 * @see PixelExpandImpl
	 *
	 * @author Andrew
	 */

	private static class PixelExpand1 extends PixelExpandImpl
	{
		/**
		 * Constrói um novo expansor de pixels PE1.
		 */

		public PixelExpand1()
		{
			super(8, 3);
		}

		@Override
		public byte parse(int index, int value)
		{
			switch (index)
			{
				case 1:		return (byte) ((value >> 7)	   );
				case 2:		return (byte) ((value >> 6)	& 1);
				case 3:		return (byte) ((value >> 5)	& 1);
				case 4:		return (byte) ((value >> 4)	& 1);
				case 5:		return (byte) ((value >> 3)	& 1);
				case 6:		return (byte) ((value >> 2)	& 1);
				case 7:		return (byte) ((value >> 1)	& 1);
				default:	return (byte) ((value)		& 1);
			}
		}
	}
}
