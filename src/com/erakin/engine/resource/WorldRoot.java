package com.erakin.engine.resource;

import java.io.File;

import org.diverproject.util.ObjectDescription;

import com.erakin.engine.resource.world.TerrainDimension;

/**
 * <h1>Mundo Raíz</h1>
 *
 * <p>Essa raíz é permite a construção de novos recursos do tipo mundos (terrenos).
 * Como outra raíz qualquer é necessário o conhecimento de algumas informações básicas.
 * Para tal uma identificação, tamanho de acordo com a unidade de medida usada.
 * E o mais importante de tudo a referência em File da pasta contendo seus arquivos.</p>
 *
 * @see ResourceRoot
 *
 * @author Andrew
 */

public class WorldRoot extends ResourceRoot
{
	/**
	 * Pré-fixo para facilitar a identificação do mundo.
	 */
	String prefix;

	/**
	 * Código de identificação do mundo no sistema da engine.
	 */
	int id;

	/**
	 * Quantidade de terrenos que podem ser alocados no eixo da longitude.
	 */
	int width;

	/**
	 * Quantidade de terrenos que podem ser alocados no eixo da latitude.
	 */
	int length;

	/**
	 * Tamanho de cada unidade (célula) do terreno.
	 */
	float unit;

	/**
	 * Pasta da onde se encontra os arquivos dos terrenos.
	 */
	File folder;

	/**
	 * Carregador de terrenos a ser usado quando necessário.
	 */
	TerrainLoader terrainLoader;

	/**
	 * Dimensão em que todos os terrenos devem seguir para estar nesse mundo.
	 */
	TerrainDimension terrainDimension;

	@Override
	public World genResource()
	{
		World map = new World(this);
		map.setWorldTerrainLoader(terrainLoader);
		addReference(map);

		return map;
	}

	@Override
	public void toString(ObjectDescription description)
	{
		description.append("id", id);
		description.append("width", width);
		description.append("height", length);
		description.append("folder", folder == null ? null : folder.getPath());
	}
}
