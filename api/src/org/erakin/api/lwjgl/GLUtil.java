package org.erakin.api.lwjgl;

import org.erakin.api.lwjgl.math.Vector3i;
import org.lwjgl.util.vector.Vector3f;

/**
 * Classe utilitária com métodos para se trabalhar com a biblioteca do LWJGL.
 * Irá conter diversos métodos estáticos que podem ser importados de forma estática.
 *
 * @author Andrew
 */

public class GLUtil
{
	/**
	 * Construtor privado, pois todos os métodos serão estáticos (classe utilitária).
	 */

	private GLUtil()
	{
		
	}

	/**
	 * Passa os valores de um vetor de 3 pontos flutuantes para um vetor de 3 pontos inteiros.
	 * @param vector referência do vetor contendo os 3 pontos flutuantes.
	 * @return vetor de 3 pontos especificados como X, Y e Z do tipo inteiro.
	 */

	public static Vector3i convert3f3i(Vector3f vector)
	{
		return new Vector3i((int) vector.x, (int) vector.y, (int) vector.z);
	}

	/**
	 * Passa os valores de um vetor de 3 pontos inteiros para um vetor de 3 pontos flutuantes.
	 * @param vector referência do vetor contendo os 3 pontos inteiros.
	 * @return vetor de 3 pontos especificados como X, Y e Z do tipo flutuante.
	 */

	public static Vector3f convert3i3f(Vector3i vector)
	{
		return new Vector3f(vector.x, vector.y, vector.z);
	}
}
