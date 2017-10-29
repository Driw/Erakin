package com.erakin.api.resources;

import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glIsProgram;
import static org.lwjgl.opengl.GL20.glIsShader;
import static org.lwjgl.opengl.GL20.glUseProgram;

import org.diverproject.util.ObjectDescription;

import com.erakin.api.resources.shader.ShaderRender;

/**
 * <h1>Computação Gráfica</h1>
 *
 * <p></p>
 *
 * @see Resource
 * @see ShaderRender
 *
 * @author Andrew
 */

public class Shader extends Resource<ShaderRoot>
{
	/**
	 * Código de identificação para desativar o uso de computação gráfica.
	 */
	public static final int NO_SHADER = 0;

	/**
	 * Constrói uma nova computação gráfica a partir de uma computação gráfica raíz especifica.
	 * @param root computação gráfica raíz que será usada para criar a computação gráfica.
	 */

	Shader(ShaderRoot root)
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

	public int getVertexID()
	{
		return root == null ? 0 : root.vertex;
	}

	public int getFragmentID()
	{
		return root == null ? 0 : root.fragment;
	}

	@Override
	public int getID()
	{
		return root == null ? 0 : root.id;
	}

	@Override
	public void bind()
	{
		glUseProgram(getID());		
	}

	@Override
	public void unbind()
	{
		glUseProgram(NO_SHADER);
	}

	@Override
	public void release()
	{
		unbind();

		glDetachShader(getID(), getVertexID());
		glDetachShader(getID(), getFragmentID());
		glDeleteShader(getID());
		glDeleteShader(getVertexID());
		glDeleteShader(getFragmentID());

		if (root != null)
		{
			root.delReference(this);
			root = null;
		}
	}

	@Override
	public boolean valid()
	{
		return	glIsShader(getVertexID()) &&
				glIsShader(getFragmentID()) &&
				glIsProgram(getID());
	}

	@Override
	public void toString(ObjectDescription description)
	{
		description.append("glID", getID());
		description.append("vertex", getVertexID());
		description.append("fragment", getFragmentID());
	}
}
