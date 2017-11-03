package com.erakin.api.resources.model;

import static com.erakin.api.resources.model.ModelAttribute.ATTRIB_NORMAL;
import static com.erakin.api.resources.model.ModelAttribute.ATTRIB_TEXTURES;
import static com.erakin.api.resources.model.ModelAttribute.ATTRIB_UV_TEXTURE;
import static com.erakin.api.resources.model.ModelAttribute.ATTRIB_VERTEX;

import org.diverproject.util.ObjectDescription;
import org.lwjgl.util.vector.Vector3f;

/**
 * <h1>Dados Tempor�rio para Modelo</h1>
 *
 * <p>Essa classe tem como finalidade guardar informa��es de um modelo.
 * Para esse, ser� permitido definir as informa��es deste adequadamente.
 * Cada tipo de dado segue uma regra e tipos de dados diferentes.</p>
 *
 * <p>Ao finalizar a defini��o dos dados desse objeto atrav�s dos procedimentos
 * adequados para tal, ser�o repassados para o carregador de modelo usado.
 * Para que esse possa passar adiante as informa��es necess�rias.</p>
 *
 * @author Andrew
 */

public class ModelDataDefault implements ModelData
{
	/**
	 * Conex�o de cada v�rtice para forma��o das faces.
	 */
	protected ModelIndiceAttribute indexes;

	/**
	 * Posicionamento dos v�rtices no espa�o.
	 */
	protected ModelFloatAttribute vertices;

	/**
	 * Coordenada da textura usada para cada v�rtice.
	 */
	protected ModelFloatAttribute uvTextures;

	/**
	 * Regulariza��o para as superf�cies.
	 */
	protected ModelFloatAttribute normals;

	/**
	 * Identifica��o de textura para cada conex�o de v�rtice.
	 */
	protected ModelFloatAttribute textures;

	/**
	 * Inicializa o atributo para modelo que determina a liga��o dos v�rtices por �ndice.
	 * @param indicesCount quantidade de liga��es de v�rtices necess�rios para formar o modelo.
	 */

	public void initIndexes(int indicesCount)
	{
		indexes = new ModelIndiceAttribute(indicesCount);
	}

	/**
	 * Inicializa o atributo para modelo que determina o posicionamento dos v�rtices no espa�o.
	 * Nesse m�todo ser� considerado que cada v�rtice possui <code>DEFAULT_VERTEX_SIZE</code> dados.
	 * @param vertexCount quantidade de v�rtices necess�rios para formar o modelo.
	 */

	public void initVertices(int vertexCount)
	{
		initVertices(DEFAULT_VERTEX_SIZE, vertexCount);
	}

	/**
	 * Inicializa o atributo para modelo que determina o posicionamento dos v�rtices no espa�o.
	 * @param vertexSize quantidade de dados necess�rios para formar cada v�rtice.
	 * @param vertexCount quantidade de v�rtices necess�rios para formar o modelo.
	 */

	public void initVertices(int vertexSize, int vertexCount)
	{
		vertices = new ModelFloatAttribute(ATTRIB_VERTEX, vertexSize, vertexCount);		
	}

	/**
	 * Inicializa o atributo para modelo que determina as coordenadas de textura por v�rtice.
	 * Nesse m�todo ser� considerado que cada v�rtice possui <code>DEFAULT_UV_SIZE</code> dados.
	 * @param uvCount quantidade de coordenadas de textura necess�rios para formar o modelo.
	 */

	public void initUVTextures(int uvCount)
	{
		initUVTextures(DEFAULT_UV_SIZE, uvCount);
	}

	/**
	 * Inicializa o atributo para modelo que determina o posicionamento dos v�rtices no espa�o.
	 * @param uvSize quantidade de dados necess�rios para formar cada coordenada UV.
	 * @param uvCount quantidade de coordenadas de textura necess�rios para formar o modelo.
	 */

	public void initUVTextures(int uvSize, int uvCount)
	{
		uvTextures = new ModelFloatAttribute(ATTRIB_UV_TEXTURE, uvSize, uvCount);		
	}

	/**
	 * Inicializa o atributo para modelo que determina as normaliza��es de cada v�rtice.
	 * Nesse m�todo ser� considerado que cada v�rtice possui <code>DEFAULT_NORMAL_SIZE</code> dados.
	 * @param normalCount quantidade de normaliza��es necess�rias para formar o modelo.
	 */

	public void initNormals(int normalCount)
	{
		initNormals(DEFAULT_NORMAL_SIZE, normalCount);
	}

