package com.erakin.api.render;

import com.erakin.api.resources.world.World;

/**
 * <h1>Mundo Renderizável</h1>
 *
 * <p>Objetos que implementem esta interface determina que o mesmo possui conteúdo suficiente para renderizar um mundo.
 * Deverá implementar alguns métodos básicos que é utilizado por um renderizador de mundos padrão da Engine.
 * Caso o renderizador não seja de base padrão essa interface pode não ser suficiente para renderizar o mundo.</p>
 *
 * @see TerrainRender
 * @see World
 *
 * @author Andrew
 */

public interface WorldRender
{
	/**
	 * Nome dos mundos normalmente são grandes e ID são gerados automaticamente.
	 * Isso torna o mundo difícil de ser identificado pelo desenvolvedor de jogo.
	 * Para isso é usado um pré-fixo que é configurado no arquivo de definições do mundo.
	 * @return aquisição do pré-fixo do mundo utilizá-lo para facilitar sua identificação.
	 */

	String getPrefix();

	/**
	 * Unidade é usada durante o carregamento de um terreno para definir o dimensionamento.
	 * Esse dimensionamento se refere a uma única célula (geralmente quadrados) do terreno.
	 * @return aquisição do tamanho de cada unidade (célula) do terreno.
	 */

	float getUnitSize();

	/**
	 * Quanto um terreno é carregado, é necessário que este possua um tamanho pré-definido.
	 * Esse método permite saber qual o tamanho que o terreno deve possuir em um dos lados.
	 * @return aquisição da quantidade de células um terreno terá no eixo da longitude.
	 */

	int getTerrainWidth();

	/**
	 * Quanto um terreno é carregado, é necessário que este possua um tamanho pré-definido.
	 * Esse método permite saber qual o tamanho que o terreno deve possuir em um dos lados.
	 * @return aquisição da quantidade de células um terreno terá no eixo da latitude.
	 */

	int getTerrainLength();

	/**
	 * Mundos são formados por terrenos de um único tamanho, assim é possível organizá-los em grade.
	 * Isso facilita o mundo também a gerenciar os terrenos através de uma matriz relativa a X e Y.
	 * @return aquisição da quantidade de terrenos que podem ser alocados no eixo da longitude.
	 */

	int getWidth();

	/**
	 * Mundos são formados por terrenos de um único tamanho, assim é possível organizá-los em grade.
	 * Isso facilita o mundo também a gerenciar os terrenos através de uma matriz relativa a X e Y.
	 * @return aquisição da quantidade de terrenos que podem ser alocados no eixo da latitude.
	 */

	int getLength();

	/**
	 * Outra forma de se usar os terrenos além de vinculá-los é usando o método de get.
	 * Procedimento usado para obter diretamente o objeto terreno contendo informações sobre.
	 * @param xTerrain coordenada para obter o terreno em relação ao eixo da latitude.
	 * @param zTerrain coordenada para obter o terreno em relação ao eixo da longitude.
	 * @return referência do terreno renderizável alocado na coordenada passada acima.
	 */

	TerrainRender getTerrain(int xTerrain, int zTerrain);
}
