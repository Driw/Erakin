package com.erakin.engine.resource.model;

import java.io.InputStream;

/**
 * <h1>Leitor de Modelagem Padr�o</h1>
 *
 * <p>Leitor de modelagem � a implementa��o de um leitor de dados para modelagens.
 * Implementando todos os getters e definindo os atributos que s�o em comum.
 * Assim, todos os tipos de leitura de modelagens ser�o implementados facilmente.</p>
 *
 * @see ModelReader
 *
 * @author Andrew
 */

public abstract class ModelReaderDefault implements ModelReader
{
	/**
	 * Objeto para armazenamento tempor�rio dos dados da modelagem carregada.
	 */
	protected ModelDataDefault data;

	/**
	 * Constr�i um novo carregador de modelagens padr�o iniciando o armazenador de dados.
	 * O armazenador de dados se refere a um objeto para armazenar temporariamente os dados.
	 * Quanto aos dados s�o referentes a posi��o dos v�rtices, texturas, normaliza��es e �ndices.
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
	 * Ser� chamado assim que for solicitado para fazer o carregamento da modelagem tri-dimensional.
	 * Dever� armazenar todo e qualquer dado da modelagem carregada no objeto <code>data</code>.
	 * Esse objeto ser� retornado com os dados para o carregador e ir� criar a modelagem de acordo.
	 * @param stream refer�ncia da stream usada com os dados da modelagem a ser carregada.
	 * @throws ModelException apenas se houver algum problema durante o carregamento.
	 */

	protected void subLoadModel(InputStream stream) throws ModelException
	{
		
	}
}
