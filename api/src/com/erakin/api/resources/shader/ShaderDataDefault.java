package com.erakin.api.resources.shader;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.SizeUtil;

/**
 * <h1>Dados Temporários para Computação Gráfica</h1>
 *
 * <p>Por ser uma classe padrão, implementa as funcionalidades básicas para referenciar os dados de uma computação gráfica.
 * Os dados mínimos para definir a programação é a codificação em si de dois possíveis programas: vértices e fragmentos.
 * Para isso StringBuilder é usado como armazenamento do código, já que é o requisitado pelo próprio OpenGL.</p>
 *
 * @see ShaderData
 * @see StringBuilder
 *
 * @author Andrew
 */

public class ShaderDataDefault implements ShaderData
{
	/**
	 * Armazenamento da codificação do programa para vértices.
	 */
	private StringBuilder vertexProgram;

	/**
	 * Armazenamento da codificação do programa para fragmentos.
	 */
	private StringBuilder fragmentProgram;

	/**
	 * Cria uma nova instância de armazenamento de uma programação para computação gráfica.
	 * Inicializa o armazenamento das duas programações disponíveis (vértices e fragmentos).
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
