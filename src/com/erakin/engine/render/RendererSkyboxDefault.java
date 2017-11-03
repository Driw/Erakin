package com.erakin.engine.render;

import static com.erakin.api.ErakinAPIUtil.nameOf;

import org.diverproject.util.ObjectDescription;

import com.erakin.api.render.SkyboxRender;
import com.erakin.engine.camera.Camera;

public abstract class RendererSkyboxDefault implements RendererSkybox
{
	public static final float DEFAULT_DISTANCE = 500f;

	private boolean initiate;
	private SkyboxRender skybox;
	private float distance;

	/**
	 * 
	 */

	public RendererSkyboxDefault()
	{
		setDistance(DEFAULT_DISTANCE);
	}

	@Override
	public void cleanup()
	{
		skybox.getTexture().release();
		skybox.getModel().release();
	}

	@Override
	public void initiate()
	{
		initiate = true;

		subInitiate();
	}

	@Override
	public boolean isInitiate()
	{
		return initiate;
	}

	@Override
	public void update(long delay)
	{
		
	}

	@Override
	public void render(long delay)
	{
		if (getCamera() == null || getSkybox() == null)
			return;

		beforeRender(delay);
		renderSkybox(getSkybox());
		afterRender(delay);
	}

	public SkyboxRender getSkybox()
	{
		return skybox;
	}

	@Override
	public void setSkybox(SkyboxRender skybox)
	{
		this.skybox = skybox;
	}

	public float getDistance()
	{
		return distance;
	}

	@Override
	public void setDistance(float distance)
	{
		if (distance > 0)
			this.distance = distance;
	}

	/**
	 * Chamado internamente quando for dito ao renderizador de c�us para ser iniciado.
	 * Ap�s definir um atributo como inicializado de modo a facilitar a implementa��o do mesmo.
	 */

	protected abstract void subInitiate();

	/**
	 * Procedimento chamado assim que for solicitado ao renderizador para renderizar.
	 * Espera-se que seja feito todo o preparamento necess�rio para iniciar a renderiza��o.
	 * Por exemplo iniciar a programa��o shader para realizar os efeitos gr�ficos.
	 * @param delay quantos milissegundos se passaram desde a �ltima renderiza��o.
	 */

	protected abstract void beforeRender(long delay);

	/**
	 * Chamado internamente para fazer a renderiza��o de um determinado c�u especificado.
	 * A renderiza��o do c�u � feita atrav�s de uma caixa gigante que ap�s texturizada
	 * da a impress�o da exist�ncia de um c�u quando visto os horizontes.
	 * @param skybox refer�ncia do c�u renderiz�vel do qual ser� utilizado.
	 */

	protected abstract void renderSkybox(SkyboxRender skybox);

	/**
	 * Procedimento chamado somente ap�s o renderizador ter renderizado o c�u.
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

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("initiate", initiate);
		description.append("camera", nameOf(getCamera()));
		description.append("skybox", nameOf(getSkybox()));

		return description.toString();
	}
}
