package com.erakin.api.resources.world;

import com.erakin.api.resources.TerrainLoader;

/**
 * <h1>Dados de Terreno</h1>
 *
 * <p>Interface usada internamente pelo carregador de mundos para tornar o carregamento din�mico.
 * Os leitores de mundos dever�o sempre retornar um objeto do qual possua essa interface em sua classe.
 * Assim, todas as informa��es b�sicas para se criar um novo mundo poder�o ser obtidas por completo.</p>
 *
 * <p>As informa��es v�o desde as descritivas como nome e pr�-fixo de reconhecimento do mundo.
 * Como algumas mais de configura��o: tamanho que cada terreno dever� ter e quantos terrenos ter�.
 * E por �ltimas as que ir�o definir o terreno de fato, como unidade de cada c�lula e carregador.</p>
 *
 * @see TerrainLoader
 *
 * @author Andrew
 */

public interface TerrainData
{
	/**
	 * Mundos s�o formados por terrenos de um �nico tamanho, para isso � necess�rio um definido.
	 * Todos os terrenos carregados devem possuir esse tamanho para evitar problemas de renderiza��o.
	 * @return aquisi��o de quantas c�lulas um terreno ter� no eixo da longitude.
	 */

	int getWidth();

	/**
	 * Mundos s�o formados por terrenos de um �nico tamanho, para isso � necess�rio um definido.
	 * Todos os terrenos carregados devem possuir esse tamanho para evitar problemas de renderiza��o.
	 * @return aquisi��o de quantas c�lulas um terreno ter� no eixo da latitude.
	 */

	int getLength();

	/**
	 * Unidade ir� definir qual o tamanho que um terreno ir� possuir para cada c�lula.
	 * O padr�o a ser usado deve ser 1.0f que representa 100% do tamanho normal.
	 * @return aquisi��o do tamanho de cada unidade (c�lula) do terreno.
	 */

	float getUnit();

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
	 * Para cada rela��o dessa ser� aplicado a um �nico v�rtice que forma o terreno.
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
	 * A matriz de altura do terreno indica o n�vel de altura ou profundidade do mesmo.
	 * O valor da altura ir� depender de como foi especificado para o mundo consider�-los.
	 * Cada �ndice da matriz ser� considerado em rela��o aos eixos X e Y de cada c�lula.
	 * @return aquisi��o da matriz contendo o valor de nivelamento do terreno (altura).
	 */

	float[][] getHeights();
}
