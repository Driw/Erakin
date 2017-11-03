package com.erakin.api.resources.model;

/**
 * <h1>Dados de Modelo</h1>
 *
 * <p>Interface que irá implementar as informações básicas que todo modelo possui.
 * Esses dados irão auxiliar na criação das modelagens dos objetos/entidades no jogo.
 * As informações necessárias é a posição dos vertices, coordenada da textura,
 * referência da normalização e anexar cada índice desses vetores respectivos.</p>
 *
 * <p>Outra finalidade dessa interface é permitir a existência de outras semelhantes.
 * De modo que essas determinem novas funcionalidades de acordo com o seu propósito.</p>
 *
 * @author Andrew
 */

public interface ModelData
{
	/**
	 * Quantidade padrão de dados para formar um vértice.
	 */
	public static final int DEFAULT_VERTEX_SIZE = 3;

	/**
	 * Quantidade padrão de coordenadas de textura por vértice.
	 */
	public static final int DEFAULT_UV_SIZE = 2;

	/**
	 * Quantidade padrão de normalizações por vértice.
	 */
	public static final int DEFAULT_NORMAL_SIZE = 3;

	/**
	 * Quantidade padrão de índice de texturas por vértice.
	 */
	public static final int DEFAULT_TEXTURE_SIZE = 1;


	/**
	 * Modelos as vezes se tornam grandes de mais e uma forma de reduzir o esforço de processamento e por índices.
	 * Nem todos os modelos podem requisitar o uso de índices, se não for definido não será usado como atributo.
	 * @return aquisição do atributo para modelo que contenha a ligação dos vértices através de índices.
	 */

	ModelIndiceAttribute getIndices();

	/**
	 * Um modelo pode possuir diversas informações necessárias para que a sua forma seja criada.
	 * Os dados são separados por atributos e cada um destes vai guardar esse tipo de informação.
	 * Por tanto a posição dos vértices é um atributo em quanto as coordenadas de textura são outro.
	 * Se houver normalização ou textura múltipla, eles serão alocados em outro atributo também.
	 * @return aquisição de todos os atributos necessários para formar o modelo em questão.
	 */

	ModelAttribute[] getAttributes();

	/**
	 * Sempre que os dados do modelo são carregados um mensagem de registro é feita no console detalhando o resumo dos dados.
	 * Esse resumo deve especificar por exemplo a quantidade de vértices ou então de coordenadas de textura se houver.
	 * @return aquisição de uma {@link String} que especifique a quantidade de informações de cada atributo existente.
	 */

	String toStringDetails();
}
