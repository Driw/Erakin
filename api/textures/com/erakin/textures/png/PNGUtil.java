package com.erakin.textures.png;

import static com.erakin.api.resources.texture.PixelFormat.FORMAT_ABGR;
import static com.erakin.api.resources.texture.PixelFormat.FORMAT_ALPHA;
import static com.erakin.api.resources.texture.PixelFormat.FORMAT_BGRA;
import static com.erakin.api.resources.texture.PixelFormat.FORMAT_LUMINANCE;
import static com.erakin.api.resources.texture.PixelFormat.FORMAT_LUMINANCE_ALPHA;
import static com.erakin.api.resources.texture.PixelFormat.FORMAT_RGB;
import static com.erakin.api.resources.texture.PixelFormat.FORMAT_RGBA;
import static com.erakin.api.resources.texture.PixelReadDirect.CONVERT_RGB;
import static com.erakin.api.resources.texture.PixelReadDirect.CONVERT_RGBA_ABGR;
import static com.erakin.api.resources.texture.PixelReadDirect.CONVERT_RGBA_BGRA;
import static com.erakin.api.resources.texture.PixelReadDirect.CONVERT_RGBA_RGB;
import static com.erakin.api.resources.texture.PixelReadDirect.CONVERT_RGBA_RGBA;
import static com.erakin.api.resources.texture.PixelReadDirect.CONVERT_RGB_ABGR;
import static com.erakin.api.resources.texture.PixelReadDirect.CONVERT_RGB_BGRA;
import static com.erakin.api.resources.texture.PixelReadDirect.CONVERT_RGB_RGBA;
import static com.erakin.textures.png.PixelExpandImpl.EXPAND1;
import static com.erakin.textures.png.PixelExpandImpl.EXPAND2;
import static com.erakin.textures.png.PixelExpandImpl.EXPAND4;
import static com.erakin.textures.png.PixelPaletteCastImpl.CONVERT_PAL_ABGR;
import static com.erakin.textures.png.PixelPaletteCastImpl.CONVERT_PAL_BGRA;
import static com.erakin.textures.png.PixelPaletteCastImpl.CONVERT_PAL_RGBA;

import java.nio.ByteBuffer;

import com.erakin.api.files.FileRuntimeException;
import com.erakin.api.resources.texture.PixelFormat;

/**
 * <h1>PNG</h1>
 *
 * <p>Classe utilitária usada para realizar operações que seguem o padrão de dados dos arquivos PNG.
 * Deve permitir fazer algumas operações a fim de converter os dados de um raster obtido do arquivo.
 * Além disso deverá permitir outras operações como trabalhar com operações de RLE comum para PNG.</p>
 *
 * @author Andrew Mello
 */

public class PNGUtil
{
	/**
	 * Decodifica uma quantidade de bytes específicos que estejam usando o formato RGB.
	 * @param buffer referência do buffer que irá armazenar os pixels decodificados em bytes.
	 * @param output formato do qual deseja que os bytes sejam convertidos, saída de dados.
	 * @param line linha contendo os dados dos pixels a serem convertidos (parcial à imagem).
	 * @param transparent vetor contendo o valor que será considerado como pixel transparente.
	 * @throws FileRuntimeException se o tipo de saída não for válido para RGB.
	 */

	public static void decodeColorTrueColor(ByteBuffer buffer, PixelFormat output, byte[] line, byte[] transparent)
	{
		if (output == FORMAT_ABGR)
			CONVERT_RGB_ABGR.read(buffer, line, transparent, 1, line.length);

		else if (output == FORMAT_RGBA)
			CONVERT_RGB_RGBA.read(buffer, line, transparent, 1, line.length);

		else if (output == FORMAT_BGRA)
			CONVERT_RGB_BGRA.read(buffer, line, transparent, 1, line.length);

		else if (output == FORMAT_RGB)
			CONVERT_RGB.read(buffer, line, transparent, 1, line.length);

		else
			throw new FileRuntimeException("tipo de cor não suportado");
	}

	/**
	 * Decodifica uma quantidade de bytes específicos que estejam usando o formato RGBA.
	 * @param buffer referência do buffer que irá armazenar os pixels decodificados em bytes.
	 * @param output formato do qual deseja que os bytes sejam convertidos, saída de dados.
	 * @param line linha contendo os dados dos pixels a serem convertidos (parcial à imagem).
	 * @param transparent vetor contendo o valor que será considerado como pixel transparente.
	 * @throws FileRuntimeException se o tipo de saída não for válido para RGBA.
	 */

