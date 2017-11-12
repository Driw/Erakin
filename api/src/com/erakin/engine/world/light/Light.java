package com.erakin.engine.world.light;

import org.lwjgl.util.vector.Vector3f;

/**
 * <h1>Luz</h1>
 *
 * <p>As luzes s�o usadas por Shaders para aperfei�oar a utiliza��o do engine o mais pr�ximo da realidade.
 * Ilumina��es podem ser vistas como ambientas (sol/lua) como objetos (lanternas/l�mpadas) e efeitos especiais.
 * Os efeitos especiais normalmente s�o referentes a habilidades, mas a luz em si � considerada um efeito.</p>
 *
 * <p>Luzes s�o compostas essencialmente de duas opera��es necess�rias quando � calculado o brilho dos efeitos.
 * Primeiramente indicar o posicionamento no espa�o atrav�s de um vetor de float com as coordenadas X, Y e Z.
 * O segundo � referente a propriedade de cores, indicando as tonalidades em RGB com um vetor de X, Y e Z.</p>
 *
 * @author Andrew Mello
 */

public interface Light
{
	/**
	 * Posi��o deve indicar o ponto central de fonte da ilumina��o para calcular a intensidade conforme dist�ncias.
	 * @return aquisi��o do vetor contendo as coordenadas no espa�o em X, Y e Z de fonte da luz.
	 */

	Vector3f getPosition();

	/**
	 * Cor deve indicar o n�vel de tonalidade de cores especificas para obter a cor real imitida pela luz.
	 * @return aquisi��o do vetor contendo as tonalidades em RGB respectivos ao XYZ do vetor.
	 */

	Vector3f getColour();
}
