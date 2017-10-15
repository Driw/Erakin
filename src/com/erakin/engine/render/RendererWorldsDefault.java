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
 * <h1>Renderizador para Mundos Padrão</h1>
 *
 * <p>Esse renderizador é uma implementação básica para gerenciador de mundos que pode ser usado.
 * Implementa a inicialização automática no mesmo sendo necessário apenas determinar o que será iniciado.</p>
 *
 * <p>Utiliza uma coleção do tipo fila como armazenamento dos mundos e usa a fila dinâmica para tal.
 * Nesse caso a fila é usada ao final de toda renderização e os mundos serão removidos do mesmo.
 * A forma que essa estrutura trabalha, é o melhor para ser alocado como armazenador dos mundos.</p>
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
	 * Define se o renderizador de entidades já foi iniciado.
	 */
	private boolean initiate;

	/**
	 * Mundo que será usado para obter as chunks e renderizá-los.
	 */
	private WorldRender world;

	/**
	 * Ponto central para efetuar a renderização.
	 */
	private Vector3i position;

	/**
	 * Distância para renderização a partir do ponto central.
	 */
	private int range;

	/**
	 * Lista contendo todas as chunks que serão renderizadas.
	 */
	private Queue<TerrainRender> terrains;

	/**
	 * Constrói um novo renderizador de mundos padrões iniciado a posição central e campo de visão.
	 * O ponto inicial será as coordenadas no mundo de 0,0 (x,y) e visão de 64x64 (em células).
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
	 * Mundo é usado para saber de onde os terrenos a serem renderizados serão obtidos.
	 * Assim sendo, o renderizado poderá saber os terrenos de que mundo devem ser visualizados.
	 * @return aquisição da referência do mundo renderizável do qual está sendo renderizado.
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
	 * Através da posição de renderização que será usada como ponto central para o mesmo,
	 * é possível saber quais terrenos são necessários serem renderizador conforme o alcance
	 * de visão dos mesmos, reduzindo assim tempo de processamento desnecessário na engine.
	 * @return aquisição de uma clonagem do objeto contendo a coordenada de renderização.
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
	 * Alcance de renderização irá indicar quantos terrenos de distância estará sendo chamado.
	 * Quanto maior o número, maior a quantidade de terrenos ao redor para serem renderizados.
	 * @return aquisição da quantidade de terrenos que deverão ser renderizados na tela.
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
	 * Chamado internamente para fazer a renderização de um determinado mundo especificado.
	 * A renderização funciona de acordo com a posição central de renderização e distância.
	 * Nenhum terreno fora do alcance será enfileirado para a renderização de terrenos.
	 * @param world referência do mundo renderizável do qual será utilizado.
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
	 * Procedimento usado internamente para fazer a renderização dos terrenos em fila.
	 * A fila de terrenos é feita de acordo com a posição central de renderização e a
	 * distância limite para renderização destes, enfileirando de forma adequada.
	 * @param terrains referência da fila contendo os terrenos que devem ser renderizados.
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
	 * Irá definir um atributo como inicializado de modo a facilitar a implementação do mesmo.
	 */

	protected abstract void subInitiate();

	/**
	 * Durante a renderização de entidades, o grupo de entidades com o mesmo modelo são chamados para renderizar.
	 * Após fazer a ativação (habilitar uso) da modelagem esse método será chamado uma única vez por cada entidade.
	 * Deverá garantir que as entidades sejam renderizadas na tela utilizando sua textura e shader adequados.
	 * @param terrain referência do terreno renderizável do qual está sendo chamada para renderizar.
	 */

	protected abstract void renderTerrain(TerrainRender terrain);

	/**
	 * Antes de fazer a renderização das entidades, é necessário habilitar no OpenGL uma modelagem para ser usada.
	 * Assim que essa for habilitada, as entidades que usam esse modelo serão chamadas para serem renderizadas.
	 * Esse procedimento pode ainda usar o shader adequadamente de acordo com as informações da modelagem.
	 * @param model referência do modelo renderizável do próximo conjunto de entidades a ser renderizada.
	 */

	protected abstract void beforeRenderChunk(ModelRender model);

	/**
	 * Após fazer a renderização de um conjunto de entidades especificados, esse procedimento será chamado.
	 * Esse conjunto de entidades tem em comum a sua modelagem tri-dimensional usada, <b>model</b>.
	 * Deve dizer ao OpenGL para desabilitar o uso dessa modelagem ou informações do shader se necessário.
	 * @param model referência do modelo renderizável do conjunto de entidades que foram renderizada.
	 */

	protected abstract void afterRenderChunk(ModelRender model);

	/**
	 * Procedimento chamado assim que for solicitado ao renderizador para renderizar.
	 * Espera-se que seja feito todo o preparamento necessário para iniciar a renderização.
	 * Por exemplo iniciar a programação shader para realizar os efeitos gráficos.
	 * @param delay quantos milissegundos se passaram desde a última renderização.
	 */

	protected abstract void beforeRender(long delay);

	/**
	 * Procedimento chamado somente após o renderizador ter renderizado todas as entidades.
	 * Espera-se que seja feito toda a finalização necessária para concluir a renderização.
	 * Por exemplo parar a programação shader evitando uso desnecessário ou incorreto.
	 * @param delay quantos milissegundos se passaram desde a última renderização.
	 */

	protected abstract void afterRender(long delay);

	/**
	 * Câmera na renderização será usada para que seja possível criar a matriz de visão.
	 * Essa matriz de visão é que irá guardar informações do quanto será visto na tela.
	 * @return aquisição da câmera atualmente usada durante a renderização.
	 */

	public abstract Camera getCamera();

	/**
	 * Essa luz deve ser a luz ambiente, como por exemplo a iluminação do sol em campos abertos.
	 * Será aplicada a toda e qualquer entidade que for chamada para ser renderizada.
	 * @return aquisição da iluminação ambiente usada por esse renderizador.
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
