package com.erakin.engine.resource.world;

import java.io.File;

import org.erakin.api.buffer.Buffer;

import com.erakin.engine.resource.TerrainLoader;

/**
 * <h1>Leitor de Dados para Mapa WDS</h1>
 *
 * <p>Arquivos no formato <b>.wds</b> podem ser lidos através desse leitor como mapas.
 * Esses arquivos possuem uma estrutura simples porém codificada em bytes dos dados do mundo.
 * Quando for usar esse carregador, o carregador de terreno não deve possuir parâmetros extras.</p>
 *
 * <p>O formado desse arquivo segue como: {int:width}{int:length}{int:terrain_width}{int:terrain_length}
 * {float:unit}{int:name_length}{char[]:name}{int:prefix_length}{char[]:prefix}
 * {int:folder:length}{char[]:folder}{int:loader_class_length}{char[]:loader_class_path}</p>
 *
 * @see WorldReaderDefault
 * @see WorldData
 * @see Buffer
 *
 * @author Andrew
 */

public class WorldReaderWDS extends WorldReaderDefault
{
	@Override
	public WorldData parse(Buffer buffer, File file) throws WorldException
	{
		int width = buffer.getInt();
		int length = buffer.getInt();
		int terrainWidth = buffer.getInt();
		int terrainLength = buffer.getInt();
		float unit = buffer.getFloat();
		String name = buffer.getString();
		String prefix = buffer.getString();
		String folder = buffer.getString();

		WorldDataDefault data = new WorldDataDefault(width, length, unit);
		data.setTerrainSize(terrainWidth, terrainLength);
		data.setName(name);
		data.setPrefix(prefix);
		data.setFolder(folder);

		String classPath = buffer.getString();

		try {

			Class<?> loaderClass = Class.forName(classPath);
			Object loaderInstance = loaderClass.newInstance();

			if (!(loaderInstance instanceof TerrainLoader))
				throw new WorldException("'%s' não é um TerrainLoader");

			TerrainLoader loader = (TerrainLoader) loaderInstance;
			data.setLoader(loader);

		} catch (ClassNotFoundException e) {
			throw new WorldException(e, "observador '%s' não encontrado", classPath);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new WorldException(e, "falha ao instanciar observador '%s'", classPath);
		}

		return data;
	}
}
