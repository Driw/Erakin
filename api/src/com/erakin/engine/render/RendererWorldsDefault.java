package com.erakin.engine.render;

import static org.diverproject.util.Util.nameOf;
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

public abstract class RendererWorldsDefault extends RendererDefault implements RendererWorlds
{
	/**
	 * Quantidade de unidades de terreno padrão do alcance de visão.
	 */
	public static final int DEFAULT_RENDER_RANGE = 64;

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
		setRenderRange(DEFAULT_RENDER_RANGE);

		position = new Vector3i();
		terrains = new DynamicQueue<TerrainRender>();
	}

	@Override
	public void cleanup()
	{
		terrains.clear();
	}

	@Override
	public void update(long delay)
	{
		
	}

	@Override
	public final void render(long delay)
	{
		if (getCamera() == null || getLight() == null || getWorld() == null)
			return;

		beforeRender(delay);
		renderWorld(getWorld());
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
	public final void setWorld(WorldRender world)
	{
		if (world != null)
			this.world = world;
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
	public void setRenderPosition(Vector3i position)
	{
		this.position.set(position.x, position.y, position.z);
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
	public void setRenderRange(int distance)
	{
		if (distance > 0)
			range = distance;
	}

	/**
	 * Chamado internamente para fazer a renderização de um determinado mundo especificado.
	 * A renderização funciona de acordo com a posição central de renderização e distância.
	 * Nenhum terreno fora do alcance será enfileirado para a renderização de terrenos.
	 * @param world referência do mundo renderizável do qual será utilizado.
	 */

	protected void renderWorld(WorldRender world)
	{
		int realRange = (int) (range * world.getUnitSize());

		int terrainWidth = (int) (world.getTerrainWidth() * world.getUnitSize());
		int terrainLength = (int) (world.getTerrainLength() * world.getUnitSize());
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
				beforeRenderTerrain(model);
				renderTerrain(terrain);
				afterRenderTerrain(model);
			}
		}
	}

	/**
	 * Antes de fazer a renderização do terreno, é necessário habilitar no OpenGL uma modelagem para ser usada.
	 * Esse procedimento pode ainda usar o shader adequadamente de acordo com as informações da modelagem.
	 * @param model referência do modelo renderizável do terreno que está sendo renderizado.
	 */

	protected abstract void beforeRenderTerrain(ModelRender model);

	/**
	 * Durante a renderização de terreno, é listado todos os terrenos que devem ser renderizados conforme o alcance de visão.
	 * Deverá garantir que o terreno seja renderizado na tela utilizando sua(s) textura(s) e shader adequados.
	 * @param terrain referência do terreno renderizável do qual está sendo chamada para renderizar.
	 */

	protected abstract void renderTerrain(TerrainRender terrain);

	/**
	 * Após fazer a renderização da lista de terrenos especificados, esse procedimento será chamado.
	 * Esse conjunto de terrenos tem em comum a sua modelagem tri-dimensional usada, <b>model</b>.
	 * Deve dizer ao OpenGL para desabilitar o uso dessa modelagem ou informações do shader se necessário.
	 * @param model referência do modelo renderizável da lista de terrenos que foram renderizada.
	 */

	protected abstract void afterRenderTerrain(ModelRender model);

	/**
	 * Procedimento chamado assim que for solicitado ao renderizador para renderizar.
	 * Espera-se que seja feito todo o preparamento necessário para iniciar a renderização.
	 * Por exemplo iniciar a programação shader para realizar os efeitos gráficos.
	 * @param delay quantos milissegundos se passaram desde a última renderização.
	 */

	protected abstract void beforeRender(long delay);

	/**
	 * Procedimento chamado somente após o renderizador ter renderizado todos os terrenos.
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
	 * Será aplicada a todo e qualquer terreno que for chamado para ser renderizada.
	 * @return aquisição da iluminação ambiente usada por esse renderizador.
	 */

	public abstract Light getLight();

	@Override
	public void toString(ObjectDescription description)
	{
		description.append("camera", nameOf(getCamera()));
		description.append("light", nameOf(getLight()));
	}
}
