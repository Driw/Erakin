package com.erakin.api.resources;

import org.diverproject.util.ObjectDescription;

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
	 * Constrói uma nova textura raíz a partir de um caminho em disco especificado.
	 * Esse caminho deverá ser respectivo ao diretório de recursos e da textura.
	 */

	ShaderRoot()
	{
		super();
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
	public void toString(ObjectDescription description)
	{
		description.append("glID", id);
		description.append("vertex", vertex);
		description.append("fragment", fragment);		
	}
}
