package com.erakin.api.resources;

import static com.erakin.api.ErakinAPIUtil.nameOf;
import static org.diverproject.log.LogSystem.logWarning;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.lang.IntUtil;

import com.erakin.api.render.WorldRender;
import com.erakin.api.resources.world.TerrainDimension;

/**
 * <h1>Mundo</h1>
 *
 * <p>Objetos desse tipo irão carregar as informações concretas e diretas dos mundos.
 * A raíz fica responsável pelos dados básicos em quanto o concreto pela renderização.
 * Por tanto para esse, é necessário o conhecimento dos vértices para formação do terreno.</p>
 *
 * <p>Além da base mínima que é o terreno, o preenchimento de textura sobre esse terreno.
 * Posicionamento de objetos em relação ao terreno e a iluminação para torná-lo mais real.
 * Podendo ainda possuir adicionais como verificação de colisão ou separação de áreas.</p>
 *
 * @author Andre Mello
 */

public class World extends Resource implements WorldRender
{
	/**
	 * Localização do terreno vinculado no eixo X.
	 */
	private int xTerrainBound;

	/**
	 * Localização do terreno vinculado no eixo Z.
	 */
	private int zTerrainBound;

	/**
	 * Terreno usado para fazer a renderização desse mundo.
	 */
	private Terrain terrains[][];

	/**
	 * Observador para carregar terrenos quando necessário.
	 */
	private TerrainLoader terrainLoader;

	/**
	 * Dimensão em que todos os terrenos devem seguir para estar nesse mundo.
	 */
	private TerrainDimension terrainDimension;

	/**
	 * Constrói um novo mundo a partir de um mundo raíz especifico.
	 * @param root mundo raíz que será usada para criar o mundo.
	 */

	World(WorldRoot root)
	{
		super(root);

		terrains = new Terrain[root.width][root.length];
		terrainDimension = root.terrainDimension;
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
		return root != null && terrains != null;
	}

	@Override
	public int getTerrainWidth()
	{
		return root == null ? 0 : ((WorldRoot) root).terrainDimension.getWidth();
	}

	@Override
	public int getTerrainLength()
	{
		return root == null ? 0 : ((WorldRoot) root).terrainDimension.getLength();
	}

	@Override
	public int getWidth()
	{
		return root == null ? 0 : ((WorldRoot) root).width;
	}

	@Override
	public int getLength()
	{
		return root == null ? 0 : ((WorldRoot) root).length;
	}

	@Override
	public float getUnitSize()
	{
		return root == null ? 0.0f : ((WorldRoot) root).unit;
	}

	@Override
	public String getPrefix()
	{
		return root == null ? null : ((WorldRoot) root).prefix;
	}

	/**
	 * Permite definir as coordenadas do terreno que será definido como usado em seguida.
	 * Quando for para vincular o mundo o terreno nas coordenadas passadas será vinculado.
	 * O mesmo irá acontecer com outras ações executadas como por exemplo desvincular.
	 * @param xTerrain coordenada do terreno no eixo X em relação ao espaço em terrenos.
	 * @param zTerrain coordenada do terreno no eixo Z em relação ao espaço em terrenos.
	 * @return true se a coordenada for válida e houver um terreno no mesmo,
	 * caso contrário false para coordenada inválida ou terreno não existe no local.
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

	@Override
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
	 * @param terrain referência do terreno que será colocado na coordenada especificada.
	 * @param xTerrain coordenada para posicionar o terreno em relação ao eixo da latitude.
	 * @param zTerrain coordenada para posicionar o terreno em relação ao eixo da longitude.
	 * @return true se for possível vincular o terreno a esse mundo ou false caso contrário.
	 */

	public boolean setTerrain(Terrain terrain, int xTerrain, int zTerrain)
	{
		String terrainDetails = String.format("(world: %s, x: %d, z: %d)", getPrefix(), xTerrain, zTerrain);

		if (!isTerrainCoordinate(xTerrain, zTerrain))
			logWarning("terreno em coordenadas inválidas %s", terrainDetails);

		else if (!isLinkable(terrain))
			logWarning("terreno não pertence a esse mundo %s", terrainDetails);

		else if (!hasValidDimension(terrain))
			logWarning("terreno com dimensionamento inválido para esse mundo %s", terrainDetails);

		else
		{
			logWarning("terreno não pertence a esse mundo %s", terrainDetails);
			return true;
		}

		return false;
	}

	/**
	 * Verifica se uma determinada coordenada de terreno é válida para esse mundo.
	 * @param xTerrain coordenada do terreno em relação ao eixo da latitude.
	 * @param zTerrain coordenada do terreno em relação ao eixo da longitude.
	 * @return true se a coordenada passada for válida ou false caso contrário.
	 */

	public boolean isTerrainCoordinate(int xTerrain, int zTerrain)
	{
		return	IntUtil.interval(xTerrain, 0, terrains.length - 1) &&
				IntUtil.interval(zTerrain, 0, terrains[xTerrain].length);
	}

	/**
	 * Verifica se um determinado terreno pode ser ligado a esse mundo.
	 * Procedimento usado para permitir que um terreno seja adicionado.
	 * @param terrain referência do terreno que será verificado.
	 * @return true se todas as condições forem atendidas ou false caso contrário,
	 * para isso deve existir uma raíz válida e o terreno pertencer a esse mundo.
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
	 * Portanto é possível que o desenvolvedor faça o carregamento do terreno como preferir.
	 * @param observer referência do observador que será usado para carregar os terrenos.
	 */

	void setWorldTerrainLoader(TerrainLoader observer)
	{
		this.terrainLoader = observer;
	}

	/**
	 * Dimensionamento do terreno permite especificar quantas células cada terreno vai possuir.
	 * Alterar o valor após o carregamento de um terreno poderá causar má renderização.
	 * As mudanças feitas nesse mundo serão aplicados a todos os mundos relacionados ao root.
	 * @return aquisição do objeto contendo o dimensionamento de terrenos do mundo.
	 */

	public TerrainDimension getTerrainDimension()
	{
		return terrainDimension;
	}

	/**
	 * Caminho dos arquivos para terrenos irá indicar onde todas os terrenos estão armazenadas.
	 * Isso é especificado através do arquivo principal de dados básicos do mundo.
	 * @return aquisição do caminho contendo os arquivos para carregar terrenos.
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

		if (terrainDimension != null)
		{
			description.append("terrainWidth", terrainDimension.getWidth());
			description.append("terrainLength", terrainDimension.getLength());
		}

		description.append("terrains", terrains != null);
	}
}
