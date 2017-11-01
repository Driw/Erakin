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
	 * Vetor de posi��o ir� guardar informa��es da localiza��o de cada v�rtice no espa�o.
	 * Essa localiza��o concite em determinar todos os tr�s pontos no espa�o, X, Y e Z.
	 * @return aquisi��o do vetor contendo as posi��es de cada v�rtice do modelo.
	 */

	float[] getVertices();

	/**
	 * Vetor de coordenada das texturas ir� indicar qual a parte da textura que ser� usada
	 * para preencher cada face do modelo, informando as coordenadas em rela��o a X e Y.
	 * As informa��es nesse contida � feito atrav�s de valores flutuantes que variam entre
	 * 0.0 at� 1.0, assim sendo, as coordenadas s�o em porcentagem e n�o em pixels.
	 * @return aquisi��o do vetor contendo as coordenadas da textura para as faces.
	 */

	float[] getTextureCoords();

	/**
	 * Normaliza��o � indica uma falsa ilumina��o para simular colis�es e cavidades.
	 * Isso ser� usado um vetor contendo os valores flutuantes em rela��o a X, Y e Z.
	 * Para cada rela��o dessa ser� aplicado a um �nico v�rtice que forma a modelagem.
	 * @return aquisi��o do vetor contendo a normaliza��o das faces formadas.
	 */

	float[] getNormals();

	/**
	 * Vetor de �ndices permite identificar quais as conex�es entre os v�rtices.
	 * Cada face do modelo � conectada por tr�s v�rtices, que ser�o identificados
	 * atrav�s desses �ndices, assim sendo, cada tr�s posi��es se refere a uma face.
	 * @return aquisi��o do vetor contendo a conex�o dos v�rtices.
	 */

	int[] getIndices();

	/**
	 * Vetor de �ndice de texturas permite identificar quais texturas usar para cada conex�o de v�rtices.
	 * Cada tr�s v�rtices conectados poder� ter uma textura diferente utilizada se necess�rio,
	 * essa identifica��o deve ser especificada aqui, caso seja apenas uma textura n�o ter� valores.
	 * @return aquisi��o do vetor contendo a textura por conex�o de v�rtice.
	 */

	float[] getTextureIndex();
}
