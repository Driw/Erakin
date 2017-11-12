package com.erakin.engine.camera;

import org.diverproject.util.ObjectDescription;
import org.lwjgl.util.vector.Vector3f;

/**
 * <h1>Câmera Padrão</h1>
 *
 * <p>Implementação básica de como uma câmera deverá funcionar, contendo procedimentos para movimentação.
 * Cada procedimento deverá identificar o que será usado e como será feito o cálculo daquele movimento.
 * Assim é possível a existência dessa câmera padrão, possuindo apenas a ideia dos movimentos.</p>
 *
 * <p>Para que tornar mais visível a ideia, usamos como exemplo o movimento de zoom na câmera que se refere
 * a aproximação e afastamento do ponto da câmera com o seu ponto de visão máximo em relação a entidade.
 * Esse método sempre será chamado quando a câmera for atualiza, porém não irá dizer como será calculada.</p>
 *
 * <p>De modo que seja necessário a construção de procedimentos abstratos que serão implementados nas especificações.
 * Há outros métodos semelhantes a estes, abstratos e referentes a outros movimentos como rotação para os lados.
 * Aqui ainda será definido as variáveis para armazenar os valores de posicionamento, rotação e taxa de zoom.</p>
 *
 * @see Camera
 * @see Vector3f
 *
 * @author Andrew Mello
 */

public abstract class CameraDefault implements Camera
{
	/**
	 * Taxa de aproximação da câmera com o ponto central.
	 */
	protected float zoom;

	/**
	 * Ponto central do qual a rotação será relacionado.
	 */
	protected Vector3f position;

	/**
	 * Propriedade de rotacionamento da câmera em relação ao seu ponto central.
	 */
	protected Vector3f rotation;

	/**
	 * Constróis uma nova câmera padrão definindo alguns valores padrões para os atributos.
	 * Irá iniciar o vetor para definir o ponto central e rotação, e a taxa de aproximação.
	 */

	public CameraDefault()
	{
		zoom = 3.0f;
		position = new Vector3f();
		rotation = new Vector3f(0, 20, 0);
	}

	@Override
	public void update(long delay)
	{
		zoom = calculateZoom();
		rotation.x = calculatePitch();
		rotation.y = calculateYaw();

		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();

		calculateCameraPosition(horizontalDistance, verticalDistance);

		rotation.z = calculateRoll();
	}

	@Override
	public float getZoom()
	{
		return zoom;
	}

	@Override
	public float getPositionX()
	{
		return position.x;
	}

	@Override
	public float getPositionY()
	{
		return position.y;
	}

	@Override
	public float getPositionZ()
	{
		return position.z;
	}

	@Override
	public float getPitch()
	{
		return rotation.x;
	}

	@Override
	public float getYaw()
	{
		return rotation.y;
	}

	@Override
	public float getRoll()
	{
		return rotation.z;
	}

	/**
	 * Procedimento que deve calcular o quanto de zoom está sendo dado.
	 */

	protected abstract float calculateZoom();

	/**
	 * Procedimento que deve calcular o quanto de rotação está sendo feito.
	 */

	protected abstract float calculatePitch();

	/**
	 * Procedimento que deve calcular o quanto de guinação está sendo feito.
	 */

	protected abstract float calculateYaw();

	/**
	 * Procedimento que deve calcular o quanto de inclinação está sendo feito.
	 */

	protected abstract float calculateRoll();

	/**
	 * Procedimento interno usado para calcular a distância percorrida na horizontal.
	 * Essa distância é entre a última posição e a nova posição no último frame.
	 * @return aquisição da distância entre a câmera e o jogador na horizontal.
	 */

	protected abstract float calculateHorizontalDistance();

	/**
	 * Procedimento interno usado para calcular a distância percorrida na vertical.
	 * Essa distância é entre a última posição e a nova posição no último frame.
	 * @return aquisição da distância entre a câmera e o jogador na vertical.
	 */

	protected abstract float calculateVerticalDistance();

	/**
	 * Procedimento interno usado para fazer o cálculo da posição da câmera.
	 * @param horizontal distância na horizontal que a câmera se movimentou.
	 * @param vertical distância na vertical que a câmera se movimentou.
	 */

	protected abstract void calculateCameraPosition(float horizontal, float vertical);

	/**
	 * Procedimento chamado por toString a fim de preencher adequadamente o mesmo.
	 * O posicionamento do conteúdo é sempre feito apenas ao final da descrição.
	 * @param description referência da descrição desse objeto que será usado.
	 */

	public abstract void toString(ObjectDescription description);

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("zoom", zoom);
		description.append("x", position.x);
		description.append("y", position.y);
		description.append("z", position.z);
		description.append("pitch", rotation.x);
		description.append("yaw", rotation.y);
		description.append("roll", rotation.z);

		toString(description);

		return description.toString();
	}
}
