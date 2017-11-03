package com.erakin.api.resources.model;

import static com.erakin.api.lwjgl.APIGLUtil.releasedMemory;

import java.nio.IntBuffer;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.SizeUtil;
import org.lwjgl.BufferUtils;

import com.erakin.api.lwjgl.VAO;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * <h1>Atributo de �ndice para Modelo</h1>
 *
 * <p>Um atributo de �ndice � usado para melhorar a performance de processamento dos dados de um modelo na renderiza��o.
 * Atrav�s dos �ndices os v�rtices ser�o conectados, j� que cada �ndice corresponde a um offset de dados de v�rtices em atributos.
 * Apesar de ser um atributo inteiro para um Modelo, os �ndices s�o especificados de outra forma, portanto � atribu�do diferentemente.</p>
 *
 * <p>De forma convencional os v�rtices precisam ser especificados e normalmente eles acabam sendo duplicados devido a liga��es.
 * Atrav�s dos �ndices, um mesmo �ndice pode ser duplicado (offset do v�rtice) por�m os dados do v�rtice em si n�o s�o duplicados.
 * Afinal com o offset do v�rtice o OpenGL percorre o atributo necess�rio e consegue localizar os dados daquele v�rtice.</p>
 *
 * @see ModelAttribute
 *
 * @author Andrew
 */

public class ModelIndiceAttribute implements ModelAttribute
{
	/**
	 * Quantidade de valores que ser�o alocados para cada v�rtice.
	 */
	private int count;

	/**
	 * Buffer interno para armazenamento dos n�meros inteiros.
	 */
	protected IntBuffer buffer;

	/**
	 * Cria uma nova inst�ncia de um objeto que armazena os dados do atributo em um buffer.
	 * Inicializa o buffer de n�meros inteiros conforme os par�metros abaixo:
	 * @param count quantidade de n�meros inteiros para formar os dados de um v�rtice.
	 */

	public ModelIndiceAttribute(int count)
	{
		this.count = count;
		this.buffer = BufferUtils.createIntBuffer(count);
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
	 * @param values valor num�rico inteiro que ser� alocado ao �ndice especificado.
	 */

	public void setValue(int index, int value)
	{
		buffer.put(index, value);
	}

	@Override
	@Deprecated
	public int getIndex()
	{
		throw new NotImplementedException();
	}

	@Override
	public int length()
	{
		return count;
	}

	@Override
	public int size()
	{
		return 1;
	}

	@Override
	public void storeInVAO(VAO vao)
	{
		vao.setIndices(buffer);
	}

	@Override
	public void release()
	{
		buffer = null;

		releasedMemory(Integer.BYTES * count);
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("count", count);
		description.append("sizeof", SizeUtil.toString(sizeof()));

		return description.toString();
	}
}
