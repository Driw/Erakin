package com.erakin.engine.resource.textures.pixel;

import java.nio.ByteBuffer;

/**
 * <h1>Implementação par Escrita de Pixels</h1>
 *
 * <p>Essa classe possui classes privadas internas que especificam alguns padrões de pixels.
 * Os objetos respectivos podem ser acessados através das variáveis WRITE_???.
 * Onde ??? é o formado em que o pixel se encontra para ser gravador de acordo com RGBA.</p>
 *
 * <p>Para a especificação de RGBA é respectivo apenas ao significado de cada letra:
 * R para red/vermelho, G para green/verde, B para blue/azul e A para alpha/alfa.</p>
 *
 * @author Andrew
 */

public class PixelWriteImpl
{
	/**
	 * Escrita de pixels no formato Alpha.
	 */
	public static final PixelWrite WRITE_ALPHA = new PixelWriteAlpha();

	/**
	 * Escrita de pixels no formato Luminance.
	 */
	public static final PixelWrite WRITE_LUMINANCE = new PixelWriteLuminance();

	/**
	 * Escrita de pixels no formato Luminance Alpha.
	 */
	public static final PixelWrite WRITE_LUMINANCE_ALPHA = new PixelWriteLuminanceAlpha();

	/**
	 * Escrita de pixels no formato ABGR.
	 */
	public static final PixelWrite WRITE_ABGR = new PixelWriteABGR();

	/**
	 * Escrita de pixels no formato RGBA.
	 */
	public static final PixelWrite WRITE_RGBA = new PixelWriteRGBA();

	/**
	 * Escrita de pixels no formato BGRA.
	 */
	public static final PixelWrite WRITE_BGRA = new PixelWriteBGRA();

	/**
	 * Escrita de pixels no formato RGB.
	 */
	public static final PixelWrite WRITE_RGB = new PixelWriteRGB();

	/**
	 * <h1>Escrita de Pixels Alpha</h1>
	 *
	 * <p>Nesse caso os dados dos pixels serão escritos apenas com o red:
	 * byte[] { 0: alpha }, respeitando obviamente o ponteiro indicador
	 * de escrita do buffer utilizado para escrever os bytes em Luminance Alpha.</p>
	 *
	 * @author Andrew
	 */

	public static class PixelWriteAlpha implements PixelWrite
	{
		@Override
		public void write(ByteBuffer buffer, byte red, byte green, byte blue, byte alpha)
		{
			buffer.put(alpha);
		}
	}

	/**
	 * <h1>Escrita de Pixels Luminance</h1>
	 *
	 * <p>Nesse caso os dados dos pixels serão escritos apenas com o red:
	 * byte[] { 0: luminance }, respeitando obviamente o ponteiro indicador
	 * de escrita do buffer utilizado para escrever os bytes em Luminance Alpha.</p>
	 *
	 * @author Andrew
	 */

	public static class PixelWriteLuminance implements PixelWrite
	{
		@Override
		public void write(ByteBuffer buffer, byte red, byte green, byte blue, byte alpha)
		{
			buffer.put(red);
		}
	}

	/**
	 * <h1>Escrita de Pixels Luminance Alpha</h1>
	 *
	 * <p>Nesse caso os dados dos pixels serão escritos com red e alpha respectivos a:
	 * byte[] { 0: luminance, 1: alpha }, respeitando obviamente o ponteiro indicador
	 * de escrita do buffer utilizado para escrever os bytes em Luminance Alpha.</p>
	 *
	 * @author Andrew
	 */

	public static class PixelWriteLuminanceAlpha implements PixelWrite
	{
		@Override
		public void write(ByteBuffer buffer, byte red, byte green, byte blue, byte alpha)
		{
			buffer.put(red);
			buffer.put(alpha);
		}
	}

	/**
	 * <h1>Escrita de Pixels RGB</h1>
	 *
	 * <p>Nesse caso os dados dos pixels serão escritos da seguinte forma para cada pixel:
	 * byte[] { 0: red, 1: green, 2: blue }, respeitando obviamente o ponteiro
	 * indicador de escrita do buffer utilizado para escrever os bytes em RGB.</p>
	 *
	 * @author Andrew
	 */

	public static class PixelWriteRGB implements PixelWrite
	{
		@Override
		public void write(ByteBuffer buffer, byte red, byte green, byte blue, byte alpha)
		{
			buffer.put(red);
			buffer.put(green);
			buffer.put(blue);
		}
	}

	/**
	 * <h1>Escrita de Pixels ABGR</h1>
	 *
	 * <p>Nesse caso os dados dos pixels serão escritos da seguinte forma para cada pixel:
	 * byte[] { 0: alpha, 1: blue, 2: green, 3: red }, respeitando obviamente o ponteiro
	 * indicador de escrita do buffer utilizado para escrever os bytes em ABGR.</p>
	 *
	 * @author Andrew
	 */

	public static class PixelWriteABGR implements PixelWrite
	{
		@Override
		public void write(ByteBuffer buffer, byte red, byte green, byte blue, byte alpha)
		{
			buffer.put(alpha);
			buffer.put(blue);
			buffer.put(green);
			buffer.put(red);
		}
	}

	/**
	 * <h1>Escrita de Pixels RGBA</h1>
	 *
	 * <p>Nesse caso os dados dos pixels serão escritos da seguinte forma para cada pixel:
	 * byte[] { 0: red, 1: green, 2: blue, 3: alpha }, respeitando obviamente o ponteiro
	 * indicador de escrita do buffer utilizado para escrever os bytes em RGBA.</p>
	 *
	 * @author Andrew
	 */

	public static class PixelWriteRGBA implements PixelWrite
	{
		@Override
		public void write(ByteBuffer buffer, byte red, byte green, byte blue, byte alpha)
		{
			buffer.put(red);
			buffer.put(green);
			buffer.put(blue);
			buffer.put(alpha);
		}
	}

	/**
	 * <h1>Escrita de Pixels BGRA</h1>
	 *
	 * <p>Nesse caso os dados dos pixels serão escritos da seguinte forma para cada pixel:
	 * byte[] { 0: blue, 1: green, 2: red, 3: alpha }, respeitando obviamente o ponteiro
	 * indicador de escrita do buffer utilizado para escrever os bytes em BGRA.</p>
	 *
	 * @author Andrew
	 */

	public static class PixelWriteBGRA implements PixelWrite
	{
		@Override
		public void write(ByteBuffer buffer, byte red, byte green, byte blue, byte alpha)
		{
			buffer.put(blue);
			buffer.put(green);
			buffer.put(red);
			buffer.put(alpha);
		}
	}
}
