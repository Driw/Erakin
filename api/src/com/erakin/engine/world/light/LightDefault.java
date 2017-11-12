package com.erakin.engine.world.light;

import org.lwjgl.util.vector.Vector3f;

/**
 * <h1>Luz Padrão</h1>
 *
 * <p>Implementação básica das funcionalidades que uma luz deverá possuir para funcionar.
 * Possuindo os atributos respectivos em formato de vetor para posição e cor emitida pela luz.
 * Para permitir o uso dinâmico desta, foi adicionado dois procedimentos para trabalhar estes.</p>
 *
 * <p>Os dois procedimentos são de redefinição, ou seja, escolher um novo valor para ser assumido,
 * em quanto o outro permite incrementar/decrementar os valores atualmente usados nos mesmos.
 * Podem ser chamados através dos setters ou substituindo o set por increment no nome do método.</p>
 *
 * @see Light
 *
 * @author Andrew Mello
 */

public class LightDefault implements Light
{
	/**
	 * Posicionamento da luz no espaço.
	 */
	private Vector3f position;

	/**
	 * Tonalidade da luz emitida em RGB.
	 */
	private Vector3f color;

	/**
	 * Cria uma noca Luz Padrão inicializando a posição a luz em ponto central (0,0,0) e cor branca (255, 255, 255).
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
	 * Permite definir exatamente a posição da fonte de luz no espaço.
	 * @param x coordenada no espaço em relação ao eixo da latitude.
	 * @param y coordenada no espaço em relação ao eixo da altura.
	 * @param z coordenada no espaço em relação ao eixo da longitude.
	 */

	public void setPosition(float x, float y, float z)
	{
		position.x = x;
		position.y = y;
		position.z = z;
	}

	/**
	 * Permite incrementar valores a atual posição da luz no espaço.
	 * Caso o valor seja negativo será um decremento no eixo em questão.
	 * Isso irá considerar a posição anterior, somando os valores abaixo:
	 * @param x movimento no espaço em relação ao eixo da latitude.
	 * @param y movimento no espaço em relação ao eixo da altura.
	 * @param z movimento no espaço em relação ao eixo da longitude.
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
	 * @param red nível de tonalidade da cor em vermelho de 0 a 255.
	 * @param green nível de tonalidade da cor em verde de 0 a 255.
	 * @param blue nível de tonalidade da cor em azul de 0 a 255.
	 */

	public void setColor(int red, int green, int blue)
	{
		color.x = red / 255;
		color.y = green / 255;
		color.z = blue / 255;
	}

	/**
	 * Permite incrementar tonalidades a atual cor emitida pela luz.
	 * Caso o valor seja negativo será um decremento na tonalidade em questão.
	 * Isso irá considerar a tonalidade anterior, somando os valores abaixo:
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
