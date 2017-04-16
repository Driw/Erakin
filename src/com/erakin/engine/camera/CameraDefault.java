package com.erakin.engine.camera;

import org.diverproject.util.ObjectDescription;
import org.lwjgl.util.vector.Vector3f;

/**
 * <h1>C�mera Padr�o</h1>
 *
 * <p>Implementa��o b�sica de como uma c�mera dever� funcionar, contendo procedimentos para movimenta��o.
 * Cada procedimento dever� identificar o que ser� usado e como ser� feito o c�lculo daquele movimento.
 * Assim � poss�vel a exist�ncia dessa c�mera padr�o, possuindo apenas a ideia dos movimentos.</p>
 *
 * <p>Para que tornar mais vis�vel a ideia, usamos como exemplo o movimento de zoom na c�mera que se refere
 * a aproxima��o e afastamento do ponto da c�mera com o seu ponto de vis�o m�ximo em rela��o a entidade.
 * Esse m�todo sempre ser� chamado quando a c�mera for atualiza, por�m n�o ir� dizer como ser� calculada.</p>
 *
 * <p>De modo que seja necess�rio a constru��o de procedimentos abstratos que ser�o implementados nas especifica��es.
 * H� outros m�todos semelhantes a estes, abstratos e referentes a outros movimentos como rota��o para os lados.
 * Aqui ainda ser� definido as vari�veis para armazenar os valores de posicionamento, rota��o e taxa de zoom.</p>
 *
 * @see Camera
 * @see Vector3f
 *
 * @author Andrew Mello
 */

public abstract class CameraDefault implements Camera
{
	/**
	 * Taxa de aproxima��o da c�mera com o ponto central.
	 */
	protected float zoom;

	/**
	 * Ponto central do qual a rota��o ser� relacionado.
	 */
	protected Vector3f position;

	/**
	 * Propriedade de rotacionamento da c�mera em rela��o ao seu ponto central.
	 */
	protected Vector3f rotation;

	/**
	 * Constr�is uma nova c�mera padr�o definindo alguns valores padr�es para os atributos.
	 * Ir� iniciar o vetor para definir o ponto central e rota��o, e a taxa de aproxima��o.
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
	 * Procedimento que deve calcular o quanto de zoom est� sendo dado.
	 */

	protected abstract float calculateZoom();

	/**
	 * Procedimento que deve calcular o quanto de rota��o est� sendo feito.
	 */

	protected abstract float calculatePitch();

	/**
	 * Procedimento que deve calcular o quanto de guina��o est� sendo feito.
	 */

	protected abstract float calculateYaw();

	/**
	 * Procedimento que deve calcular o quanto de inclina��o est� sendo feito.
	 */

	protected abstract float calculateRoll();

	/**
	 * Procedimento interno usado para calcular a dist�ncia percorrida na horizontal.
	 * Essa dist�ncia � entre a �ltima posi��o e a nova posi��o no �ltimo frame.
	 * @return aquisi��o da dist�ncia entre a c�mera e o jogador na horizontal.
	 */

	protected abstract float calculateHorizontalDistance();

	/**
	 * Procedimento interno usado para calcular a dist�ncia percorrida na vertical.
	 * Essa dist�ncia � entre a �ltima posi��o e a nova posi��o no �ltimo frame.
	 * @return aquisi��o da dist�ncia entre a c�mera e o jogador na vertical.
	 */

	protected abstract float calculateVerticalDistance();

	/**
	 * Procedimento interno usado para fazer o c�lculo da posi��o da c�mera.
	 * @param horizontal dist�ncia na horizontal que a c�mera se movimentou.
	 * @param vertical dist�ncia na vertical que a c�mera se movimentou.
	 */

	protected abstract void calculateCameraPosition(float horizontal, float vertical);

	/**
	 * Procedimento chamado por toString a fim de preencher adequadamente o mesmo.
	 * O posicionamento do conte�do � sempre feito apenas ao final da descri��o.
	 * @param description refer�ncia da descri��o desse objeto que ser� usado.
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
