package com.erakin.api.resources.shader;

import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glUseProgram;

import org.diverproject.util.ObjectDescription;

import com.erakin.api.resources.ResourceRoot;

public class ShaderRoot extends ResourceRoot<Shader>
{
	/**
	 * Código para identificação da computação gráfica no OpenGL.
	 */
	int id;

	/**
	 * Identificação do programa para computação gráfica de vértices.
	 */
	int vertex;

	/**
	 * Identificação do programa para computação gráfica de faces.
	 */
	int fragment;

	/**
	 * Construtor em package para permitir apenas que ShaderLoader construa um.
	 * Isso irá garantir que um Shader Raíz inválido possa ser criado na engine.
	 * @param filepath caminho do arquivo em disco com os dados do modelo carregado.
	 */

	ShaderRoot(String filepath)
	{
		super(filepath);
	}

	/**
	 * Essa identificação é importante para que o OpenGL possa localizar os dados da
	 * computação gráfica para quando esta for utilizada poder ser chamada adequadamente.
	 * @return aquisição do código da computação gráfica no sistema de shader do OpenGL.
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
