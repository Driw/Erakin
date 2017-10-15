package com.erakin.worlds.raw;

import static com.erakin.api.resources.Model.ATTRIB_NORMALS;
import static com.erakin.api.resources.Model.ATTRIB_TEXTURE_COORDS;
import static com.erakin.api.resources.Model.ATTRIB_VERTICES;
import static org.diverproject.util.Util.format;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.SizeUtil;
import org.lwjgl.util.vector.Vector3f;

import com.erakin.api.lwjgl.VAO;
import com.erakin.api.lwjgl.math.enumeration.DrawElement;
import com.erakin.api.render.ModelRender;
import com.erakin.api.resources.Model;
import com.erakin.api.resources.ModelLoader;
import com.erakin.api.resources.model.ModelData;
import com.erakin.api.resources.model.ModelDataDefault;
import com.erakin.api.resources.world.TerrainRuntimeException;

/**
 * <h1>Modelo de Terreno Bruno</h1>
 *
 * <p>Classe usada para criar o modelo de um terreno bruno com base nas informações do mesmo.
 * Aqui é feito o cálculo para posicionar cada unidade do terreno conforme o seu tamanho.
 * Também registra o modelo para que fique salvo na engine e no OpenGL.</p>
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
	 * Quantidade de bytes do último modelo gerado.
	 */
	private int sizeof;

	/**
	 * Referência do último modelo do terreno gerado.
	 */
	private Model model;

	/**
	 * Referência do terreno bruno do qual o modelo deve ser gerado.
	 */
	private RawTerrain terrain;

	/**
	 * Cria uma nova instância de um Modelo para Terreno Bruto.
	 * Lembrando que é necessário gerar o terreno por <code>generateullModel()</code>.
	 * @param terrain referência do terreno bruto do qual o modelo será gerado.
	 */

	public RawTerrainModel(RawTerrain terrain)
	{
		this.terrain = terrain;
	}

	/**
	 * Permite saber aproximadamente o tamanho de ocupação em memória do modelo gerado.
	 * @return aquisição do tamanho em bytes do modelo de terreno gerado.
	 */

	public int sizeof()
	{
		return sizeof;
	}

	/**
	 * Gera as informações de vértices, normalizações e coordenadas de textura para o modelo do terreno.
	 * Esse terreno gerado irá levar em consideração as informações contidas em cada unidade do terreno.
	 * Após gerar os dados irá notificar a Engine para salvar o modelo no OpenGL.
	 */

	public void generateFullModel()
	{
		if (terrain.getWidth() < 1 || terrain.getLength() < 1)
			return;

		if (isGenerated())
			throw new TerrainRuntimeException("modelo do terreno já foi gerado");

		ModelData data = createModelData();
		String path = format("%s/terrain_%d_%d", terrain.world.getPrefix(), terrain.getX(), terrain.getZ());

		model = ModelLoader.getIntance().createModel(path, data);
	}

	/**
	 * Procedimento interno utilizado criar adequadamente as informações de modelo do terreno.
	 * @return aquisição dos dados de modelo do terreno prontos para criar um modelo renderizável.
	 */

	private ModelData createModelData()
	{
		int faceCount = terrain.getWidth() * terrain.getLength() * 6;
		int vertexCount = (terrain.getWidth() * terrain.getLength()) + terrain.getWidth() + terrain.getLength() + 1;
		int textureCount = vertexCount;
		int normalCount = vertexCount;

		sizeof = ((vertexCount * 7) + faceCount) * 4;

		ModelDataDefault data = new ModelDataDefault();
		data.init(vertexCount, textureCount, normalCount, faceCount);
		generateCellVertex(data);
		generateCellNormals(data);
		generateCellTextures(data);
		generateCellIndices(data);

		return data;
	}

	/**
	 * Procedimento interno que faz o calculo para gerar o posicionamento dos vértices.
	 * @param data referência do objeto que irá armazenar os vértices do modelo.
	 */

	private void generateCellVertex(ModelDataDefault data)
	{
		float unitSize = terrain.world.getUnitSize();

		for (int z = 0; z <= terrain.getLength(); z++)
			for (int x = 0; x <= terrain.getWidth(); x++)
				data.setVertice(terrain.offset(x, z), x * unitSize, 0f, z * unitSize);
	}

	/**
	 * Procedimento interno que faz o calculo para gerar o posicionamento da normalização.
	 * @param data referência do objeto que irá armazenar a normalização do modelo.
	 */

	private void generateCellNormals(ModelDataDefault data)
	{
		for (int z = 0; z <= terrain.getLength(); z++)
			for (int x = 0; x <= terrain.getWidth(); x++)
			{
				Vector3f normal = new Vector3f(0, 2f, 0);
				normal.normalise();		

				data.setNormal(terrain.offset(x, z), normal.x, normal.y, normal.z);
			}
	}

	/**
	 * Procedimento interno que faz o calculo para gerar o posicionamento de coordenadas da textura.
	 * @param data referência do objeto que irá armazenar a normalização de coordenadas da textura.
	 */

	private void generateCellTextures(ModelDataDefault data)
	{
		for (int z = 0; z <= terrain.getLength(); z++)
			for (int x = 0; x <= terrain.getWidth(); x++)
			{
				float tx = (float) x / ((float) terrain.getWidth());
				float ty = (float) z / ((float) terrain.getLength());

				data.setTexture(terrain.offset(x, z), tx, ty);
			}		
	}

	/**
	 * Procedimento interno que faz o calculo para gerar a conexão dos vértices (índices).
	 * @param data referência do objeto que irá armazenar a conexão dos vértices (índices).
	 */

	private void generateCellIndices(ModelDataDefault data)
	{
		for (int z = 0; z < terrain.getLength(); z++)
			for (int x = 0; x < terrain.getWidth(); x++)
			{
				int offset = ((z * terrain.getWidth()) + x) * 6;

				int topLeft = (z * terrain.getWidth()) + x + z;
				int topRight = topLeft + 1;
				int bottomLeft = topLeft + terrain.getWidth() + 1;
				int bottomRight = bottomLeft + 1;

				data.setIndice(offset + 0, topLeft);
				data.setIndice(offset + 1, bottomLeft);
				data.setIndice(offset + 2, topRight);
				data.setIndice(offset + 3, topRight);
				data.setIndice(offset + 4, bottomLeft);
				data.setIndice(offset + 5, bottomRight);
			}		
	}

	/**
	 * Informa ao OpenGL novamente todos os dados de composição de modelo do terreno.
	 * Irá iterar cada unidade do terreno calculando novamente vértices e afins.
	 */

	public void updateFullModel()
	{
		ModelData data = createModelData();

		VAO vao = new VAO(model.getID());
		vao.setIndices(data.getIndices());
		vao.setAttribute(ATTRIB_VERTICES, 3, data.getVertices());
		vao.setAttribute(ATTRIB_TEXTURE_COORDS, 2, data.getTextureCoords());
		vao.setAttribute(ATTRIB_NORMALS, 3, data.getNormals());
	}

	/**
	 * Verifica se um modelo de terreno para ser renderizado já foi gerado.
	 * @return true se já foi gerado ou false caso contrário.
	 */

	public boolean isGenerated()
	{
		return model != null;
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
	public void draw(DrawElement mode)
	{
		model.draw(mode);
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
