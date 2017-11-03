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
 * <p>São estruturas em espaço carregadas em disco para o engine e armazenadas no OpenGL.
 * Objetos desse tipo irão permitir que durante o desenvolvimento estas possam ser usadas.
 * Apesar do OpenGL oferecer essa possibilidade, o engine utiliza uma forma melhor.</p>
 *
 * <p>Essa forma melhor pode ser vista como a existência da documentação no seu uso,
 * e obviamente o acesso mais rápido e compreensível das funcionalidades do modelo.
 * Como um bônus permite determinar uma textura para ser usada junto ao modelo.</p>
 *
 * <p>Além disso é um recurso fictício, por tanto não possui informações diretas.
 * A utilização desse objeto permite uma utilização de estruturas espaciais (modelagens)
 * de forma muito mais simplória e rápida, reduzindo códigos e aumenta o entendimento.</p>
 *
 * @see Texture
 * @see Resource
 *
 * @author Andrew
 */

public class Model extends Resource<ModelRoot> implements ModelRender, ResourceFileLocation
{
	/**
	 * Textura que está sendo usada pelo modelo.
	 */
	private Texture texture;

	/**
	 * Nível da força para refletir iluminações.
	 */
	float reflectivity;

	/**
	 * Nível da redução para brilhos refletidos.
	 */
	float shineDamping;

	/**
	 * Constrói um novo modelo a partir de uma modelo raíz especifica.
	 * @param root modelo raíz que será usada para criar o modelo.
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
	 * Permite definir qual será a textura usada por esse modelo na renderização.
	 * Essa textura não pode ser usada por mais nenhum outro modelo ou objeto.
	 * Quando esse modelo for liberado, a textura também será, caso outro
	 * objeto esteja usando essa mesma textura fictícia será perdido também.
	 * @param texture referência da textura fictícia que será usada.
	 */

	public void setTexture(Texture texture)
	{
		this.texture = texture;
	}

	/**
	 * Contagem de vértices determina quantos índices existem no VAO desse modelo.
	 * @return aquisição do número de vértices nessa modelagem.
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
	 * Aumentar a refletividade significa que as luzes que atingir a modelagem serão refletidas mais fortes.
	 * Quanto mais forte for o reflexo mais intenso a modelagem será sobreposta pela cor da luz refletida.
	 * Não será aceito valores negativos, caso seja passado um, será automaticamente ignorado pelo método.
	 * @param reflectivity novo nível da força para refletir iluminações através do brilho.
	 */

	public void setReflectivity(float reflectivity)
	{
		if (reflectivity >= 0)
			this.reflectivity = reflectivity;
	}

	/**
	 * Uma modelagem utiliza o valor de refletividade padrão de acordo com a modelagem raíz referente.
	 * Quando esse método for chamado, irá pedir a raíz o valor padrão de refletividade e usá-lo.
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
	 * Aumentar a redução do brilho significa que será mais difícil notar o brilho obtido do reflexo da luz.
	 * Não será aceito valores negativos, caso seja passado um, será automaticamente ignorado pelo método.
	 * @param shineDamping novo nível da força para redução do brilho para iluminações refletidas.
	 */

	public void setShineDamping(float shineDamping)
	{
		if (shineDamping >= 0)
			this.shineDamping = shineDamping;
	}

	/**
	 * Uma modelagem utiliza o valor de redução do brilho padrão de acordo com a modelagem raíz referente.
	 * Quando esse método for chamado, irá pedir a raíz o valor padrão de redução do brilho e usá-lo.
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