	public static void decodeColorTrueAlpha(ByteBuffer buffer, PixelFormat output, byte[] line, byte[] transparent)
	{
		if (output == FORMAT_ABGR)
			CONVERT_RGBA_ABGR.read(buffer, line, transparent, 1, line.length);

		else if (output == FORMAT_RGBA)
			CONVERT_RGBA_RGBA.read(buffer, line, transparent, 1, line.length);

		else if (output == FORMAT_BGRA)
			CONVERT_RGBA_BGRA.read(buffer, line, transparent, 1, line.length);

		else if (output == FORMAT_RGB)
			CONVERT_RGBA_RGB.read(buffer, line, transparent, 1, line.length);

		else
			throw new FileRuntimeException("tipo de cor não suportado");		
	}

	/**
	 * Decodifica uma quantidade de bytes específicos que estejam usando o formato Luminance ou Alpha.
	 * @param buffer referência do buffer que irá armazenar os pixels decodificados em bytes.
	 * @param output formato do qual deseja que os bytes sejam convertidos, saída de dados.
	 * @param line linha contendo os dados dos pixels a serem convertidos (parcial à imagem).
	 * @throws FileRuntimeException se o tipo de saída não for válido para Luminance ou Alpha.
	 */

	public static void decodeColorGrayScale(ByteBuffer buffer, PixelFormat output, byte[] line)
	{
		if (output != FORMAT_LUMINANCE && output != FORMAT_ALPHA)
			throw new FileRuntimeException("tipo de cor não suportado");		

		copy(buffer, line);
	}

	/**
	 * Decodifica uma quantidade de bytes específicos que estejam usando o formato Escala Cinza.
	 * @param buffer referência do buffer que irá armazenar os pixels decodificados em bytes.
	 * @param output formato do qual deseja que os bytes sejam convertidos, saída de dados.
	 * @param line linha contendo os dados dos pixels a serem convertidos (parcial à imagem).
	 * @throws FileRuntimeException se o tipo de saída não for válido para Escala Cinza.
	 */

	public static void decodeColorGrayAlpha(ByteBuffer buffer, PixelFormat output, byte[] line)
	{
		if (output != FORMAT_LUMINANCE_ALPHA)
			throw new FileRuntimeException("tipo de cor não suportado");

		copy(buffer, line);
	}

	/**
	 * Decodifica uma quantidade de bytes específicos que estejam usando o formato Cor Indexada.
	 * @param buffer referência do buffer que irá armazenar os pixels decodificados em bytes.
	 * @param output formato do qual deseja que os bytes sejam convertidos, saída de dados.
	 * @param depth quantos bits serão usados da paleta para especificar um único índice.
	 * @param line linha contendo os dados dos pixels a serem convertidos (parcial à imagem).
	 * @param pal paleta contendo as cores que serão consideradas para cada índice encontrado.
	 * @throws FileRuntimeException se o tipo de saída não for válido para Cor Indexada.
	 */

	public static void decodeColorIndexed(ByteBuffer buffer, PixelFormat output, int depth, byte[] line, byte[] pal)
	{
		switch (depth)
		{
			case 8:
				pal = line;
				break;

			case 4:
				EXPAND4.parse(line, pal);
				break;

			case 2:
				EXPAND2.parse(line, pal);
				break;

			case 1:
				EXPAND1.parse(line, pal);
				break;

			default:
				throw new FileRuntimeException("tipo de cor não suportado");
		}
	}

	/**
	 * Decodifica uma quantidade de bytes específicos que estejam usando o formato Paleta.
	 * @param buffer referência do buffer que irá armazenar os pixels decodificados em bytes.
	 * @param output formato do qual deseja que os bytes sejam convertidos, saída de dados.
	 * @param line referência da linha de varredura contendo informações dos pixels.
	 * @param palette vetor contendo a lista de cores do qual será considerada.
	 * @param transparency vetor contendo a cor considerada como transparente.
	 * @throws FileRuntimeException se o tipo de saída não for válido para RGBA|ABGR|BGRA.
	 */

	public static void decodeColorIndexedSub(ByteBuffer buffer, PixelFormat output, byte[] line, byte[] palette, byte[] transparency)
	{
		if (output == FORMAT_ABGR)
			CONVERT_PAL_ABGR.cast(buffer, line, palette, transparency);

		else if (output == FORMAT_RGBA)
			CONVERT_PAL_RGBA.cast(buffer, line, palette, transparency);

		else if (output == FORMAT_BGRA)
			CONVERT_PAL_BGRA.cast(buffer, line, palette, transparency);

		else
			throw new FileRuntimeException("tipo de cor não suportado");
	}

