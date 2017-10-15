package com.erakin.engine;

import org.lwjgl.util.vector.Matrix4f;

public class EngineUtil
{
	private EngineUtil()
	{
		
	}

	/**
	 * A matriz de projeção é usada para indicar a área e distância para visualizar o espaço na tela.
	 * @return aquisição da matriz de projeção global usada pelo engine.
	 */

	public static Matrix4f getProjectMatrix()
	{
		ProjectionMatrix projectionMatrix = ProjectionMatrix.getInstance();
		Matrix4f matrix = projectionMatrix.getMatrix();

		return matrix;
	}

	/**
	 * Permite definir alguns atributos da matriz de projeção e atualizar o mesmo após.
	 * @param fieldOfView campo de visão indica o quanto será visível pela câmera.
	 * @param nearPlane indica onde será iniciado o campo de visão respectivo a câmera.
	 * @param farPlane indica onde será terminado o campo de visão respectivo a câmera.
	 */

	public static void setProjection(float fieldOfView, float nearPlane, float farPlane)
	{
		ProjectionMatrix projectionMatrix = ProjectionMatrix.getInstance();
		projectionMatrix.updateWith(fieldOfView, nearPlane, farPlane);
	}
}
