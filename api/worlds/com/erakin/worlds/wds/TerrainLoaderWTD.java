package com.erakin.worlds.wds;

import java.awt.image.BufferedImage;
import java.io.File;

import com.erakin.api.buffer.Buffer;
import com.erakin.api.buffer.BufferInput;
import com.erakin.api.resources.Model;
import com.erakin.api.resources.ModelLoader;
import com.erakin.api.resources.Terrain;
import com.erakin.api.resources.TerrainLoader;
import com.erakin.api.resources.World;
import com.erakin.api.resources.model.ModelDataDefault;
import com.erakin.api.resources.world.WorldRuntimeException;

/**
 * <h1>Carregador de Terreno com WTD</h1>
 *
 * <p>Os terrenos carregados em formatos de arquivos do tipo WTD são dados em bytes diretos.
 * Por serem dados diretos em bytes o carregamento do mundo se torna mais fácil através de stream.
 * A formatação do arquivo consiste em especificar os vértices, texturas, normalizações e faces.</p>
 *
 * <p>De uma forma mais detalhada os dados ficam da seguinte forma:<br>
 * {int|vertex_count}{int[]|vertices_array}{int|texture_coords_count}{float[]|texture_coords_array}
 * {int|normals_count}{float[]|normals_array}{int|face_count}{int[]|vertices_of_faces}</p>
 *
 * <p>Esse tipo de arquivo tende a ter um peso maior se comparado a terrenos em BMP por especificar
 * completamente a posição de cada vértice, normalização e face no mesmo, porém é carregada mais
 * rapidamente por não ser necessário realizar qualquer operação de cálculo para tal.</p>
 *
 * <p>TODO: atualizações futuras deverão implementar alguma forma de especificar a textura usada.</p>
 *
 * @see Terrain
 * @see TerrainLoader
 * @see BufferedImage
 * @see World
 *
 * @author Andrew
 */

public class TerrainLoaderWTD implements TerrainLoader
{
	@Override
	public Terrain load(World world, int xTerrain, int yTerrain)
	{
		String filepath = world.getTerrainFilepath();
		File file = new File(filepath);

		if (!file.isDirectory())
			throw new WorldRuntimeException("terreno não encontrado (mundo: %d, %dx %y)", world.getID(), xTerrain, yTerrain);

		String terrainPath = String.format("%s/terrain_%d-%d.wtd", filepath, xTerrain, yTerrain);

		int width = world.getTerrainWidth();
		int length = world.getTerrainLength();

		File terrainFile = new File(terrainPath);
		Buffer buffer = new BufferInput(terrainFile);
		Terrain terrain = new Terrain(xTerrain, yTerrain, width, length);

		Model model = createModel(terrain, buffer, terrainPath);
		terrain.setModel(model);

		return terrain;
	}

	/**
	 * Procedimento interno para criar e salvar a modelagem do terreno tal como ler os dados.
	 * @param terrain referência do terreno que foi criado no método principal do carregador.
	 * @param buffer referência do buffer contendo os dados do arquivo WTD que foi lido.
	 * @param terrainPath caminho parcial ou completo do terreno para identificar no ModelLoader.
	 * @return aquisição de uma modelagem criada para representar o terreno na renderização.
	 */

	private Model createModel(Terrain terrain, Buffer buffer, String terrainPath)
	{
		int vertexCount = buffer.getInt();
		float vertices[] = new float[vertexCount];
		Buffer.load(buffer, vertices);

		int textureCount = buffer.getInt();
		float textures[] = new float[textureCount];
		Buffer.load(buffer, textures);

		int normalCount = buffer.getInt();
		float normals[] = new float[normalCount];
		Buffer.load(buffer, normals);

		int faceCount = buffer.getInt();
		int faces[] = new int[faceCount];
		Buffer.load(buffer, faces);

		ModelDataDefault data = new ModelDataDefault();
		data.init(vertexCount, textureCount, normalCount, faceCount);

		for (int vertex = 0, offset = 0; vertex < vertexCount; vertex++, offset += 3)
			data.setVertice(vertexCount, vertices[offset], vertices[offset + 1], vertices[offset + 2]);

		for (int vertex = 0, offset = 0; vertex < textureCount; vertex++, offset+= 2)
			data.setTexture(vertex, textures[offset], textures[offset + 1]);

		for (int vertex = 0, offset = 0; vertex < normalCount; vertex++, offset+= 3)
			data.setNormal(vertexCount, normals[offset], normals[offset + 1], normals[offset + 2]);

		for (int index = 0; index < faceCount; index++)
			data.setIndice(index, faces[index]);

		ModelLoader modelLoader = ModelLoader.getIntance();
		Model model = modelLoader.createModel(terrainPath, data);

		return model;
	}
}
