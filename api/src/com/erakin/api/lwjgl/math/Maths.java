package com.erakin.api.lwjgl.math;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * <h1>Matemática</h1>
 *
 * <p>Classe utilitária que é composta apenas de procedimentos estáticos a fim de fazer contas matemáticas.
 * Utilizado por diversas classes que precisam realizar as mesmas operações e muitas vezes complexas.
 * Assim é possível reduzir quantidade de códigos e facilitar o entendimento nessas outras classes.</p>
 *
 * <p>Aqui apenas os cálculos gerais serão especificados, estes podem ser utilizados por diversas partes do sistema.
 * Outras classes de cálculos podem existir, porém terá utilidade apenas para uma parte específica do sistema.</p>
 *
 * @author Andrew Mello
 */

public class Maths
{
	/**
	 * Deve obter um valor na escola dobrada de 2 num limite máximo possível.
	 * O limite possível será o valor passado, em quanto o valor retornado,
	 * irá verificar qual o dobro de 2 em loop que poderá ser alcançado.
	 * @param value valor "limite" que será considerado.
	 * @return maior valor possivelmente alcançado.
	 */

	public static int fold(int value)
	{
		int current = 2;

		while (current < value)
			current *= 2;

		return current;
	}

	/**
	 * A matriz de transformação tem como objetivo determinar uma transformação para alguns valores abaixo.
	 * Essa transformação permite que os vértices de uma modelagem que são estáticos sejam "dinâmicos".
	 * Por exemplo, um cubo que tenha 1f em todos os lados, pode passar a ter 2f ou 0.7f como ainda também,
	 * será possível reposicionar e rotacionar em todos os três eixos do tri-dimensional (X, Y e Z).
	 * @param position vetor da posição para calcular o novo posicionamento dos vértices.
	 * @param rotation vetor da rotação para calcular o novo posicionamento dos vértices.
	 * @param scale vetor da escala para calcular o novo posicionamento dos vértices.
	 * @return matriz 4x4 contendo informações de transformação para reposicionar os vértices se necessário,
	 * de acordo com as informações de posicionamento, rotação e escala definidas a cima.
	 */

	public static Matrix4f createTransformationMatrix(Vector3f position, Vector3f rotation, Vector3f scale)
	{
		Matrix4f matrix = new Matrix4f();

		Matrix4f.translate(position, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0 ,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1 ,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0 ,1), matrix, matrix);
		Matrix4f.scale(scale, matrix, matrix);

		return matrix;
	}

	/**
	 * A matriz de projeção irá calcular conter informações sobre o espaço do qual está sendo projeto.
	 * Essa matriz de projeção não é relativa ao posicionamento da câmera, mas irá indicar seu espaço.
	 * Ou seja, indicar um espaço N que será ocupado na projeção quando for aplicado a visão da câmera.
	 * @param fov tamanho do campo de visão que irá indicar a extensão angular da visão da projeção.
	 * @param nearPlane o quão próximo do ponto central estará sendo iniciada a projeção.
	 * @param farPlane o quão distante do ponto central será oferecida a projeção.
	 * @return aquisição de uma matriz 4x4 contendo informações do espaço de projeção.
	 */

	public static Matrix4f createProjectionMatrix(float fov, float nearPlane, float farPlane)
	{
		return createProjectionMatrix(new Matrix4f(), fov, nearPlane, farPlane);
	}

	/**
	 * A matriz de projeção irá calcular conter informações sobre o espaço do qual está sendo projeto.
	 * Essa matriz de projeção não é relativa ao posicionamento da câmera, mas irá indicar seu espaço.
	 * Ou seja, indicar um espaço N que será ocupado na projeção quando for aplicado a visão da câmera.
	 * @param projectionMatrix matriz que será usada para definir os dados como matriz de projeção.
	 * @param fov tamanho do campo de visão que irá indicar a extensão angular da visão da projeção.
	 * @param nearPlane o quão próximo do ponto central estará sendo iniciada a projeção.
	 * @param farPlane o quão distante do ponto central será oferecida a projeção.
	 * @return aquisição de uma matriz 4x4 contendo informações do espaço de projeção.
	 */

	public static Matrix4f createProjectionMatrix(Matrix4f projectionMatrix, float fov, float nearPlane, float farPlane)
	{
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float scaleY = (float) ((1f / Math.tan(Math.toRadians(fov / 2f))) * aspectRatio);
		float scaleX = scaleY / aspectRatio;
		float frustumLength = farPlane - nearPlane;

		projectionMatrix.m00 = scaleX;
		projectionMatrix.m11 = scaleY;
		projectionMatrix.m22 = -((farPlane + nearPlane) / frustumLength);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * nearPlane * farPlane) / frustumLength);
		projectionMatrix.m33 = 0;

		return projectionMatrix;
	}

	/**
	 * Construtor privado para evitar instâncias desnecessárias dessa classe utilitária.
	 */

	private Maths()
	{
		
	}
}