	/**
	 * Inicializa o atributo para modelo que determina as normaliza��es de cada v�rtice.
	 * @param normalSize quantidade de dados necess�rios para formar cada normaliza��o.
	 * @param normalCount quantidade de normaliza��es necess�rias para formar o modelo.
	 */

	public void initNormals(int normalSize, int normalCount)
	{
		normals = new ModelFloatAttribute(ATTRIB_NORMAL, normalSize, normalCount);
	}

	/**
	 * Inicializa o atributo para modelo que determina as texturas utilizadas por cada v�rtice.
	 * Nesse m�todo ser� considerado que cada v�rtice possui <code>DEFAULT_TEXTURE_SIZE</code> dados.
	 * @param textureCount quantidade de texturas utilizadas necess�rias para formar o modelo.
	 */

	public void initTextures(int textureCount)
	{
		initTextures(DEFAULT_TEXTURE_SIZE, textureCount);
	}

	/**
	 * Inicializa o atributo para modelo que determina as texturas utilizadas por cada v�rtice.
	 * @param textureSize quantidade da dos necess�rios para formar cada textura utilizada.
	 * @param textureCount quantidade de texturas utilizadas necess�rias para formar o modelo.
	 */

	public void initTextures(int textureSize, int textureCount)
	{
		textures = new ModelFloatAttribute(ATTRIB_TEXTURES, textureSize, textureCount);
	}

	/**
	 * Deve definir as informa��es de uma determinada face encontrada no modelo.
	 * Para cada tr�s v�rtices ser� considerado o conjunto de dados de uma face.
	 * @param index �ndice do v�rtice no vetor de �ndices a ser definido.
	 * @param vertex �ndice do v�rtice que ser� alocado no �ndice acima.
	 */

	public void setIndice(int index, int vertex)
	{
		indexes.setValue(index, vertex);
	}

	/**
	 * Deve definir o ponto exato no espa�o para um determinado v�rtice.
	 * @param vertex �ndice do v�rtice que ser� definido sua textura.
	 * @param x coordenada da posi��o que ser� usada no eixo horizontal.
	 * @param y coordenada da posi��o que ser� usada no eixo vertical.
	 * @param z coordenada da posi��o que ser� usada no eixo de profundidade.
	 */

	public void setVertice(int vertex, float x, float y, float z)
	{
		vertices.setValue(vertex, x, y, z);
	}

	/**
	 * Deve definir o ponto exato no espa�o para um determinado v�rtice.
	 * @param vertex �ndice do v�rtice que ser� definido sua textura.
	 * @param vector vetor com as coordenadas de posicionamento (x, y e z).
	 */

	public void setVertice(int vertex, Vector3f vector)
	{
		vertices.setValue(vertex, vector.x, vector.y, vector.z);
	}

	/**
	 * Deve definir o ponto exato de uso da textura para um v�rtice.
	 * @param vertex �ndice do v�rtice que ser� definido sua textura.
	 * @param x coordenada da textura que ser� usada no eixo horizontal.
	 * @param y coordenada da textura que ser� usada no eixo vertical.
	 */

	public void setUVTexture(int vertex, float x, float y)
	{
		uvTextures.setValue(vertex, x, y);
	}

	/**
	 * Deve definir as propriedades para regulariza��o das superf�cies.
	 * @param vertex �ndice do v�rtice que ser� definido sua regulariza��o.
	 * @param x coordenada da regulariza��o que ser� usada no eixo horizontal.
	 * @param y coordenada da regulariza��o que ser� usada no eixo vertical.
	 * @param z coordenada da regulariza��o que ser� usada no eixo de profundidade.
	 */

	public void setNormal(int vertex, float x, float y, float z)
	{
		normals.setValue(vertex, x, y, z);
	}

	/**
	 * Deve definir as propriedades para regulariza��o das superf�cies.
	 * @param vertex �ndice do v�rtice que ser� definido sua regulariza��o.
	 * @param vector vetor com os valores de normaliza��o (x, y e z).
	 */

	public void setNormal(int vertex, Vector3f vector)
	{
		normals.setValue(vertex, vector.x, vector.y);
	}

	/**
	 * Deve definir qual a textura que ser� usada na renderiza��o de um tri�ngulo do modelo.
	 * @param index �ndice do tri�ngulo (conex�o de v�rtices) que ser� definido a textura.
	 * @param textureID c�digo de identifica��o da textura j� carregada no OpenGL.
	 */

	public void setTextureIndex(int index, int textureID)
	{
		textures.setValue(index, textureID);
	}

	/**
	 * Calcula aproximadamente quantos bytes esse objeto est� ocupando em mem�ria.
	 * @return aquisi��o do espa�o em mem�ria ocupado pelo objeto em bytes.
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
