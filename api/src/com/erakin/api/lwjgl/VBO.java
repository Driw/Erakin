package com.erakin.api.lwjgl;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL15.glIsBuffer;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.diverproject.util.ObjectDescription;

/**
 * Vertex-Buffer Object
 *
 * Um VAO detém vários dados relacionados para uma coleção de vértices.
 * Esses dados são armazenados em buffer e gerenciados pelo atual VAO.
 * O VAO pode armazenar mais do que um tipo de dado em seus atributos.
 *
 * VBO irá usar um buffer alocado pelo OpenGL para armazenar dados.
 * Esses buffer terá os dados alocados através do VAO conforme a necessidade.
 * Podendo ainda ser liberado da memória como vinculado para uso ou desvincular.
 *
 * <p><code>ARRAY_BUFFER</code>: é usado para definir dados no vetor de vértices
 * para que possam utilizar <code>glVertexAttribPointer()</code>, opção que
 * deverá vir a ser usado mais frequentemente do qualquer outra.</p>
 *
 * <p><code>ELEMENT_ARRAY_BUFFER</code>: é usado para definir conteúdo como índices,
 * são usados por comandos de desenha por índice como <code>glDrawElements()</code>.
 *
 * @author Andrew
 */

public class VBO implements GLBind
{
	/**
	 * Código que irá determinar que o VBO será dados atribuídos.
	 */
	public static final byte ARRAY_BUFFER = 0;

	/**
	 * Código que irá determinar que o VBO será dados indexados.
	 */
	public static final byte ELEMENT_ARRAY_BUFFER = 1;

	/**
	 * Código GL respectivo aos códigos dos modos de 
	 */
	private static final int OPENGL_MODE[] = new int[]
	{
		GL_ARRAY_BUFFER,
		GL_ELEMENT_ARRAY_BUFFER,
	};

	/**
	 * Nome descritivo respectivos aos códigos dos modos de 
	 */
	private static final String OPENGL_MODE_STRING[] = new String[]
	{
		"ARRAY_BUFFER",
		"ELEMENT_ARRAY_BUFFER",
	};

	/**
	 * Identificação do VBO no sistema OpenGL.
	 */
	private int id;

	/**
	 * Qual o modo utilizado por esse 
	 */
	private final byte mode;

	/**
	 * Constrói um novo VBO a partir das informações abaixo.
	 * @param id identificação do VBO no sistema do OpenGL.
	 * @param mode qual o modo desse VBO: <code>OPENGL_MODE</code>
	 */

	VBO(byte mode)
	{
		this.id = glGenBuffers();
		this.mode = mode;
	}

	@Override
	public int getID()
	{
		return id;
	}

	@Override
	public void bind()
	{
		glBindBuffer(getMode(), id);
	}

	@Override
	public void unbind()
	{
		glBindBuffer(getMode(), UNBIND_CODE);
	}

	@Override
	public boolean valid()
	{
		return glIsBuffer(id);
	}

	@Override
	public void release()
	{
		glDeleteBuffers(id);

		id = 0;
	}

	/**
	 * Código de identificação do modo que esse VBO foi armazenado.
	 * @return segue abaixo os modos que podem ser obtidos:<br>
	 * <code>GL_ARRAY_BUFFER</code>: .<br>
	 * <code>GL_ELEMENT_ARRAY_BUFFER</code>: .<br>
	 */

	public int getMode()
	{
		return OPENGL_MODE[mode];
	}

	/**
	 * Esse procedimento irá definir quais serão os dados armazenados por esse VBO.
	 * @param buffer referência do buffer que contém os dados a compor o VBO.
	 */

	void bufferData(IntBuffer buffer)
	{
		glBufferData(getMode(), buffer, GL_STATIC_DRAW);
	}

	/**
	 * Esse procedimento irá definir quais serão os dados armazenados por esse VBO.
	 * @param buffer referência do buffer que contém os dados a compor o VBO.
	 */

	void bufferData(FloatBuffer buffer)
	{
		glBufferData(getMode(), buffer, GL_STATIC_DRAW);
	}

	/**
	 * Especifica onde o valor dos dados para cada atributo do vértice com um índice.
	 * Nesse caso será considerado o tipo de dados como números inteiros, 4 bytes.
	 * @param index qual será o atributo do VAO em que esse VBO será atribuído.
	 * @param size quantos elementos cada índice do VAO respectivo deve possuir.
	 */

	void attribPointerInt(int index, int size)
	{
		glVertexAttribPointer(index, size, GL_INT, false, 0, 0);
	}

	/**
	 * Especifica onde o valor dos dados para cada atributo do vértice com um índice.
	 * Nesse caso será considerado o tipo de dados como números flutuantes, 4 bytes.
	 * @param index qual será o atributo do VAO em que esse VBO será atribuído.
	 * @param size quantos elementos cada índice do VAO respectivo deve possuir.
	 */

	void attribPointerFloat(int index, int size)
	{
		glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("id", id);
		description.append("mode", OPENGL_MODE_STRING[mode]);

		return description.toString();
	}
}
