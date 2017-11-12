package com.erakin.worlds.raw;

import org.diverproject.util.ObjectDescription;
import org.lwjgl.util.vector.Vector3f;

/**
 * <h1>Vetor de 3 Floats Desencade�vel</h1>
 *
 * <p>Esse vetor � o mesmo que um vetor de 3 floats, por�m podemos utilizar um listener nele.
 * Com o listener podemos saber quando os dados de um vetor � alterado e realizar opera��es no mesmo.</p>
 *
 * <p>Por esse vetor ter um listener, podemos criar um editor de mundos/terrenos mais eficiente.
 * Como vamos saber qual o vetor e de qual unidade pertence, podemos saber quais os valores alterados,
 * assim � poss�vel informar ao OpenGL quais os valores de modelo do terreno � atualizar.</p>
 *
 * @see Vector3f
 * @see Vector3fListener
 * @see RawTerrainUnit
 *
 * @author Andrew
 */

@SuppressWarnings("serial")
public class Vector3fTriggerable extends Vector3f
{
	/**
	 * Refer�ncia da unidade de terreno que det�m esse vetor.
	 */
	private RawTerrainUnit cell;

	/**
	 * Listener que ser� chamado quando os valores do vetor forem alterados.
	 */
	private Vector3fListener listener;

	/**
	 * Cria uma nova inst�ncia de um vector de 3 floats desencade�vel.
	 * @param cell refer�ncia da unidade de terreno que det�m o vector.
	 * @param listener refer�ncia do listener (opcional).
	 */

	public Vector3fTriggerable(RawTerrainUnit cell, Vector3fListener listener)
	{
		this.cell = cell;
		this.listener = listener;
	}

	@Override
	public void set(float x, float y, float z)
	{
		super.set(x, y, z);

		if (listener != null)
			listener.onSet(cell, x, y, z);
	}

	/**
	 * @return aquisi��o do listener chamado quando os valores do vetor forem alterados.
	 */

	public Vector3fListener getListener()
	{
		return listener;
	}

	/**
	 * @param listener listener que ser� chamado quando so valores do vetor forem alterados.
	 */

	public void setListener(Vector3fListener listener)
	{
		this.listener = listener;
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("x", x);
		description.append("y", y);
		description.append("z", z);

		return description.toString();
	}
}
