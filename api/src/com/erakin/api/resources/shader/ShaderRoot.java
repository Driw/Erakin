package com.erakin.api.resources.shader;

import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glUseProgram;

import org.diverproject.util.ObjectDescription;

import com.erakin.api.resources.ResourceRoot;

public class ShaderRoot extends ResourceRoot<Shader>
{
	/**
	 * C�digo para identifica��o da computa��o gr�fica no OpenGL.
	 */
	int id;

	/**
	 * Identifica��o do programa para computa��o gr�fica de v�rtices.
	 */
	int vertex;

	/**
	 * Identifica��o do programa para computa��o gr�fica de faces.
	 */
	int fragment;

	/**
	 * Construtor em package para permitir apenas que ShaderLoader construa um.
	 * Isso ir� garantir que um Shader Ra�z inv�lido possa ser criado na engine.
	 * @param filepath caminho do arquivo em disco com os dados do modelo carregado.
	 */

	ShaderRoot(String filepath)
	{
		super(filepath);
	}

	/**
	 * Essa identifica��o � importante para que o OpenGL possa localizar os dados da
	 * computa��o gr�fica para quando esta for utilizada poder ser chamada adequadamente.
	 * @return aquisi��o do c�digo da computa��o gr�fica no sistema de shader do OpenGL.
	 */

	public int getGLShaderID()
	{
		return id;
	}

	@Override
	public Shader genResource()
	{
		Shader shader = new Shader(this);
		addReference(shader);

		return shader;
	}

	@Override
	public void release()
	{
		super.release();

		glUseProgram(0);
		glDetachShader(id, vertex);
		glDetachShader(id, fragment);
		glDeleteShader(id);
		glDeleteShader(vertex);
		glDeleteShader(fragment);
	}

	@Override
	public void toString(ObjectDescription description)
	{
		description.append("glID", id);
		description.append("vertex", vertex);
		description.append("fragment", fragment);		
	}
}
