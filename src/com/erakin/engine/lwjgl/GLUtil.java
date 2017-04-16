package com.erakin.engine.lwjgl;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MAX_TEXTURE_SIZE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

/**
 * <h1>Utilit�rios para OpenGL</h1>
 *
 * <p>Composto apenas de procedimentos est�ticos e n�o permite a cria��o de inst�ncias para tal.
 * Funciona como um utilit�rio contendo procedimentos para facilitar o desenvolvimento da aplica��o.
 * Todos os procedimentos aqui contidos s�o referentes apenas a utiliza��o da biblioteca LWJGL.</p>
 *
 * <p>Durante o desenvolvimento � recomend�vel utilizar os procedimentos como importa��o est�tica.
 * Os procedimentos aqui contidos n�o possuem nomes repetidos em rela��o a biblioteca LWJGL.
 * Al�m de facilitar a codifica��o possui alguns procedimentos extras usados pelo engine.</p>
 *
 * @author Andrew Mello
 */

public class GLUtil
{
	/**
	 * Construtor privado para evitar inst�ncias dessa classe.
	 * N�o h� necessidade de existir inst�ncias para tal.
	 */

	private GLUtil()
	{
		
	}

	/**
	 * Limpa um determinado buffer que � especificado internamente como:<br>
	 * <code>GL_COLOR_BUFFER_BIT</code>: Color Buffer.<br>
	 * <code>GL_DEPTH_BUFFER_BIT</code>: Depth Buffer.<br>
	 */

	public static void glClearScreen()
	{
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0, 0, 0, 1);
	}

	/**
	 * TODO
	 */

	public static void glDisableCulling()
	{
		glDisable(GL_CULL_FACE);
	}

	/**
	 * TODO
	 */

	public static void glEnableCulling()
	{
	    glEnable(GL_CULL_FACE);
	    glCullFace(GL_BACK);
	}

	/**
	 * No OpenGL as texturas possuem um tamanho limite que � aceito pela biblioteca.
	 * @return aquisi��o do tamanho m�ximo de largura/altura para texturas.
	 */

	public static int glMaxTextureSize()
	{
		IntBuffer buffer = BufferUtils.createIntBuffer(16);
		GL11.glGetInteger(GL_MAX_TEXTURE_SIZE, buffer);

		return buffer.get(0);
	}

	/**
	 * Armazena um determinado vetor de n�meros float em um buffer para OpenGL.
	 * @param data vetor contendo os valores float que devem ser armazenados.
	 * @return buffer gerado contendo os dados float do vetor passado.
	 */

	public static FloatBuffer glStore(float[] data)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();

		return buffer;
	}

	/**
	 * Armazena um determinado vetor de n�meros int em um buffer para OpenGL.
	 * @param data vetor contendo os valores int que devem ser armazenados.
	 * @return buffer gerado contendo os dados int do vetor passado.
	 */

	public static IntBuffer glStore(int[] data)
	{
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();

		return buffer;
	}
}
