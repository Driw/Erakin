package com.erakin.api.resources.world;

import com.erakin.api.resources.TerrainLoader;

/**
 * <h1>Dados de Terreno</h1>
 *
 * <p>Interface usada internamente pelo carregador de mundos para tornar o carregamento dinâmico.
 * Os leitores de mundos deverão sempre retornar um objeto do qual possua essa interface em sua classe.
 * Assim, todas as informações básicas para se criar um novo mundo poderão ser obtidas por completo.</p>
 *
 * <p>As informações vão desde as descritivas como nome e pré-fixo de reconhecimento do mundo.
 * Como algumas mais de configuração: tamanho que cada terreno deverá ter e quantos terrenos terá.
 * E por últimas as que irão definir o terreno de fato, como unidade de cada célula e carregador.</p>
 *
 * @see TerrainLoader
 *
 * @author Andrew
 */

public interface TerrainData
{
	/**
	 * Mundos são formados por terrenos de um único tamanho, para isso é necessário um definido.
	 * Todos os terrenos carregados devem possuir esse tamanho para evitar problemas de renderização.
	 * @return aquisição de quantas células um terreno terá no eixo da longitude.
	 */

	int getWidth();

	/**
	 * Mundos são formados por terrenos de um único tamanho, para isso é necessário um definido.
	 * Todos os terrenos carregados devem possuir esse tamanho para evitar problemas de renderização.
	 * @return aquisição de quantas células um terreno terá no eixo da latitude.
	 */

	int getLength();

	/**
	 * Unidade irá definir qual o tamanho que um terreno irá possuir para cada célula.
	 * O padrão a ser usado deve ser 1.0f que representa 100% do tamanho normal.
	 * @return aquisição do tamanho de cada unidade (célula) do terreno.
	 */

	float getUnit();

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
	 * Para cada relação dessa será aplicado a um único vértice que forma o terreno.
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
	 * A matriz de altura do terreno indica o nível de altura ou profundidade do mesmo.
	 * O valor da altura irá depender de como foi especificado para o mundo considerá-los.
	 * Cada índice da matriz será considerado em relação aos eixos X e Y de cada célula.
	 * @return aquisição da matriz contendo o valor de nivelamento do terreno (altura).
	 */

	float[][] getHeights();
}
