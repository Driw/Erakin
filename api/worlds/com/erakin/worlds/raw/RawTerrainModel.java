package com.erakin.worlds.raw;

import static org.diverproject.util.Util.format;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.SizeUtil;
import org.lwjgl.util.vector.Vector3f;

import com.erakin.api.lwjgl.VAO;
import com.erakin.api.lwjgl.math.enumeration.DrawElement;
import com.erakin.api.render.ModelRender;
import com.erakin.api.resources.model.Model;
import com.erakin.api.resources.model.ModelAttribute;
import com.erakin.api.resources.model.ModelData;
import com.erakin.api.resources.model.ModelDataDefault;
import com.erakin.api.resources.model.ModelLoader;
import com.erakin.api.resources.texture.Texture;
import com.erakin.api.resources.world.TerrainRuntimeException;

/**
 * <h1>Modelo de Terreno Bruno</h1>
 *
 * <p>Classe usada para criar o modelo de um terreno bruno com base nas informa��es do mesmo.
 * Aqui � feito o c�lculo para posicionar cada unidade do terreno conforme o seu tamanho.
 * Tamb�m registra o modelo para que fique salvo na engine e no OpenGL.</p>
 *
 * @see ModelRender
 * @see Model
 * @see RawTerrain
 * @see ModelLoader
 *
 * @author Andrew
 */

public class RawTerrainModel implements ModelRender
{
	/**
	 * Posicionamento relativo para o v�rtice do sudoeste.
	 */
	public static final int OFFSET_SOUTH_WEST = 0;

	/**
	 * Posicionamento relativo para o v�rtice do sudeste.
	 */
	public static final int OFFSET_SOUTH_EAST = 1;

	/**
	 * Posicionamento relativo para o v�rtice do nordeste.
	 */
	public static final int OFFSET_NORTH_EAST = 2;

	/**
	 * Posicionamento relativo para o v�rtice do noroeste.
	 */
	public static final int OFFSET_NORTH_WEST = 3;


	/**
	 * Quantidade de bytes do �ltimo modelo gerado.
	 */
	private int sizeof;

	/**
	 * Refer�ncia do �ltimo modelo do terreno gerado.
	 */
	private Model model;

	/**
	 * Refer�ncia do terreno bruno do qual o modelo deve ser gerado.
	 */
	private RawTerrain terrain;

	/**
	 * Cria uma nova inst�ncia de um Modelo para Terreno Bruto.
	 * Lembrando que � necess�rio gerar o terreno por <code>generateullModel()</code>.
	 * @param terrain refer�ncia do terreno bruto do qual o modelo ser� gerado.
	 */

	public RawTerrainModel(RawTerrain terrain)
	{
		this.terrain = terrain;
	}

	/**
	 * Permite saber aproximadamente o tamanho de ocupa��o em mem�ria do modelo gerado.
	 * @return aquisi��o do tamanho em bytes do modelo de terreno gerado.
	 */

	public int sizeof()
	{
		return sizeof;
	}

	/**
	 * Gera as informa��es de v�rtices, normaliza��es e coordenadas de textura para o modelo do terreno.
	 * Esse terreno gerado ir� levar em considera��o as informa��es contidas em cada unidade do terreno.
	 * Ap�s gerar os dados ir� notificar a Engine para salvar o modelo no OpenGL.
	 */

	public void generateFullModel()
	{
		if (terrain.getWidth() < 1 || terrain.getLength() < 1)
			return;

		if (isGenerated())
			throw new TerrainRuntimeException("modelo do terreno j� foi gerado");

		String path = format("%s/terrain_%d_%d", terrain.world.getPrefix(), terrain.getX(), terrain.getZ());

		ModelData data = createModelData();
		model = ModelLoader.getIntance().createModel(path, data);
	}

	/**
	 * Procedimento interno utilizado criar adequadamente as informa��es de modelo do terreno.
	 * @return aquisi��o dos dados de modelo do terreno prontos para criar um modelo renderiz�vel.
	 */

	private ModelData createModelData()
	{
		int unitCount = terrain.getWidth() * terrain.getLength();
		int faceCount = unitCount * 6;
		int vertexCount = unitCount * 4;
		int textureCount = unitCount * 4;
		int normalCount = unitCount * 4;

		ModelDataDefault data = new ModelDataDefault();
		data.initVertices(vertexCount);
		data.initUVTextures(textureCount);
		data.initNormals(normalCount);
		data.initIndexes(faceCount);
		sizeof = data.sizeof();

		generateCellVertex(data);
		generateCellNormals(data);
		generateCellTextures(data);
		generateCellIndices(data);

		return data;
	}

	/**
	 * Procedimento interno que faz o calculo para gerar o posicionamento dos v�rtices.
	 * @param data refer�ncia do objeto que ir� armazenar os v�rtices do modelo.
	 */

