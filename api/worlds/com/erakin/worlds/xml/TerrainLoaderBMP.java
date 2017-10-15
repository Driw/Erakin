package com.erakin.worlds.xml;

import static org.diverproject.log.LogSystem.logWarning;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector3f;

import com.erakin.api.resources.Model;
import com.erakin.api.resources.ModelLoader;
import com.erakin.api.resources.Terrain;
import com.erakin.api.resources.TerrainLoader;
import com.erakin.api.resources.Texture;
import com.erakin.api.resources.TextureLoader;
import com.erakin.api.resources.World;
import com.erakin.api.resources.model.ModelDataDefault;
import com.erakin.api.resources.texture.TextureException;
import com.erakin.api.resources.world.TerrainRuntimeException;

/**
 * <h1>Carregador de Terreno com Bitmap</h1>
 *
 * <p>Terrenos carregados nesse formato devem possuir seus terrenos armazenados em imagens BMP.
 * Esse arquivo BMP dever� ainda ser de formato escala cinza (TYPE_BYTE_GRAY) para ser v�lido.
 * Para que terrenos nesse formato funcionem � necess�rio ter uma margem de pixels repetidos.
 * A margem � de 1 pixel na parte superior e na esquerda, e 2 pixels na parte inferior e na direita.</p>
 *
 * <p>TODO: atualiza��es futuras dever�o implementar alguma forma de especificar a textura usada.</p>
 *
 * @see Terrain
 * @see TerrainLoader
 * @see BufferedImage
 * @see World
 *
 * @author Andrew
 */

public class TerrainLoaderBMP implements TerrainLoader
{
	/**
	 * Refer�ncia do terreno que foi gerado no carregador.
	 */
	private Terrain terrain;

	/**
	 * Refer�ncia da imagem BMP carregada para facilitar o uso no carregador.
	 */
	private BufferedImage image;

	@Override
	public Terrain load(World world, int xTerrain, int zTerrain)
	{
		String filepath = world.getTerrainFilepath();
		String terrainPath = makeTerrainPath(filepath, xTerrain, zTerrain);
		File file = new File(filepath);

		if (!file.isDirectory())
			throw new TerrainRuntimeException("terreno n�o encontrado (mundo: %d, %dx %y)",
					world.getID(), xTerrain, zTerrain);

		image = loadImage(filepath, xTerrain, zTerrain);

		if (image.getType() != BufferedImage.TYPE_BYTE_GRAY)
			throw new TerrainRuntimeException("n�o � escala cinza (path: %s)", terrainPath);

		int width = world.getTerrainWidth();
		int length = world.getTerrainLength();

		if (image.getWidth() != width + 3)
			throw new TerrainRuntimeException("largura inv�lida (path: %s)", terrainPath);

		if (image.getHeight() != length + 3)
			throw new TerrainRuntimeException("comprimento inv�lido (path: %s)", terrainPath);

		try {

			Texture texture = TextureLoader.getIntance().getTexture("dirt");
			terrain = new Terrain(xTerrain, zTerrain, width, length);

			Model model = createModel(world, terrainPath);
			model.setTexture(texture);

			terrain.setModel(model);

			return terrain;

		} catch (TextureException e) {
			throw new TerrainRuntimeException("falha ao definir textura de nivelamento (�path: %s)", terrainPath);
		}
	}

	/**
	 * Carrega uma imagem de acordo com o caminho da pasta que a cont�m.
	 * A imagem dever� estar no formato especificado na documenta��o do m�todo.
	 * @param filepath caminho completo ou parcial da pasta que cont�m a imagem.
	 * @param xTerrain coordenada do terreno no mundo em rela��o ao eixo da longitude.
	 * @param zTerrain coordenada do terreno no mundo em rela��o ao eixo da longitude.
	 * @return aquisi��o da imagem carregada conforme especifica��es acima.
	 */

	private BufferedImage loadImage(String filepath, int xTerrain, int zTerrain)
	{
		String terrainPath = makeTerrainPath(filepath, xTerrain, zTerrain);
		File terrainFile = new File(terrainPath);

		try {
			return ImageIO.read(terrainFile);
		} catch (IOException e) {
			throw new TerrainRuntimeException("falha ao ler terreno (path: %s) [%s]", terrainFile.getPath(), e.getMessage());
		}
	}

