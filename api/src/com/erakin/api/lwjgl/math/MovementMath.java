package com.erakin.api.lwjgl.math;

import org.diverproject.util.ObjectDescription;
import org.lwjgl.util.vector.Vector3f;

/**
 * <h1>Cálculo de Movimento</h1>
 *
 * <p>Através dessa classe é possível saber quanto um objeto se movimentou para cada uma das direções no espaço (X, Y e Z).
 * Para isso é necessário definir quais as direções do qual o objeto tentou se mover (cima, baixo, frente, trás, esquerda e direita).
 * Depois deve se chamar o método que vai realizar o cálculo da movimentação, sendo necessário definir a distância e guinada.</p>
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
	 * Calcular o movimento para trás.
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
	 * Vetor para definir a distância da movimentação em cada eixo.
	 */
	private Vector3f movement;

	/**
	 * Cria uma nova instância de um objeto que permite calcular a movimentação de um objeto no espaço.
	 * Inicializa o vetor que vai armazenar a distância percorrida em cada eixo conforme preferências.
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
	 * Realiza o cálculo de movimentação de um objeto no espaço considerando todas as possibilidades de movimentação.
	 * Cria um vetor de dois pontos (float) internamente para determinar qual a distância percorrida no movimento.
	 * <i>OBS: aqui é usado os pontos X e Y, mas deve ser levado em consideração a orientação no espaço usado.</i>
	 * @param distance parâmetro de distância que determina quanto o objeto deve se movimentar.
	 * @param yaw posicionamento relativo de guinada da orientação de visão do objeto.
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
	 * Determina se durante o cálculo de movimentação deve ser feito o de movimentação para cima.
	 * @param up true se o objeto se moveu para cima ou false caso contrário.
	 */

	public void setUp(boolean up)
	{
		this.up = up;
	}

	/**
	 * Determina se durante o cálculo de movimentação deve ser feito o de movimentação para baixo.
	 * @param down true se o objeto se moveu para baixo ou false caso contrário.
	 */

	public void setDown(boolean down)
	{
		this.down = down;
	}

	/**
	 * Determina se durante o cálculo de movimentação deve ser feito o de movimentação para frente.
	 * @param forward true se o objeto se moveu para frente ou false caso contrário.
	 */

	public void setForward(boolean forward)
	{
		this.forward = forward;
	}

	/**
	 * Determina se durante o cálculo de movimentação deve ser feito o de movimentação para trás.
	 * @param behind true se o objeto se moveu para trás ou false caso contrário.
	 */

	public void setBehind(boolean behind)
	{
		this.behind = behind;
	}

	/**
	 * Determina se durante o cálculo de movimentação deve ser feito o de movimentação para esquerda.
	 * @param left true se o objeto se moveu para esquerda ou false caso contrário.
	 */

	public void setLeft(boolean left)
	{
		this.left = left;
	}

	/**
	 * Determina se durante o cálculo de movimentação deve ser feito o de movimentação para direita.
	 * @param right true se o objeto se moveu para direita ou false caso contrário.
	 */

	public void setRight(boolean right)
	{
		this.right = right;
	}

	/**
	 * Durante o cálculo de movimentação do objeto no espaço, os valores de distância percorrida são armazenados.
	 * @return aquisição do vetor contendo a distância percorrida pelo objeto no espaço em cada orientação.
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
	 * Calcula o movimento de um objeto no espaço quando está querendo se movimentar para frente.
	 * @param distance parâmetro de distância que determina quanto o objeto deve se movimentar.
	 * @param yaw posicionamento relativo de guinada da orientação de visão do objeto.
	 * @return aquisição da movimentação do objeto para frente no eixo X.
	 */

	public static float calcForwardMovementX(float distance, float yaw)
	{
		return distance * (float) Math.sin(Math.toRadians(yaw));
	}

	/**
	 * Calcula o movimento de um objeto no espaço quando está querendo se movimentar para frente.
	 * @param distance parâmetro de distância que determina quanto o objeto deve se movimentar.
	 * @param yaw posicionamento relativo de guinada da orientação de visão do objeto.
	 * @return aquisição da movimentação do objeto para frente no eixo Z.
	 */

	public static float calcForwardMovementZ(float distance, float yaw)
	{
		return -(distance * (float) Math.cos(Math.toRadians(yaw)));
	}

	/**
	 * Calcula o movimento de um objeto no espaço quando está querendo se movimentar para trás.
	 * @param distance parâmetro de distância que determina quanto o objeto deve se movimentar.
	 * @param yaw posicionamento relativo de guinada da orientação de visão do objeto.
	 * @return aquisição da movimentação do objeto para trás no eixo X.
	 */

	public static float calcBehindMovementX(float distance, float yaw)
	{
		return -(distance * (float) Math.sin(Math.toRadians(yaw)));
	}

	/**
	 * Calcula o movimento de um objeto no espaço quando está querendo se movimentar para trás.
	 * @param distance parâmetro de distância que determina quanto o objeto deve se movimentar.
	 * @param yaw posicionamento relativo de guinada da orientação de visão do objeto.
	 * @return aquisição da movimentação do objeto para trás no eixo Z.
	 */

	public static float calcBehindMovementZ(float distance, float yaw)
	{
		return distance * (float) Math.cos(Math.toRadians(yaw));
	}

	/**
	 * Calcula o movimento de um objeto no espaço quando está querendo se movimentar para esquerda.
	 * @param distance parâmetro de distância que determina quanto o objeto deve se movimentar.
	 * @param yaw posicionamento relativo de guinada da orientação de visão do objeto.
	 * @return aquisição da movimentação do objeto para esquerda no eixo X.
	 */

	public static float calcLeftMovementX(float distance, float yaw)
	{
		return -(distance * (float) Math.sin(Math.toRadians(yaw + 90)));
	}

	/**
	 * Calcula o movimento de um objeto no espaço quando está querendo se movimentar para esquerda.
	 * @param distance parâmetro de distância que determina quanto o objeto deve se movimentar.
	 * @param yaw posicionamento relativo de guinada da orientação de visão do objeto.
	 * @return aquisição da movimentação do objeto para esquerda no eixo Z.
	 */

	public static float calcLeftMovementZ(float distance, float yaw)
	{
		return distance * (float) Math.cos(Math.toRadians(yaw + 90));
	}

	/**
	 * Calcula o movimento de um objeto no espaço quando está querendo se movimentar para direita.
	 * @param distance parâmetro de distância que determina quanto o objeto deve se movimentar.
	 * @param yaw posicionamento relativo de guinada da orientação de visão do objeto.
	 * @return aquisição da movimentação do objeto para direita no eixo X.
	 */

	public static float calcRightMovementX(float distance, float yaw)
	{
		return -(distance * (float) Math.sin(Math.toRadians(yaw - 90)));
	}

	/**
	 * Calcula o movimento de um objeto no espaço quando está querendo se movimentar para direita.
	 * @param distance parâmetro de distância que determina quanto o objeto deve se movimentar.
	 * @param yaw posicionamento relativo de guinada da orientação de visão do objeto.
	 * @return aquisição da movimentação do objeto para direita no eixo Z.
	 */

	public static float calcRightMovementZ(float distance, float yaw)
	{
		return distance * (float) Math.cos(Math.toRadians(yaw - 90));
	}

	/**
	 * Realiza um cálculo simples de movimentação de um objeto no espaço, porém somente no eixo da vertical.
	 * @param distance parâmetro de distância que determina quanto o objeto deve se movimentar.
	 * @param up o objeto está tentando se movimentar para cima.
	 * @param down o objeto está tentando se movimentar para baixo.
	 * @return aquisição da movimentação do objeto no eixo da altitude.
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
