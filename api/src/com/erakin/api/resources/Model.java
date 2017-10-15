package com.erakin.api.resources;

import org.diverproject.util.FileUtil;
import org.diverproject.util.ObjectDescription;

import com.erakin.api.lwjgl.VAO;
import com.erakin.api.lwjgl.math.enumeration.DrawElement;

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

public class Model extends Resource
{
	/**
	 * C�digo de atribuir um VBO de v�rtices no espa�o para um modelo.
	 */
	public static final int ATTRIB_VERTICES = 0;

	/**
	 * C�digo de atribuir um VBO de coordenada de textura para um modelo.
	 */
	public static final int ATTRIB_TEXTURE_COORDS = 1;

	/**
	 * C�digo de atribuir um VBO de normaliza��o para um modelo.
	 */
	public static final int ATTRIB_NORMALS = 2;

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

	/**
	 * Uma textura possui informa��es sobre uma imagem, usado para colorir o modelo.
	 * A textura internamente ser� usada durante a computa��o gr�fica (shader).
	 * @return aquisi��o da textura que est� sendo utilizada pela modelagem.
	 */

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
		return !valid() ? 0 : ((ModelRoot) root).vao.getVertexCount();
	}

	@Override
	public int getID()
	{
		VAO vao = ((ModelRoot) root).vao;

		return vao == null ? 0 : vao.getID();
	}

	@Override
	public void bind()
	{
		if (!valid())
			return;

		ModelRoot model = ((ModelRoot) root);

		VAO vao = model.vao;
		vao.bind();
		vao.enable(ATTRIB_VERTICES);
		vao.enable(ATTRIB_TEXTURE_COORDS);
		vao.enable(ATTRIB_NORMALS);
	}

	@Override
	public void unbind()
	{
		if (!valid())
			return;

		ModelRoot model = ((ModelRoot) root);

		VAO vao = model.vao;
		vao.disable(ATTRIB_VERTICES);
		vao.disable(ATTRIB_TEXTURE_COORDS);
		vao.disable(ATTRIB_NORMALS);
		vao.unbind();
	}

	/**
	 * Ao ser chamado ir� acessar o VAO respectivo a essa modelagem e desenhar esta.
	 * O resultado do desenhado varia de acordo com o tipo de computa��o gr�fica usado.
	 * @param mode em que modo dever� ser feito o desenho dos v�rtices da modelagem.
	 * @see DrawElement
	 */

	public void draw(DrawElement mode)
	{
		ModelRoot model = ((ModelRoot) root);

		VAO vao = model.vao;
		vao.draw(mode);
	}

	/**
	 * Refletividade indica o quanto a luz ser� refletida quando atingir o objeto em quest�o (modelagem).
	 * Esse reflexo varia ainda tamb�m com as vari�veis da luz como dist�ncia e sua intensidade (for�a).
	 * @return aquisi��o do n�vel da for�a para refletir ilumina��es atrav�s do brilho.
	 */

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
		this.reflectivity = ((ModelRoot) root).defaultReflectivity;
	}

	/**
	 * Redu��o do brilho � usado na computa��o gr�fica para reduzir o efeito da luz ao atingir um objeto.
	 * Essa redu��o possui duas variantes, a dist�ncia da dire��o do reflexo em rela��o com a c�mera,
	 * e o n�vel de redu��o do brilho, quanto maior ambos os valores, menor ser� o brilho refletido visto.
	 * @return aquisi��o do n�vel da redu��o do brilho para ilumina��es refletidas.
	 */

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
		this.reflectivity = ((ModelRoot) root).defaultShineDamping;
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
		return root != null && root.isAlive() && ((ModelRoot) root).vao != null;
	}

	@Override
	public void toString(ObjectDescription description)
	{
		description.append("model", FileUtil.getFileName(root.filePath));

		if (texture != null)
			description.append("texture", FileUtil.getFileName(texture.root.filePath));

		description.append("reflectivity", reflectivity);
		description.append("shineDamping", shineDamping);
	}
}