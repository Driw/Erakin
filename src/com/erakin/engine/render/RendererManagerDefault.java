package com.erakin.engine.render;

import static com.erakin.common.Utilities.nameOf;

import org.diverproject.util.ObjectDescription;

import com.erakin.engine.entity.Entity;
import com.erakin.engine.resource.World;

/**
 * <h1>Gerenciador de Renderização Padrão</h1>
 *
 * <p>Essa classe pode ser usada como base para implementação de um gerenciador de renderização no engine.
 * Ele possui getters e setters adequados para determinar quais renderizadores devem ser usada.
 * Assim é possível escolher qual renderizador será usada para cada tipo de situação ou objeto.</p>
 *
 * <p>Uma de suas características e possuir um atributo para identificação se o gerenciador foi iniciado.
 * O engine só irá iniciar uma única vez o engine, após ter iniciado ele será usado de forma adequada.</p>
 *
 * @see RendererManager
 * @see RendererEntities
 *
 * @author Andrew Mello
 */

public abstract class RendererManagerDefault implements RendererManager
{
	/**
	 * Define se o gerenciador de renderização já foi iniciado.
	 */
	private boolean initiate;

	/**
	 * Renderizador de Entidades que está sendo usado.
	 */
	private RendererEntities rendererEntities;

	/**
	 * Renderizador de Mundos que está sendo usado.
	 */
	private RendererWorlds rendererWorlds;

	@Override
	public void update(long delay)
	{
		if (rendererWorlds != null)
			rendererWorlds.update(delay);

		if (rendererEntities != null)
			rendererEntities.update(delay);
	}

	@Override
	public void render(long delay)
	{
		if (rendererWorlds != null)
			rendererWorlds.render(delay);

		if (rendererEntities != null)
			rendererEntities.render(delay);
	}

	@Override
	public void cleanup()
	{
		if (rendererWorlds != null)
			rendererWorlds.cleanup();

		if (rendererEntities != null)
			rendererEntities.cleanup();
	}

	@Override
	public void initiate()
	{
		subInitiate();

		initiate = true;

		rendererWorlds.initiate();
		rendererEntities.initiate();
	}

	@Override
	public boolean isInitiate()
	{
		return initiate;
	}

	/**
	 * O renderizador de entidades é responsável por renderizar qualquer objeto de interface <code>Entity</code>.
	 * As entidades podem ser simples objetos até construções, jogadores, monstros, npcs e outros.
	 * @return aquisição do atual renderizador de entidades utilizado, por padrão nenhum definido.
	 * @see Entity
	 */

	public RendererEntities getRendererEntities()
	{
		return rendererEntities;
	}

	/**
	 * Permite definir qual será o renderizador de entidades do qual será usado pelo gerenciador de renderização.
	 * Caso o novo gerenciador seja diferente do antigo, irá garantir que esse seja inicializado internamente.
	 * @param rendererEntities referência do novo renderizador de entidades do qual deve ser usado.
	 */

	public void setRendererEntities(RendererEntities rendererEntities)
	{
		if (rendererEntities != null)
		{
			if (!rendererEntities.isInitiate() && isInitiate())
				rendererEntities.initiate();

			this.rendererEntities = rendererEntities;
		}
	}

	/**
	 * O renderizador de mundos é responsável por renderizar qualquer objeto de interface <code>World</code>.
	 * Os mundos são conjuntos de chunks que irão formar toda a área para que as entidades possam percorrer.
	 * @return aquisição  do atual renderizador de mundos utilizado, por padrão nenhum definido.
	 * @see World
	 */

	public RendererWorlds getRendererWorlds()
	{
		return rendererWorlds;
	}

	/**
	 * Permite definir qual será o renderizador de mundos do qual será usado pelo gerenciador de renderização.
	 * Caso o novo gerenciador seja diferente do antigo, irá garantir que esse seja inicializado internamente.
	 * @param rendererWorld referência do novo renderizador de mundos do qual deve ser usado.
	 */

	public void setRendererWorlds(RendererWorlds rendererWorld)
	{
		if (rendererWorld != null)
		{
			if (!rendererWorld.isInitiate() && isInitiate())
				rendererWorld.initiate();

			this.rendererWorlds = rendererWorld;
		}
	}

	/**
	 * Chamado internamente quando for dito ao gerenciador de renderização para ser iniciado.
	 * Irá definir um atributo como inicializado de modo a facilitar a implementação do mesmo.
	 */

	protected abstract void subInitiate();

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("initiate", initiate);
		description.append("entities", nameOf(rendererEntities));
		description.append("worlds", nameOf(rendererWorlds));

		return description.toString();
	}
}