	/**
	 * M�todo para criar o caminho do terreno conforme as especifica��es abaixo.
	 * @param filepath caminho completo ou parcial da pasta que cont�m a imagem.
	 * @param xTerrain coordenada do terreno no mundo em rela��o ao eixo da longitude.
	 * @param zTerrain coordenada do terreno no mundo em rela��o ao eixo da longitude.
	 * @return aquisi��o do caminho passado adicionado o nome do arquivo ao final.
	 */

	private String makeTerrainPath(String filepath, int xTerrain, int zTerrain)
	{
		String terrainName = String.format("terrain_%d-%d.bmp", xTerrain, zTerrain);
		String terrainPath = String.format("%s/%s", filepath, terrainName);

		return terrainPath;
	}

	/**
	 * Procedimento chamado ap�s fazer a valida��o da imagem carregada.
	 * Seu objetivo � determinar o tamanho da modelagem e assim cri�-la.
	 * Ap�s isso chama alguns m�todos internos para especificar seus valores.
	 * @param world refer�ncia do mundo que solicitou o terreno.
	 * @param path caminho do terreno carregado para identific�-lo no ModelLoader.
	 * @return aquisi��o da modelagem criada a partir das informa��es do mundo.
	 */

	private Model createModel(World world, String path)
	{
		int width = world.getTerrainWidth();
		int length = world.getTerrainLength();
		int size = (width * length) + width + length + 1;

		int vertexCount = size;
		int textureCount = size;
		int normalCount = size;
		int faceCount = width * length * 6;

		ModelDataDefault data = new ModelDataDefault();
		data.init(vertexCount, textureCount, normalCount, faceCount);

		generateVertices(world, data, world.getUnitSize());
		generateNormals(world, data);
		generateTextures(world, data);
		generateIndices(world, data);

		return ModelLoader.getIntance().createModel(path, data);
	}

	/**
	 * M�todo interno chamado ap�s a cria��o do objeto para armazenar a modelagem do terreno.
	 * Esse m�todo ir� criar os v�rtices de cada c�lula do terreno respeitando o valor de unidade.
	 * @param world refer�ncia do mundo que o solicitou para saber o tamanho de seus terrenos.
	 * @param data refer�ncia do objeto contendo os dados tempor�rios da modelagem criada.
	 * @param unit qual o tamanho de cada c�lula do terreno no espa�o quando for renderizado.
	 */

	private void generateVertices(World world, ModelDataDefault data, float unit)
	{
		int offset = 0;

		for (int wz = 0; wz <= world.getTerrainLength(); wz++)
			for (int wx = 0; wx <= world.getTerrainWidth(); wx++)
			{
				float x = wx * unit;
				float y = getHeight(wx, wz);
				float z = wz * unit;

				data.setVertice(offset++, x, y, z);
				terrain.setHeight(wx, wz, y);
			}

		if (data.getVertices().length != offset * 3)
			logWarning("vertices sobrando (vertices: %d, offset: %d)",
					data.getVertices().length, offset * 3);
	}

	/**
	 * M�todo interno chamado ap�s a cria��o do objeto para armazenar a modelagem do terreno.
	 * Esse m�todo ir� especificar a intensidade da luz nas c�lulas conforme suas altitudes.
	 * Considera as altitudes das c�lulas vizinhas (norte, sul, leste e oeste) para tal.
	 * @param world refer�ncia do mundo que o solicitou para saber o tamanho de seus terrenos.
	 * @param data refer�ncia do objeto contendo os dados tempor�rios da modelagem criada.
	 */

	private void generateNormals(World world, ModelDataDefault data)
	{
		int offset = 0;

		for (int wz = 0; wz <= world.getTerrainLength(); wz++)
			for (int wx = 0; wx <= world.getTerrainWidth(); wx++)
			{
				float heightL = getHeight(wx - 1, wz);
				float heightR = getHeight(wx + 1, wz);
				float heightD = getHeight(wx, wz - 1);
				float heightU = getHeight(wx, wz + 1);

				Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
				normal.normalise();

				data.setNormal(offset++, normal.x, normal.y, normal.z);
			}

		if (data.getNormals().length != offset * 3)
			logWarning("normaliza��o sobrando (normals: %d, offset: %d)",
					data.getNormals().length, offset * 3);
	}

	/**
	 * M�todo interno chamado ap�s a cria��o do objeto para armazenar a modelagem do terreno.
	 * Utiliza um padr�o de textura simples de modo que o terreno use apenas uma textura no ch�o.
	 * TODO funcionalidade tempor�ria, dever� ser melhor especificado em atualiza��es futuras.
	 * @param world refer�ncia do mundo que o solicitou para saber o tamanho de seus terrenos.
	 * @param data refer�ncia do objeto contendo os dados tempor�rios da modelagem criada.
	 */

