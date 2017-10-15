package com.erakin.engine.render;

import static com.erakin.api.ErakinAPIUtil.nameOf;
import static org.diverproject.log.LogSystem.logWarning;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.collection.Queue;
import org.diverproject.util.collection.abstraction.DynamicQueue;
import org.diverproject.util.lang.IntUtil;

import com.erakin.api.lwjgl.math.Vector3i;
import com.erakin.api.render.ModelRender;
import com.erakin.api.render.TerrainRender;
import com.erakin.api.render.WorldRender;
import com.erakin.engine.camera.Camera;
import com.erakin.engine.world.light.Light;

/**
 * <h1>Renderizador para Mundos Padr�o</h1>
 *
 * <p>Esse renderizador � uma implementa��o b�sica para gerenciador de mundos que pode ser usado.
 * Implementa a inicializa��o autom�tica no mesmo sendo necess�rio apenas determinar o que ser� iniciado.</p>
 *
 * <p>Utiliza uma cole��o do tipo fila como armazenamento dos mundos e usa a fila din�mica para tal.
 * Nesse caso a fila � usada ao final de toda renderiza��o e os mundos ser�o removidos do mesmo.
 * A forma que essa estrutura trabalha, � o melhor para ser alocado como armazenador dos mundos.</p>
 *
 * @see RendererWorlds
 * @see ModelRender
 * @see TerrainRender
 * @see WorldRender
 * @see Camera
 * @see Light
 *
 * @author Andre Mello
 */

public abstract class RendererWorldsDefault implements RendererWorlds
{
	/**
	 * Define se o renderizador de entidades j� foi iniciado.
	 */
	private boolean initiate;

	/**
	 * Mundo que ser� usado para obter as chunks e renderiz�-los.
	 */
	private WorldRender world;

	/**
	 * Ponto central para efetuar a renderiza��o.
	 */
	private Vector3i position;

	/**
	 * Dist�ncia para renderiza��o a partir do ponto central.
	 */
	private int range;

	/**
	 * Lista contendo todas as chunks que ser�o renderizadas.
	 */
	private Queue<TerrainRender> terrains;

	/**
	 * Constr�i um novo renderizador de mundos padr�es iniciado a posi��o central e campo de vis�o.
	 * O ponto inicial ser� as coordenadas no mundo de 0,0 (x,y) e vis�o de 64x64 (em c�lulas).
	 */

	public RendererWorldsDefault()
	{
		range = 64;
		position = new Vector3i();
		terrains = new DynamicQueue<TerrainRender>();
	}

	@Override
	public void cleanup()
	{
		terrains.clear();
	}

	@Override
	public final void initiate()
	{
		initiate = true;

		subInitiate();
	}

	@Override
	public final boolean isInitiate()
	{
		return initiate;
	}

	@Override
	public void update(long delay)
	{
		
	}

	@Override
	public final void render(long delay)
	{
		if (getCamera() == null || getLight() == null || world == null)
			return;

		beforeRender(delay);
		renderWorld(world);
		afterRender(delay);
	}

	/**
	 * Mundo � usado para saber de onde os terrenos a serem renderizados ser�o obtidos.
	 * Assim sendo, o renderizado poder� saber os terrenos de que mundo devem ser visualizados.
	 * @return aquisi��o da refer�ncia do mundo renderiz�vel do qual est� sendo renderizado.
	 */

	public WorldRender getWorld()
	{
		return world;
	}

	@Override
	public void setRenderPosition(Vector3i position)
	{
		this.position.set(position.x, position.y, position.z);
	}

	/**
	 * Atrav�s da posi��o de renderiza��o que ser� usada como ponto central para o mesmo,
	 * � poss�vel saber quais terrenos s�o necess�rios serem renderizador conforme o alcance
	 * de vis�o dos mesmos, reduzindo assim tempo de processamento desnecess�rio na engine.
	 * @return aquisi��o de uma clonagem do objeto contendo a coordenada de renderiza��o.
	 */

	public Vector3i getRenderPosition()
	{
		return position.clone();
	}

	@Override
	public void setRenderRange(int distance)
	{
		if (distance > 0)
			range = distance;
	}

	/**
	 * Alcance de renderiza��o ir� indicar quantos terrenos de dist�ncia estar� sendo chamado.
	 * Quanto maior o n�mero, maior a quantidade de terrenos ao redor para serem renderizados.
	 * @return aquisi��o da quantidade de terrenos que dever�o ser renderizados na tela.
	 */

	public int getRenderRange()
	{
		return range;
	}

	@Override
	public final void setWorld(WorldRender world)
	{
		if (world != null)
			this.world = world;
	}

	/**
	 * Chamado internamente para fazer a renderiza��o de um determinado mundo especificado.
	 * A renderiza��o funciona de acordo com a posi��o central de renderiza��o e dist�ncia.
	 * Nenhum terreno fora do alcance ser� enfileirado para a renderiza��o de terrenos.
	 * @param world refer�ncia do mundo renderiz�vel do qual ser� utilizado.
	 */

