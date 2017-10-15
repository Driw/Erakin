package com.erakin.api.resources.texture;

import static com.erakin.api.lwjgl.math.Maths.fold;

import java.nio.ByteBuffer;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Dados Tempor�rio para Textura</h1>
 *
 * <p>Essa classe tem como finalidade guardar informa��es de uma textura.
 * Para esse, ser� permitido definir as informa��es deste adequadamente.
 * Cada tipo de dado segue uma regra e tipos de dados diferentes.</p>
 *
 * <p>Ao finalizar a defini��o dos dados desse objeto atrav�s dos procedimentos
 * adequados para tal, ser�o repassados para o carregador de textura usado.
 * Para que esse possa passar adiante as informa��es necess�rias.</p>
 *
 * @see TextureData
 * @see ByteBuffer
 *
 * @author Andrew Mello
 */

public class TextureDataDefault implements TextureData
{
	/**
	 * Quantidade de bits usados por pixels.
	 */
	protected int depth;

	/**
	 * Largura da textura em pixels.
	 */
	protected int width;

	/**
	 * Altura da textura em pixels.
	 */
	protected int height;

	/**
	 * Bytes correspondentes aos pixels.
	 */
	protected ByteBuffer pixels;

	@Override
	public int getDepth()
	{
		return depth;
	}

	@Override
	public int getWidth()
	{
		return width;
	}

	@Override
	public int getHeight()
	{
		return height;
	}

	@Override
	public int getTexWidth()
	{
		return fold(width);
	}

	@Override
	public int getTexHeight()
	{
		return fold(height);
	}

	@Override
	public ByteBuffer getPixels()
	{
		return pixels;
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("depth", depth);
		description.append("width", width);
		description.append("height", height);
		description.append("pixels", pixels != null);

		return description.toString();
	}
}
