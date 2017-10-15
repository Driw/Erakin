package com.erakin.api.resources;

/**
 * <h1>Carregador de Terreno</h1>
 *
 * <p>Interface usada para que seja possível carregar terrenos de forma dinâmica para os mundos.
 * Isso possibilita que um mundo possa ser carregado a partir de um Bitmap ou arquivos codificados.
 * Tal como ainda um arquivo zip dentre outras possibilidades conforme a segurança ou necessidade.</p>
 *
 * @see World
 *
 * @author Andrew
 */

public interface TerrainLoader
{
	/**
	 * Procedimento chamado sempre que o mundo encontrar um terreno do qual não foi carregado.
	 * A forma como o terreno é carregado não é padrão, podendo ser carregado de imagens ou arquivos.
	 * Cada mundo deverá ter um carregador de terrenos pré-definido em seu arquivo de definições.
	 * @param world referência do mundo do qual está solicitando o carregamento do terreno.
	 * @param xTerrain coordenada do terreno na grade de terrenos do mundo no eixo da longitude.
	 * @param zTerrain coordenada do terreno na grade de terrenos do mundo no eixo da latitude.
	 * @return aquisição do terreno carregado de acordo com o mundo e coordenadas.
	 */

	Terrain load(World world, int xTerrain, int zTerrain);
}
