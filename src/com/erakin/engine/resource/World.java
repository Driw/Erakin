package com.erakin.engine.resource;

import static com.erakin.common.Utilities.nameOf;
import static org.diverproject.log.LogSystem.logWarning;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.lang.IntUtil;

import com.erakin.engine.resource.world.TerrainDimension;
import com.erakin.engine.resource.world.WorldRuntimeException;

/**
 * <h1>Mundo</h1>
 *
 * <p>Objetos desse tipo ir�o carregar as informa��es concretas e diretas dos mundos.
 * A ra�z fica respons�vel pelos dados b�sicos em quanto o concreto pela renderiza��o.
 * Por tanto para esse, � necess�rio o conhecimento dos v�rtices para forma��o do terreno.</p>
 *
 * <p>Al�m da base m�nima que � o terreno, o preenchimento de textura sobre esse terreno.
 * Posicionamento de objetos em rela��o ao terreno e a ilumina��o para torn�-lo mais real.
 * Podendo ainda possuir adicionais como verifica��o de colis�o ou separa��o de �reas.</p>
 *
 * @author Andre Mello
 */

public class World extends Resource
{
	/**
	 * Localiza��o do terreno vinculado no eixo X.
	 */
	private int xTerrainBound;

	/**
	 * Localiza��o do terreno vinculado no eixo Z.
	 */
	private int zTerrainBound;

	/**
	 * Terreno usado para fazer a renderiza��o desse mundo.
	 */
	private Terrain terrains[][];

	/**
	 * Observador para carregar terrenos quando necess�rio.
	 */
	private TerrainLoader terrainLoader;

	/**
	 * Dimens�o em que todos os terrenos devem seguir para estar nesse mundo.
	 */
	private TerrainDimension terrainDimension;

	/**
	 * Constr�i um novo mundo a partir de um mundo ra�z especifico.
	 * @param root mundo ra�z que ser� usada para criar o mundo.
	 */

	World(WorldRoot root)
	{
		super(root);

		terrains = new Terrain[root.width][root.length];
	}

	@Override
	public int getID()
	{
		return root == null ? 0 : ((WorldRoot) root).id;
	}

	@Override
	public void bind()
	{
		if (terrains != null)
		{
			Terrain terrain = terrains[xTerrainBound][zTerrainBound];

			if (terrain != null && terrain.getModel() != null)
				terrain.getModel().bind();
		}
	}

	@Override
	public void unbind()
	{
		if (terrains != null)
		{
			Terrain terrain = terrains[xTerrainBound][zTerrainBound];

			if (terrain != null && terrain.getModel() != null)
				terrain.getModel().unbind();
		}
	}

	@Override
	public boolean valid()
	{
		return root != null && terrains != null && terrains != null;
	}

	/**
	 * Quanto um terreno � carregado, � necess�rio que este possua um tamanho pr�-definido.
	 * Esse m�todo permite saber qual o tamanho que o terreno deve possuir em um dos lados.
	 * @return aquisi��o da quantidade de c�lulas um terreno ter� no eixo da longitude.
	 */

	public int getTerrainWidth()
	{
		return root == null ? 0 : ((WorldRoot) root).terrainWidth;
	}

	/**
	 * Quanto um terreno � carregado, � necess�rio que este possua um tamanho pr�-definido.
	 * Esse m�todo permite saber qual o tamanho que o terreno deve possuir em um dos lados.
	 * @return aquisi��o da quantidade de c�lulas um terreno ter� no eixo da latitude.
	 */

	public int getTerrainLength()
	{
		return root == null ? 0 : ((WorldRoot) root).terrainLength;
	}

	/**
	 * Mundos s�o formados por terrenos de um �nico tamanho, assim � poss�vel organiz�-los em grade.
	 * Isso facilita o mundo tamb�m a gerenciar os terrenos atrav�s de uma matriz relativa a X e Y.
	 * @return aquisi��o da quantidade de terrenos que podem ser alocados no eixo da longitude.
	 */

	public int getWidth()
	{
		return root == null ? 0 : ((WorldRoot) root).width;
	}

