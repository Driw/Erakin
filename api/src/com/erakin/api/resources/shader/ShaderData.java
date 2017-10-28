package com.erakin.api.resources.shader;

/**
 * <h1>Dados para Computação Gráfica</h1>
 *
 * <p>Interface que irá implementar as informações básicas que toda computação gráfica possui.
 * Esses dados irão auxiliar na criação dos programas que realizam a computação gráfica no jogo.
 * A única informações necessária para tal é uma String contendo o código da programação.</p>
 *
 * <p>Outra finalidade dessa interface é permitir a existência de outras semelhantes.
 * De modo que essas determinem novas funcionalidades de acordo com o seu propósito.</p>
 *
 * @author Andrew
 */

public interface ShaderData
{
	/**
	 * O programa determina sequência de comandos para realização da computação gráfica.
	 * @return aquisição do programa para computação gráfica.
	 */

	StringBuilder getVertexProgram();

	/**
	 * O programa determina sequência de comandos para realização da computação gráfica.
	 * @return aquisição do programa para computação gráfica.
	 */

	StringBuilder getFragmentProgram();
}
