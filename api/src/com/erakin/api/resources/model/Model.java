package com.erakin.api.resources.model;

import org.diverproject.util.FileUtil;
import org.diverproject.util.ObjectDescription;

import com.erakin.api.lwjgl.VAO;
import com.erakin.api.lwjgl.math.enumeration.DrawElement;
import com.erakin.api.render.ModelRender;
import com.erakin.api.resources.Resource;
import com.erakin.api.resources.ResourceFileLocation;
import com.erakin.api.resources.texture.Texture;

/**
 * <h1>Modelo</h1>
 *
 * <p>S�o estruturas em espa�o carregadas em disco para o engine e armazenadas no OpenGL.
 * Objetos desse tipo ir�o permitir que durante o desenvolvimento estas possam ser usadas.
 * Apesar do OpenGL oferecer essa possibilidade, o engine utiliza uma forma melhor.</p>
 *
 * <p>Essa forma melhor pode ser vista como a exist�ncia da documenta��o no seu uso,
 * e obviamente o acesso mais r�pido e compreens�vel das funcionalidades do modelo.
 * Como um b�nus permite determinar uma textura para ser usada junto ao modelo.</p>
 *
 * <p>Al�m disso � um recurso fict�cio, por tanto n�o possui informa��es diretas.
 * A utiliza��o desse objeto permite uma utiliza��o de estruturas espaciais (modelagens)
 * de forma muito mais simpl�ria e r�pida, reduzindo c�digos e aumenta o entendimento.</p>
 *
 * @see Texture
 * @see Resource
 *
 * @author Andrew
 */

public class Model extends Resource<ModelRoot> implements ModelRender, ResourceFileLocation
{
	/**
	 * Textura que est� sendo usada pelo modelo.
	 */
	private Texture texture;

	/**
	 * N�vel da for�a para refletir ilumina��es.
	 */
	float reflectivity;

	/**
	 * N�vel da redu��o para brilhos refletidos.
	 */
	float shineDamping;

	/**
	 * Constr�i um novo modelo a partir de uma modelo ra�z especifica.
	 * @param root modelo ra�z que ser� usada para criar o modelo.
	 */

	Model(ModelRoot root)
	{
		super(root);

		this.reflectivity = root.defaultReflectivity;
		this.shineDamping = root.defaultShineDamping;
	}

	@Override
	public Texture getTexture()
	{
		return texture;
	}

	/**
	 * Permite definir qual ser� a textura usada por esse modelo na renderiza��o.
	 * Essa textura n�o pode ser usada por mais nenhum outro modelo ou objeto.
	 * Quando esse modelo for liberado, a textura tamb�m ser�, caso outro
	 * objeto esteja usando essa mesma textura fict�cia ser� perdido tamb�m.
	 * @param texture refer�ncia da textura fict�cia que ser� usada.
	 */

	public void setTexture(Texture texture)
	{
		this.texture = texture;
	}

	/**
	 * Contagem de v�rtices determina quantos �ndices existem no VAO desse modelo.
	 * @return aquisi��o do n�mero de v�rtices nessa modelagem.
	 */

	public int getVertexCount()
	{
		return !valid() ? 0 : root.vao.getVertexCount();
	}

	@Override
	public int getID()
	{
		VAO vao = root.vao;

		return vao == null ? 0 : vao.getID();
	}

	@Override
	public void bind()
	{
		if (!valid())
			return;

		root.vao.bind();

		for (int attribute : root.attributes)
			root.vao.enable(attribute);
	}

	@Override
	public void unbind()
	{
		if (!valid())
			return;

		for (int attribute : root.attributes)
			root.vao.disable(attribute);

		root.vao.unbind();
	}

	@Override
	public void draw(DrawElement mode)
	{
		root.vao.draw(mode);
	}

	@Override
	public float getReflectivity()
	{
		return reflectivity;
	}

	/**
	 * Aumentar a refletividade significa que as luzes que atingir a modelagem ser�o refletidas mais fortes.
	 * Quanto mais forte for o reflexo mais intenso a modelagem ser� sobreposta pela cor da luz refletida.
	 * N�o ser� aceito valores negativos, caso seja passado um, ser� automaticamente ignorado pelo m�todo.
	 * @param reflectivity novo n�vel da for�a para refletir ilumina��es atrav�s do brilho.
	 */

	public void setReflectivity(float reflectivity)
	{
		if (reflectivity >= 0)
			this.reflectivity = reflectivity;
	}

	/**
	 * Uma modelagem utiliza o valor de refletividade padr�o de acordo com a modelagem ra�z referente.
	 * Quando esse m�todo for chamado, ir� pedir a ra�z o valor padr�o de refletividade e us�-lo.
	 */

	public void restoreReflectivity()
	{
		this.reflectivity = root.defaultReflectivity;
	}

	@Override
	public float getShineDamping()
	{
		return shineDamping;
	}

	/**
	 * Aumentar a redu��o do brilho significa que ser� mais dif�cil notar o brilho obtido do reflexo da luz.
	 * N�o ser� aceito valores negativos, caso seja passado um, ser� automaticamente ignorado pelo m�todo.
	 * @param shineDamping novo n�vel da for�a para redu��o do brilho para ilumina��es refletidas.
	 */

	public void setShineDamping(float shineDamping)
	{
		if (shineDamping >= 0)
			this.shineDamping = shineDamping;
	}

	/**
	 * Uma modelagem utiliza o valor de redu��o do brilho padr�o de acordo com a modelagem ra�z referente.
	 * Quando esse m�todo for chamado, ir� pedir a ra�z o valor padr�o de redu��o do brilho e us�-lo.
	 */

	public void restoreShineDamping()
	{
		this.reflectivity = root.defaultShineDamping;
	}

	@Override
	public void release()
	{
		super.release();

		if (texture != null)
		{
			texture.release();
			texture = null;
		}
	}

	@Override
	public boolean valid()
	{
		return root != null && root.isAlive() && root.vao != null;
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
	protected Model clone()
	{
		return root.genResource();
	}

	@Override
	public void toString(ObjectDescription description)
	{
		description.append("model", FileUtil.getFileName(root.getFilePath()));

		if (texture != null)
			description.append("texture", FileUtil.getFileName(texture.getFilePath()));

		description.append("reflectivity", reflectivity);
		description.append("shineDamping", shineDamping);
	}
}
