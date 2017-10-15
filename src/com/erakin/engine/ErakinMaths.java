package com.erakin.engine;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.erakin.engine.camera.Camera;

/**
 * <h1>Matemática</h1>
 *
 * <p>Classe utilitária que é composta apenas de procedimentos estáticos a fim de fazer contas matemáticas.
 * Utilizado por diversas classes que precisam realizar as mesmas operações e muitas vezes complexas.
 * Assim é possível reduzir quantidade de códigos e facilitar o entendimento nessas outras classes.</p>
 *
 * @author Andrew Mello
 */

public class ErakinMaths
{
	/**
	 * Construtor privado para evitar instâncias desnecessárias dessa classe utilitária.
	 */

	private ErakinMaths()
	{
		
	}

	/**
	 * Matriz de visão é usada para simular a tela da aplicação como a visualização de uma câmera.
	 * Onde aparenta que a tela está se movendo para os lados quando na verdade é todo o resto.
	 * Através de uma interface câmera será possível criar essa matriz de visão apropriadamente.
	 * @param camera referência da câmera do qual deseja criar uma matriz de visão.
	 * @return matrix 4x4 de visão de acordo com as informações da câmera passada.
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
