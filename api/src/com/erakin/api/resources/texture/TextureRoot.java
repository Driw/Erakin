package com.erakin.api.resources.texture;

import org.diverproject.util.ObjectDescription;

import com.erakin.api.resources.ResourceRoot;

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

public class TextureRoot extends ResourceRoot<Texture>
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
	 * Qual o tipo de textura que est� sendo usado:
	 * 
	 */
	TextureTarget target;

	/**
	 * Construtor em package para permitir apenas que TextureLoader construa um.
	 * Isso ir� garantir que uma Textura Ra�z inv�lida possa ser criada na engine.
	 * @param filepath caminho do arquivo em disco com os dados da textura carregado.
	 */

	TextureRoot(String filepath)
	{
		super(filepath);
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
		description.append("target", target);
	}
}
