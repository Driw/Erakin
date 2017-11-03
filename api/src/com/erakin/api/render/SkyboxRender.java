package com.erakin.api.render;

import com.erakin.api.resources.texture.Texture;
import com.erakin.api.resources.texture.TextureTarget;

/**
 * <h1>C�u</h1>
 *
 * <p>A caixa de c�u � um cubo gigante que fica nos "limites" dos mundos para que seja renderizado o c�u do mundo.
 * Ele � renderizado com texturas carregadas previamente, sendo necess�rio a exist�ncia de seis texturas para tal.
 * As texturas em suas extremidades devem ser continuas com as texturas que ligam com outra face do cubo,
 * portanto um grupo de seis texturas espec�ficas deve possuir um determinado conte�do e n�o por qualquer textura.</p>
 *
 * <p>Para que seja poss�vel renderizar um c�u no mundo, � necess�rio somente duas informa��es:
 * a primeira � a textura e essa textura deve ser de formato c�bico caso contr�rio n�o ter� o efeito esperado;
 * a segunda � um modelo que neste caso ser� tri-dimensional e ser� usado para ser texturizado formando o c�u.</p>
 *
 * @see Texture
 * @see ModelRender
 *
 * @author Andrew
 */

public interface SkyboxRender
{
	/**
	 * Atrav�s da textura a Engine consegue texturizar o modelo que forma a caixa nos extremos horizontes.
	 * <i>Essa textura precisa ser do tipo <code>TT_CUBE_MAP</code>, caso contr�rio n�o vai funcionar.</i>
	 * @return aquisi��o da textura c�bica que ser� usada para texturizar a caixa.
	 * @see TextureTarget
	 */

	Texture getTexture();

	/**
	 * Para que a caixa de c�u seja vis�vel, precisamos de um modelo tri-dimensional que deve ter forma de caixa.
	 * Essa caixa ser� renderizada em um tamanho consideravelmente grande para que fique nos extremos horizontes.
	 * Ap�s texturizado e renderizado dar� a impress�o de que existe de fato um c�u em todos os horizontes.
	 * @return aquisi��o do modelo tri-dimensional com a forma da caixa para este c�u.
	 */

	ModelRender getModel();
}
