package com.erakin.engine.resource.model;

import org.diverproject.util.ObjectDescription;

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
	 * Posicionamento dos v�rtices no espa�o.
	 */
	protected float vertexPositions[];

	/**
	 * Coordenada da textura usada para cada v�rtice.
	 */
	protected float textureCoords[];

	/**
	 * Regulariza��o para as superf�cies.
	 */
	protected float vertexNormals[];

	/**
	 * Conex�o de cada v�rtice para forma��o das faces.
	 */
	protected int indices[];

	@Override
	public float[] getVertices()
	{
		return vertexPositions;
	}

	@Override
	public float[] getTextureCoords()
	{
		return textureCoords;
	}

	@Override
	public float[] getNormals()
	{
		return vertexNormals;
	}

	@Override
	public int[] getIndices()
	{
		return indices;
	}

	/**
	 * Procedimento chamado fazer a inicializa��o do vetor dos dados do modelo.
	 * Ir� considerar que a quantidade de v�rtices � o mesmo definido em vertexCount.
	 * @param vertexCount quantidade de v�rtices no espa�o que existem no modelo.
	 * @param textureCount quantidade de coordenadas de textura que existem no modelo.
	 * @param normalCount quantidade de normaliza��o da ilumina��o que existem no modelo.
	 * @param faceCount quantidade de faces que existem no modelo.
	 */

	public void init(int vertexCount, int textureCount, int normalCount, int faceCount)
	{
		vertexPositions = new float[vertexCount * 3];
		vertexNormals = new float[normalCount * 3];
		textureCoords = new float[textureCount * 2];
		indices = new int[faceCount];		
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
		vertexPositions[(vertex * 3) + 0] = x;
		vertexPositions[(vertex * 3) + 1] = y;
		vertexPositions[(vertex * 3) + 2] = z;
	}

	/**
	 * Deve definir o ponto exato de uso da textura para um v�rtice.
	 * @param vertex �ndice do v�rtice que ser� definido sua textura.
	 * @param x coordenada da textura que ser� usada no eixo horizontal.
	 * @param y coordenada da textura que ser� usada no eixo vertical.
	 */

	public void setTexture(int vertex, float x, float y)
	{
		textureCoords[(vertex * 2) + 0] = x;
		textureCoords[(vertex * 2) + 1] = y;
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
		vertexNormals[(vertex * 3) + 0] = x;
		vertexNormals[(vertex * 3) + 1] = y;
		vertexNormals[(vertex * 3) + 2] = z;
	}

	/**
	 * Deve definir as informa��es de uma determinada face encontrada no modelo.
	 * Para cada tr�s v�rtices ser� considerado o conjunto de dados de uma face.
	 * @param index �ndice do v�rtice no vetor de �ndices a ser definido.
	 * @param vertex �ndice do v�rtice que ser� alocado no �ndice acima.
	 */

	public void setIndice(int index, int vertex)
	{
		indices[index] = vertex;
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("vertex", vertexPositions.length / 2);
		description.append("textures", textureCoords.length / 2);
		description.append("normals", vertexNormals.length / 3);
		description.append("indices", indices.length);

		return description.toString();
	}
}
