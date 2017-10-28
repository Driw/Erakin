package com.erakin.api.resources.shader;

import org.diverproject.util.stream.Input;

/**
 * <h1>Leitor de Dados para Computação Gráfica</h1>
 *
 * <p>Todo leitor de computação gráfica deverá implementar essa interface.
 * Qualquer classe que a implemente poderá ler uma computação gráfica especifica.
 * Podendo assim ser utilizada em ResourceManager para carregar o mesmo.</p>
 *
 * @author Andrew
 */

public interface ShaderReader
{
	/**
	 * Deve carregar os dados de uma determinada stream com entrada de dados.
	 * Será construído um objeto para dados temporários da computação gráfica no mesmo.
	 * Esse objeto será retornado contendo as informações de programação carregadas.
	 * @param input referência da stream com entrada de dados da computação gráfica.
	 * @return aquisição do objeto contendo os dados temporários da programação.
	 * @throws ShaderException ocorre por falha na leitura da programação.
	 */

	ShaderData readShader(Input input) throws ShaderException;
}