	/**
	 * Mundos s�o formados por terrenos de um �nico tamanho, assim � poss�vel organiz�-los em grade.
	 * Isso facilita o mundo tamb�m a gerenciar os terrenos atrav�s de uma matriz relativa a X e Y.
	 * @return aquisi��o da quantidade de terrenos que podem ser alocados no eixo da latitude.
	 */

	public int getLength()
	{
		return root == null ? 0 : ((WorldRoot) root).length;
	}

	/**
	 * Unidade � usada durante o carregamento de um terreno para definir o dimensionamento.
	 * Esse dimensionamento se refere a uma �nica c�lula (geralmente quadrados) do terreno.
	 * @return aquisi��o do tamanho de cada unidade (c�lula) do terreno.
	 */

	public float getUnit()
	{
		return root == null ? 0.0f : ((WorldRoot) root).unit;
	}

	/**
	 * Nome dos mundos normalmente s�o grandes e ID s�o gerados automaticamente.
	 * Isso torna o mundo dif�cil de ser identificado pelo desenvolvedor de jogo.
	 * Para isso � usado um pr�-fixo que � configurado no arquivo de defini��es do mundo.
	 * @return aquisi��o do pr�-fixo do mundo utiliz�-lo para facilitar sua identifica��o.
	 */

	public String getPrefix()
	{
		return root == null ? null : ((WorldRoot) root).prefix;
	}

	/**
	 * Permite definir as coordenadas do terreno que ser� definido como usado em seguida.
	 * Quando for para vincular o mundo o terreno nas coordenadas passadas ser� vinculado.
	 * O mesmo ir� acontecer com outras a��es executadas como por exemplo desvincular.
	 * @param xTerrain coordenada do terreno no eixo X em rela��o ao espa�o em terrenos.
	 * @param zTerrain coordenada do terreno no eixo Z em rela��o ao espa�o em terrenos.
	 * @return true se a coordenada for v�lida e houver um terreno no mesmo,
	 * caso contr�rio false para coordenada inv�lida ou terreno n�o existe no local.
	 */

	public boolean useTerrain(int xTerrain, int zTerrain)
	{
		if (!isTerrainCoordinate(xTerrain, zTerrain))
			return false;

		if (terrains[xTerrain][zTerrain] == null)
		{
			if (terrainLoader != null)
				terrains[xTerrain][zTerrain] = terrainLoader.load(this, xTerrain, zTerrain);

			if (terrains[xTerrain][zTerrain] == null)
				return false;
		}

		xTerrainBound = xTerrain;
		zTerrainBound = zTerrain;

		return true;
	}

	/**
	 * Outra forma de se usar os terrenos al�m de vincul�-los � usando o m�todo de get.
	 * Procedimento usado para obter diretamente o objeto terreno contendo informa��es sobre.
	 * @param xTerrain coordenada para obter o terreno em rela��o ao eixo da latitude.
	 * @param zTerrain coordenada para obter o terreno em rela��o ao eixo da longitude.
	 * @return refer�ncia do terreno alocado na coordenada passada acima.
	 */

	public Terrain getTerrain(int xTerrain, int zTerrain)
	{
		if (!isTerrainCoordinate(xTerrain, zTerrain))
			return null;

		Terrain terrain = terrains[xTerrain][zTerrain];

		if (terrain == null)
			terrains[xTerrain][zTerrain] = terrain = terrainLoader.load(this, xTerrain, zTerrain);

		return terrain;
	}

	/**
	 * Substitui um determinado terreno desse mundo de acordo com a coordenada abaixo:
	 * @param terrain refer�ncia do terreno que ser� colocado na coordenada especificada.
	 * @param xTerrain coordenada para posicionar o terreno em rela��o ao eixo da latitude.
	 * @param zTerrain coordenada para posicionar o terreno em rela��o ao eixo da longitude.
	 * @return true se for poss�vel vincular o terreno a esse mundo ou false caso contr�rio.
	 */

