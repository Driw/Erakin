package com.erakin.api.resources.world;

import java.io.File;

/**
 * <h1>Leitor de Dados para Mapa</h1>
 *
 * <p>Todo leitor de mundos deverá implementar essa interface.
 * Qualquer classe que a implemente poderá ler um mundo especifico.
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
	 * @param file referência de um objeto File contendo informações do arquivo.
	 * @return objeto contendo os dados necessários do mundo passado por arquivo.
	 * @throws WorldException ocorre por falha na leitura do mapa.
	 */

	WorldData readWorld(File file) throws WorldException;
}
