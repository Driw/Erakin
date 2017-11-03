package com.erakin.api.resources.model;

import static com.erakin.api.lwjgl.APIGLUtil.releasedMemory;

import java.nio.IntBuffer;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.SizeUtil;
import org.lwjgl.BufferUtils;

import com.erakin.api.lwjgl.VAO;

/**
 * <h1>Atributo Inteiro para Modelo</h1>
 *
 * <p>Um atributo permite armazenar um tipo especifico de dado que quando juntos correspondem a uma informação de um modelo.
 * Para esse caso o atributo é para guardar informações que correspondam a valores numéricos inteiros.</p>
 *
 * @see ModelAttribute
 *
 * @author Andrew
 */

public class ModelIntAttribute implements ModelAttribute
{
	/**
	 * Índice para alocação do atributo no {@link VAO}.
	 */
	private int attribute;

	/**
	 * Quantidade de valores que serão alocados para cada vértice.
	 */
	private int size;

	/**
	 * Quantidade de vértices existentes para vincular os valores.
	 */
	private int length;

	/**
	 * Buffer interno para armazenamento dos números inteiros.
	 */
	protected IntBuffer buffer;

	/**
	 * Cria uma nova instância de um objeto que armazena os dados do atributo em um buffer.
	 * Inicializa o buffer de números inteiros conforme os parâmetros abaixo:
	 * @param attribute identificação do atributo para ser vinculado ao {@link VAO}
	 * @param size quantidade de números flutuantes para formar os dados de um vértice.
	 * @param length quantidade de conjunto de dados necessários para especificar o atributo.
	 */

	public ModelIntAttribute(int attribute, int size, int length)
	{
		this.attribute = attribute;
		this.size = size;
		this.length = length;
		this.buffer = BufferUtils.createIntBuffer(size * length);
	}

	/**
	 * Calcula quantos bytes são necessários para que esse atributo esteja disponível em memória.
	 * @return aquisição do espaço em memória ocupado para definir os valores do buffer do atributo.
	 */

	public int sizeof()
	{
		return buffer.capacity() * Float.BYTES;
	}

	/**
	 * Define valores para serem alocados no buffer interno como dados do atributo.
	 * @param index índice do vértice do qual os valores serão alocados no buffer.
	 * @param values valores numéricos inteiros que serão alocados ao índice especificado.
	 */

	public void setValue(int index, int... values)
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
		vao.setAttribute(getIndex(), size(), buffer);
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
