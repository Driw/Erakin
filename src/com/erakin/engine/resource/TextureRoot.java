package com.erakin.engine.resource;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Textura Raíz</h1>
 *
 * <p>Essa raíz é permite a construção de novos recursos do tipo textura (imagens).
 * Nele será armazenado as informações para identificação da textura no OpenGL,
 * qual o tamanho da textura como também se usa propriedade alpha e depth.</p>
 *
 * @see ResourceRoot
 *
 * @author Andrew
 */

public class TextureRoot extends ResourceRoot
{
	/**
	 * Código para identificação da textura no OpenGL.
	 */
	int id;

	/**
	 * Quantos bits cada pixel deverá possuir.
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
	 * Constrói uma nova textura raíz a partir de um caminho em disco especificado.
	 * Esse caminho deverá ser respectivo ao diretório de recursos e da textura.
	 */

	TextureRoot()
	{
		
	}

	/**
	 * Essa identificação é importante para que o OpenGL possa localizar os dados da
	 * textura para quando esta for utilizada poder ser chamada adequadamente.
	 * @return aquisição do código da textura no sistema de textura do OpenGL.
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
