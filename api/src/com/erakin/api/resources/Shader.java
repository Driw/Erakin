package com.erakin.api.resources;

import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glIsProgram;
import static org.lwjgl.opengl.GL20.glIsShader;
import static org.lwjgl.opengl.GL20.glUseProgram;

import org.diverproject.util.ObjectDescription;

import com.erakin.api.resources.shader.ShaderRender;

/**
 * <h1>Computa��o Gr�fica</h1>
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
	 * C�digo de identifica��o para desativar o uso de computa��o gr�fica.
	 */
	public static final int NO_SHADER = 0;

	/**
	 * Constr�i uma nova computa��o gr�fica a partir de uma computa��o gr�fica ra�z especifica.
	 * @param root computa��o gr�fica ra�z que ser� usada para criar a computa��o gr�fica.
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
