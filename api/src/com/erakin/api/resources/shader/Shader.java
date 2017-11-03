package com.erakin.api.resources.shader;

import static org.lwjgl.opengl.GL20.glIsProgram;
import static org.lwjgl.opengl.GL20.glIsShader;
import static org.lwjgl.opengl.GL20.glUseProgram;

import org.diverproject.util.ObjectDescription;

import com.erakin.api.resources.Resource;
import com.erakin.api.resources.ResourceFileLocation;

/**
 * <h1>Computação Gráfica</h1>
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
	 * Código de identificação para desativar o uso de computação gráfica.
	 */
	public static final int NO_SHADER = 0;

	/**
	 * Constrói uma nova computação gráfica a partir de uma computação gráfica raíz especifica.
	 * @param root computação gráfica raíz que será usada para criar a computação gráfica.
	 */

	Shader(ShaderRoot root)
	{
		super(root);
	}

	/**
	 * O programa de vértices é responsável por posicionar o vértice no espaço e outros cálculos.
	 * Como por exemplo em caso de gráficos com brilhos a intensidade do brilho é calculado aqui.
	 * @return aquisição do código de identificação do programa de vértices.
	 */

	public int getVertexID()
	{
		return root == null ? 0 : root.vertex;
	}

	/**
	 * O programa de fragmentos é responsável por realizar a texturização dos vértices.
	 * Responsável por processar cada pixel que será renderizado de um objeto na tela.
	 * @return aquisição do código de identificação do programa de fragmentos.
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
