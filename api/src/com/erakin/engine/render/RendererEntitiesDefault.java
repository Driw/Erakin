package com.erakin.engine.render;

import static org.diverproject.util.Util.nameOf;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.collection.Map;
import org.diverproject.util.collection.Map.MapItem;
import org.diverproject.util.collection.Queue;
import org.diverproject.util.collection.abstraction.DynamicMap;
import org.diverproject.util.collection.abstraction.DynamicQueue;

import com.erakin.api.resources.model.Model;
import com.erakin.engine.camera.Camera;
import com.erakin.engine.entity.Entity;
import com.erakin.engine.world.light.Light;

/**
 * <h1>Renderizador de Entidades Padrão</h1>
 *
 * <p>Esse renderizador é uma implementação básica para gerenciador de entidades que pode ser usado.
 * Implementa a inicialização automática no mesmo sendo necessário apenas determinar o que será iniciado.</p>
 * 
 * <p>Utiliza uma coleção do tipo fila como armazenamento das entidades e usa a fila dinâmica para tal.
 * Nesse caso a fila é usada ao final de toda renderização as entidades serão removidas do mesmo.
 * A forma que essa estrutura trabalha, é o melhor para ser alocado como armazenador das entidades.</p>
 *
 * @see RendererEntities
 * @see Queue
 *
 * @author Andrew Mello
 */

public abstract class RendererEntitiesDefault extends RendererDefault implements RendererEntities
{
	/**
	 * Fila para armazenar as entidades a serem renderizadas.
	 */
	private Map<Model, Queue<Entity>> entities;

	/**
	 * Constrói um novo renderizador de entidades padrões iniciado a fila para armazenar entidades.
	 * A fila aqui utilizada é a fila dinâmica, que irá usar nós duplos para facilitar a inserção.
	 * @see DynamicQueue
	 */

	public RendererEntitiesDefault()
	{
		entities = new DynamicMap<Model, Queue<Entity>>();
	}

	@Override
	public void cleanup()
	{
		entities.clear();
	}

	@Override
	public final void update(long delay)
	{
		for (MapItem<Model, Queue<Entity>> item : entities.iterateItems())
			for (Entity entity : item.getValue())
				entity.update(delay);
	}

	@Override
	public final void render(long delay)
	{
		if (getCamera() == null || getLight() == null)
			return;

		beforeRender(delay);
		renderEntities(getMapEntities());
		afterRender(delay);
	}

	@Override
	public final void insert(Entity entity)
	{
		Model model = entity.getModel();
		Queue<Entity> queue;

		if (!entities.containsKey(model))
		{
			queue = new DynamicQueue<Entity>();
			entities.add(model, queue);
		}

		else
			queue = entities.get(model);

		queue.offer(entity);
	}

	/**
	 * Quando é chamado para fazer a renderização faz o preparamento (beforeRender) em seguida desenha.
	 * As entidades são organizadas no mapa através da sua modelagem para melhorar a performance.
	 * Essa melhora de performance é de renderizar todas entidades com o mesmo modelo,
	 * assim o OpenGL não terá de trocar de VAO sempre que uma nova entidade for renderizada.
	 * @param entities mapa contendo todas as entidades do qual deve ser renderizada.
	 */

	private void renderEntities(Map<Model, Queue<Entity>> entities)
	{
		for (MapItem<Model, Queue<Entity>> item : entities.iterateItems())
		{
			Model model = item.getKey();
			Queue<Entity> queue = item.getValue();

			beforeRenderEntity(model);

			while (queue.size() > 0)
				renderEnity(queue.poll());

			afterRenderEntity(model);
		}
	}

	/**
	 * Chamado internamente quando for dito ao renderizador de entidades para ser iniciado.
	 * Após definir um atributo como inicializado de modo a facilitar a implementação do mesmo.
	 */

	protected abstract void subInitiate();

	/**
	 * Durante a renderização de entidades, o grupo de entidades com o mesmo modelo são chamados para renderizar.
	 * Após fazer a ativação (habilitar uso) da modelagem esse método será chamado uma única vez por cada entidade.
	 * Deverá garantir que as entidades sejam renderizadas na tela utilizando sua textura e shader adequados.
	 * @param entity referência da entidade do qual está sendo chamada para renderizar.
	 */

	protected abstract void renderEnity(Entity entity);

	/**
	 * Antes de fazer a renderização das entidades, é necessário habilitar no OpenGL uma modelagem para ser usada.
	 * Assim que essa for habilitada, as entidades que usam esse modelo serão chamadas para serem renderizadas.
	 * Esse procedimento pode ainda usar o shader adequadamente de acordo com as informações da modelagem.
	 * @param model referência da modelagem do próximo conjunto de entidades a ser renderizada.
	 */

	protected abstract void beforeRenderEntity(Model model);

	/**
	 * Após fazer a renderização de um conjunto de entidades especificados, esse procedimento será chamado.
	 * Esse conjunto de entidades tem em comum a sua modelagem tri-dimensional usada, <b>model</b>.
	 * Deve dizer ao OpenGL para desabilitar o uso dessa modelagem ou informações do shader se necessário.
	 * @param model referência da modelagem do conjunto de entidades que foram renderizada.
	 */

	protected abstract void afterRenderEntity(Model model);

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
	 * Por padrão esse renderizador de entidades utiliza a estrutura de fila para armazenar as entidades.
	 * A maioria das implementações das filas utilização de ideia de inserir no final e retirar do começo.
	 * Durante a atualização das entidades é feito uma iteração para não remover as entidades.
	 * Porém quando é feito a renderização utiliza da remoção da primeira entidade na fila.
	 * @return aquisição da fila que irá armazenar as entidades a ser renderizadas.
	 */

	public Map<Model, Queue<Entity>> getMapEntities()
	{
		return entities;
	}

	/**
	 * Permite definir uma nova estrutura do tipo file para armazenamento das entidades a ser renderizadas.
	 * Caso seja definido uma estrutura nula não será substituída a antiga para prevenir erros no sistema.
	 * @param entities referência da nova fila para armazenar as entidades a ser renderizadas.
	 */

	public void setMapEntities(Map<Model, Queue<Entity>> entities)
	{
		if (entities != null)
			this.entities = entities;
	}

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
	public void toString(ObjectDescription description)
	{
		description.append("camera", nameOf(getCamera()));
		description.append("light", nameOf(getLight()));
	}
}
