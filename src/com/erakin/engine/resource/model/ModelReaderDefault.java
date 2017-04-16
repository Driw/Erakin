package com.erakin.engine.resource.model;

import java.io.InputStream;

/**
 * <h1>Leitor de Modelagem Padrão</h1>
 *
 * <p>Leitor de modelagem é a implementação de um leitor de dados para modelagens.
 * Implementando todos os getters e definindo os atributos que são em comum.
 * Assim, todos os tipos de leitura de modelagens serão implementados facilmente.</p>
 *
 * @see ModelReader
 *
 * @author Andrew
 */

public abstract class ModelReaderDefault implements ModelReader
{
	/**
	 * Objeto para armazenamento temporário dos dados da modelagem carregada.
	 */
	protected ModelDataDefault data;

	/**
	 * Constrói um novo carregador de modelagens padrão iniciando o armazenador de dados.
	 * O armazenador de dados se refere a um objeto para armazenar temporariamente os dados.
	 * Quanto aos dados são referentes a posição dos vértices, texturas, normalizações e índices.
	 */

	public ModelReaderDefault()
	{
		data = new ModelDataDefault();
	}

	@Override
	public ModelDataDefault readModel(InputStream stream) throws ModelException
	{
		subLoadModel(stream);

		return data;
	}

	/**
	 * Será chamado assim que for solicitado para fazer o carregamento da modelagem tri-dimensional.
	 * Deverá armazenar todo e qualquer dado da modelagem carregada no objeto <code>data</code>.
	 * Esse objeto será retornado com os dados para o carregador e irá criar a modelagem de acordo.
	 * @param stream referência da stream usada com os dados da modelagem a ser carregada.
	 * @throws ModelException apenas se houver algum problema durante o carregamento.
	 */

	protected void subLoadModel(InputStream stream) throws ModelException
	{
		
	}
}
