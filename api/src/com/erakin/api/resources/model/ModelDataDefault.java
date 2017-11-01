package com.erakin.api.resources.model;

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

	/**
	 * Identificação de textura para cada conexão de vértice.
	 */
	protected float textureIndex[];

	/**
	 * Cria uma nova instância de um objeto de armazenamento dos dados para criação de um modelo 3D.
	 * Inicializa todos os vetores com tamanho zero para evitar NullPointerException.
	 */

	public ModelDataDefault()
	{
		vertexPositions = new float[0];
		vertexNormals = new float[0];
		textureCoords = new float[0];
		indices = new int[0];
		textureIndex = new float[0];
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

		textureIndex = new float[0];
	}

	/**
	 * Determina que o modelo irá utilizar múltiplas texturas durante a texturização do mesmo.
	 * Será necessário definir a textura utilizada para cada uma das faces existentes no modelo.
	 * @param triangleCount quantidade de triângulos formado a conectividade do modelo.
	 */

	public void setMultipleTextures(int triangleCount)
	{
		textureIndex = new float[triangleCount];
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
	 * Deve definir o ponto exato no espaço para um determinado vértice.
	 * @param vertex índice do vértice que será definido sua textura.
	 * @param vector vetor com as coordenadas de posicionamento (x, y e z).
	 */

	public void setVertice(int vertex, Vector3f vector)
	{
		vertexPositions[(vertex * 3) + 0] = vector.x;
		vertexPositions[(vertex * 3) + 1] = vector.y;
		vertexPositions[(vertex * 3) + 2] = vector.z;
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
	 * @param vector vetor com os valores de normalização (x, y e z).
	 */

	public void setNormal(int vertex, Vector3f vector)
	{
		vertexNormals[(vertex * 3) + 0] = vector.x;
		vertexNormals[(vertex * 3) + 1] = vector.y;
		vertexNormals[(vertex * 3) + 2] = vector.z;
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

	/**
	 * Deve definir qual a textura que será usada na renderização de um triângulo do modelo.
	 * @param index índice do triângulo (conexão de vértices) que será definido a textura.
	 * @param textureID código de identificação da textura já carregada no OpenGL.
	 */

	public void setTextureIndex(int index, int textureID)
	{
		textureIndex[index] = textureID;
	}

	/**
	 * Calcula aproximadamente quantos bytes esse objeto está ocupando em memória.
	 * @return aquisição do espaço em memória ocupado pelo objeto em bytes.
	 */

	public int sizeof()
	{
		return	(vertexPositions.length * Float.BYTES) +
				(textureCoords.length * Float.BYTES) + 
				(vertexNormals.length * Float.BYTES) +
				(indices.length * Integer.BYTES) +
				(textureIndex.length * Float.BYTES);
	}

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

	@Override
	public float[] getTextureIndex()
	{
		return textureIndex;
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("vertex", vertexPositions.length / 2);
		description.append("textures", textureCoords.length / 2);
		description.append("normals", vertexNormals.length / 3);
		description.append("indices", indices.length);
		description.append("textureIndex", textureIndex.length / 2);
		description.append("sizeof", sizeof());

		return description.toString();
	}
}
