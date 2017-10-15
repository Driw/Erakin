package com.erakin.engine;

import org.lwjgl.util.vector.Matrix4f;

public class EngineUtil
{
	private EngineUtil()
	{
		
	}

	/**
	 * A matriz de proje��o � usada para indicar a �rea e dist�ncia para visualizar o espa�o na tela.
	 * @return aquisi��o da matriz de proje��o global usada pelo engine.
	 */

	public static Matrix4f getProjectMatrix()
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
}
