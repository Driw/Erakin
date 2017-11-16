package com.erakin.engine.render;

import static org.diverproject.util.Util.nameOf;

import org.diverproject.util.ObjectDescription;

import com.erakin.api.render.SkyboxRender;
import com.erakin.api.render.WorldRender;
import com.erakin.engine.entity.Entity;

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
 * @see RendererWorlds
 * @see RendererSkybox
 *
 * @author Andrew Mello
 */

public abstract class RendererManagerDefault extends RendererDefault implements RendererManager
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

	/**
	 * Renderizador de Céus que está sendo usado.
	 */
	private RendererSkybox rendererSkybox;

	@Override
	public void pause()
	{
		super.pause();

		if (rendererWorlds != null)
			rendererWorlds.pause();

		if (rendererEntities != null)
			rendererEntities.pause();

		if (rendererSkybox != null)
			rendererSkybox.pause();
	}

	@Override
	public void resume()
	{
		super.resume();

		if (rendererWorlds != null)
			rendererWorlds.resume();

		if (rendererEntities != null)
			rendererEntities.resume();

		if (rendererSkybox != null)
			rendererSkybox.resume();
	}

	@Override
	public void update(long delay)
	{
		if (rendererWorlds != null)
			rendererWorlds.update(delay);

		if (rendererEntities != null)
			rendererEntities.update(delay);

		if (rendererSkybox != null)
			rendererSkybox.update(delay);
	}

	@Override
	public void render(long delay)
	{
		if (rendererWorlds != null)
			rendererWorlds.render(delay);

		if (rendererEntities != null)
			rendererEntities.render(delay);

		if (rendererSkybox != null)
			rendererSkybox.render(delay);
	}

	@Override
	public void cleanup()
	{
		if (rendererWorlds != null)
			rendererWorlds.cleanup();

		if (rendererEntities != null)
			rendererEntities.cleanup();

		if (rendererSkybox != null)
			rendererSkybox.cleanup();
	}

	@Override
	protected void subInitiate()
	{
		rendererWorlds.initiate();
		rendererEntities.initiate();
		rendererSkybox.initiate();
	}

	/**
	 * O renderizador de entidades é responsável por renderizar qualquer objeto de interface {@link Entity}.
	 * As entidades podem ser simples objetos até construções, jogadores, monstros, npcs e outros.
	 * @return aquisição do atual renderizador de entidades utilizado, por padrão nenhum definido.
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
	 * O renderizador de mundos é responsável por renderizar qualquer objeto de interface {@link WorldRender}.
	 * Os mundos são conjuntos de chunks que irão formar toda a área para que as entidades possam percorrer.
	 * @return aquisição do atual renderizador de mundos utilizado, por padrão nenhum definido.
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
	 * O renderizador de céu é responsável por renderizar qualquer objeto de interface {@link SkyboxRender}.
	 * O céu é uma caixa tri-dimensional que quando texturizado causa um efeito de existência do céu nos horizontes.
	 * @return aquisição do atual renderizador de céu utilizado, por padrão nenhum definido.
	 */

	public RendererSkybox getRendererSkybox()
	{
		return rendererSkybox;
	}

	/**
	 * Permite definir qual será o renderizador de céus do qual será usado pelo gerenciador de renderização.
	 * Caso o novo gerenciador seja diferente do antigo, irá garantir que esse seja inicializado internamente.
	 * @param rendererSkybox referência do novo renderizador de céus do qual deve ser usado.
	 */

	public void setRendererSkybox(RendererSkybox rendererSkybox)
	{
		if (rendererSkybox != null)
		{
			if (!rendererSkybox.isInitiate() && isInitiate())
				rendererSkybox.initiate();

			this.rendererSkybox = rendererSkybox;
		}
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("initiate", initiate);
		description.append("entities", nameOf(rendererEntities));
		description.append("worlds", nameOf(rendererWorlds));
		description.append("skybox", nameOf(rendererSkybox));

		return description.toString();
	}

	@Override
	protected void toString(ObjectDescription description)
	{
		description.append("rendererSkybox", nameOf(rendererSkybox));
		description.append("rendererWorlds", nameOf(rendererWorlds));
		description.append("rendererEntities", nameOf(rendererEntities));
	}
}
