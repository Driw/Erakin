package com.erakin.engine;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.erakin.engine.camera.Camera;

public class EngineUtil
{
	/**
	 * Aquisição da instância da engine.
	 */
	public static final Engine ENGINE = Engine.getInstance();

	/**
	 * Verifica se está sendo processado o primeiro quadro do segundo em questão.
	 * Pode ser usado para que determinadas informações sejam atualizadas uma vez por segundo.
	 * @return true se for o primeiro quadro ou false caso contrário.
	 */

	public static boolean isFirstFrame()
	{
		return DisplayManager.getInstance().getFrameCount() == 0;
	}

	/**
	 * A matriz de projeção é usada para indicar a área e distância para visualizar o espaço na tela.
	 * @return aquisição da matriz de projeção global usada pelo engine.
	 */

	public static Matrix4f getProjectionMatrix()
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

	/**
	 * Uma câmera não possui método para se criar/obter um vetor com suas coordenadas no espaço.
	 * Através desse método se cria esse vetor internamente e definine a posição da câmera.
	 * @param camera referência da câmera para se obter as coordenadas X, Y e Z.
	 * @return aquisição de um novo vetor contendo a posição da câmera.
	 */

	public static Vector3f newVector3f(Camera camera)
	{
		return new Vector3f(camera.getPositionX(), camera.getPositionY(), camera.getPositionZ());
	}

	private EngineUtil()
	{
		
	}
}
