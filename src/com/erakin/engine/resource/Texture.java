package com.erakin.engine.resource;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.lang.IntUtil;

/**
 * <h1>Textura</h1>
 *
 * <p>Textura são imagens carregadas em disco para o engine e armazenadas no OpenGL.
 * Objetos desse tipo irão permitir que durante o desenvolvimento estas possam ser usadas.
 * Apesar do OpenGL oferecer essa possibilidade, o engine utiliza uma forma melhor.</p>
 *
 * <p>Essa forma melhor pode ser vista como a existência da documentação no seu uso,
 * e obviamente o acesso mais rápido e compreensível das funcionalidades da textura.</p>
 *
 * <p>Além disso é um recurso fictício, por tanto não possui informações diretas.
 * Apesar de não ser direto (raíz), possui as funcionalidades que permitem obter as
 * informações diretas dessa raíz como o tamanho da imagem, caminho desta e outros.</p>
 *
 * @see Resource
 *
 * @author Andrew
 */

public class Texture extends Resource
{
	/**
	 * Número de separação que há na textura (multi-textura).
	 */
	private int split;

	/**
	 * TODO
	 */
	private float shineDamper;

	/**
	 * TODO
	 */
	private float reflectivity;

	/**
	 * TODO
	 */
	private boolean useTransparency;

	/**
	 * TODO
	 */
	private boolean useFakeLighting;

	/**
	 * Constrói uma nova textura a partir de uma textura raíz especifica.
	 * @param root textura raíz que será usada para criar a textura.
	 */

	Texture(TextureRoot root)
	{
		super(root);
	}

	/**
	 * Caminho da textura determina de onde a imagem foi carregada em disco.
	 * @return string contendo o caminho da imagem usada para essa textura.
	 */

	public String getPath()
	{
		return root == null ? null : root.filePath;
	}

	/**
	 * Toda textura é formada por uma imagem do qual podemos dimensionar.
	 * @return aquisição do tamanho da largura da imagem em pixels.
	 */

	public int getWidth()
	{
		return root == null ? 0 : ((TextureRoot) root).width;
	}

	/**
	 * Toda textura é formada por uma imagem do qual podemos dimensionar.
	 * @return aquisição do tamanho da altura da imagem em pixels.
	 */

	public int getHeight()
	{
		return root == null ? 0 : ((TextureRoot) root).height;
	}

	@Override
	public int getID()
	{
		return root == null ? 0 : ((TextureRoot) root).id;
	}

	@Override
	public void bind()
	{
		glBindTexture(GL_TEXTURE_2D, getID());
	}

	@Override
	public void unbind()
	{
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	@Override
	public void release()
	{
		if (!valid())
			return;

		glDeleteTextures(getID());

		if (root != null)
		{
			root.delReference(this);
			root = null;
		}
	}

	@Override
	public boolean valid()
	{
		return getID() != 0;
	}

	/**
	 * TODO ???
	 * @param index ???
	 */

	public void active(int index)
	{
		if (IntUtil.interval(index, 0, 31))
			glActiveTexture(GL_TEXTURE0 + index);
	}

	/**
	 * TODO ???
	 * @return ???
	 */

	public int getSplit()
	{
		return split;
	}

	/**
	 * TODO ???
	 * @param split ???
	 */

	public void setSplit(int split)
	{
		this.split = split;
	}

	/**
	 * TODO ???
	 * @return ???
	 */

	public float getShineDamper()
	{
		return shineDamper;
	}

	/**
	 * TODO ???
	 * @param shineDamper ???
	 */

	public void setShineDamper(float shineDamper)
	{
		this.shineDamper = shineDamper;
	}

	/**
	 * TODO ???
	 * @return ???
	 */

	public float getReflectivity()
	{
		return reflectivity;
	}

	/**
	 * TODO ???
	 * @param reflectivity ???
	 */

	public void setReflectivity(float reflectivity)
	{
		this.reflectivity = reflectivity;
	}

	/**
	 * TODO ???
	 * @return ???
	 */

	public boolean isUseTransparency()
	{
		return useTransparency;
	}

	/**
	 * TODO ???
	 * @param use ???
	 */

	public void setTransparency(boolean use)
	{
		this.useTransparency = use;
	}

	/**
	 * TODO ???
	 * @return ???
	 */

	public boolean isUseFakeLighting()
	{
		return useFakeLighting;
	}

	/**
	 * TODO ???
	 * @param use ???
	 */

	public void setFakeLighting(boolean use)
	{
		this.useFakeLighting = use;
	}

	@Override
	public Texture clone()
	{
		return ((TextureRoot) root).genResource();
	}

	@Override
	public void toString(ObjectDescription description)
	{
		description.append("split", split);
		description.append("shineDamper", shineDamper);
		description.append("reflectivity", reflectivity);
		description.append("hasTransparency", useTransparency);
		description.append("useFakeLighting", useFakeLighting);
	}
}
