package com.erakin.engine.resource.model;

import org.diverproject.util.ObjectDescription;

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
	 * Posicionamento dos vértices no espaço.
	 */
	protected float vertexPositions[];

	/**
	 * Coordenada da textura usada para cada vértice.
	 */
	protected float textureCoords[];

	/**
	 * Regularização para as superfícies.
	 */
	protected float vertexNormals[];

	/**
	 * Conexão de cada vértice para formação das faces.
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
	 * Procedimento chamado fazer a inicialização do vetor dos dados do modelo.
	 * Irá considerar que a quantidade de vértices é o mesmo definido em vertexCount.
	 * @param vertexCount quantidade de vértices no espaço que existem no modelo.
	 * @param textureCount quantidade de coordenadas de textura que existem no modelo.
	 * @param normalCount quantidade de normalização da iluminação que existem no modelo.
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
	 * Deve definir o ponto exato no espaço para um determinado vértice.
	 * @param vertex índice do vértice que será definido sua textura.
	 * @param x coordenada da posição que será usada no eixo horizontal.
	 * @param y coordenada da posição que será usada no eixo vertical.
	 * @param z coordenada da posição que será usada no eixo de profundidade.
	 */

	public void setVertice(int vertex, float x, float y, float z)
	{
		vertexPositions[(vertex * 3) + 0] = x;
		vertexPositions[(vertex * 3) + 1] = y;
		vertexPositions[(vertex * 3) + 2] = z;
	}

	/**
	 * Deve definir o ponto exato de uso da textura para um vértice.
	 * @param vertex índice do vértice que será definido sua textura.
	 * @param x coordenada da textura que será usada no eixo horizontal.
	 * @param y coordenada da textura que será usada no eixo vertical.
	 */

	public void setTexture(int vertex, float x, float y)
	{
		textureCoords[(vertex * 2) + 0] = x;
		textureCoords[(vertex * 2) + 1] = y;
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
		vertexNormals[(vertex * 3) + 0] = x;
		vertexNormals[(vertex * 3) + 1] = y;
		vertexNormals[(vertex * 3) + 2] = z;
	}

	/**
	 * Deve definir as informações de uma determinada face encontrada no modelo.
	 * Para cada três vértices será considerado o conjunto de dados de uma face.
	 * @param index índice do vértice no vetor de índices a ser definido.
	 * @param vertex índice do vértice que será alocado no índice acima.
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
