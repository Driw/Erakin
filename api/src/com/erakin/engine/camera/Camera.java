package com.erakin.engine.camera;

import com.erakin.engine.Updatable;

/**
 * <h1>Câmera</h1>
 *
 * <p>Câmeras será uma simulação de uma câmera filmadora real, onde há um ponto de visão que irá "filmar".
 * O ponto de visão será tudo aquilo que foi renderizado no plano 2D, que é tudo aquilo visível na tela.
 * Em quanto a relação de filmar, será tudo dentro desse plano 2D transformado em 3D através do OpenGL.</p>
 *
 * <p>Trabalha considerando um ponto central do qual será determinado como posicionamento na câmera.
 * Haverá uma propriedade denominada zoom que é o nível de aproximação ou afastamento da tela com o ponto central.
 * E por último as propriedades de rotação que irá permitir uma livre movimentação e realismo na utilização.</p>
 *
 * <p>Essa interface irá obrigar a classe a implementar alguns atributos que permite obter os valores acima.
 * Esses valores serão utilizados dentro de engine para criar algumas matriz e permitir a renderização correta.</p>
 *
 * @author Andrew Mello
 */

public interface Camera extends Updatable
{
	/**
	 * Zoom é usado para aumentar ou diminuir o campo de visão que a câmera irá oferecer.
	 * Quanto maior for o campo de visão, mais longe do ponto central estará o visor.
	 * @return aquisição do nível de aproximação da visão da câmera com o ponto central.
	 */

	float getZoom();

	/**
	 * Posicionamento da câmera irá indicar onde se encontra o ponto central no espaço.
	 * Pode ser um ponto fixo, pré-programado no caso de uma filmagem ou especificado.
	 * @return aquisição da localização do ponto central no espaço em relação ao eixo X.
	 */

	float getPositionX();

	/**
	 * Posicionamento da câmera irá indicar onde se encontra o ponto central no espaço.
	 * Pode ser um ponto fixo, pré-programado no caso de uma filmagem ou especificado.
	 * @return aquisição da localização do ponto central no espaço em relação ao eixo Y.
	 */

	float getPositionY();

	/**
	 * Posicionamento da câmera irá indicar onde se encontra o ponto central no espaço.
	 * Pode ser um ponto fixo, pré-programado no caso de uma filmagem ou especificado.
	 * @return aquisição da localização do ponto central no espaço em relação ao eixo Z.
	 */

	float getPositionZ();

	/**
	 * Rotação da câmera permite que esta não se tenha uma direção fixa no seu visor.
	 * Permitindo olhar para os lados, para cima ou para baixo e outros pontos.
	 * @return aquisição da rotação feita para as laterais respectivo ao eixo X.
	 */

	float getPitch();

	/**
	 * Rotação da câmera permite que esta não se tenha uma direção fixa no seu visor.
	 * Permitindo olhar para os lados, para cima ou para baixo e outros pontos.
	 * @return aquisição da rotação feita para cima ou para baixo respectivo ao eixo Y.
	 */

	float getYaw();

	/**
	 * Rotação da câmera permite que esta não se tenha uma direção fixa no seu visor.
	 * Permitindo olhar para os lados, para cima ou para baixo e outros pontos.
	 * @return aquisição da rotação feita para os cantos respectivos ao eixo Z.
	 */

	float getRoll();
}
