package com.erakin.api.lwjgl;

import org.lwjgl.util.vector.Vector3f;

import com.erakin.api.lwjgl.math.Vector3i;

/**
 * Classe utilit�ria com m�todos para se trabalhar com a biblioteca do LWJGL.
 * Ir� conter diversos m�todos est�ticos que podem ser importados de forma est�tica.
 *
 * @author Andrew
 */

public class APIGLUtil
{
	/**
	 * Quantidade de bytes liberados necess�rios para chamar o Garbage Collector.
	 */
	private static final int RELEASE_MEMORY_SIZE = 16 * (1024 * 1024);

	/**
	 * Quantidade da bytes j� liberados na Engine.
	 */
	private static int releasedMemory;

	/**
	 * Registra quantos bytes foram liberados na Engine para serem contabilizados.
	 * Quando atingido <code>RELEASE_MEMORY_SIZE</code> vai chamar manualmente o Garbage Collector.
	 * @param bytes quantidade de bytes que foram liberados de um objeto.
	 */

	public static void releasedMemory(int bytes)
	{
		if ((releasedMemory += bytes) >= RELEASE_MEMORY_SIZE)
		{
			System.gc();
			releasedMemory -= RELEASE_MEMORY_SIZE;
		}
	}

	/**
	 * Passa os valores de um vetor de 3 pontos flutuantes para um vetor de 3 pontos inteiros.
	 * @param vector refer�ncia do vetor contendo os 3 pontos flutuantes.
	 * @return vetor de 3 pontos especificados como X, Y e Z do tipo inteiro.
	 */

	public static Vector3i convert3f3i(Vector3f vector)
	{
		return new Vector3i((int) vector.x, (int) vector.y, (int) vector.z);
	}

	/**
	 * Passa os valores de um vetor de 3 pontos inteiros para um vetor de 3 pontos flutuantes.
	 * @param vector refer�ncia do vetor contendo os 3 pontos inteiros.
	 * @return vetor de 3 pontos especificados como X, Y e Z do tipo flutuante.
	 */

	public static Vector3f convert3i3f(Vector3i vector)
	{
		return new Vector3f(vector.x, vector.y, vector.z);
	}

	/**
	 * Construtor privado, pois todos os m�todos ser�o est�ticos (classe utilit�ria).
	 */

	private APIGLUtil()
	{
		
	}
}
