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
	 * Chamado internamente quando for dito ao renderizador de céus para ser iniciado.
	 * Após definir um atributo como inicializado de modo a facilitar a implementação do mesmo.
	 */

	protected abstract void subInitiate();

	/**
	 * Procedimento chamado assim que for solicitado ao renderizador para renderizar.
	 * Espera-se que seja feito todo o preparamento necessário para iniciar a renderização.
	 * Por exemplo iniciar a programação shader para realizar os efeitos gráficos.
	 * @param delay quantos milissegundos se passaram desde a última renderização.
	 */

	protected abstract void beforeRender(long delay);

	/**
	 * Chamado internamente para fazer a renderização de um determinado céu especificado.
	 * A renderização do céu é feita através de uma caixa gigante que após texturizada
	 * da a impressão da existência de um céu quando visto os horizontes.
	 * @param skybox referência do céu renderizável do qual será utilizado.
	 */

	protected abstract void renderSkybox(SkyboxRender skybox);

	/**
	 * Procedimento chamado somente após o renderizador ter renderizado o céu.
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
