package com.erakin.api.resources.model;

import static com.erakin.api.lwjgl.APIGLUtil.releasedMemory;

import java.nio.FloatBuffer;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.SizeUtil;
import org.lwjgl.BufferUtils;

import com.erakin.api.lwjgl.VAO;

/**
 * <h1>Atributo Flutuante para Modelo</h1>
 *
 * <p>Um atributo permite armazenar um tipo especifico de dado que quando juntos correspondem a uma informa��o de um modelo.
 * Para esse caso o atributo � para guardar informa��es que correspondam a valores num�ricos flutuantes.</p>
 *
 * @see ModelAttribute
 *
 * @author Andrew
 */

public class ModelFloatAttribute implements ModelAttribute
{
	/**
	 * �ndice para aloca��o do atributo no {@link VAO}.
	 */
	private int attribute;

	/**
	 * Quantidade de valores que ser�o alocados para cada v�rtice.
	 */
	private int size;

	/**
	 * Quantidade de v�rtices existentes para vincular os valores.
	 */
	private int length;

	/**
	 * Buffer interno para armazenamento dos n�meros flutuantes.
	 */
	private FloatBuffer buffer;

	/**
	 * Cria uma nova inst�ncia de um objeto que armazena os dados do atributo em um buffer.
	 * Inicializa o buffer de n�meros flutuantes conforme os par�metros abaixo:
	 * @param attribute identifica��o do atributo para ser vinculado ao {@link VAO}
	 * @param size quantidade de n�meros flutuantes para formar os dados de um v�rtice.
	 * @param length quantidade de conjunto de dados necess�rios para especificar o atributo.
	 */

	public ModelFloatAttribute(int attribute, int size, int length)
	{
		this.attribute = attribute;
		this.size = size;
		this.length = length;
		this.buffer = BufferUtils.createFloatBuffer(size * length);
	}

	/**
	 * Calcula quantos bytes s�o necess�rios para que esse atributo esteja dispon�vel em mem�ria.
	 * @return aquisi��o do espa�o em mem�ria ocupado para definir os valores do buffer do atributo.
	 */

	public int sizeof()
	{
		return buffer.capacity() * Float.BYTES;
	}

	/**
	 * Define valores para serem alocados no buffer interno como dados do atributo.
	 * @param index �ndice do v�rtice do qual os valores ser�o alocados no buffer.
	 * @param values valores num�ricos flutuantes que ser�o alocados ao �ndice especificado.
	 */

	public void setValue(int index, float... values)
	{
		for (int i = 0; i < values.length; i++)
			buffer.put((index * size) + i, values[i]);
	}

	@Override
	public int getIndex()
	{
		return attribute;
	}

	@Override
	public int length()
	{
		return length;
	}

	@Override
	public int size()
	{
		return size;
	}

	@Override
	public void storeInVAO(VAO vao)
	{
		vao.bind();
		vao.setAttribute(getIndex(), size(), buffer);
		vao.unbind();
	}

	@Override
	public void release()
	{
		buffer = null;

		releasedMemory(Integer.BYTES * size * length);
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("attribute", attribute);
		description.append("size", size);
		description.append("length", length);
		description.append("sizeof", SizeUtil.toString(sizeof()));

		return description.toString();
	}
}
