package com.erakin.engine.render;

import com.erakin.api.render.SkyboxRender;

/**
 * <h1>Renderizador de Céus</h1>
 *
 * <p>Essa interface é usada no gerenciador de renderização com as funcionalidades padrões que deve possuir.
 * Assim é possível fazer com que as renderizações possam atuar de formas diferentes para cada aplicação.
 * Podendo ainda melhorar a performance conforme o desenvolvedor deseja ou conforme a aplicação funcione.</p>
 *
 * <p>Possui funcionalidades básicas relacionadas sempre a céus, somente céus serão trabalhos.
 * Permite definir um único céu por vez para que possa ser renderizado na tela e sua distância.</p>
 *
 * @see RendererManager
 * @see SkyboxRender
 *
 * @author Andrew
 */

public interface RendererSkybox extends RendererManager
{
	void setSkybox(SkyboxRender skybox);

	void setDistance(float distance);
}
