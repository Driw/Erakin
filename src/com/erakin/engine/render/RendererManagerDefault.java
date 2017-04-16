package com.erakin.engine.render;

import static com.erakin.common.Utilities.nameOf;

import org.diverproject.util.ObjectDescription;

import com.erakin.engine.entity.Entity;
import com.erakin.engine.resource.World;

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
 *
 * @author Andrew Mello
 */

public abstract class RendererManagerDefault implements RendererManager
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
	 * O renderizador de entidades � respons�vel por renderizar qualquer objeto de interface <code>Entity</code>.
	 * As entidades podem ser simples objetos at� constru��es, jogadores, monstros, npcs e outros.
	 * @return aquisi��o do atual renderizador de entidades utilizado, por padr�o nenhum definido.
	 * @see Entity
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
	 * O renderizador de mundos � respons�vel por renderizar qualquer objeto de interface <code>World</code>.
	 * Os mundos s�o conjuntos de chunks que ir�o formar toda a �rea para que as entidades possam percorrer.
	 * @return aquisi��o  do atual renderizador de mundos utilizado, por padr�o nenhum definido.
	 * @see World
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
	 * Chamado internamente quando for dito ao gerenciador de renderiza��o para ser iniciado.
	 * Ir� definir um atributo como inicializado de modo a facilitar a implementa��o do mesmo.
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