	public boolean setTerrain(Terrain terrain, int xTerrain, int zTerrain)
	{
		String terrainDetails = String.format("(world: %s, x: %d, z: %d)", getPrefix(), xTerrain, zTerrain);

		if (!isTerrainCoordinate(xTerrain, zTerrain))
			logWarning("terreno em coordenadas inv�lidas %s", terrainDetails);

		else if (!isLinkable(terrain))
			logWarning("terreno n�o pertence a esse mundo %s", terrainDetails);

		else if (!hasValidDimension(terrain))
			logWarning("terreno com dimensionamento inv�lido para esse mundo %s", terrainDetails);

		else
		{
			logWarning("terreno n�o pertence a esse mundo %s", terrainDetails);
			return true;
		}

		return false;
	}

	/**
	 * Verifica se uma determinada coordenada de terreno � v�lida para esse mundo.
	 * @param xTerrain coordenada do terreno em rela��o ao eixo da latitude.
	 * @param zTerrain coordenada do terreno em rela��o ao eixo da longitude.
	 * @return true se a coordenada passada for v�lida ou false caso contr�rio.
	 */

	public boolean isTerrainCoordinate(int xTerrain, int zTerrain)
	{
		return	IntUtil.interval(xTerrain, 0, terrains.length - 1) &&
				IntUtil.interval(zTerrain, 0, terrains[xTerrain].length);
	}

	/**
	 * Verifica se um determinado terreno pode ser ligado a esse mundo.
	 * Procedimento usado para permitir que um terreno seja adicionado.
	 * @param terrain refer�ncia do terreno que ser� verificado.
	 * @return true se todas as condi��es forem atendidas ou false caso contr�rio,
	 * para isso deve existir uma ra�z v�lida e o terreno pertencer a esse mundo.
	 */

	boolean isLinkable(Terrain terrain)
	{
		if (root == null || ((WorldRoot) root).id != terrain.getWorldID())
			return false;

		return true;
	}

	boolean hasValidDimension(Terrain terrain)
	{
		if (root == null)
			return false;

		return	terrain.getWidth() == terrainDimension.getWidth() &&
				terrain.getLength() == terrainDimension.getLength();
	}

	/**
	 * Permite definir um novo observador para garantir que terrenos sejam carregado adequadamente.
	 * Observadores para terrenos de mundos funcionam como interface podendo ser implementando de acordo.
	 * Portanto � poss�vel que o desenvolvedor fa�a o carregamento do terreno como preferir.
	 * @param observer refer�ncia do observador que ser� usado para carregar os terrenos.
	 */

	void setWorldTerrainLoader(TerrainLoader observer)
	{
		this.terrainLoader = observer;
	}

	/**
	 * Permite definir qual ser� a dimens�o que os terrenos dever�o possuir para estar nesse mundo.
	 * Uma vez que a dimens�o do mundo seja definida n�o ser� permitido que este seja trocado.
	 * Isso � restringido a fim de evitar que haja colis�o ou espa�o em branco entre os terrenos.
	 * @param dimension refer�ncia da dimens�o de terrenos que esse mundo dever� se usar.
	 */

	void setTerrainDimension(TerrainDimension dimension)
	{
		if (terrainDimension != null)
			throw new WorldRuntimeException("tentativa de mudar a dimens�o de terrenos de '%s'", root.fileName);

		terrainDimension = dimension;
	}

	/**
	 * Caminho dos arquivos para terrenos ir� indicar onde todas os terrenos est�o armazenadas.
	 * Isso � especificado atrav�s do arquivo principal de dados b�sicos do mundo.
	 * @return aquisi��o do caminho contendo os arquivos para carregar terrenos.
	 */

	public String getTerrainFilepath()
	{
		return root == null ? null : ((WorldRoot) root).folder.getPath();
	}

	@Override
	public void release()
	{
		if (terrains != null)
		{
			for (Terrain array[] : terrains)
				for (Terrain terrain : array)
					if (terrain != null)
					{
						if (terrain.getModel() != null)
							terrain.getModel().release();

						if (terrain.getHeightTexture() != null)
							terrain.getHeightTexture().release();
					}

			terrains = null;
		}

		terrainLoader = null;

		super.release();
	}

	@Override
	public void toString(ObjectDescription description)
	{
		description.append("prefix", getPrefix());
		description.append("loader", nameOf(terrainLoader));
		description.append("terrainWidth", terrainDimension.getWidth());
		description.append("terrainLength", terrainDimension.getLength());
		description.append("terrains", terrains != null);
	}
}
