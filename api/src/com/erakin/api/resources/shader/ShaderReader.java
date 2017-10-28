package com.erakin.api.resources.shader;

import org.diverproject.util.stream.Input;

/**
 * <h1>Leitor de Dados para Computa��o Gr�fica</h1>
 *
 * <p>Todo leitor de computa��o gr�fica dever� implementar essa interface.
 * Qualquer classe que a implemente poder� ler uma computa��o gr�fica especifica.
 * Podendo assim ser utilizada em ResourceManager para carregar o mesmo.</p>
 *
 * @author Andrew
 */

public interface ShaderReader
{
	/**
	 * Deve carregar os dados de uma determinada stream com entrada de dados.
	 * Ser� constru�do um objeto para dados tempor�rios da computa��o gr�fica no mesmo.
	 * Esse objeto ser� retornado contendo as informa��es de programa��o carregadas.
	 * @param input refer�ncia da stream com entrada de dados da computa��o gr�fica.
	 * @return aquisi��o do objeto contendo os dados tempor�rios da programa��o.
	 * @throws ShaderException ocorre por falha na leitura da programa��o.
	 */

	ShaderData readShader(Input input) throws ShaderException;
}
