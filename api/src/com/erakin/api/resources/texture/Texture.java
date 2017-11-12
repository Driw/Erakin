package com.erakin.api.resources.texture;

import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.lang.IntUtil;

import com.erakin.api.resources.Resource;
import com.erakin.api.resources.ResourceFileLocation;

/**
 * <h1>Textura</h1>
 *
 * <p>Textura são imagens carregadas em disco para o engine e armazenadas no OpenGL.
 * Objetos desse tipo irão permitir que durante o desenvolvimento estas possam ser usadas.
 * Apesar do OpenGL oferecer essa possibilidade, o engine utiliza uma forma melhor.</p>
 *
 * <p>Essa forma melhor pode ser vista como a existência da documentação no seu uso,
 * e obviamente o acesso mais rápido e compreensível das funcionalidades da textura.</p>
 *
 * <p>Além disso é um recurso fictício, por tanto não possui informações diretas.
 * Apesar de não ser direto (raíz), possui as funcionalidades que permitem obter as
 * informações diretas dessa raíz como o tamanho da imagem, caminho desta e outros.</p>
 *
 * @see Resource
 *
 * @author Andrew
 */

public class Texture extends Resource<TextureRoot> implements ResourceFileLocation
{
	/**
	 * Número de separação que há na textura (multi-textura).
	 */
	private int split;

	/**
	 * Nível de dispersão do brilho na textura.
	 */
	private float shineDamper;

	/**
	 * Intensidade da refletividade da luz na textura.
	 */
	private float reflectivity;

	/**
	 * Textura com transparência.
	 */
	private boolean useTransparency;

	/**
	 * TODO
	 */
	private boolean useFakeLighting;

	/**
	 * Constrói uma nova textura a partir de uma textura raíz especifica.
	 * @param root textura raíz que será usada para criar a textura.
	 */

	Texture(TextureRoot root)
	{
		super(root);
	}

	/**
	 * Toda textura é formada por uma imagem do qual podemos dimensionar.
	 * @return aquisição do tamanho da largura da imagem em pixels.
	 */

	public int getWidth()
	{
		return root == null ? 0 : root.width;
	}

	/**
	 * Toda textura é formada por uma imagem do qual podemos dimensionar.
	 * @return aquisição do tamanho da altura da imagem em pixels.
	 */

	public int getHeight()
	{
		return root == null ? 0 : root.height;
	}

	@Override
	public int getID()
	{
		return root == null ? 0 : root.id;
	}

	@Override
	public void bind()
	{
		glBindTexture(root.target.GL_CODE, getID());
	}

	@Override
	public void unbind()
	{
		glBindTexture(root.target.GL_CODE, 0);
	}

	@Override
	public boolean valid()
	{
		return getID() != 0;
	}

	@Override
	public void release()
	{
		super.release();

		root.delReference(this);
	}

	/**
	 * Seleciona essa unidade de textura será afetada pelos efeitos de texturas chamados na sequência.
	 * O número de unidades de textura que uma implementação suporte depende da sua implementação, mas deve ser menor que 80.
	 * @param index qual unidade de textura será ativada por esta textura, a textura deve ser um GL_TEXTURE{i},
	 * onde <b>i</b> varia entre zero e o valor de <code>GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS</code> menos um. O valor inicial é GL_TEXTURE0.
	 */

	public void active(int index)
	{
		if (IntUtil.interval(index, 0, 31))
			glActiveTexture(GL_TEXTURE0 + index);
	}

	/**
	 * Uma textura dividida determina que a imagem da textura possui na verdade mais de uma textura disponível.
	 * @return aquisição de qual parte da textura será usada para representar essa textura.
	 */

	public int getSplit()
	{
		return split;
	}

	/**
	 * Uma textura dividida determina que a imagem da textura possui na verdade mais de uma textura disponível.
	 * @param split qual parte da textura será usada para representar essa textura.
	 */

	public void setSplit(int split)
	{
		this.split = split;
	}

	/**
	 * A dispersão do brilho determina o quão próximo da linha de refletividade do brilho a câmera precisa estar.
	 * Quanto menor mais perto da reta deve estar e quanto maior mais distante da reta deve estar.
	 * @return aquisição do nível de dispersão do brilho emitido pela luz refletida na textura.
	 */

	public float getShineDamper()
	{
		return shineDamper;
	}

	/**
	 * A dispersão do brilho determina o quão próximo da linha de refletividade do brilho a câmera precisa estar.
	 * Quanto menor mais perto da reta deve estar e quanto maior mais distante da reta deve estar.
	 * @param shineDamper nível de dispersão do brilho emitido pela luz refletida na textura.
	 */

	public void setShineDamper(float shineDamper)
	{
		this.shineDamper = shineDamper;
	}

	/**
	 * A refletividade determina quão a intensidade do brilho emitido devido a refletividade de luzes na textura.
	 * Quanto menor mais fraco será o brilho emitido e quanto maior mais forte será o brilho emitido.
	 * @return aquisição do nível de intensidade do brilho emitido da luz refletida na textura.
	 */

	public float getReflectivity()
	{
		return reflectivity;
	}

	/**
	 * A refletividade determina quão a intensidade do brilho emitido devido a refletividade de luzes na textura.
	 * Quanto menor mais fraco será o brilho emitido e quanto maior mais forte será o brilho emitido.
	 * @param reflectivity nível de intensidade do brilho emitido da luz refletida na textura.
	 */

	public void setReflectivity(float reflectivity)
	{
		this.reflectivity = reflectivity;
	}

	/**
	 * Uma textura que possui transparência deve ser especificado anteriormente no carregamento da textura, caso contrário não funciona.
	 * A transparência irá considerar pixels com tonalidade alpha transparentes de acordo com o valor especificado no mesmo.
	 * @return true se a textura deve utilizar o recurso de transparência.
	 */

	public boolean isUseTransparency()
	{
		return useTransparency;
	}

	/**
	 * Uma textura que possui transparência deve ser especificado anteriormente no carregamento da textura, caso contrário não funciona.
	 * A transparência irá considerar pixels com tonalidade alpha transparentes de acordo com o valor especificado no mesmo.
	 * @param use true para habilitar o recurso de transparência ou false caso contrário.
	 */

	public void setTransparency(boolean use)
	{
		this.useTransparency = use;
	}

	/**
	 * TODO ???
	 * @return ???
	 */

	public boolean isUseFakeLighting()
	{
		return useFakeLighting;
	}

	/**
	 * TODO ???
	 * @param use ???
	 */

	public void setFakeLighting(boolean use)
	{
		this.useFakeLighting = use;
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
	public Texture clone()
	{
		return root.genResource();
	}

	@Override
	public void toString(ObjectDescription description)
	{
		description.append("split", split);
		description.append("shineDamper", shineDamper);
		description.append("reflectivity", reflectivity);
		description.append("hasTransparency", useTransparency);
		description.append("useFakeLighting", useFakeLighting);
	}
}
