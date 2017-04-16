package com.erakin.engine.resource.world;

import com.erakin.engine.resource.TerrainLoader;

/**
 * <h1>Dados do Mundo Padrão</h1>
 *
 * <p>Classe que implementa a interface para representação de dados de um mondo afim de carregá-lo.
 * Essa implementação é simples apenas especificados os dados necessários para que os getters funcionem.
 * Esse atributos consistem em especificar o nome do mundo, pré-fixo, pasta dos terrenos, qual será o
 * carregador de terrenos usado, quantos terrenos irá possuir e qual o tamanho dos terrenos e da unidade.</p>
 *
 * @see WorldData
 * @see TerrainLoader
 *
 * @author Andrew
 */

public class WorldDataDefault implements WorldData
{
	/**
	 * Nome para que os usuários identifiquem o mundo.
	 */
	private String name;

	/**
	 * Pré-fixo usado mais para o desenvolvedor identificar o mundo.
	 */
	private String prefix;

	/**
	 * Pasta onde será encontrado os arquivos referentes aos terrenos.
	 */
	private String folder;

	/**
	 * Carregador de terrenos que será usado quando o mundo solicitar terrenos.
	 */
	private TerrainLoader loader;

	/**
	 * Quantos terrenos o mundo poderá alocar na vertical, o eixo da longitude.
	 */
	private int width;

	/**
	 * Quantos terrenos o mundo poderá alocar na horizontal, o eixo da latitude.
	 */
	private int length;

	/**
	 * Tamanho da largura do terreno em células, o tamanho varia conforme a unidade.
	 */
	private int terrainWidth;

	/**
	 * Tamanho do comprimento do terreno em células, o tamanho varia conforme a unidade.
	 */
	private int terrainLength;

	/**
	 * Tamanho da unidade de cada célula dos terrenos desse mundo.
	 */
	private float unit;

	/**
	 * Cria um novo objeto para armazenar os dados temporários de um mundo carregado.
	 * Deverá verificar se os valores de tamanho do mundo e unidade são válidos.
	 * @param width quantidade de terrenos que podem ser alocados no eixo da longitude.
	 * @param length quantidade de terrenos que podem ser alocados no eixo da latitude.
	 * @param unit tamanho de ocupação de cada célula dos terrenos desse mundo.
	 */

	public WorldDataDefault(int width, int length, float unit)
	{
		init(width, length, unit);
	}

	/**
	 * Inicializa os atributos desse objeto representando os dados de um mundo.
	 * Garante que os valores passados por parâmetro no construtor sejam válidos.
	 * @param width largura do mundo em quantidade de terrenos tendo mínimo de 2.
	 * @param length comprimento do mundo em quantidade de terrenos tendo mínimo de 2.
	 * @param unit tamanho de ocupação de cada célula tendo mínimo de 0.1f.
	 * @throw 
	 */

	private void init(int width, int length, float unit)
	{
		if (width < 2 || length < 2)
			throw new WorldRuntimeException("tamanho do mundo muito pequeno");

		if (unit < 0.1f)
			throw new WorldRuntimeException("unidade de espaço muito pequena");

		this.width = width;
		this.length = length;
		this.unit = unit;
	}

	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * Permite definir um nome para o mundo usado como identificação ao jogador.
	 * @param name novo nome a ser especificado para os dados desse mundo.
	 */

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String getPrefix()
	{
		return prefix;
	}

	/**
	 * Permite definir o pré-fixo do mundo usado como identificação ao desenvolvedor.
	 * @param prefix novo pré-fixo a ser especificado para os dados desse mundo.
	 */

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	@Override
	public String getFolder()
	{
		return folder;
	}

	/**
	 * Permite definir o diretório da pasta que irá conter os arquivos de terreno.
	 * @param folder novo caminho completo ou parcial do diretório dos terrenos.
	 */

	public void setFolder(String folder)
	{
		this.folder = folder;
	}

	@Override
	public TerrainLoader getTerrainLoader()
	{
		return loader;
	}

	/**
	 * Permite definir qual será o carregador de terrenos usado quando o mundo for criado.
	 * @param loader referência do novo carregador de terrenos que será considerado.
	 */

	public void setLoader(TerrainLoader loader)
	{
		this.loader = loader;
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
	public int getTerrainWidth()
	{
		return terrainWidth;
	}

	@Override
	public int getTerrainLength()
	{
		return terrainLength;
	}

	/**
	 * Permite definir qual será o tamanho em células de cada terreno.
	 * O mínimo permitido é de 2 células de largura e comprimento.
	 * @param terrainWidth quantidade de células no eixo da longitude (vertical).
	 * @param terrainLength quantidade de células no eixo da latitude (horizontal).
	 */

	public void setTerrainSize(int terrainWidth, int terrainLength)
	{
		if (terrainWidth > 2 && terrainLength > 2)
		{
			this.terrainWidth = terrainWidth;
			this.terrainLength = terrainLength;
		}
	}

	@Override
	public float getUnit()
	{
		return unit;
	}
}