	private void generateCellVertex(ModelDataDefault data)
	{
		for (int z = 0; z < terrain.getLength(); z++)
			for (int x = 0; x < terrain.getWidth(); x++)
			{
				int offset = terrain.offset(x, z) * 4;

				RawTerrainUnit unit = terrain.getUnit(x, z);
				data.setVertice(offset + OFFSET_SOUTH_WEST, unit.getSouthWest());
				data.setVertice(offset + OFFSET_SOUTH_EAST, unit.getSouthEast());
				data.setVertice(offset + OFFSET_NORTH_EAST, unit.getNorthEast());
				data.setVertice(offset + OFFSET_NORTH_WEST, unit.getNorthWest());
			}
	}

	/**
	 * Procedimento interno que faz o calculo para gerar o posicionamento da normaliza��o.
	 * @param data refer�ncia do objeto que ir� armazenar a normaliza��o do modelo.
	 */

	private void generateCellNormals(ModelDataDefault data)
	{
		int offset = 0;

		for (int z = 0; z < terrain.getLength(); z++)
			for (int x = 0; x < terrain.getWidth(); x++)
			{
				RawTerrainUnit unit = terrain.getUnit(x, z);

				float defaultHeight = unit.avarageHeight();
				float heightL = getHeight(x - 1, z, defaultHeight);
				float heightR = getHeight(x + 1, z, defaultHeight);
				float heightD = getHeight(x, z - 1, defaultHeight);
				float heightU = getHeight(x, z + 1, defaultHeight);

				Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
				normal.normalise();

				data.setNormal(offset++, normal.x, normal.y, normal.z);
				data.setNormal(offset++, normal.x, normal.y, normal.z);
				data.setNormal(offset++, normal.x, normal.y, normal.z);
				data.setNormal(offset++, normal.x, normal.y, normal.z);
			}
	}

	private float getHeight(int x, int z, float defaultHeight)
	{
		try {

			RawTerrainUnit unit = terrain.getUnit(x, z);
			return unit.avarageHeight();

		} catch (TerrainRuntimeException e) {
			return defaultHeight;
		}
	}

	/**
	 * Procedimento interno que faz o calculo para gerar o posicionamento de coordenadas da textura.
	 * @param data refer�ncia do objeto que ir� armazenar a normaliza��o de coordenadas da textura.
	 */

	private void generateCellTextures(ModelDataDefault data)
	{
		int offset = 0;

		for (int z = 0; z < terrain.getLength(); z++)
			for (int x = 0; x < terrain.getWidth(); x++)
			{
				data.setTexture(offset++, 1f, 1f);
				data.setTexture(offset++, 0f, 1f);
				data.setTexture(offset++, 0f, 0f);
				data.setTexture(offset++, 1f, 0f);
			}
	}

	/**
	 * Procedimento interno que faz o calculo para gerar a conex�o dos v�rtices (�ndices).
	 * @param data refer�ncia do objeto que ir� armazenar a conex�o dos v�rtices (�ndices).
	 */

	private void generateCellIndices(ModelDataDefault data)
	{
		int offset = 0;

		for (int z = 0; z < terrain.getLength(); z++)
			for (int x = 0; x < terrain.getWidth(); x++)
			{
				int vertexOffset = terrain.offset(x, z) * 4;

				data.setIndice(offset++, vertexOffset + OFFSET_SOUTH_WEST);
				data.setIndice(offset++, vertexOffset + OFFSET_NORTH_WEST);
				data.setIndice(offset++, vertexOffset + OFFSET_SOUTH_EAST);
				data.setIndice(offset++, vertexOffset + OFFSET_SOUTH_EAST);
				data.setIndice(offset++, vertexOffset + OFFSET_NORTH_WEST);
				data.setIndice(offset++, vertexOffset + OFFSET_NORTH_EAST);
			}		
	}

	/**
	 * Informa ao OpenGL novamente todos os dados de composi��o de modelo do terreno.
	 * Ir� iterar cada unidade do terreno calculando novamente v�rtices e afins.
	 */

	public void updateFullModel()
	{
		ModelData data = createModelData();

		VAO vao = new VAO(model.getID());
		data.getIndices().storeInVAO(vao);

		for (ModelAttribute attribute : data.getAttributes())
			attribute.storeInVAO(vao);
	}

	/**
	 * Verifica se um modelo de terreno para ser renderizado j� foi gerado.
	 * @return true se j� foi gerado ou false caso contr�rio.
	 */

	public boolean isGenerated()
	{
		return model != null;
	}

	@Override
	public int getID()
	{
		return model.getID();
	}

	@Override
	public void bind()
	{
		model.bind();
	}

	@Override
	public void unbind()
	{
		model.unbind();
	}

	@Override
	public boolean valid()
	{
		return model.valid();
	}

	@Override
	public void release()
	{
		model.release();
	}

	@Override
	public void draw(DrawElement mode)
	{
		model.draw(mode);
	}

	@Override
	public Texture getTexture()
	{
		return model == null ? null : model.getTexture();
	}

	@Override
	public float getReflectivity()
	{
		return model.getReflectivity();
	}

	@Override
	public float getShineDamping()
	{
		return model.getShineDamping();
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("width", terrain.getWidth());
		description.append("length", terrain.getLength());

		if (model != null)
		{
			description.append("model", model.getID());
			description.append("vertexCount", model.getVertexCount());
			description.append("sizeof", SizeUtil.toString(sizeof()));
		}

		return description.toString();
	}
}
