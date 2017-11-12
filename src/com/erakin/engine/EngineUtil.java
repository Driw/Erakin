package com.erakin.engine;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.erakin.engine.camera.Camera;

public class EngineUtil
{
	/**
	 * Aquisi��o da inst�ncia da engine.
	 */
	public static final Engine ENGINE = Engine.getInstance();

	/**
	 * Verifica se est� sendo processado o primeiro quadro do segundo em quest�o.
	 * Pode ser usado para que determinadas informa��es sejam atualizadas uma vez por segundo.
	 * @return true se for o primeiro quadro ou false caso contr�rio.
	 */

	public static boolean isFirstFrame()
	{
		return DisplayManager.getInstance().getFrameCount() == 0;
	}

	/**
	 * A matriz de proje��o � usada para indicar a �rea e dist�ncia para visualizar o espa�o na tela.
	 * @return aquisi��o da matriz de proje��o global usada pelo engine.
	 */

	public static Matrix4f getProjectionMatrix()
	{
		ProjectionMatrix projectionMatrix = ProjectionMatrix.getInstance();
		Matrix4f matrix = projectionMatrix.getMatrix();

		return matrix;
	}

	/**
	 * Permite definir alguns atributos da matriz de proje��o e atualizar o mesmo ap�s.
	 * @param fieldOfView campo de vis�o indica o quanto ser� vis�vel pela c�mera.
	 * @param nearPlane indica onde ser� iniciado o campo de vis�o respectivo a c�mera.
	 * @param farPlane indica onde ser� terminado o campo de vis�o respectivo a c�mera.
	 */

	public static void setProjection(float fieldOfView, float nearPlane, float farPlane)
	{
		ProjectionMatrix projectionMatrix = ProjectionMatrix.getInstance();
		projectionMatrix.updateWith(fieldOfView, nearPlane, farPlane);
	}

	/**
	 * Uma c�mera n�o possui m�todo para se criar/obter um vetor com suas coordenadas no espa�o.
	 * Atrav�s desse m�todo se cria esse vetor internamente e definine a posi��o da c�mera.
	 * @param camera refer�ncia da c�mera para se obter as coordenadas X, Y e Z.
	 * @return aquisi��o de um novo vetor contendo a posi��o da c�mera.
	 */

	public static Vector3f newVector3f(Camera camera)
	{
		return new Vector3f(camera.getPositionX(), camera.getPositionY(), camera.getPositionZ());
	}

	private EngineUtil()
	{
		
	}
}