	/**
	 * Procedimento que irá fazer uma cópia do conteúdo de uma linha de pixels
	 * para dentro de um buffer especificado, sem fazer qualquer validação.
	 * @param buffer referência do buffer que será armazenado os pixels.
	 * @param line bytes respectivos de uma única linha de pixels.
	 */

	public static void copy(ByteBuffer buffer, byte[] line)
	{
		buffer.put(line, 1, line.length - 1);
	}


	/**
	 * PRocedimento que deverá fazer a varredura dos pixels de cada linha da imagem.
	 * @param bpp quantidade de bytes por pixel que foi usado durante a filtragem.
	 * @param current vetor correspondente aos pixels da linha para varredura atual.
	 * @param previous linha de varredura dos pixels superiores a linha atual.
	 */

	public static void unfilter(int bpp, byte[] current, byte[] previous)
	{
		switch (current[0])
		{
			case 0:	break;
			case 1: PNGUtil.unfilterSub(bpp, current); break;
			case 2: PNGUtil.unfilterUp(current, previous); break;
			case 3: PNGUtil.unfilterAverage(bpp, current, previous); break;
			case 4: PNGUtil.unfilterPaeth(bpp, current, previous); break;

			default:
				PNGUtil.unfilterPaeth(bpp, current, previous);
		}
	}

	/**
	 * Filtro que transmite a diferença entre cada byte e o valor do byte correspondente ao
	 * pixel anterior. Para cada byte da linha de varredura será feito a filtragem.
	 * @param bpp quantidade de bytes por pixel que foi usado durante a filtragem.
	 * @param current vetor contendo os bytes da atual linha de pixels.
	 */

	public static void unfilterSub(int bpp, byte[] current)
	{
		for (int i = bpp + 1; i < current.length; i++)
			current[i] += current[i - bpp];
	}

	/**
	 * Filtro superior é como o filtro inferior exceto que o pixel imediatamente acima do
	 * pixel da imagem correspondente, em vez de apenas para o seu lado esquerdo. E para
	 * cada byte da linha de varredura será aplicado o mesmo cálculo desse filtro.
	 * @param current vetor contendo os bytes da atual linha de pixels.
	 * @param previous vetor contendo os bytes da linha de pixels anterior.
	 */

	public static void unfilterUp(byte[] current, byte[] previous)
	{
		for (int i = 1; i < current.length; i++)
			current[i] += previous[i];
	}

	/**
	 * Filtro médio utiliza uma média entre os dois pixels vizinhos (esquerda e cima) para prever o valor de um pixel.
	 * E para cada byte da linha de varredura será aplicado o mesmo cálculo desse filtro.
	 * @param bpp quantidade de bytes por pixel que foi usado durante a filtragem.
	 * @param current vetor contendo os bytes da atual linha de pixels.
	 * @param previous vetor contendo os bytes da linha de pixels anterior.
	 */

	public static void unfilterAverage(int bpp, byte[] current, byte[] previous)
	{
		int i;

		for (i = 1; i <= bpp; i++)
			current[i] += (byte) ((previous[i] & 0xFF) >>> 1);

		for (; i < current.length; i++)
			current[i] += (byte) (((previous[i] & 0xFF) + (current[i - bpp] & 0xFF)) >>> 1);
	}

	/**
	 * O filtro Paeth calcula uma função linear simples dos três pixels vizinhos,
	 * em seguida, escolhe como preditor o pixel vizinho mais próximo do valor calculado.
	 * E para cada byte da linha de varredura será aplicado o mesmo cálculo desse filtro.
	 * @param bpp quantidade de bytes por pixel que foi usado durante a filtragem.
	 * @param current vetor contendo os bytes da atual linha de pixels.
	 * @param previous vetor contendo os bytes da linha de pixels anterior.
	 */

	public static void unfilterPaeth(int bpp, byte[] current, byte[] previous)
	{
		int i;

		for (i = 1; i <= bpp; i++)
			current[i] += previous[i];

		for (; i < current.length; i++)
		{
			int a = current[i - bpp] & 255;
			int b = previous[i] & 255;
			int c = previous[i - bpp] & 255;
			int p = a + b - c;

			int pa = p - a; if (pa < 0) pa = -pa;
			int pb = p - b; if (pb < 0) pb = -pb;
			int pc = p - c; if (pc < 0) pc = -pc;

			if (pa <= pb && pa <= pc)
				c = a;

			else if (pb <= pc)
				c = b;

			current[i] += (byte) c;
		}
	}
}
