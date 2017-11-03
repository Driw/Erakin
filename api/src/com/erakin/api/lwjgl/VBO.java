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
 * Um VAO det�m v�rios dados relacionados para uma cole��o de v�rtices.
 * Esses dados s�o armazenados em buffer e gerenciados pelo atual VAO.
 * O VAO pode armazenar mais do que um tipo de dado em seus atributos.
 *
 * VBO ir� usar um buffer alocado pelo OpenGL para armazenar dados.
 * Esses buffer ter� os dados alocados atrav�s do VAO conforme a necessidade.
 * Podendo ainda ser liberado da mem�ria como vinculado para uso ou desvincular.
 *
 * <p><code>ARRAY_BUFFER</code>: � usado para definir dados no vetor de v�rtices
 * para que possam utilizar <code>glVertexAttribPointer()</code>, op��o que
 * dever� vir a ser usado mais frequentemente do qualquer outra.</p>
 *
 * <p><code>ELEMENT_ARRAY_BUFFER</code>: � usado para definir conte�do como �ndices,
 * s�o usados por comandos de desenha por �ndice como <code>glDrawElements()</code>.
 *
 * @author Andrew
 */

public class VBO implements GLBind
{
	/**
	 * C�digo que ir� determinar que o VBO ser� dados atribu�dos.
	 */
	public static final byte ARRAY_BUFFER = 0;

	/**
	 * C�digo que ir� determinar que o VBO ser� dados indexados.
	 */
	public static final byte ELEMENT_ARRAY_BUFFER = 1;

	/**
	 * C�digo GL respectivo aos c�digos dos modos de 
	 */
	private static final int OPENGL_MODE[] = new int[]
	{
		GL_ARRAY_BUFFER,
		GL_ELEMENT_ARRAY_BUFFER,
	};

	/**
	 * Nome descritivo respectivos aos c�digos dos modos de 
	 */
	private static final String OPENGL_MODE_STRING[] = new String[]
	{
		"ARRAY_BUFFER",
		"ELEMENT_ARRAY_BUFFER",
	};

	/**
	 * Identifica��o do VBO no sistema OpenGL.
	 */
	private int id;

	/**
	 * Qual o modo utilizado por esse 
	 */
	private final byte mode;

	/**
	 * Constr�i um novo VBO a partir das informa��es abaixo.
	 * @param id identifica��o do VBO no sistema do OpenGL.
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
	 * C�digo de identifica��o do modo que esse VBO foi armazenado.
	 * @return segue abaixo os modos que podem ser obtidos:<br>
	 * <code>GL_ARRAY_BUFFER</code>: .<br>
	 * <code>GL_ELEMENT_ARRAY_BUFFER</code>: .<br>
	 */

	public int getMode()
	{
		return OPENGL_MODE[mode];
	}

	/**
	 * Esse procedimento ir� definir quais ser�o os dados armazenados por esse VBO.
	 * @param buffer refer�ncia do buffer que cont�m os dados a compor o VBO.
	 */

	void bufferData(IntBuffer buffer)
	{
		glBufferData(getMode(), buffer, GL_STATIC_DRAW);
	}

	/**
	 * Esse procedimento ir� definir quais ser�o os dados armazenados por esse VBO.
	 * @param buffer refer�ncia do buffer que cont�m os dados a compor o VBO.
	 */

	void bufferData(FloatBuffer buffer)
	{
		glBufferData(getMode(), buffer, GL_STATIC_DRAW);
	}

	/**
	 * Especifica onde o valor dos dados para cada atributo do v�rtice com um �ndice.
	 * Nesse caso ser� considerado o tipo de dados como n�meros inteiros, 4 bytes.
	 * @param index qual ser� o atributo do VAO em que esse VBO ser� atribu�do.
	 * @param size quantos elementos cada �ndice do VAO respectivo deve possuir.
	 */

	void attribPointerInt(int index, int size)
	{
		glVertexAttribPointer(index, size, GL_INT, false, 0, 0);
	}

	/**
	 * Especifica onde o valor dos dados para cada atributo do v�rtice com um �ndice.
	 * Nesse caso ser� considerado o tipo de dados como n�meros flutuantes, 4 bytes.
	 * @param index qual ser� o atributo do VAO em que esse VBO ser� atribu�do.
	 * @param size quantos elementos cada �ndice do VAO respectivo deve possuir.
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
