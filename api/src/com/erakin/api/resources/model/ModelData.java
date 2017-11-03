package com.erakin.api.resources.model;

/**
 * <h1>Dados de Modelo</h1>
 *
 * <p>Interface que ir� implementar as informa��es b�sicas que todo modelo possui.
 * Esses dados ir�o auxiliar na cria��o das modelagens dos objetos/entidades no jogo.
 * As informa��es necess�rias � a posi��o dos vertices, coordenada da textura,
 * refer�ncia da normaliza��o e anexar cada �ndice desses vetores respectivos.</p>
 *
 * <p>Outra finalidade dessa interface � permitir a exist�ncia de outras semelhantes.
 * De modo que essas determinem novas funcionalidades de acordo com o seu prop�sito.</p>
 *
 * @author Andrew
 */

public interface ModelData
{
	/**
	 * Quantidade padr�o de dados para formar um v�rtice.
	 */
	public static final int DEFAULT_VERTEX_SIZE = 3;

	/**
	 * Quantidade padr�o de coordenadas de textura por v�rtice.
	 */
	public static final int DEFAULT_UV_SIZE = 2;

	/**
	 * Quantidade padr�o de normaliza��es por v�rtice.
	 */
	public static final int DEFAULT_NORMAL_SIZE = 3;

	/**
	 * Quantidade padr�o de �ndice de texturas por v�rtice.
	 */
	public static final int DEFAULT_TEXTURE_SIZE = 1;


	/**
	 * Modelos as vezes se tornam grandes de mais e uma forma de reduzir o esfor�o de processamento e por �ndices.
	 * Nem todos os modelos podem requisitar o uso de �ndices, se n�o for definido n�o ser� usado como atributo.
	 * @return aquisi��o do atributo para modelo que contenha a liga��o dos v�rtices atrav�s de �ndices.
	 */

	ModelIndiceAttribute getIndices();

	/**
	 * Um modelo pode possuir diversas informa��es necess�rias para que a sua forma seja criada.
	 * Os dados s�o separados por atributos e cada um destes vai guardar esse tipo de informa��o.
	 * Por tanto a posi��o dos v�rtices � um atributo em quanto as coordenadas de textura s�o outro.
	 * Se houver normaliza��o ou textura m�ltipla, eles ser�o alocados em outro atributo tamb�m.
	 * @return aquisi��o de todos os atributos necess�rios para formar o modelo em quest�o.
	 */

	ModelAttribute[] getAttributes();

	/**
	 * Sempre que os dados do modelo s�o carregados um mensagem de registro � feita no console detalhando o resumo dos dados.
	 * Esse resumo deve especificar por exemplo a quantidade de v�rtices ou ent�o de coordenadas de textura se houver.
	 * @return aquisi��o de uma {@link String} que especifique a quantidade de informa��es de cada atributo existente.
	 */

	String toStringDetails();
}
