package com.erakin.api.render;

import com.erakin.api.resources.texture.Texture;
import com.erakin.api.resources.texture.TextureTarget;

/**
 * <h1>Céu</h1>
 *
 * <p>A caixa de céu é um cubo gigante que fica nos "limites" dos mundos para que seja renderizado o céu do mundo.
 * Ele é renderizado com texturas carregadas previamente, sendo necessário a existência de seis texturas para tal.
 * As texturas em suas extremidades devem ser continuas com as texturas que ligam com outra face do cubo,
 * portanto um grupo de seis texturas específicas deve possuir um determinado conteúdo e não por qualquer textura.</p>
 *
 * <p>Para que seja possível renderizar um céu no mundo, é necessário somente duas informações:
 * a primeira é a textura e essa textura deve ser de formato cúbico caso contrário não terá o efeito esperado;
 * a segunda é um modelo que neste caso será tri-dimensional e será usado para ser texturizado formando o céu.</p>
 *
 * @see Texture
 * @see ModelRender
 *
 * @author Andrew
 */

public interface SkyboxRender
{
	/**
	 * Através da textura a Engine consegue texturizar o modelo que forma a caixa nos extremos horizontes.
	 * <i>Essa textura precisa ser do tipo <code>TT_CUBE_MAP</code>, caso contrário não vai funcionar.</i>
	 * @return aquisição da textura cúbica que será usada para texturizar a caixa.
	 * @see TextureTarget
	 */

	Texture getTexture();

	/**
	 * Para que a caixa de céu seja visível, precisamos de um modelo tri-dimensional que deve ter forma de caixa.
	 * Essa caixa será renderizada em um tamanho consideravelmente grande para que fique nos extremos horizontes.
	 * Após texturizado e renderizado dará a impressão de que existe de fato um céu em todos os horizontes.
	 * @return aquisição do modelo tri-dimensional com a forma da caixa para este céu.
	 */

	ModelRender getModel();
}
