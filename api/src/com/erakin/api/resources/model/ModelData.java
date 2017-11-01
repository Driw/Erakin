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
	 * Vetor de posição irá guardar informações da localização de cada vértice no espaço.
	 * Essa localização concite em determinar todos os três pontos no espaço, X, Y e Z.
	 * @return aquisição do vetor contendo as posições de cada vértice do modelo.
	 */

	float[] getVertices();

	/**
	 * Vetor de coordenada das texturas irá indicar qual a parte da textura que será usada
	 * para preencher cada face do modelo, informando as coordenadas em relação a X e Y.
	 * As informações nesse contida é feito através de valores flutuantes que variam entre
	 * 0.0 até 1.0, assim sendo, as coordenadas são em porcentagem e não em pixels.
	 * @return aquisição do vetor contendo as coordenadas da textura para as faces.
	 */

	float[] getTextureCoords();

	/**
	 * Normalização é indica uma falsa iluminação para simular colisões e cavidades.
	 * Isso será usado um vetor contendo os valores flutuantes em relação a X, Y e Z.
	 * Para cada relação dessa será aplicado a um único vértice que forma a modelagem.
	 * @return aquisição do vetor contendo a normalização das faces formadas.
	 */

	float[] getNormals();

	/**
	 * Vetor de índices permite identificar quais as conexões entre os vértices.
	 * Cada face do modelo é conectada por três vértices, que serão identificados
	 * através desses índices, assim sendo, cada três posições se refere a uma face.
	 * @return aquisição do vetor contendo a conexão dos vértices.
	 */

	int[] getIndices();

	/**
	 * Vetor de índice de texturas permite identificar quais texturas usar para cada conexão de vértices.
	 * Cada três vértices conectados poderá ter uma textura diferente utilizada se necessário,
	 * essa identificação deve ser especificada aqui, caso seja apenas uma textura não terá valores.
	 * @return aquisição do vetor contendo a textura por conexão de vértice.
	 */

	float[] getTextureIndex();
}
