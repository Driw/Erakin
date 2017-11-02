package com.erakin.api.render;

import com.erakin.api.resources.world.World;

/**
 * <h1>Mundo Renderiz�vel</h1>
 *
 * <p>Objetos que implementem esta interface determina que o mesmo possui conte�do suficiente para renderizar um mundo.
 * Dever� implementar alguns m�todos b�sicos que � utilizado por um renderizador de mundos padr�o da Engine.
 * Caso o renderizador n�o seja de base padr�o essa interface pode n�o ser suficiente para renderizar o mundo.</p>
 *
 * @see TerrainRender
 * @see World
 *
 * @author Andrew
 */

public interface WorldRender
{
	/**
	 * Nome dos mundos normalmente s�o grandes e ID s�o gerados automaticamente.
	 * Isso torna o mundo dif�cil de ser identificado pelo desenvolvedor de jogo.
	 * Para isso � usado um pr�-fixo que � configurado no arquivo de defini��es do mundo.
	 * @return aquisi��o do pr�-fixo do mundo utiliz�-lo para facilitar sua identifica��o.
	 */

	String getPrefix();

	/**
	 * Unidade � usada durante o carregamento de um terreno para definir o dimensionamento.
	 * Esse dimensionamento se refere a uma �nica c�lula (geralmente quadrados) do terreno.
	 * @return aquisi��o do tamanho de cada unidade (c�lula) do terreno.
	 */

	float getUnitSize();

	/**
	 * Quanto um terreno � carregado, � necess�rio que este possua um tamanho pr�-definido.
	 * Esse m�todo permite saber qual o tamanho que o terreno deve possuir em um dos lados.
	 * @return aquisi��o da quantidade de c�lulas um terreno ter� no eixo da longitude.
	 */

	int getTerrainWidth();

	/**
	 * Quanto um terreno � carregado, � necess�rio que este possua um tamanho pr�-definido.
	 * Esse m�todo permite saber qual o tamanho que o terreno deve possuir em um dos lados.
	 * @return aquisi��o da quantidade de c�lulas um terreno ter� no eixo da latitude.
	 */

	int getTerrainLength();

	/**
	 * Mundos s�o formados por terrenos de um �nico tamanho, assim � poss�vel organiz�-los em grade.
	 * Isso facilita o mundo tamb�m a gerenciar os terrenos atrav�s de uma matriz relativa a X e Y.
	 * @return aquisi��o da quantidade de terrenos que podem ser alocados no eixo da longitude.
	 */

	int getWidth();

	/**
	 * Mundos s�o formados por terrenos de um �nico tamanho, assim � poss�vel organiz�-los em grade.
	 * Isso facilita o mundo tamb�m a gerenciar os terrenos atrav�s de uma matriz relativa a X e Y.
	 * @return aquisi��o da quantidade de terrenos que podem ser alocados no eixo da latitude.
	 */

	int getLength();

	/**
	 * Outra forma de se usar os terrenos al�m de vincul�-los � usando o m�todo de get.
	 * Procedimento usado para obter diretamente o objeto terreno contendo informa��es sobre.
	 * @param xTerrain coordenada para obter o terreno em rela��o ao eixo da latitude.
	 * @param zTerrain coordenada para obter o terreno em rela��o ao eixo da longitude.
	 * @return refer�ncia do terreno renderiz�vel alocado na coordenada passada acima.
	 */

	TerrainRender getTerrain(int xTerrain, int zTerrain);
}
