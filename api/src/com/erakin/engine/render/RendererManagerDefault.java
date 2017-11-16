package com.erakin.engine.render;

import static org.diverproject.util.Util.nameOf;

import org.diverproject.util.ObjectDescription;

import com.erakin.api.render.SkyboxRender;
import com.erakin.api.render.WorldRender;
import com.erakin.engine.entity.Entity;

/**
 * <h1>Gerenciador de Renderiza��o Padr�o</h1>
 *
 * <p>Essa classe pode ser usada como base para implementa��o de um gerenciador de renderiza��o no engine.
 * Ele possui getters e setters adequados para determinar quais renderizadores devem ser usada.
 * Assim � poss�vel escolher qual renderizador ser� usada para cada tipo de situa��o ou objeto.</p>
 *
 * <p>Uma de suas caracter�sticas e possuir um atributo para identifica��o se o gerenciador foi iniciado.
 * O engine s� ir� iniciar uma �nica vez o engine, ap�s ter iniciado ele ser� usado de forma adequada.</p>
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
	 * Define se o gerenciador de renderiza��o j� foi iniciado.
	 */
	private boolean initiate;

	/**
	 * Renderizador de Entidades que est� sendo usado.
	 */
	private RendererEntities rendererEntities;

	/**
	 * Renderizador de Mundos que est� sendo usado.
	 */
	private RendererWorlds rendererWorlds;

	/**
	 * Renderizador de C�us que est� sendo usado.
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
	 * O renderizador de entidades � respons�vel por renderizar qualquer objeto de interface {@link Entity}.
	 * As entidades podem ser simples objetos at� constru��es, jogadores, monstros, npcs e outros.
	 * @return aquisi��o do atual renderizador de entidades utilizado, por padr�o nenhum definido.
	 */

	public RendererEntities getRendererEntities()
	{
		return rendererEntities;
	}

	/**
	 * Permite definir qual ser� o renderizador de entidades do qual ser� usado pelo gerenciador de renderiza��o.
	 * Caso o novo gerenciador seja diferente do antigo, ir� garantir que esse seja inicializado internamente.
	 * @param rendererEntities refer�ncia do novo renderizador de entidades do qual deve ser usado.
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
	 * O renderizador de mundos � respons�vel por renderizar qualquer objeto de interface {@link WorldRender}.
	 * Os mundos s�o conjuntos de chunks que ir�o formar toda a �rea para que as entidades possam percorrer.
	 * @return aquisi��o do atual renderizador de mundos utilizado, por padr�o nenhum definido.
	 */

	public RendererWorlds getRendererWorlds()
	{
		return rendererWorlds;
	}

	/**
	 * Permite definir qual ser� o renderizador de mundos do qual ser� usado pelo gerenciador de renderiza��o.
	 * Caso o novo gerenciador seja diferente do antigo, ir� garantir que esse seja inicializado internamente.
	 * @param rendererWorld refer�ncia do novo renderizador de mundos do qual deve ser usado.
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
	 * O renderizador de c�u � respons�vel por renderizar qualquer objeto de interface {@link SkyboxRender}.
	 * O c�u � uma caixa tri-dimensional que quando texturizado causa um efeito de exist�ncia do c�u nos horizontes.
	 * @return aquisi��o do atual renderizador de c�u utilizado, por padr�o nenhum definido.
	 */

	public RendererSkybox getRendererSkybox()
	{
		return rendererSkybox;
	}

	/**
	 * Permite definir qual ser� o renderizador de c�us do qual ser� usado pelo gerenciador de renderiza��o.
	 * Caso o novo gerenciador seja diferente do antigo, ir� garantir que esse seja inicializado internamente.
	 * @param rendererSkybox refer�ncia do novo renderizador de c�us do qual deve ser usado.
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
