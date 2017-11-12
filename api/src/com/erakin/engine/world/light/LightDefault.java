package com.erakin.engine.world.light;

import org.lwjgl.util.vector.Vector3f;

/**
 * <h1>Luz Padr�o</h1>
 *
 * <p>Implementa��o b�sica das funcionalidades que uma luz dever� possuir para funcionar.
 * Possuindo os atributos respectivos em formato de vetor para posi��o e cor emitida pela luz.
 * Para permitir o uso din�mico desta, foi adicionado dois procedimentos para trabalhar estes.</p>
 *
 * <p>Os dois procedimentos s�o de redefini��o, ou seja, escolher um novo valor para ser assumido,
 * em quanto o outro permite incrementar/decrementar os valores atualmente usados nos mesmos.
 * Podem ser chamados atrav�s dos setters ou substituindo o set por increment no nome do m�todo.</p>
 *
 * @see Light
 *
 * @author Andrew Mello
 */

public class LightDefault implements Light
{
	/**
	 * Posicionamento da luz no espa�o.
	 */
	private Vector3f position;

	/**
	 * Tonalidade da luz emitida em RGB.
	 */
	private Vector3f color;

	/**
	 * Cria uma noca Luz Padr�o inicializando a posi��o a luz em ponto central (0,0,0) e cor branca (255, 255, 255).
	 */

	public LightDefault()
	{
		position = new Vector3f();
		color = new Vector3f(1, 1, 1);
	}

	@Override
	public Vector3f getPosition()
	{
		return position;
	}

	/**
	 * Permite definir exatamente a posi��o da fonte de luz no espa�o.
	 * @param x coordenada no espa�o em rela��o ao eixo da latitude.
	 * @param y coordenada no espa�o em rela��o ao eixo da altura.
	 * @param z coordenada no espa�o em rela��o ao eixo da longitude.
	 */

	public void setPosition(float x, float y, float z)
	{
		position.x = x;
		position.y = y;
		position.z = z;
	}

	/**
	 * Permite incrementar valores a atual posi��o da luz no espa�o.
	 * Caso o valor seja negativo ser� um decremento no eixo em quest�o.
	 * Isso ir� considerar a posi��o anterior, somando os valores abaixo:
	 * @param x movimento no espa�o em rela��o ao eixo da latitude.
	 * @param y movimento no espa�o em rela��o ao eixo da altura.
	 * @param z movimento no espa�o em rela��o ao eixo da longitude.
	 */

	public void increasePosition(float x, float y, float z)
	{
		position.x += x;
		position.y += y;
		position.z += z;
	}

	@Override
	public Vector3f getColour()
	{
		return color;
	}

	/**
	 * Permite definir exatamente a tonalidade das cores emitida pela luz.
	 * @param red n�vel de tonalidade da cor em vermelho de 0 a 255.
	 * @param green n�vel de tonalidade da cor em verde de 0 a 255.
	 * @param blue n�vel de tonalidade da cor em azul de 0 a 255.
	 */

	public void setColor(int red, int green, int blue)
	{
		color.x = red / 255;
		color.y = green / 255;
		color.z = blue / 255;
	}

	/**
	 * Permite incrementar tonalidades a atual cor emitida pela luz.
	 * Caso o valor seja negativo ser� um decremento na tonalidade em quest�o.
	 * Isso ir� considerar a tonalidade anterior, somando os valores abaixo:
	 * @param red incremento de tonalidade da cor em vermelho de 0 a 255.
	 * @param green incremento de tonalidade da cor em verde de 0 a 255.
	 * @param blue incremento de tonalidade da cor em azul de 0 a 255.
	 */

	public void increaseColor(int red, int green, int blue)
	{
		color.x += red / 255;
		color.y += green / 255;
		color.z += blue / 255;
	}
}
