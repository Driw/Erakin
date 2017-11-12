package com.erakin.engine.render;

import com.erakin.api.render.SkyboxRender;

/**
 * <h1>Renderizador de C�us</h1>
 *
 * <p>Essa interface � usada no gerenciador de renderiza��o com as funcionalidades padr�es que deve possuir.
 * Assim � poss�vel fazer com que as renderiza��es possam atuar de formas diferentes para cada aplica��o.
 * Podendo ainda melhorar a performance conforme o desenvolvedor deseja ou conforme a aplica��o funcione.</p>
 *
 * <p>Possui funcionalidades b�sicas relacionadas sempre a c�us, somente c�us ser�o trabalhos.
 * Permite definir um �nico c�u por vez para que possa ser renderizado na tela e sua dist�ncia.</p>
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
