package com.erakin.api.resources.world;

import java.io.File;

/**
 * <h1>Leitor de Dados para Mapa</h1>
 *
 * <p>Todo leitor de mundos dever� implementar essa interface.
 * Qualquer classe que a implemente poder� ler um mundo especifico.
 * Podendo assim ser utilizada em ResourceManager para carregar o mesmo.</p>
 *
 * @see WorldData
 *
 * @author Andrew
 */

public interface WorldReader
{
	/**
	 * Deve fazer o carregamento dos dados de um mundo de forma adequada conforme o formato.
	 * @param file refer�ncia de um objeto File contendo informa��es do arquivo.
	 * @return objeto contendo os dados necess�rios do mundo passado por arquivo.
	 * @throws WorldException ocorre por falha na leitura do mapa.
	 */

	WorldData readWorld(File file) throws WorldException;
}
