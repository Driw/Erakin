package com.erakin.engine.render;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Renderizador Padrão</h1>
 *
 * Renderizador com as funcionalidades básicas para funcionamento de um gerenciador de renderizações.
 * Aqui é implementado os métodos que garantem que o renderizador seja iniciado, pausado e resumido.
 * 
 *
 * @author Andrew
 */

public abstract class RendererDefault implements RendererManager
{
	/**
	 * Define se o renderizador de mundos já foi iniciado.
	 */
	private boolean initiate;

	/**
	 * Define ser o renderizador será chamado para renderizar.
	 */
	private boolean paused;

	/**
	 * Cria uma nova instância de uma renderizador padrão e é definido como não iniciado e pausado.
	 */

	public RendererDefault()
	{
		initiate = false;
		paused = true;
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
	public boolean isStoped()
	{
		return paused;
	}

	@Override
	public void pause()
	{
		paused = true;
	}

	@Override
	public void resume()
	{
		paused = false;
	}

	/**
	 * Chamado internamente quando for dito ao renderizador de mundos para ser iniciado.
	 * Após definir um atributo como inicializado de modo a facilitar a implementação do mesmo.
	 */

	protected abstract void subInitiate();

	protected abstract void toString(ObjectDescription description);

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("initiate", isInitiate());
		description.append("stoped", isStoped());

		toString(description);

		return description.toString();
	}
}