	private void generateTextures(World world, ModelDataDefault data)
	{
		int offset = 0;

		for (int wz = 0; wz <= world.getTerrainLength(); wz++)
			for (int wx = 0; wx <= world.getTerrainWidth(); wx++)
			{
				float x = (float) wx / ((float) world.getTerrainWidth());
				float y = (float) wz / ((float) world.getTerrainLength());

				data.setTexture(offset++, x, y);
			}

		if (data.getTextureCoords().length != offset * 2)
			logWarning("coordenada de textura sobrando (textureCoords: %d, offset: %d)",
					data.getTextureCoords().length, offset * 2);
	}

	/**
	 * M�todo interno chamado ap�s a cria��o do objeto para armazenar a modelagem do terreno.
	 * Ir� garantir que os v�rtices criados sejam conectados corretamente para formar o terreno.
	 * @param world refer�ncia do mundo que o solicitou para saber o tamanho de seus terrenos.
	 * @param data refer�ncia do objeto contendo os dados tempor�rios da modelagem criada.
	 */

	private void generateIndices(World world, ModelDataDefault data)
	{
		int offset = 0;

		for (int wz = 0; wz < world.getTerrainLength(); wz++)
			for (int wx = 0; wx < world.getTerrainWidth(); wx++)
			{
				int topLeft = (wz * world.getTerrainWidth()) + wx + wz;
				int topRight = topLeft + 1;
				int bottomLeft = topLeft + world.getTerrainWidth() + 1;
				int bottomRight = bottomLeft + 1;

				data.setIndice(offset++, topLeft);
				data.setIndice(offset++, bottomLeft);
				data.setIndice(offset++, topRight);
				data.setIndice(offset++, topRight);
				data.setIndice(offset++, bottomLeft);
				data.setIndice(offset++, bottomRight);
			}

		if (data.getIndices().length != offset)
			logWarning("�ndices sobrando (indices: %d, offset: %d)", data.getIndices().length, offset);
	}

	/**
	 * Permite obter o valor de altura ou profundidade de uma c�lula do terreno.
	 * A altura varia de acordo com as especifica��es de escala cinza da imagem BMP.
	 * @param x coordenada da c�lula do terreno no eixo da longitude desejada.
	 * @param z coordenada da c�lula do terreno no eixo da latitude desejada.
	 * @return aquisi��o da altura/profundidade nas coordenadas especificadas acima.
	 */

	private float getHeight(int x, int z)
	{
		int pixel = getPixelOf(x + 1, z + 1, image);
		float height = calculatePixelHeight(pixel);

		return height;
	}

	/**
	 * Permite obter o valor de um determinado pixel (neste caso de 0 a 255 - escala cinza).
	 * Esse pixel internamente ser� usado para saber a altura ou profundidade de uma c�lula.
	 * @param x coordenada do pixel da imagem (c�lula do terreno) no eixo da longitude desejada.
	 * @param z coordenada do pixel da imagem (c�lula do terreno) no eixo da latitude desejada.
	 * @param bi refer�ncia da imagem que ser� considerada para obter esse pixel.
	 * @return aquisi��o do valor do pixel conforme as especifica��es acima.
	 */

	private int getPixelOf(int x, int z, BufferedImage bi)
	{
		int i = (x * bi.getHeight()) + z;

		WritableRaster raster = bi.getRaster();
		DataBuffer buffer = raster.getDataBuffer();
		int pixel = buffer.getElem(i);

		return pixel;
	}

	/**
	 * Calcula a altura ou profundidade de uma c�lula do terreno conforme o valor do pixel.
	 * A conta � feita considerando um valor de altura 64 (padr�o) sendo os pixels abaixo de
	 * 128 considerados como profundidade no terreno e a partir de 128 s�o de altura.
	 * @param pixelValue valor respectivo ao pixel do qual deseja saber o valor em altura.
	 * @return aquisi��o do valor da altura ou profundidade atrav�s do valor do pixel acima.
	 */

	private float calculatePixelHeight(int pixelValue)
	{
		float adjusted = pixelValue - 128;
		float relative = adjusted / 128;                                                                                                                                                                             
		float height = relative * 64;

		return height;
	}
}