	private void renderWorld(WorldRender world)
	{
		int realRange = (int) (range * world.getUnit());

		int terrainWidth = (int) (world.getTerrainWidth() * world.getUnit());
		int terrainLength = (int) (world.getTerrainLength() * world.getUnit());
		int maxX = terrainWidth * world.getWidth();
		int maxZ = terrainLength * world.getLength();

		int entityX = position.getX();
		int entityZ = position.getZ();
		int startX = IntUtil.limit(entityX - realRange, 0, maxX);
		int endX = IntUtil.limit(entityX + realRange, 0, maxX);
		int startZ = IntUtil.limit(entityZ - realRange, 0, maxZ);
		int endZ = IntUtil.limit(entityZ + realRange, 0, maxZ);

		for (int renderZ = startZ; renderZ < endZ; renderZ += terrainLength)
			for (int renderX = startX; renderX < endX; renderX += terrainWidth)
			{
				int xTerrain = renderX / terrainWidth;
				int zTerrain = renderZ / terrainLength;

				TerrainRender terrain = world.getTerrain(xTerrain, zTerrain);

				if (terrain != null && !terrains.contains(terrain))
					terrains.offer(terrain);
			}

		renderTerrains(terrains);
	}

	/**
	 * Procedimento usado internamente para fazer a renderiza��o dos terrenos em fila.
	 * A fila de terrenos � feita de acordo com a posi��o central de renderiza��o e a
	 * dist�ncia limite para renderiza��o destes, enfileirando de forma adequada.
	 * @param terrains refer�ncia da fila contendo os terrenos que devem ser renderizados.
	 */

	private void renderTerrains(Queue<TerrainRender> terrains)
	{
		while (terrains.size() > 0)
		{
			TerrainRender terrain = terrains.poll();
			ModelRender model = terrain.getModel();

			if (model == null)
				logWarning("terreno sem modelagem (world: %s, terreno: %d,%d)\n", world.getPrefix(), terrain.getX(), terrain.getZ());

			else
			{
				beforeRenderChunk(model);
				renderTerrain(terrain);
				afterRenderChunk(model);
			}
		}
	}

	/**
	 * Chamado internamente quando for dito ao renderizador de entidades para ser iniciado.
	 * Ir� definir um atributo como inicializado de modo a facilitar a implementa��o do mesmo.
	 */

	protected abstract void subInitiate();

	/**
	 * Durante a renderiza��o de entidades, o grupo de entidades com o mesmo modelo s�o chamados para renderizar.
	 * Ap�s fazer a ativa��o (habilitar uso) da modelagem esse m�todo ser� chamado uma �nica vez por cada entidade.
	 * Dever� garantir que as entidades sejam renderizadas na tela utilizando sua textura e shader adequados.
	 * @param terrain refer�ncia do terreno renderiz�vel do qual est� sendo chamada para renderizar.
	 */

	protected abstract void renderTerrain(TerrainRender terrain);

	/**
	 * Antes de fazer a renderiza��o das entidades, � necess�rio habilitar no OpenGL uma modelagem para ser usada.
	 * Assim que essa for habilitada, as entidades que usam esse modelo ser�o chamadas para serem renderizadas.
	 * Esse procedimento pode ainda usar o shader adequadamente de acordo com as informa��es da modelagem.
	 * @param model refer�ncia do modelo renderiz�vel do pr�ximo conjunto de entidades a ser renderizada.
	 */

	protected abstract void beforeRenderChunk(ModelRender model);

	/**
	 * Ap�s fazer a renderiza��o de um conjunto de entidades especificados, esse procedimento ser� chamado.
	 * Esse conjunto de entidades tem em comum a sua modelagem tri-dimensional usada, <b>model</b>.
	 * Deve dizer ao OpenGL para desabilitar o uso dessa modelagem ou informa��es do shader se necess�rio.
	 * @param model refer�ncia do modelo renderiz�vel do conjunto de entidades que foram renderizada.
	 */

	protected abstract void afterRenderChunk(ModelRender model);

	/**
	 * Procedimento chamado assim que for solicitado ao renderizador para renderizar.
	 * Espera-se que seja feito todo o preparamento necess�rio para iniciar a renderiza��o.
	 * Por exemplo iniciar a programa��o shader para realizar os efeitos gr�ficos.
	 * @param delay quantos milissegundos se passaram desde a �ltima renderiza��o.
	 */

	protected abstract void beforeRender(long delay);

	/**
	 * Procedimento chamado somente ap�s o renderizador ter renderizado todas as entidades.
	 * Espera-se que seja feito toda a finaliza��o necess�ria para concluir a renderiza��o.
	 * Por exemplo parar a programa��o shader evitando uso desnecess�rio ou incorreto.
	 * @param delay quantos milissegundos se passaram desde a �ltima renderiza��o.
	 */

	protected abstract void afterRender(long delay);

	/**
	 * C�mera na renderiza��o ser� usada para que seja poss�vel criar a matriz de vis�o.
	 * Essa matriz de vis�o � que ir� guardar informa��es do quanto ser� visto na tela.
	 * @return aquisi��o da c�mera atualmente usada durante a renderiza��o.
	 */

	public abstract Camera getCamera();

	/**
	 * Essa luz deve ser a luz ambiente, como por exemplo a ilumina��o do sol em campos abertos.
	 * Ser� aplicada a toda e qualquer entidade que for chamada para ser renderizada.
	 * @return aquisi��o da ilumina��o ambiente usada por esse renderizador.
	 */

	public abstract Light getLight();

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("initiate", initiate);
		description.append("camera", nameOf(getCamera()));
		description.append("light", nameOf(getLight()));

		return description.toString();
	}
}
