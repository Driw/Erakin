package com.erakin.api.resources.world;

import org.diverproject.util.lang.IntUtil;

/**
 * <h1>Dados de Terreno Padr�o</h1>
 *
 * <p>Classe padr�o usada internamente para simular o conjunto de dados carregados de um terreno.
 * Pode ser usado fora do pacote para especificar outras informa��es se assim necess�rio.
 * Por ser padr�o, possui alguns atributos referentes aos m�todos getters implementados.</p>
 *
 * <p>Os atributos s�o referentes ao tamanho do terreno como largura, altura e tamanho de unidade.
 * Seguindo ainda as informa��es referentes aos v�rtices, normaliza��o, texturas, nivela��o do
 * terreno e �ndices para ligar os v�rtices afim de formar uma modelagem para renderizar o mundo.</p>
 *
 * @see TerrainData
 *
 * @author Andrew
 */

public class TerrainDataDefault implements TerrainData
{
	/**
	 * Quantidade de c�lulas que o terreno possui na largura.
	 */
	private int width;

	/**
	 * Quantidade de c�lulas que o terreno possui no comprimento.
	 */
	private int length;

	/**
	 * Valor unit�rio do terreno indica o tamanho de cada c�lula do mesmo.
	 */
	private float unit;

	/**
	 * Vetor para os �ndices para realizar a liga��o dos v�rtices.
	 */
	private int indices[];

	/**
	 * Vetor para as informa��es dos v�rtices afim de formar a modelagem.
	 */
	private float vertices[];

	/**
	 * Vetor para normaliza��o para indicar a forma que as luzes ir�o reagir.
	 */
	private float normals[];

	/**
	 * Vetor para indicar as coordenadas das texturas que forem usadas.
	 */
	private float textureCoords[];

	/**
	 * Matriz para armazenar a altura de cada c�lula do terreno.
	 */
	private float heights[][];

	/**
	 * Cria um novo objeto para armazenar dados de um terreno de implementa��o padr�o.
	 * Inicializa o objeto criando os vetores para �ndices e outros dados conforme:
	 * @param width tamanho que o terreno dever� possuir em c�lulas na largura.
	 * @param length tamanho que o terreno dever� possuir em c�lulas no comprimento.
	 * @param unit tamanho de cada unidade do terreno, ou seja, as c�lulas.
	 */

	public TerrainDataDefault(int width, int length, float unit)
	{
		init(width, length, unit);
	}

	/**
	 * Chamado pelo construtor para realizar a inicializa��o do objeto conforme os par�metros.
	 * @param width quantas c�lulas o terreno deve ter na largura, sendo no m�nimo 1.
	 * @param length quantas c�lulas o terreno deve ter no comprimento, sendo no m�nimo 1.
	 * @param unit unidade indica o tamanho de cada c�lula no espa�o, sendo no m�nimo 0.1f.
	 * @throws TerrainRuntimeException ir� ocorrer apenas se os par�metros forem inv�lidos.
	 */

	private void init(int width, int length, float unit)
	{
		if (width < 1 || length < 1)
			throw new TerrainRuntimeException("tamanho do mundo muito pequeno");

		if (unit < 0.1f)
			throw new TerrainRuntimeException("unidade de espa�o muito pequena");

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
	 * Permite obter o valor de uma altura da c�lula no espa�o, podendo ser ainda profundidade.
	 * @param x coordenada da altura da c�lula que deseja saber no eixo longitude.
	 * @param z coordenada da altura da c�lula que deseja saber no eixo da latitude.
	 * @return aquisi��o da altura/profundidade da c�lula especificada acima.
	 */

	public float getHeight(int x, int z)
	{
		if (IntUtil.interval(x, 0, width - 1) & IntUtil.interval(z, 0, length - 1))
			return heights[x][z];

		throw new WorldRuntimeException("coordenada inv�lida (%dx %dz)", x, z);
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
