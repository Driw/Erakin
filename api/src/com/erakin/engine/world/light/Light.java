package com.erakin.engine.world.light;

import org.lwjgl.util.vector.Vector3f;

/**
 * <h1>Luz</h1>
 *
 * <p>As luzes são usadas por Shaders para aperfeiçoar a utilização do engine o mais próximo da realidade.
 * Iluminações podem ser vistas como ambientas (sol/lua) como objetos (lanternas/lâmpadas) e efeitos especiais.
 * Os efeitos especiais normalmente são referentes a habilidades, mas a luz em si é considerada um efeito.</p>
 *
 * <p>Luzes são compostas essencialmente de duas operações necessárias quando é calculado o brilho dos efeitos.
 * Primeiramente indicar o posicionamento no espaço através de um vetor de float com as coordenadas X, Y e Z.
 * O segundo é referente a propriedade de cores, indicando as tonalidades em RGB com um vetor de X, Y e Z.</p>
 *
 * @author Andrew Mello
 */

public interface Light
{
	/**
	 * Posição deve indicar o ponto central de fonte da iluminação para calcular a intensidade conforme distâncias.
	 * @return aquisição do vetor contendo as coordenadas no espaço em X, Y e Z de fonte da luz.
	 */

	Vector3f getPosition();

	/**
	 * Cor deve indicar o nível de tonalidade de cores especificas para obter a cor real imitida pela luz.
	 * @return aquisição do vetor contendo as tonalidades em RGB respectivos ao XYZ do vetor.
	 */

	Vector3f getColour();
}
