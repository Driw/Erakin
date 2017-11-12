package com.erakin.api.lwjgl.math;

import org.diverproject.util.ObjectDescription;
import org.lwjgl.util.vector.Vector3f;

/**
 * <h1>C�lculo de Movimento</h1>
 *
 * <p>Atrav�s dessa classe � poss�vel saber quanto um objeto se movimentou para cada uma das dire��es no espa�o (X, Y e Z).
 * Para isso � necess�rio definir quais as dire��es do qual o objeto tentou se mover (cima, baixo, frente, tr�s, esquerda e direita).
 * Depois deve se chamar o m�todo que vai realizar o c�lculo da movimenta��o, sendo necess�rio definir a dist�ncia e guinada.</p>
 *
 * @author Andrew
 */

public class MovementMath
{
	/**
	 * Calcular o movimento para cima.
	 */
	private boolean up;

	/**
	 * Calcular o movimento para baixo.
	 */
	private boolean down;

	/**
	 * Calcular o movimento para frente.
	 */
	private boolean forward;

	/**
	 * Calcular o movimento para tr�s.
	 */
	private boolean behind;

	/**
	 * Calcular o movimento para esquerda.
	 */
	private boolean left;

	/**
	 * Calcular o movimento para direita.
	 */
	private boolean right;

	/**
	 * Vetor para definir a dist�ncia da movimenta��o em cada eixo.
	 */
	private Vector3f movement;

	/**
	 * Cria uma nova inst�ncia de um objeto que permite calcular a movimenta��o de um objeto no espa�o.
	 * Inicializa o vetor que vai armazenar a dist�ncia percorrida em cada eixo conforme prefer�ncias.
	 */

	public MovementMath()
	{
		movement = new Vector3f();
	}

	/**
	 * Procedimento interno usado para restabelecer o movimento com valor zerado.
	 */

	private void resetMovement()
	{
		movement.x = movement.y = movement.z = 0f;
	}

	/**
	 * Realiza o c�lculo de movimenta��o de um objeto no espa�o considerando todas as possibilidades de movimenta��o.
	 * Cria um vetor de dois pontos (float) internamente para determinar qual a dist�ncia percorrida no movimento.
	 * <i>OBS: aqui � usado os pontos X e Y, mas deve ser levado em considera��o a orienta��o no espa�o usado.</i>
	 * @param distance par�metro de dist�ncia que determina quanto o objeto deve se movimentar.
	 * @param yaw posicionamento relativo de guinada da orienta��o de vis�o do objeto.
	 */

	public void calcMovement(float distance, float yaw)
	{
		resetMovement();

		if (forward)
		{
			movement.x += calcForwardMovementX(distance, yaw);
			movement.z += calcForwardMovementZ(distance, yaw);
		}

		if (behind)
		{
			movement.x += calcBehindMovementX(distance, yaw);
			movement.z += calcBehindMovementZ(distance, yaw);
		}

		if (left)
		{
			movement.x += calcLeftMovementX(distance, yaw);
			movement.z += calcLeftMovementZ(distance, yaw);
		}

		if (right)
		{
			movement.x += calcRightMovementX(distance, yaw);
			movement.z += calcRightMovementZ(distance, yaw);
		}

		movement.y += calcVerticalMovement(distance, up, down);
	}

	/**
	 * Determina se durante o c�lculo de movimenta��o deve ser feito o de movimenta��o para cima.
	 * @param up true se o objeto se moveu para cima ou false caso contr�rio.
	 */

	public void setUp(boolean up)
	{
		this.up = up;
	}

	/**
	 * Determina se durante o c�lculo de movimenta��o deve ser feito o de movimenta��o para baixo.
	 * @param down true se o objeto se moveu para baixo ou false caso contr�rio.
	 */

	public void setDown(boolean down)
	{
		this.down = down;
	}

	/**
	 * Determina se durante o c�lculo de movimenta��o deve ser feito o de movimenta��o para frente.
	 * @param forward true se o objeto se moveu para frente ou false caso contr�rio.
	 */

	public void setForward(boolean forward)
	{
		this.forward = forward;
	}

	/**
	 * Determina se durante o c�lculo de movimenta��o deve ser feito o de movimenta��o para tr�s.
	 * @param behind true se o objeto se moveu para tr�s ou false caso contr�rio.
	 */

	public void setBehind(boolean behind)
	{
		this.behind = behind;
	}

	/**
	 * Determina se durante o c�lculo de movimenta��o deve ser feito o de movimenta��o para esquerda.
	 * @param left true se o objeto se moveu para esquerda ou false caso contr�rio.
	 */

	public void setLeft(boolean left)
	{
		this.left = left;
	}

	/**
	 * Determina se durante o c�lculo de movimenta��o deve ser feito o de movimenta��o para direita.
	 * @param right true se o objeto se moveu para direita ou false caso contr�rio.
	 */

	public void setRight(boolean right)
	{
		this.right = right;
	}

	/**
	 * Durante o c�lculo de movimenta��o do objeto no espa�o, os valores de dist�ncia percorrida s�o armazenados.
	 * @return aquisi��o do vetor contendo a dist�ncia percorrida pelo objeto no espa�o em cada orienta��o.
	 */

