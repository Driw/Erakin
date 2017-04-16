package com.erakin.engine.resource;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Textura Ra�z</h1>
 *
 * <p>Essa ra�z � permite a constru��o de novos recursos do tipo textura (imagens).
 * Nele ser� armazenado as informa��es para identifica��o da textura no OpenGL,
 * qual o tamanho da textura como tamb�m se usa propriedade alpha e depth.</p>
 *
 * @see ResourceRoot
 *
 * @author Andrew
 */

public class TextureRoot extends ResourceRoot
{
	/**
	 * C�digo para identifica��o da textura no OpenGL.
	 */
	int id;

	/**
	 * Quantos bits cada pixel dever� possuir.
	 */
	int depth;

	/**
	 * Tamanho da largura da imagem em pixels.
	 */
	int width;

	/**
	 * Tamanho da altura da imagem em pixels.
	 */
	int height;

	/**
	 * Imagem contendo cores transparentes.
	 */
	boolean alpha;

	/**
	 * Constr�i uma nova textura ra�z a partir de um caminho em disco especificado.
	 * Esse caminho dever� ser respectivo ao diret�rio de recursos e da textura.
	 */

	TextureRoot()
	{
		
	}

	/**
	 * Essa identifica��o � importante para que o OpenGL possa localizar os dados da
	 * textura para quando esta for utilizada poder ser chamada adequadamente.
	 * @return aquisi��o do c�digo da textura no sistema de textura do OpenGL.
	 */

	public int getGLTextureID()
	{
		return id;
	}

	@Override
	public Texture genResource()
	{
		Texture texture = new Texture(this);
		addReference(texture);

		return texture;
	}

	@Override
	public void toString(ObjectDescription description)
	{
		description.append("glID", id);
		description.append("width", width);
		description.append("height", height);
		description.append("depth", depth);
		description.append("alpha", alpha);
	}
}
