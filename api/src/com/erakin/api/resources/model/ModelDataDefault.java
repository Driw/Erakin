package com.erakin.api.resources.model;

import static com.erakin.api.resources.model.ModelAttribute.ATTRIB_NORMAL;
import static com.erakin.api.resources.model.ModelAttribute.ATTRIB_TEXTURES;
import static com.erakin.api.resources.model.ModelAttribute.ATTRIB_UV_TEXTURE;
import static com.erakin.api.resources.model.ModelAttribute.ATTRIB_VERTEX;

import org.diverproject.util.ObjectDescription;
import org.lwjgl.util.vector.Vector3f;

/**
 * <h1>Dados Temporário para Modelo</h1>
 *
 * <p>Essa classe tem como finalidade guardar informações de um modelo.
 * Para esse, será permitido definir as informações deste adequadamente.
 * Cada tipo de dado segue uma regra e tipos de dados diferentes.</p>
 *
 * <p>Ao finalizar a definição dos dados desse objeto através dos procedimentos
 * adequados para tal, serão repassados para o carregador de modelo usado.
 * Para que esse possa passar adiante as informações necessárias.</p>
 *
 * @author Andrew
 */

public class ModelDataDefault implements ModelData
{
	/**
	 * Conexão de cada vértice para formação das faces.
	 */
	protected ModelIndiceAttribute indexes;

	/**
	 * Posicionamento dos vértices no espaço.
	 */
	protected ModelFloatAttribute vertices;

	/**
	 * Coordenada da textura usada para cada vértice.
	 */
	protected ModelFloatAttribute uvTextures;

	/**
	 * Regularização para as superfícies.
	 */
	protected ModelFloatAttribute normals;

	/**
	 * Identificação de textura para cada conexão de vértice.
	 */
	protected ModelFloatAttribute textures;

	/**
	 * Inicializa o atributo para modelo que determina a ligação dos vértices por índice.
	 * @param indicesCount quantidade de ligações de vértices necessários para formar o modelo.
	 */

	public void initIndexes(int indicesCount)
	{
		indexes = new ModelIndiceAttribute(indicesCount);
	}

	/**
	 * Inicializa o atributo para modelo que determina o posicionamento dos vértices no espaço.
	 * Nesse método será considerado que cada vértice possui <code>DEFAULT_VERTEX_SIZE</code> dados.
	 * @param vertexCount quantidade de vértices necessários para formar o modelo.
	 */

	public void initVertices(int vertexCount)
	{
		initVertices(DEFAULT_VERTEX_SIZE, vertexCount);
	}

	/**
	 * Inicializa o atributo para modelo que determina o posicionamento dos vértices no espaço.
	 * @param vertexSize quantidade de dados necessários para formar cada vértice.
	 * @param vertexCount quantidade de vértices necessários para formar o modelo.
	 */

	public void initVertices(int vertexSize, int vertexCount)
	{
		vertices = new ModelFloatAttribute(ATTRIB_VERTEX, vertexSize, vertexCount);		
	}

	/**
	 * Inicializa o atributo para modelo que determina as coordenadas de textura por vértice.
	 * Nesse método será considerado que cada vértice possui <code>DEFAULT_UV_SIZE</code> dados.
	 * @param uvCount quantidade de coordenadas de textura necessários para formar o modelo.
	 */

	public void initUVTextures(int uvCount)
	{
		initUVTextures(DEFAULT_UV_SIZE, uvCount);
	}

	/**
	 * Inicializa o atributo para modelo que determina o posicionamento dos vértices no espaço.
	 * @param uvSize quantidade de dados necessários para formar cada coordenada UV.
	 * @param uvCount quantidade de coordenadas de textura necessários para formar o modelo.
	 */

	public void initUVTextures(int uvSize, int uvCount)
	{
		uvTextures = new ModelFloatAttribute(ATTRIB_UV_TEXTURE, uvSize, uvCount);		
	}

	/**
	 * Inicializa o atributo para modelo que determina as normalizações de cada vértice.
	 * Nesse método será considerado que cada vértice possui <code>DEFAULT_NORMAL_SIZE</code> dados.
	 * @param normalCount quantidade de normalizações necessárias para formar o modelo.
	 */

	public void initNormals(int normalCount)
	{
		initNormals(DEFAULT_NORMAL_SIZE, normalCount);
	}

	/**
	 * Inicializa o atributo para modelo que determina as normalizações de cada vértice.
	 * @param normalSize quantidade de dados necessários para formar cada normalização.
	 * @param normalCount quantidade de normalizações necessárias para formar o modelo.
	 */

	public void initNormals(int normalSize, int normalCount)
	{
		normals = new ModelFloatAttribute(ATTRIB_NORMAL, normalSize, normalCount);
	}

	/**
	 * Inicializa o atributo para modelo que determina as texturas utilizadas por cada vértice.
	 * Nesse método será considerado que cada vértice possui <code>DEFAULT_TEXTURE_SIZE</code> dados.
	 * @param textureCount quantidade de texturas utilizadas necessárias para formar o modelo.
	 */

	public void initTextures(int textureCount)
	{
		initTextures(DEFAULT_TEXTURE_SIZE, textureCount);
	}

