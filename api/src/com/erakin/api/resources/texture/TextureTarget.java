package com.erakin.api.resources.texture;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_1D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_3D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_1D_ARRAY;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;
import static org.lwjgl.opengl.GL31.GL_TEXTURE_BUFFER;
import static org.lwjgl.opengl.GL31.GL_TEXTURE_RECTANGLE;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE_ARRAY;
import static org.lwjgl.opengl.GL40.GL_TEXTURE_CUBE_MAP_ARRAY;

/**
 * <h1>Textura Alvo</h1>
 *
 * <p>Quando queremos trabalhar uma textura no OpenGL devemos definir o alvo que por sua vez define a forma como a textura pode ser trabalhada.
 * Uma textura 2D funciona de forma diferente de uma textura 3D ou então uma textura cúbica, assim sendo toda textura precisa ser tipada.</p>
 *
 * @author Andrew
 */

public enum TextureTarget
{
	/**
	 * Textura do tipo unidimensional.
	 */
	TT_1D(GL_TEXTURE_1D),

	/**
	 * Textura do tipo bidimensional.
	 */
	TT_2D(GL_TEXTURE_2D),

	/**
	 * Textura do tipo tridimensional.
	 */
	TT_3D(GL_TEXTURE_3D),
	/**
	 * Vetor de textura do tipo unidimensional.
	 */
	TT_1D_ARRAY(GL_TEXTURE_1D_ARRAY),

	/**
	 * Vetor de textura do tipo bidimensional.
	 */
	TT_2D_ARRAY(GL_TEXTURE_2D_ARRAY),

	/**
	 * Textura do tipo retangular.
	 */
	TT_RECTANGLE(GL_TEXTURE_RECTANGLE),

	/**
	 * Textura do tipo cúbica.
	 */
	TT_CUBE_MAP(GL_TEXTURE_CUBE_MAP),

	/**
	 * Vetor de textura do tipo cúbica.
	 */
	TT_CUBE_MAP_ARRAY(GL_TEXTURE_CUBE_MAP_ARRAY),

	/**
	 * Textura do tipo em buffer.
	 */
	TT_BUFFER(GL_TEXTURE_BUFFER),

	/**
	 * Textura do tipo bidimensional (Multi Sample).
	 */
	TT_2D_MULTISAMPLE(GL_TEXTURE_2D_MULTISAMPLE),

	/**
	 * Vetor de textura do tipo bidimensional (Multi Sample).
	 */
	TT_2D_MULTISAMPLE_ARRAY(GL_TEXTURE_2D_MULTISAMPLE_ARRAY);

	/**
	 * Código de identificação do tipo de textura no OpenGL.
	 */
	public final int GL_CODE;

	/**
	 * Cria um novo alvo de textura para determinar os tipos de texturas disponíveis no sistema.
	 * @param glCode código de identificação do tipo de textura no OpenGL.
	 */

	private TextureTarget(int glCode)
	{
		GL_CODE = glCode;
	}
}
