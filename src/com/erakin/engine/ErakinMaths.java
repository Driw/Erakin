package com.erakin.engine;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.erakin.engine.camera.Camera;

/**
 * <h1>Matem�tica</h1>
 *
 * <p>Classe utilit�ria que � composta apenas de procedimentos est�ticos a fim de fazer contas matem�ticas.
 * Utilizado por diversas classes que precisam realizar as mesmas opera��es e muitas vezes complexas.
 * Assim � poss�vel reduzir quantidade de c�digos e facilitar o entendimento nessas outras classes.</p>
 *
 * @author Andrew Mello
 */

public class ErakinMaths
{
	/**
	 * Construtor privado para evitar inst�ncias desnecess�rias dessa classe utilit�ria.
	 */

	private ErakinMaths()
	{
		
	}

	/**
	 * Matriz de vis�o � usada para simular a tela da aplica��o como a visualiza��o de uma c�mera.
	 * Onde aparenta que a tela est� se movendo para os lados quando na verdade � todo o resto.
	 * Atrav�s de uma interface c�mera ser� poss�vel criar essa matriz de vis�o apropriadamente.
	 * @param camera refer�ncia da c�mera do qual deseja criar uma matriz de vis�o.
	 * @return matrix 4x4 de vis�o de acordo com as informa��es da c�mera passada.
	 */

	public static Matrix4f createViewMatrix(Camera camera)
	{
		Matrix4f viewMatrix = new Matrix4f();

		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);

		Vector3f negativeCameraPos = new Vector3f(-camera.getPositionX(), -camera.getPositionY(), -camera.getPositionZ());
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);

		return viewMatrix;
	}
}
