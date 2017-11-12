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
 * <p>Textura s�o imagens carregadas em disco para o engine e armazenadas no OpenGL.
 * Objetos desse tipo ir�o permitir que durante o desenvolvimento estas possam ser usadas.
 * Apesar do OpenGL oferecer essa possibilidade, o engine utiliza uma forma melhor.</p>
 *
 * <p>Essa forma melhor pode ser vista como a exist�ncia da documenta��o no seu uso,
 * e obviamente o acesso mais r�pido e compreens�vel das funcionalidades da textura.</p>
 *
 * <p>Al�m disso � um recurso fict�cio, por tanto n�o possui informa��es diretas.
 * Apesar de n�o ser direto (ra�z), possui as funcionalidades que permitem obter as
 * informa��es diretas dessa ra�z como o tamanho da imagem, caminho desta e outros.</p>
 *
 * @see Resource
 *
 * @author Andrew
 */

public class Texture extends Resource<TextureRoot> implements ResourceFileLocation
{
	/**
	 * N�mero de separa��o que h� na textura (multi-textura).
	 */
	private int split;

	/**
	 * N�vel de dispers�o do brilho na textura.
	 */
	private float shineDamper;

	/**
	 * Intensidade da refletividade da luz na textura.
	 */
	private float reflectivity;

	/**
	 * Textura com transpar�ncia.
	 */
	private boolean useTransparency;

	/**
	 * TODO
	 */
	private boolean useFakeLighting;

	/**
	 * Constr�i uma nova textura a partir de uma textura ra�z especifica.
	 * @param root textura ra�z que ser� usada para criar a textura.
	 */

	Texture(TextureRoot root)
	{
		super(root);
	}

	/**
	 * Toda textura � formada por uma imagem do qual podemos dimensionar.
	 * @return aquisi��o do tamanho da largura da imagem em pixels.
	 */

	public int getWidth()
	{
		return root == null ? 0 : root.width;
	}

	/**
	 * Toda textura � formada por uma imagem do qual podemos dimensionar.
	 * @return aquisi��o do tamanho da altura da imagem em pixels.
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
	 * Seleciona essa unidade de textura ser� afetada pelos efeitos de texturas chamados na sequ�ncia.
	 * O n�mero de unidades de textura que uma implementa��o suporte depende da sua implementa��o, mas deve ser menor que 80.
	 * @param index qual unidade de textura ser� ativada por esta textura, a textura deve ser um GL_TEXTURE{i},
	 * onde <b>i</b> varia entre zero e o valor de <code>GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS</code> menos um. O valor inicial � GL_TEXTURE0.
	 */

	public void active(int index)
	{
		if (IntUtil.interval(index, 0, 31))
			glActiveTexture(GL_TEXTURE0 + index);
	}

	/**
	 * Uma textura dividida determina que a imagem da textura possui na verdade mais de uma textura dispon�vel.
	 * @return aquisi��o de qual parte da textura ser� usada para representar essa textura.
	 */

	public int getSplit()
	{
		return split;
	}

	/**
	 * Uma textura dividida determina que a imagem da textura possui na verdade mais de uma textura dispon�vel.
	 * @param split qual parte da textura ser� usada para representar essa textura.
	 */

	public void setSplit(int split)
	{
		this.split = split;
	}

	/**
	 * A dispers�o do brilho determina o qu�o pr�ximo da linha de refletividade do brilho a c�mera precisa estar.
	 * Quanto menor mais perto da reta deve estar e quanto maior mais distante da reta deve estar.
	 * @return aquisi��o do n�vel de dispers�o do brilho emitido pela luz refletida na textura.
	 */

	public float getShineDamper()
	{
		return shineDamper;
	}

	/**
	 * A dispers�o do brilho determina o qu�o pr�ximo da linha de refletividade do brilho a c�mera precisa estar.
	 * Quanto menor mais perto da reta deve estar e quanto maior mais distante da reta deve estar.
	 * @param shineDamper n�vel de dispers�o do brilho emitido pela luz refletida na textura.
	 */

	public void setShineDamper(float shineDamper)
	{
		this.shineDamper = shineDamper;
	}

	/**
	 * A refletividade determina qu�o a intensidade do brilho emitido devido a refletividade de luzes na textura.
	 * Quanto menor mais fraco ser� o brilho emitido e quanto maior mais forte ser� o brilho emitido.
	 * @return aquisi��o do n�vel de intensidade do brilho emitido da luz refletida na textura.
	 */

	public float getReflectivity()
	{
		return reflectivity;
	}

	/**
	 * A refletividade determina qu�o a intensidade do brilho emitido devido a refletividade de luzes na textura.
	 * Quanto menor mais fraco ser� o brilho emitido e quanto maior mais forte ser� o brilho emitido.
	 * @param reflectivity n�vel de intensidade do brilho emitido da luz refletida na textura.
	 */

	public void setReflectivity(float reflectivity)
	{
		this.reflectivity = reflectivity;
	}

	/**
	 * Uma textura que possui transpar�ncia deve ser especificado anteriormente no carregamento da textura, caso contr�rio n�o funciona.
	 * A transpar�ncia ir� considerar pixels com tonalidade alpha transparentes de acordo com o valor especificado no mesmo.
	 * @return true se a textura deve utilizar o recurso de transpar�ncia.
	 */

	public boolean isUseTransparency()
	{
		return useTransparency;
	}

	/**
	 * Uma textura que possui transpar�ncia deve ser especificado anteriormente no carregamento da textura, caso contr�rio n�o funciona.
	 * A transpar�ncia ir� considerar pixels com tonalidade alpha transparentes de acordo com o valor especificado no mesmo.
	 * @param use true para habilitar o recurso de transpar�ncia ou false caso contr�rio.
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
