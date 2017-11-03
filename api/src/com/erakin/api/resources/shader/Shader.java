package com.erakin.api.resources.shader;

import static org.lwjgl.opengl.GL20.glIsProgram;
import static org.lwjgl.opengl.GL20.glIsShader;
import static org.lwjgl.opengl.GL20.glUseProgram;

import org.diverproject.util.ObjectDescription;

import com.erakin.api.resources.Resource;
import com.erakin.api.resources.ResourceFileLocation;

/**
 * <h1>Computa��o Gr�fica</h1>
 *
 * <p></p>
 *
 * @see Resource
 * @see ShaderRender
 *
 * @author Andrew
 */

public class Shader extends Resource<ShaderRoot> implements ResourceFileLocation
{
	/**
	 * C�digo de identifica��o para desativar o uso de computa��o gr�fica.
	 */
	public static final int NO_SHADER = 0;

	/**
	 * Constr�i uma nova computa��o gr�fica a partir de uma computa��o gr�fica ra�z especifica.
	 * @param root computa��o gr�fica ra�z que ser� usada para criar a computa��o gr�fica.
	 */

	Shader(ShaderRoot root)
	{
		super(root);
	}

	/**
	 * O programa de v�rtices � respons�vel por posicionar o v�rtice no espa�o e outros c�lculos.
	 * Como por exemplo em caso de gr�ficos com brilhos a intensidade do brilho � calculado aqui.
	 * @return aquisi��o do c�digo de identifica��o do programa de v�rtices.
	 */

	public int getVertexID()
	{
		return root == null ? 0 : root.vertex;
	}

	/**
	 * O programa de fragmentos � respons�vel por realizar a texturiza��o dos v�rtices.
	 * Respons�vel por processar cada pixel que ser� renderizado de um objeto na tela.
	 * @return aquisi��o do c�digo de identifica��o do programa de fragmentos.
	 */

	public int getFragmentID()
	{
		return root == null ? 0 : root.fragment;
	}

	@Override
	public int getID()
	{
		return root == null ? 0 : root.id;
	}

	@Override
	public void bind()
	{
		glUseProgram(getID());		
	}

	@Override
	public void unbind()
	{
		glUseProgram(NO_SHADER);
	}

	@Override
	public void release()
	{
		super.release();

		root.delReference(this);
	}

	@Override
	public boolean valid()
	{
		return	glIsShader(getVertexID()) &&
				glIsShader(getFragmentID()) &&
				glIsProgram(getID());
	}

	@Override
	public String getFileExtension()
	{
		return root.getFileExtension();
	}

	@Override
	public String getFileName()
	{
		return root.getFileExtension();
	}

	@Override
	public String getFileFullName()
	{
		return root.getFileFullName();
	}

	@Override
	public String getFileDirectory()
	{
		return root.getFileDirectory();
	}

	@Override
	public String getFilePath()
	{
		return root.getFilePath();
	}

	@Override
	protected Shader clone()
	{
		return root.genResource();
	}

	@Override
	public void toString(ObjectDescription description)
	{
		description.append("glID", getID());
		description.append("vertex", getVertexID());
		description.append("fragment", getFragmentID());
	}
}
