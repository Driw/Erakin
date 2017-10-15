package com.erakin.api.resources;

/**
 * <h1>Carregador de Terreno</h1>
 *
 * <p>Interface usada para que seja poss�vel carregar terrenos de forma din�mica para os mundos.
 * Isso possibilita que um mundo possa ser carregado a partir de um Bitmap ou arquivos codificados.
 * Tal como ainda um arquivo zip dentre outras possibilidades conforme a seguran�a ou necessidade.</p>
 *
 * @see World
 *
 * @author Andrew
 */

public interface TerrainLoader
{
	/**
	 * Procedimento chamado sempre que o mundo encontrar um terreno do qual n�o foi carregado.
	 * A forma como o terreno � carregado n�o � padr�o, podendo ser carregado de imagens ou arquivos.
	 * Cada mundo dever� ter um carregador de terrenos pr�-definido em seu arquivo de defini��es.
	 * @param world refer�ncia do mundo do qual est� solicitando o carregamento do terreno.
	 * @param xTerrain coordenada do terreno na grade de terrenos do mundo no eixo da longitude.
	 * @param zTerrain coordenada do terreno na grade de terrenos do mundo no eixo da latitude.
	 * @return aquisi��o do terreno carregado de acordo com o mundo e coordenadas.
	 */

	Terrain load(World world, int xTerrain, int zTerrain);
}
