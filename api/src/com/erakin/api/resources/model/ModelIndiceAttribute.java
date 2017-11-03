package com.erakin.api.resources.model;

import static com.erakin.api.lwjgl.APIGLUtil.releasedMemory;

import java.nio.IntBuffer;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.SizeUtil;
import org.lwjgl.BufferUtils;

import com.erakin.api.lwjgl.VAO;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * <h1>Atributo de Índice para Modelo</h1>
 *
 * <p>Um atributo de índice é usado para melhorar a performance de processamento dos dados de um modelo na renderização.
 * Através dos índices os vértices serão conectados, já que cada índice corresponde a um offset de dados de vértices em atributos.
 * Apesar de ser um atributo inteiro para um Modelo, os índices são especificados de outra forma, portanto é atribuído diferentemente.</p>
 *
 * <p>De forma convencional os vértices precisam ser especificados e normalmente eles acabam sendo duplicados devido a ligações.
 * Através dos índices, um mesmo índice pode ser duplicado (offset do vértice) porém os dados do vértice em si não são duplicados.
 * Afinal com o offset do vértice o OpenGL percorre o atributo necessário e consegue localizar os dados daquele vértice.</p>
 *
 * @see ModelAttribute
 *
 * @author Andrew
 */

public class ModelIndiceAttribute implements ModelAttribute
{
	/**
	 * Quantidade de valores que serão alocados para cada vértice.
	 */
	private int count;

	/**
	 * Buffer interno para armazenamento dos números inteiros.
	 */
	protected IntBuffer buffer;

	/**
	 * Cria uma nova instância de um objeto que armazena os dados do atributo em um buffer.
	 * Inicializa o buffer de números inteiros conforme os parâmetros abaixo:
	 * @param count quantidade de números inteiros para formar os dados de um vértice.
	 */

	public ModelIndiceAttribute(int count)
	{
		this.count = count;
		this.buffer = BufferUtils.createIntBuffer(count);
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
	 * @param values valor numérico inteiro que será alocado ao índice especificado.
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
