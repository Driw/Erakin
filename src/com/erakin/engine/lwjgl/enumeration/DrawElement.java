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
 * <p>Enumera��o usada para restringir as formas como elementos podem ser desenhados na tela.
 * Al�m de restringir os modos, garante que um modo v�lido seja utilizado ao desenh�-los.
 * Internamente h� um valor atribu�do a cada um respectivo a sua constante no OpenGL.</p>
 *
 * @author Andrew Mello
 */

public enum DrawElement
{
	/**
	 * Trata cada v�rtice como um ponto.
	 */
	POINTS(GL_POINTS),

	/**
	 * Trata cada par de v�rtices como uma linha de seguimento independente.
	 */
	LINES(GL_LINES),

	/**
	 * Desenha um grupo conectado de seguimentos de linhas do primeiro v�rtice at� o �ltimo.
	 */
	LINE_STRIP(GL_LINE_STRIP),

	/**
	 * Desenha um grupo conectado de seguimentos de linhas do primeiro v�rtice at� o �ltimo, ent�o volta para o primeiro.
	 */
	LINE_LOOP(GL_LINE_LOOP),

	/**
	 * Trata cada trio de v�rtices como um tri�ngulo independente.
	 */
	TRIANGLES(GL_TRIANGLES),

	/**
	 * Trata um grupo conectado de tri�ngulos.
	 * Um tri�ngulo � definida por cada v�rtice apresentado ap�s os dois primeiros v�rtices.
	 */
	TRIANGLE_STRIP(GL_TRIANGLE_STRIP),

	/**
	 * Desenha um grupo conectado de tri�ngulos.
	 * Um tri�ngulo � definida por cada v�rtice apresentado ap�s os dois primeiros v�rtices.
	 */
	TRIANGLE_FAN(GL_TRIANGLE_FAN),

	/**
	 * Trata cada grupo de quatro v�rtices como um quadril�tero independente.
	 */
	QUADS(GL_QUADS),

	/**
	 * Desenha um grupo conectado de quadril�teros.
	 * Um quadril�tero � definido por cada par de v�rtices apresentado ap�s o primeiro par.
	 * Note que a ordem em que os v�rtices s�o usado para construir o quadril�tero da tira de dados � diferente do usado com dados independentes.
	 */
	QUAD_STRIP(GL_QUAD_STRIP),

	/**
	 * Desenha um �nico, pol�gono convexo.
	 * V�rtice de 1 a N define esse pol�gono.
	 */
	POLYGON(GL_POLYGON),

	/**
	 * Trata cada grupo de quatro v�rtices um quadril�tero independente.
	 */
	PATCHES(GL_PATCHES);

	/**
	 * Valor do par�metro OpenGL desse tipo de desenho.
	 */
	private final int value;

	/**
	 * Cria uma nova forma em que VAOs podem ser desenhados na tela.
	 * @param value refer�ncia do modo como par�metro OpenGL.
	 */

	private DrawElement(int value)
	{
		this.value = value;
	}

	/**
	 * Valor � respectivo a constante <code>GL_</code> desse tipo de desenho.
	 * @return aquisi��o do par�metro OpenGL para esse tipo de desenho.
	 */

	public int getValue()
	{
		return value;
	}
}
