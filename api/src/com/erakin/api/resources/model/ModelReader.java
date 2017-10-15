package com.erakin.api.resources.model;

import java.io.InputStream;

/**
 * <h1>Leitor de Dados para Modelagem</h1>
 *
 * <p>Todo leitor de modelagens deverá implementar essa interface.
 * Qualquer classe que a implemente poderá ler uma modelagem especifica.
 * Podendo assim ser utilizada em ResourceManager para carregar o mesmo.</p>
 *
 * @author Andrew
 */

public interface ModelReader
{
	/**
	 * Deve carregar os dados de uma determinada stream com entrada de dados.
	 * Será construido um objeto para dados temporários da modelagem no mesmo.
	 * Esse objeto será retornado contendo as informações do modelo carregado.
	 * @param stream referência da stream com entrada de dados do modelo.
	 * @return aquisição do objeto contendo os dados temporários da modelagem.
	 * @throws ModelException ocorre por falha na leitura do modelo.
	 */

	ModelData readModel(InputStream stream) throws ModelException;
}
