package com.erakin.api.resources.model;

import java.io.InputStream;

/**
 * <h1>Leitor de Dados para Modelagem</h1>
 *
 * <p>Todo leitor de modelagens dever� implementar essa interface.
 * Qualquer classe que a implemente poder� ler uma modelagem especifica.
 * Podendo assim ser utilizada em ResourceManager para carregar o mesmo.</p>
 *
 * @author Andrew
 */

public interface ModelReader
{
	/**
	 * Deve carregar os dados de uma determinada stream com entrada de dados.
	 * Ser� construido um objeto para dados tempor�rios da modelagem no mesmo.
	 * Esse objeto ser� retornado contendo as informa��es do modelo carregado.
	 * @param stream refer�ncia da stream com entrada de dados do modelo.
	 * @return aquisi��o do objeto contendo os dados tempor�rios da modelagem.
	 * @throws ModelException ocorre por falha na leitura do modelo.
	 */

	ModelData readModel(InputStream stream) throws ModelException;
}