	/**
	 * Inicializa o atributo para modelo que determina as texturas utilizadas por cada vértice.
	 * @param textureSize quantidade da dos necessários para formar cada textura utilizada.
	 * @param textureCount quantidade de texturas utilizadas necessárias para formar o modelo.
	 */

	public void initTextures(int textureSize, int textureCount)
	{
		textures = new ModelFloatAttribute(ATTRIB_TEXTURES, textureSize, textureCount);
	}

	/**
	 * Deve definir as informações de uma determinada face encontrada no modelo.
	 * Para cada três vértices será considerado o conjunto de dados de uma face.
	 * @param index índice do vértice no vetor de índices a ser definido.
	 * @param vertex índice do vértice que será alocado no índice acima.
	 */

	public void setIndice(int index, int vertex)
	{
		indexes.setValue(index, vertex);
	}

	/**
	 * Deve definir o ponto exato no espaço para um determinado vértice.
	 * @param vertex índice do vértice que será definido sua textura.
	 * @param x coordenada da posição que será usada no eixo horizontal.
	 * @param y coordenada da posição que será usada no eixo vertical.
	 * @param z coordenada da posição que será usada no eixo de profundidade.
	 */

	public void setVertice(int vertex, float x, float y, float z)
	{
		vertices.setValue(vertex, x, y, z);
	}

	/**
	 * Deve definir o ponto exato no espaço para um determinado vértice.
	 * @param vertex índice do vértice que será definido sua textura.
	 * @param vector vetor com as coordenadas de posicionamento (x, y e z).
	 */

	public void setVertice(int vertex, Vector3f vector)
	{
		vertices.setValue(vertex, vector.x, vector.y, vector.z);
	}

	/**
	 * Deve definir o ponto exato de uso da textura para um vértice.
	 * @param vertex índice do vértice que será definido sua textura.
	 * @param x coordenada da textura que será usada no eixo horizontal.
	 * @param y coordenada da textura que será usada no eixo vertical.
	 */

	public void setUVTexture(int vertex, float x, float y)
	{
		uvTextures.setValue(vertex, x, y);
	}

	/**
	 * Deve definir as propriedades para regularização das superfícies.
	 * @param vertex índice do vértice que será definido sua regularização.
	 * @param x coordenada da regularização que será usada no eixo horizontal.
	 * @param y coordenada da regularização que será usada no eixo vertical.
	 * @param z coordenada da regularização que será usada no eixo de profundidade.
	 */

	public void setNormal(int vertex, float x, float y, float z)
	{
		normals.setValue(vertex, x, y, z);
	}

	/**
	 * Deve definir as propriedades para regularização das superfícies.
	 * @param vertex índice do vértice que será definido sua regularização.
	 * @param vector vetor com os valores de normalização (x, y e z).
	 */

	public void setNormal(int vertex, Vector3f vector)
	{
		normals.setValue(vertex, vector.x, vector.y);
	}

	/**
	 * Deve definir qual a textura que será usada na renderização de um triângulo do modelo.
	 * @param index índice do triângulo (conexão de vértices) que será definido a textura.
	 * @param textureID código de identificação da textura já carregada no OpenGL.
	 */

	public void setTextureIndex(int index, int textureID)
	{
		textures.setValue(index, textureID);
	}

	/**
	 * Calcula aproximadamente quantos bytes esse objeto está ocupando em memória.
	 * @return aquisição do espaço em memória ocupado pelo objeto em bytes.
	 */

	public int sizeof()
	{
		int count = 0;

		if (indexes != null) count += indexes.sizeof();
		if (vertices != null) count += vertices.sizeof();
		if (uvTextures != null) count += uvTextures.sizeof();
		if (normals != null) count += normals.sizeof();
		if (textures != null) count += textures.sizeof();

		return count;
	}

	private int getAttributesCount()
	{
		int count = 0;

		if (vertices != null) count++;
		if (uvTextures != null) count++;
		if (normals != null) count++;
		if (textures != null) count++;

		return count;
	}

	@Override
	public ModelIndiceAttribute getIndices()
	{
		return indexes;
	}

	@Override
	public ModelAttribute[] getAttributes()
	{
		int length = getAttributesCount();
		int offset = 0;

		ModelAttribute attributes[] = new ModelAttribute[length];

		if (vertices != null) attributes[offset++] = vertices;
		if (uvTextures != null) attributes[offset++] = uvTextures;
		if (normals != null) attributes[offset++] = normals;
		if (textures != null) attributes[offset++] = textures;

		return attributes;
	}

	@Override
	public String toStringDetails()
	{
		ObjectDescription description = new ObjectDescription(null);

		if (indexes != null) description.append("indnices", indexes.length());
		if (vertices != null) description.append("vertex", vertices.length());
		if (uvTextures != null) description.append("uv", uvTextures.length());
		if (normals != null) description.append("normal", normals.length());
		if (textures != null) description.append("textures", textures.length());

		return description.toString();
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		if (indexes != null) description.append("indnices", indexes.length());
		if (vertices != null) description.append("vertex", vertices.length());
		if (uvTextures != null) description.append("uv", uvTextures.length());
		if (normals != null) description.append("normal", normals.length());
		if (textures != null) description.append("textures", textures.length());

		return description.toString();
	}
}
