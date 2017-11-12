package com.erakin.engine.camera;

import com.erakin.engine.Updatable;

/**
 * <h1>C�mera</h1>
 *
 * <p>C�meras ser� uma simula��o de uma c�mera filmadora real, onde h� um ponto de vis�o que ir� "filmar".
 * O ponto de vis�o ser� tudo aquilo que foi renderizado no plano 2D, que � tudo aquilo vis�vel na tela.
 * Em quanto a rela��o de filmar, ser� tudo dentro desse plano 2D transformado em 3D atrav�s do OpenGL.</p>
 *
 * <p>Trabalha considerando um ponto central do qual ser� determinado como posicionamento na c�mera.
 * Haver� uma propriedade denominada zoom que � o n�vel de aproxima��o ou afastamento da tela com o ponto central.
 * E por �ltimo as propriedades de rota��o que ir� permitir uma livre movimenta��o e realismo na utiliza��o.</p>
 *
 * <p>Essa interface ir� obrigar a classe a implementar alguns atributos que permite obter os valores acima.
 * Esses valores ser�o utilizados dentro de engine para criar algumas matriz e permitir a renderiza��o correta.</p>
 *
 * @author Andrew Mello
 */

public interface Camera extends Updatable
{
	/**
	 * Zoom � usado para aumentar ou diminuir o campo de vis�o que a c�mera ir� oferecer.
	 * Quanto maior for o campo de vis�o, mais longe do ponto central estar� o visor.
	 * @return aquisi��o do n�vel de aproxima��o da vis�o da c�mera com o ponto central.
	 */

	float getZoom();

	/**
	 * Posicionamento da c�mera ir� indicar onde se encontra o ponto central no espa�o.
	 * Pode ser um ponto fixo, pr�-programado no caso de uma filmagem ou especificado.
	 * @return aquisi��o da localiza��o do ponto central no espa�o em rela��o ao eixo X.
	 */

	float getPositionX();

	/**
	 * Posicionamento da c�mera ir� indicar onde se encontra o ponto central no espa�o.
	 * Pode ser um ponto fixo, pr�-programado no caso de uma filmagem ou especificado.
	 * @return aquisi��o da localiza��o do ponto central no espa�o em rela��o ao eixo Y.
	 */

	float getPositionY();

	/**
	 * Posicionamento da c�mera ir� indicar onde se encontra o ponto central no espa�o.
	 * Pode ser um ponto fixo, pr�-programado no caso de uma filmagem ou especificado.
	 * @return aquisi��o da localiza��o do ponto central no espa�o em rela��o ao eixo Z.
	 */

	float getPositionZ();

	/**
	 * Rota��o da c�mera permite que esta n�o se tenha uma dire��o fixa no seu visor.
	 * Permitindo olhar para os lados, para cima ou para baixo e outros pontos.
	 * @return aquisi��o da rota��o feita para as laterais respectivo ao eixo X.
	 */

	float getPitch();

	/**
	 * Rota��o da c�mera permite que esta n�o se tenha uma dire��o fixa no seu visor.
	 * Permitindo olhar para os lados, para cima ou para baixo e outros pontos.
	 * @return aquisi��o da rota��o feita para cima ou para baixo respectivo ao eixo Y.
	 */

	float getYaw();

	/**
	 * Rota��o da c�mera permite que esta n�o se tenha uma dire��o fixa no seu visor.
	 * Permitindo olhar para os lados, para cima ou para baixo e outros pontos.
	 * @return aquisi��o da rota��o feita para os cantos respectivos ao eixo Z.
	 */

	float getRoll();
}
