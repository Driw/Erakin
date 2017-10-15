package com.erakin.api.resources.world;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Defini��es de Mundo</h1>
 *
 * <p>Essa classe permite criar objetos que ir�o armazenar informa��es de um mundo.
 * Todas as informa��es b�sicas necess�rias para se criar um novo mundo aqui est�o.
 * Quando novos mundos s�o lidos, eles sempre v�o retornar um objeto desse ao carregador.
 * Ap�s o terem sido preenchido com as informa��es pertinentes o carregador cria o mundo.</p>
 *
 * @author Andrew
 */

public class WorldSettings
{
	/**
	 * Nome do mundo que � exibido em logs ou para o jogador.
	 */
	private String name;

	/**
	 * Pr�-fixo usado para que o desenvolvedor encontre o mundo.
	 */
	private String prefix;

	/**
	 * Quantos terrenos o mundo ir� possuir no eixo na longitude.
	 */
	private int width;

	/**
	 * Quantos terrenos o mundo ir� possuir no eixo na latitude.
	 */
	private int length;

	/**
	 * Quantas c�lulas os terrenos devem possuir na largura.
	 */
	private int terrainWidth;

	/**
	 * Quantas c�lulas os terrenos devem possuir no comprimento.
	 */
	private int terrainLength;

	/**
	 * Tamanho do espa�o que uma c�lula dever� ocupar.
	 */
	private float unit;

	/**
	 * Caminho parcial ou completo do diret�rio que ser� lido os terrenos.
	 */
	private String path;

	/**
	 * Refer�ncia da classe que ser� usada como carregador de terrenos.
	 */
	private Class<?> loader;

	/**
	 * Nome do mundo � usado visualmente na interface de usu�rio ou logs.
	 * @return aquisi��o do nome que foi obtido atrav�s da leitura do mundo.
	 */

	public String getName()
	{
		return name;
	}

	/**
	 * Permite definir qual o nome a ser visualizado na interface de usu�rio ou logs.
	 * @param name nome que foi obtido atrav�s da leitura do mundo.
	 */

	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Pr�-fixo � usado somente internamente pelos desenvolvedores para identificar o mundo.
	 * Internamente a engine define uma identifica��o incremental e num�rica para os mundos.
	 * Sendo necess�rio ter uma identifica��o de f�cil memoriza��o e est�tico.
	 * @return aquisi��o do pr�-fixo que foi obtido atrav�s da leitura do mundo.
	 */

	public String getPrefix()
	{
		return prefix;
	}

	/**
	 * Permite definir qual o nome a ser vinculado para an�lise de desenvolvimento ou comandos.
	 * @param prefix pr�-fixo que foi obtido atrav�s da leitura do mundo.
	 */

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	/**
	 * A largura do mundo especifica o n�mero de terrenos alocados na vertical.
	 * @return aquisi��o da quantidade de terrenos no eixo da longitude.
	 */

	public int getWidth()
	{
		return width;
	}

	/**
	 * Permite definir quantos terrenos poder�o ser alocados no eixo da longitude.
	 * @param width n�mero limite de terrenos na vertical obtidos da leitura do mundo.
	 */

	public void setWidth(int width)
	{
		this.width = width;
	}

	/**
	 * O comprimento do mundo especifica o n�mero de terrenos alocados na horizontal.
	 * @return aquisi��o da quantidade de terrenos no eixo da latitude.
	 */

	public int getLength()
	{
		return length;
	}

	/**
	 * Permite definir quantos terrenos poder�o ser alocados no eixo da latitude.
	 * @param length n�mero limite de terrenos na horizontal obtidos da leitura do mundo.
	 */

	public void setLength(int length)
	{
		this.length = length;
	}

	/**
	 * A largura do terreno especifica quantas c�lulas ir� possuir na vertical.
	 * @return aquisi��o do n�mero de c�lulas alocados no eixo da longitude.
	 */

	public int getTerrainWidth()
	{
		return terrainWidth;
	}

	/**
	 * Permite definir um novo valor para defini��o do n�mero de c�lulas por terreno.
	 * @param terrainWidth n�mero de c�lulas no eixo da longitude obtidos atrav�s da leitura do mundo.
	 */

	public void setChunkWidth(int terrainWidth)
	{
		this.terrainWidth = terrainWidth;
	}

	/**
	 * A largura do terreno especifica quantas c�lulas ir� possuir na horizontal.
	 * @return aquisi��o do n�mero de c�lulas alocados no eixo da latitude.
	 */

	public int getTerrainLength()
	{
		return terrainLength;
	}

	/**
	 * Permite definir um novo valor para defini��o do n�mero de c�lulas por terreno.
	 * @param terrainLength n�mero de c�lulas no eixo da latitude obtidos atrav�s da leitura do mundo.
	 */

	public void setChunkLength(int terrainLength)
	{
		this.terrainLength = terrainLength;
	}

	/**
	 * Unidade especifica o tamanho que cada c�lula dos terrenos ter� de ocupa��o no espa�o renderizado.
	 * @return aquisi��o do tamanho em valor flutuante para cada c�lula de terreno, padr�o 1.0f.
	 */

	public float getUnit()
	{
		return unit;
	}

	/**
	 * Permite definir qual ser� o tamanho de cada c�lula dos terrenos carregados por esse mundo.
	 * @param unit valor unit�rio para as c�lulas de terrenos pertinentes a este mundo.
	 */

	public void setUnit(float unit)
	{
		this.unit = unit;
	}

	/**
	 * O caminho � usado para que seja poss�vel especificar um diret�rio com os terrenos.
	 * @return aquisi��o do caminho parcial ou completo contendo os terrenos � carregar.
	 */

	public String getPath()
	{
		return path;
	}

	/**
	 * Permite definir qual o caminho do diret�rio que possui os terrenos a serem carregados.
	 * @param path caminho parcial ou completo contendo os terrenos � carregar.
	 */

	public void setPath(String path)
	{
		this.path = path;
	}

	/**
	 * Carregador deve ser uma classe que implemente a interface TerrainLoader.
	 * Essa classe dever� especificar como os terrenos poder�o ser carregados.
	 * @return aquisi��o da classe que permitir� os terrenos serem carregados.
	 */

	public Class<?> getLoader()
	{
		return loader;
	}

	/**
	 * Permite definir uma classe que dever� implementar a forma como os terrenos s�o carregados.
	 * Pode existir diversos tipos de carregador, por�m cada mundo s� poder� usar um tipo.
	 * @param loader refer�ncia da classe que permitir� os terrenos serem carregados.
	 */

	public void setLoader(Class<?> loader)
	{
		this.loader = loader;
	}

	/**
	 * Permite definir uma classe que dever� implementar a forma como os terrenos s�o carregados.
	 * Pode existir diversos tipos de carregador, por�m cada mundo s� poder� usar um tipo.
	 * @param loaderPath caminho da classe no projeto em Java para se obter a classe.
	 * @throws ClassNotFoundException apenas se a classe n�o for encontrada no projeto.
	 */

	public void setLoader(String loaderPath) throws ClassNotFoundException
	{
		this.loader = Class.forName(loaderPath);
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("name", name);
		description.append("prefix", prefix);
		description.append("width", width);
		description.append("length", length);
		description.append("chunkWidth", terrainWidth);
		description.append("chunkLenght", terrainLength);
		description.append("unit", unit);
		description.append("observer", loader.getSimpleName());

		return description.toString();
	}
	
}
