package com.erakin.api.lwjgl.math;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * <h1>Matem�tica</h1>
 *
 * <p>Classe utilit�ria que � composta apenas de procedimentos est�ticos a fim de fazer contas matem�ticas.
 * Utilizado por diversas classes que precisam realizar as mesmas opera��es e muitas vezes complexas.
 * Assim � poss�vel reduzir quantidade de c�digos e facilitar o entendimento nessas outras classes.</p>
 *
 * <p>Aqui apenas os c�lculos gerais ser�o especificados, estes podem ser utilizados por diversas partes do sistema.
 * Outras classes de c�lculos podem existir, por�m ter� utilidade apenas para uma parte espec�fica do sistema.</p>
 *
 * @author Andrew Mello
 */

public class Maths
{
	/**
	 * Deve obter um valor na escola dobrada de 2 num limite m�ximo poss�vel.
	 * O limite poss�vel ser� o valor passado, em quanto o valor retornado,
	 * ir� verificar qual o dobro de 2 em loop que poder� ser alcan�ado.
	 * @param value valor "limite" que ser� considerado.
	 * @return maior valor possivelmente alcan�ado.
	 */

	public static int fold(int value)
	{
		int current = 2;

		while (current < value)
			current *= 2;

		return current;
	}

	/**
	 * A matriz de transforma��o tem como objetivo determinar uma transforma��o para alguns valores abaixo.
	 * Essa transforma��o permite que os v�rtices de uma modelagem que s�o est�ticos sejam "din�micos".
	 * Por exemplo, um cubo que tenha 1f em todos os lados, pode passar a ter 2f ou 0.7f como ainda tamb�m,
	 * ser� poss�vel reposicionar e rotacionar em todos os tr�s eixos do tri-dimensional (X, Y e Z).
	 * @param position vetor da posi��o para calcular o novo posicionamento dos v�rtices.
	 * @param rotation vetor da rota��o para calcular o novo posicionamento dos v�rtices.
	 * @param scale vetor da escala para calcular o novo posicionamento dos v�rtices.
	 * @return matriz 4x4 contendo informa��es de transforma��o para reposicionar os v�rtices se necess�rio,
	 * de acordo com as informa��es de posicionamento, rota��o e escala definidas a cima.
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
	 * A matriz de proje��o ir� calcular conter informa��es sobre o espa�o do qual est� sendo projeto.
	 * Essa matriz de proje��o n�o � relativa ao posicionamento da c�mera, mas ir� indicar seu espa�o.
	 * Ou seja, indicar um espa�o N que ser� ocupado na proje��o quando for aplicado a vis�o da c�mera.
	 * @param fov tamanho do campo de vis�o que ir� indicar a extens�o angular da vis�o da proje��o.
	 * @param nearPlane o qu�o pr�ximo do ponto central estar� sendo iniciada a proje��o.
	 * @param farPlane o qu�o distante do ponto central ser� oferecida a proje��o.
	 * @return aquisi��o de uma matriz 4x4 contendo informa��es do espa�o de proje��o.
	 */

	public static Matrix4f createProjectionMatrix(float fov, float nearPlane, float farPlane)
	{
		return createProjectionMatrix(new Matrix4f(), fov, nearPlane, farPlane);
	}

	/**
	 * A matriz de proje��o ir� calcular conter informa��es sobre o espa�o do qual est� sendo projeto.
	 * Essa matriz de proje��o n�o � relativa ao posicionamento da c�mera, mas ir� indicar seu espa�o.
	 * Ou seja, indicar um espa�o N que ser� ocupado na proje��o quando for aplicado a vis�o da c�mera.
	 * @param projectionMatrix matriz que ser� usada para definir os dados como matriz de proje��o.
	 * @param fov tamanho do campo de vis�o que ir� indicar a extens�o angular da vis�o da proje��o.
	 * @param nearPlane o qu�o pr�ximo do ponto central estar� sendo iniciada a proje��o.
	 * @param farPlane o qu�o distante do ponto central ser� oferecida a proje��o.
	 * @return aquisi��o de uma matriz 4x4 contendo informa��es do espa�o de proje��o.
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
	 * Construtor privado para evitar inst�ncias desnecess�rias dessa classe utilit�ria.
	 */

	private Maths()
	{
		
	}
}
