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
 * <h1>Renderizador de Entidades Padr�o</h1>
 *
 * <p>Esse renderizador � uma implementa��o b�sica para gerenciador de entidades que pode ser usado.
 * Implementa a inicializa��o autom�tica no mesmo sendo necess�rio apenas determinar o que ser� iniciado.</p>
 * 
 * <p>Utiliza uma cole��o do tipo fila como armazenamento das entidades e usa a fila din�mica para tal.
 * Nesse caso a fila � usada ao final de toda renderiza��o as entidades ser�o removidas do mesmo.
 * A forma que essa estrutura trabalha, � o melhor para ser alocado como armazenador das entidades.</p>
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
	 * Constr�i um novo renderizador de entidades padr�es iniciado a fila para armazenar entidades.
	 * A fila aqui utilizada � a fila din�mica, que ir� usar n�s duplos para facilitar a inser��o.
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
	 * Quando � chamado para fazer a renderiza��o faz o preparamento (beforeRender) em seguida desenha.
	 * As entidades s�o organizadas no mapa atrav�s da sua modelagem para melhorar a performance.
	 * Essa melhora de performance � de renderizar todas entidades com o mesmo modelo,
	 * assim o OpenGL n�o ter� de trocar de VAO sempre que uma nova entidade for renderizada.
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
	 * Ap�s definir um atributo como inicializado de modo a facilitar a implementa��o do mesmo.
	 */

	protected abstract void subInitiate();

	/**
	 * Durante a renderiza��o de entidades, o grupo de entidades com o mesmo modelo s�o chamados para renderizar.
	 * Ap�s fazer a ativa��o (habilitar uso) da modelagem esse m�todo ser� chamado uma �nica vez por cada entidade.
	 * Dever� garantir que as entidades sejam renderizadas na tela utilizando sua textura e shader adequados.
	 * @param entity refer�ncia da entidade do qual est� sendo chamada para renderizar.
	 */

	protected abstract void renderEnity(Entity entity);

	/**
	 * Antes de fazer a renderiza��o das entidades, � necess�rio habilitar no OpenGL uma modelagem para ser usada.
	 * Assim que essa for habilitada, as entidades que usam esse modelo ser�o chamadas para serem renderizadas.
	 * Esse procedimento pode ainda usar o shader adequadamente de acordo com as informa��es da modelagem.
	 * @param model refer�ncia da modelagem do pr�ximo conjunto de entidades a ser renderizada.
	 */

	protected abstract void beforeRenderEntity(Model model);

	/**
	 * Ap�s fazer a renderiza��o de um conjunto de entidades especificados, esse procedimento ser� chamado.
	 * Esse conjunto de entidades tem em comum a sua modelagem tri-dimensional usada, <b>model</b>.
	 * Deve dizer ao OpenGL para desabilitar o uso dessa modelagem ou informa��es do shader se necess�rio.
	 * @param model refer�ncia da modelagem do conjunto de entidades que foram renderizada.
	 */

	protected abstract void afterRenderEntity(Model model);

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
	 * Por padr�o esse renderizador de entidades utiliza a estrutura de fila para armazenar as entidades.
	 * A maioria das implementa��es das filas utiliza��o de ideia de inserir no final e retirar do come�o.
	 * Durante a atualiza��o das entidades � feito uma itera��o para n�o remover as entidades.
	 * Por�m quando � feito a renderiza��o utiliza da remo��o da primeira entidade na fila.
	 * @return aquisi��o da fila que ir� armazenar as entidades a ser renderizadas.
	 */

	public Map<Model, Queue<Entity>> getMapEntities()
	{
		return entities;
	}

	/**
	 * Permite definir uma nova estrutura do tipo file para armazenamento das entidades a ser renderizadas.
	 * Caso seja definido uma estrutura nula n�o ser� substitu�da a antiga para prevenir erros no sistema.
	 * @param entities refer�ncia da nova fila para armazenar as entidades a ser renderizadas.
	 */

	public void setMapEntities(Map<Model, Queue<Entity>> entities)
	{
		if (entities != null)
			this.entities = entities;
	}

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
	public void toString(ObjectDescription description)
	{
		description.append("camera", nameOf(getCamera()));
		description.append("light", nameOf(getLight()));
	}
}
