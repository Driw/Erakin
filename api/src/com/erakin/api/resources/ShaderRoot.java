package com.erakin.api.resources;

import org.diverproject.util.ObjectDescription;

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
	 * Constr�i uma nova textura ra�z a partir de um caminho em disco especificado.
	 * Esse caminho dever� ser respectivo ao diret�rio de recursos e da textura.
	 */

	ShaderRoot()
	{
		super();
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
	public void toString(ObjectDescription description)
	{
		description.append("glID", id);
		description.append("vertex", vertex);
		description.append("fragment", fragment);		
	}
}