	public Vector3f getMovement()
	{
		return movement;
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		return description.toString();
	}

	/**
	 * Calcula o movimento de um objeto no espa�o quando est� querendo se movimentar para frente.
	 * @param distance par�metro de dist�ncia que determina quanto o objeto deve se movimentar.
	 * @param yaw posicionamento relativo de guinada da orienta��o de vis�o do objeto.
	 * @return aquisi��o da movimenta��o do objeto para frente no eixo X.
	 */

	public static float calcForwardMovementX(float distance, float yaw)
	{
		return distance * (float) Math.sin(Math.toRadians(yaw));
	}

	/**
	 * Calcula o movimento de um objeto no espa�o quando est� querendo se movimentar para frente.
	 * @param distance par�metro de dist�ncia que determina quanto o objeto deve se movimentar.
	 * @param yaw posicionamento relativo de guinada da orienta��o de vis�o do objeto.
	 * @return aquisi��o da movimenta��o do objeto para frente no eixo Z.
	 */

	public static float calcForwardMovementZ(float distance, float yaw)
	{
		return -(distance * (float) Math.cos(Math.toRadians(yaw)));
	}

	/**
	 * Calcula o movimento de um objeto no espa�o quando est� querendo se movimentar para tr�s.
	 * @param distance par�metro de dist�ncia que determina quanto o objeto deve se movimentar.
	 * @param yaw posicionamento relativo de guinada da orienta��o de vis�o do objeto.
	 * @return aquisi��o da movimenta��o do objeto para tr�s no eixo X.
	 */

	public static float calcBehindMovementX(float distance, float yaw)
	{
		return -(distance * (float) Math.sin(Math.toRadians(yaw)));
	}

	/**
	 * Calcula o movimento de um objeto no espa�o quando est� querendo se movimentar para tr�s.
	 * @param distance par�metro de dist�ncia que determina quanto o objeto deve se movimentar.
	 * @param yaw posicionamento relativo de guinada da orienta��o de vis�o do objeto.
	 * @return aquisi��o da movimenta��o do objeto para tr�s no eixo Z.
	 */

	public static float calcBehindMovementZ(float distance, float yaw)
	{
		return distance * (float) Math.cos(Math.toRadians(yaw));
	}

	/**
	 * Calcula o movimento de um objeto no espa�o quando est� querendo se movimentar para esquerda.
	 * @param distance par�metro de dist�ncia que determina quanto o objeto deve se movimentar.
	 * @param yaw posicionamento relativo de guinada da orienta��o de vis�o do objeto.
	 * @return aquisi��o da movimenta��o do objeto para esquerda no eixo X.
	 */

	public static float calcLeftMovementX(float distance, float yaw)
	{
		return -(distance * (float) Math.sin(Math.toRadians(yaw + 90)));
	}

	/**
	 * Calcula o movimento de um objeto no espa�o quando est� querendo se movimentar para esquerda.
	 * @param distance par�metro de dist�ncia que determina quanto o objeto deve se movimentar.
	 * @param yaw posicionamento relativo de guinada da orienta��o de vis�o do objeto.
	 * @return aquisi��o da movimenta��o do objeto para esquerda no eixo Z.
	 */

	public static float calcLeftMovementZ(float distance, float yaw)
	{
		return distance * (float) Math.cos(Math.toRadians(yaw + 90));
	}

	/**
	 * Calcula o movimento de um objeto no espa�o quando est� querendo se movimentar para direita.
	 * @param distance par�metro de dist�ncia que determina quanto o objeto deve se movimentar.
	 * @param yaw posicionamento relativo de guinada da orienta��o de vis�o do objeto.
	 * @return aquisi��o da movimenta��o do objeto para direita no eixo X.
	 */

	public static float calcRightMovementX(float distance, float yaw)
	{
		return -(distance * (float) Math.sin(Math.toRadians(yaw - 90)));
	}

	/**
	 * Calcula o movimento de um objeto no espa�o quando est� querendo se movimentar para direita.
	 * @param distance par�metro de dist�ncia que determina quanto o objeto deve se movimentar.
	 * @param yaw posicionamento relativo de guinada da orienta��o de vis�o do objeto.
	 * @return aquisi��o da movimenta��o do objeto para direita no eixo Z.
	 */

	public static float calcRightMovementZ(float distance, float yaw)
	{
		return distance * (float) Math.cos(Math.toRadians(yaw - 90));
	}

	/**
	 * Realiza um c�lculo simples de movimenta��o de um objeto no espa�o, por�m somente no eixo da vertical.
	 * @param distance par�metro de dist�ncia que determina quanto o objeto deve se movimentar.
	 * @param up o objeto est� tentando se movimentar para cima.
	 * @param down o objeto est� tentando se movimentar para baixo.
	 * @return aquisi��o da movimenta��o do objeto no eixo da altitude.
	 */

	public static float calcVerticalMovement(float distance, boolean up, boolean down)
	{
		float movement = 0f;

		if (up)
			movement += distance;

		if (down)
			movement -= distance;

		return movement;
	}
}
