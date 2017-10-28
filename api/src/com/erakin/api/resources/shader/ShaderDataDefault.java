package com.erakin.api.resources.shader;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.SizeUtil;

/**
 * <h1>Dados Tempor�rios para Computa��o Gr�fica</h1>
 *
 * <p>Por ser uma classe padr�o, implementa as funcionalidades b�sicas para referenciar os dados de uma computa��o gr�fica.
 * Os dados m�nimos para definir a programa��o � a codifica��o em si de dois poss�veis programas: v�rtices e fragmentos.
 * Para isso StringBuilder � usado como armazenamento do c�digo, j� que � o requisitado pelo pr�prio OpenGL.</p>
 *
 * @see ShaderData
 * @see StringBuilder
 *
 * @author Andrew
 */

public class ShaderDataDefault implements ShaderData
{
	/**
	 * Armazenamento da codifica��o do programa para v�rtices.
	 */
	private StringBuilder vertexProgram;

	/**
	 * Armazenamento da codifica��o do programa para fragmentos.
	 */
	private StringBuilder fragmentProgram;

	/**
	 * Cria uma nova inst�ncia de armazenamento de uma programa��o para computa��o gr�fica.
	 * Inicializa o armazenamento das duas programa��es dispon�veis (v�rtices e fragmentos).
	 */

	public ShaderDataDefault()
	{
		vertexProgram = new StringBuilder();
		fragmentProgram = new StringBuilder();
	}

	@Override
	public StringBuilder getVertexProgram()
	{
		return vertexProgram;
	}

	@Override
	public StringBuilder getFragmentProgram()
	{
		return fragmentProgram;
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("vertexProgram", SizeUtil.toString(vertexProgram.length()));
		description.append("fragmentProgram", SizeUtil.toString(fragmentProgram.length()));

		return description.toString();
	}
}
