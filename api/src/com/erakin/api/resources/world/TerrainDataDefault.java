package com.erakin.api.resources.world;

import org.diverproject.util.lang.IntUtil;

/**
 * <h1>Dados de Terreno Padrão</h1>
 *
 * <p>Classe padrão usada internamente para simular o conjunto de dados carregados de um terreno.
 * Pode ser usado fora do pacote para especificar outras informações se assim necessário.
 * Por ser padrão, possui alguns atributos referentes aos métodos getters implementados.</p>
 *
 * <p>Os atributos são referentes ao tamanho do terreno como largura, altura e tamanho de unidade.
 * Seguindo ainda as informações referentes aos vértices, normalização, texturas, nivelação do
 * terreno e índices para ligar os vértices afim de formar uma modelagem para renderizar o mundo.</p>
 *
 * @see TerrainData
 *
 * @author Andrew
 */

public class TerrainDataDefault implements TerrainData
{
	/**
	 * Quantidade de células que o terreno possui na largura.
	 */
	private int width;

	/**
	 * Quantidade de células que o terreno possui no comprimento.
	 */
	private int length;

	/**
	 * Valor unitário do terreno indica o tamanho de cada célula do mesmo.
	 */
	private float unit;

	/**
	 * Vetor para os índices para realizar a ligação dos vértices.
	 */
	private int indices[];

	/**
	 * Vetor para as informações dos vértices afim de formar a modelagem.
	 */
	private float vertices[];

	/**
	 * Vetor para normalização para indicar a forma que as luzes irão reagir.
	 */
	private float normals[];

	/**
	 * Vetor para indicar as coordenadas das texturas que forem usadas.
	 */
	private float textureCoords[];

	/**
	 * Matriz para armazenar a altura de cada célula do terreno.
	 */
	private float heights[][];

	/**
	 * Cria um novo objeto para armazenar dados de um terreno de implementação padrão.
	 * Inicializa o objeto criando os vetores para índices e outros dados conforme:
	 * @param width tamanho que o terreno deverá possuir em células na largura.
	 * @param length tamanho que o terreno deverá possuir em células no comprimento.
	 * @param unit tamanho de cada unidade do terreno, ou seja, as células.
	 */

	public TerrainDataDefault(int width, int length, float unit)
	{
		init(width, length, unit);
	}

	/**
	 * Chamado pelo construtor para realizar a inicialização do objeto conforme os parâmetros.
	 * @param width quantas células o terreno deve ter na largura, sendo no mínimo 1.
	 * @param length quantas células o terreno deve ter no comprimento, sendo no mínimo 1.
	 * @param unit unidade indica o tamanho de cada célula no espaço, sendo no mínimo 0.1f.
	 * @throws TerrainRuntimeException irá ocorrer apenas se os parâmetros forem inválidos.
	 */

	private void init(int width, int length, float unit)
	{
		if (width < 1 || length < 1)
			throw new TerrainRuntimeException("tamanho do mundo muito pequeno");

		if (unit < 0.1f)
			throw new TerrainRuntimeException("unidade de espaço muito pequena");

		this.width = width;
		this.length = length;
		this.unit = unit;

		int cells = width * length;

		indices = new int[cells * 6];
		vertices = new float[cells * 3];
		normals = new float[cells * 3];
		textureCoords = new float[cells * 2];
	}

	/**
	 * Permite obter o valor de uma altura da célula no espaço, podendo ser ainda profundidade.
	 * @param x coordenada da altura da célula que deseja saber no eixo longitude.
	 * @param z coordenada da altura da célula que deseja saber no eixo da latitude.
	 * @return aquisição da altura/profundidade da célula especificada acima.
	 */

	public float getHeight(int x, int z)
	{
		if (IntUtil.interval(x, 0, width - 1) & IntUtil.interval(z, 0, length - 1))
			return heights[x][z];

		throw new WorldRuntimeException("coordenada inválida (%dx %dz)", x, z);
	}

	@Override
	public int getWidth()
	{
		return width;
	}

	@Override
	public int getLength()
	{
		return length;
	}

	@Override
	public float getUnit()
	{
		return unit;
	}

	@Override
	public float[] getVertices()
	{
		return vertices;
	}

	@Override
	public float[] getTextureCoords()
	{
		return textureCoords;
	}

	@Override
	public float[] getNormals()
	{
		return normals;
	}

	@Override
	public int[] getIndices()
	{
		return indices;
	}

	@Override
	public float[][] getHeights()
	{
		return heights;
	}
}
