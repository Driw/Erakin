package com.erakin.api.resources.world;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Definições de Mundo</h1>
 *
 * <p>Essa classe permite criar objetos que irão armazenar informações de um mundo.
 * Todas as informações básicas necessárias para se criar um novo mundo aqui estão.
 * Quando novos mundos são lidos, eles sempre vão retornar um objeto desse ao carregador.
 * Após o terem sido preenchido com as informações pertinentes o carregador cria o mundo.</p>
 *
 * @author Andrew
 */

public class WorldSettings
{
	/**
	 * Nome do mundo que é exibido em logs ou para o jogador.
	 */
	private String name;

	/**
	 * Pré-fixo usado para que o desenvolvedor encontre o mundo.
	 */
	private String prefix;

	/**
	 * Quantos terrenos o mundo irá possuir no eixo na longitude.
	 */
	private int width;

	/**
	 * Quantos terrenos o mundo irá possuir no eixo na latitude.
	 */
	private int length;

	/**
	 * Quantas células os terrenos devem possuir na largura.
	 */
	private int terrainWidth;

	/**
	 * Quantas células os terrenos devem possuir no comprimento.
	 */
	private int terrainLength;

	/**
	 * Tamanho do espaço que uma célula deverá ocupar.
	 */
	private float unit;

	/**
	 * Caminho parcial ou completo do diretório que será lido os terrenos.
	 */
	private String path;

	/**
	 * Referência da classe que será usada como carregador de terrenos.
	 */
	private Class<?> loader;

	/**
	 * Nome do mundo é usado visualmente na interface de usuário ou logs.
	 * @return aquisição do nome que foi obtido através da leitura do mundo.
	 */

	public String getName()
	{
		return name;
	}

	/**
	 * Permite definir qual o nome a ser visualizado na interface de usuário ou logs.
	 * @param name nome que foi obtido através da leitura do mundo.
	 */

	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Pré-fixo é usado somente internamente pelos desenvolvedores para identificar o mundo.
	 * Internamente a engine define uma identificação incremental e numérica para os mundos.
	 * Sendo necessário ter uma identificação de fácil memorização e estático.
	 * @return aquisição do pré-fixo que foi obtido através da leitura do mundo.
	 */

	public String getPrefix()
	{
		return prefix;
	}

	/**
	 * Permite definir qual o nome a ser vinculado para análise de desenvolvimento ou comandos.
	 * @param prefix pré-fixo que foi obtido através da leitura do mundo.
	 */

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	/**
	 * A largura do mundo especifica o número de terrenos alocados na vertical.
	 * @return aquisição da quantidade de terrenos no eixo da longitude.
	 */

	public int getWidth()
	{
		return width;
	}

	/**
	 * Permite definir quantos terrenos poderão ser alocados no eixo da longitude.
	 * @param width número limite de terrenos na vertical obtidos da leitura do mundo.
	 */

	public void setWidth(int width)
	{
		this.width = width;
	}

	/**
	 * O comprimento do mundo especifica o número de terrenos alocados na horizontal.
	 * @return aquisição da quantidade de terrenos no eixo da latitude.
	 */

	public int getLength()
	{
		return length;
	}

	/**
	 * Permite definir quantos terrenos poderão ser alocados no eixo da latitude.
	 * @param length número limite de terrenos na horizontal obtidos da leitura do mundo.
	 */

	public void setLength(int length)
	{
		this.length = length;
	}

	/**
	 * A largura do terreno especifica quantas células irá possuir na vertical.
	 * @return aquisição do número de células alocados no eixo da longitude.
	 */

	public int getTerrainWidth()
	{
		return terrainWidth;
	}

	/**
	 * Permite definir um novo valor para definição do número de células por terreno.
	 * @param terrainWidth número de células no eixo da longitude obtidos através da leitura do mundo.
	 */

	public void setChunkWidth(int terrainWidth)
	{
		this.terrainWidth = terrainWidth;
	}

	/**
	 * A largura do terreno especifica quantas células irá possuir na horizontal.
	 * @return aquisição do número de células alocados no eixo da latitude.
	 */

	public int getTerrainLength()
	{
		return terrainLength;
	}

	/**
	 * Permite definir um novo valor para definição do número de células por terreno.
	 * @param terrainLength número de células no eixo da latitude obtidos através da leitura do mundo.
	 */

	public void setChunkLength(int terrainLength)
	{
		this.terrainLength = terrainLength;
	}

	/**
	 * Unidade especifica o tamanho que cada célula dos terrenos terá de ocupação no espaço renderizado.
	 * @return aquisição do tamanho em valor flutuante para cada célula de terreno, padrão 1.0f.
	 */

	public float getUnit()
	{
		return unit;
	}

	/**
	 * Permite definir qual será o tamanho de cada célula dos terrenos carregados por esse mundo.
	 * @param unit valor unitário para as células de terrenos pertinentes a este mundo.
	 */

	public void setUnit(float unit)
	{
		this.unit = unit;
	}

	/**
	 * O caminho é usado para que seja possível especificar um diretório com os terrenos.
	 * @return aquisição do caminho parcial ou completo contendo os terrenos à carregar.
	 */

	public String getPath()
	{
		return path;
	}

	/**
	 * Permite definir qual o caminho do diretório que possui os terrenos a serem carregados.
	 * @param path caminho parcial ou completo contendo os terrenos à carregar.
	 */

	public void setPath(String path)
	{
		this.path = path;
	}

	/**
	 * Carregador deve ser uma classe que implemente a interface TerrainLoader.
	 * Essa classe deverá especificar como os terrenos poderão ser carregados.
	 * @return aquisição da classe que permitirá os terrenos serem carregados.
	 */

	public Class<?> getLoader()
	{
		return loader;
	}

	/**
	 * Permite definir uma classe que deverá implementar a forma como os terrenos são carregados.
	 * Pode existir diversos tipos de carregador, porém cada mundo só poderá usar um tipo.
	 * @param loader referência da classe que permitirá os terrenos serem carregados.
	 */

	public void setLoader(Class<?> loader)
	{
		this.loader = loader;
	}

	/**
	 * Permite definir uma classe que deverá implementar a forma como os terrenos são carregados.
	 * Pode existir diversos tipos de carregador, porém cada mundo só poderá usar um tipo.
	 * @param loaderPath caminho da classe no projeto em Java para se obter a classe.
	 * @throws ClassNotFoundException apenas se a classe não for encontrada no projeto.
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
