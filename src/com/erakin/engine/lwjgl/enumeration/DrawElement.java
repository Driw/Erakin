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

import com.erakin.common.ErakinParseException;

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
	POINTS(GL_POINTS, "points"),

	/**
	 * Trata cada par de v�rtices como uma linha de seguimento independente.
	 */
	LINES(GL_LINES, "lines"),

	/**
	 * Desenha um grupo conectado de seguimentos de linhas do primeiro v�rtice at� o �ltimo.
	 */
	LINE_STRIP(GL_LINE_STRIP, "lineStrip"),

	/**
	 * Desenha um grupo conectado de seguimentos de linhas do primeiro v�rtice at� o �ltimo, ent�o volta para o primeiro.
	 */
	LINE_LOOP(GL_LINE_LOOP, "lineLoop"),

	/**
	 * Trata cada trio de v�rtices como um tri�ngulo independente.
	 */
	TRIANGLES(GL_TRIANGLES, "triangles"),

	/**
	 * Trata um grupo conectado de tri�ngulos.
	 * Um tri�ngulo � definida por cada v�rtice apresentado ap�s os dois primeiros v�rtices.
	 */
	TRIANGLE_STRIP(GL_TRIANGLE_STRIP, "triangleStrip"),

	/**
	 * Desenha um grupo conectado de tri�ngulos.
	 * Um tri�ngulo � definida por cada v�rtice apresentado ap�s os dois primeiros v�rtices.
	 */
	TRIANGLE_FAN(GL_TRIANGLE_FAN, "triangleFan"),

	/**
	 * Trata cada grupo de quatro v�rtices como um quadril�tero independente.
	 */
	QUADS(GL_QUADS, "quads"),

	/**
	 * Desenha um grupo conectado de quadril�teros.
	 * Um quadril�tero � definido por cada par de v�rtices apresentado ap�s o primeiro par.
	 * Note que a ordem em que os v�rtices s�o usado para construir o quadril�tero da tira de dados � diferente do usado com dados independentes.
	 */
	QUAD_STRIP(GL_QUAD_STRIP, "quadStrip"),

	/**
	 * Desenha um �nico, pol�gono convexo.
	 * V�rtice de 1 a N define esse pol�gono.
	 */
	POLYGON(GL_POLYGON, "polygon"),

	/**
	 * Trata cada grupo de quatro v�rtices um quadril�tero independente.
	 */
	PATCHES(GL_PATCHES, "patches");

	/**
	 * Valor do par�metro OpenGL desse tipo de desenho.
	 */
	private final int value;

	/**
	 * Nome de identifica��o na utiliza��o de comandos.
	 */
	private final String commandName;

	/**
	 * Cria uma nova forma em que VAOs podem ser desenhados na tela.
	 * @param value refer�ncia do modo como par�metro OpenGL.
	 * @param commandName identifica��o na utiliza��o de comandos.
	 */

	private DrawElement(int value, String commandName)
	{
		this.value = value;
		this.commandName = commandName;
	}

	/**
	 * Valor � respectivo a constante <code>GL_</code> desse tipo de desenho.
	 * @return aquisi��o do par�metro OpenGL para esse tipo de desenho.
	 */

	public int getValue()
	{
		return value;
	}

	/**
	 * @return aquisi��o do nome de identifica��o para utiliza��o em comandos.
	 */

	public String commandName()
	{
		return commandName;
	}

	/**
	 * Converte um valor num�rico do tipo inteiro em um DrawElement baseado em seu valor.
	 * @param code n�mero referente ao c�digo do tipo de DrawElement do qual deseja.
	 * @return aquisi��o do DrawElement que corresponde ao c�digo especificado.
	 * @throws ParseException apenas se o c�digo for inv�lido (n�o existe).
	 */

	public static DrawElement parse(int code) throws ErakinParseException
	{
		for (DrawElement draw : values())
			if (draw.value == code)
				return draw;

		throw new ErakinParseException("DrawElement#%d n�o encontrado", code);
	}

	/**
	 * Converte uma string para um DrawElement baseado no seu nome de identifica��o.
	 * @param value nome de identifica��o do DrawElement do qual deseja.
	 * @return aquisi��o do DrawElement que corresponde ao nome de identifica��o.
	 * @throws ParseException apenas o nome for inv�lido (n�o existe).
	 */

	public static DrawElement parse(String value) throws ErakinParseException
	{
		for (DrawElement draw : values())
			if (draw.commandName().equals(value))
				return draw;

		throw new ErakinParseException("DrawElement#%s n�o encontrado", value);
	}
}
