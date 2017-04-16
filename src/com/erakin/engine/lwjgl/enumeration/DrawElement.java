package com.erakin.engine.lwjgl.enumeration;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_QUAD_STRIP;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL40.GL_PATCHES;

/**
 * <h1>Modos para Desenhar Elementos</h1>
 *
 * <p>Enumeração usada para restringir as formas como elementos podem ser desenhados na tela.
 * Além de restringir os modos, garante que um modo válido seja utilizado ao desenhá-los.
 * Internamente há um valor atribuí­do a cada um respectivo a sua constante no OpenGL.</p>
 *
 * @author Andrew Mello
 */

public enum DrawElement
{
	/**
	 * Trata cada vértice como um ponto.
	 */
	POINTS(GL_POINTS),

	/**
	 * Trata cada par de vértices como uma linha de seguimento independente.
	 */
	LINES(GL_LINES),

	/**
	 * Desenha um grupo conectado de seguimentos de linhas do primeiro vértice até o último.
	 */
	LINE_STRIP(GL_LINE_STRIP),

	/**
	 * Desenha um grupo conectado de seguimentos de linhas do primeiro vértice até o último, então volta para o primeiro.
	 */
	LINE_LOOP(GL_LINE_LOOP),

	/**
	 * Trata cada trio de vértices como um triângulo independente.
	 */
	TRIANGLES(GL_TRIANGLES),

	/**
	 * Trata um grupo conectado de triângulos.
	 * Um triângulo é definida por cada vértice apresentado após os dois primeiros vértices.
	 */
	TRIANGLE_STRIP(GL_TRIANGLE_STRIP),

	/**
	 * Desenha um grupo conectado de triângulos.
	 * Um triângulo é definida por cada vértice apresentado após os dois primeiros vértices.
	 */
	TRIANGLE_FAN(GL_TRIANGLE_FAN),

	/**
	 * Trata cada grupo de quatro vértices como um quadrilátero independente.
	 */
	QUADS(GL_QUADS),

	/**
	 * Desenha um grupo conectado de quadriláteros.
	 * Um quadrilátero é definido por cada par de vértices apresentado após o primeiro par.
	 * Note que a ordem em que os vértices são usado para construir o quadrilátero da tira de dados é diferente do usado com dados independentes.
	 */
	QUAD_STRIP(GL_QUAD_STRIP),

	/**
	 * Desenha um único, polígono convexo.
	 * Vértice de 1 a N define esse polígono.
	 */
	POLYGON(GL_POLYGON),

	/**
	 * Trata cada grupo de quatro vértices um quadrilátero independente.
	 */
	PATCHES(GL_PATCHES);

	/**
	 * Valor do parâmetro OpenGL desse tipo de desenho.
	 */
	private final int value;

	/**
	 * Cria uma nova forma em que VAOs podem ser desenhados na tela.
	 * @param value referência do modo como parâmetro OpenGL.
	 */

	private DrawElement(int value)
	{
		this.value = value;
	}

	/**
	 * Valor é respectivo a constante <code>GL_</code> desse tipo de desenho.
	 * @return aquisição do parâmetro OpenGL para esse tipo de desenho.
	 */

	public int getValue()
	{
		return value;
	}
}
