package com.erakin.api.resources.model;

import com.erakin.api.lwjgl.VAO;
import com.erakin.api.resources.shader.Shader;

/**
 * <h1>Atributo para Modelo</h1>
 *
 * <p>Um atributo do modelo cont�m informa��es necess�rias para que sua renderiza��o seja feita corretamente.
 * Por exemplo, a posi��o dos v�rtices representa um atributo em quanto as coordenadas de textura outro.
 * Para definir um atributo primeiro � necess�rio especificar a posi��o do atributo e tamanho da informa��o.</p>
 *
 * @see VAO
 *
 * @author Andrew
 */

public interface ModelAttribute
{
	/**
	 * �ndice que define um atributo para identifica��o do posicionamento dos v�rtices.
	 */
	public static final int ATTRIB_VERTEX = 0;

	/**
	 * �ndice que define um atributo para identifica��o das coordenadas de texturas.
	 */
	public static final int ATTRIB_UV_TEXTURE = 1;

	/**
	 * �ndice que define um atributo para identifica��o da normaliza��o.
	 */
	public static final int ATTRIB_NORMAL = 2;

	/**
	 * �ndice que define um atributo para identifica��o de textura por v�rtice.
	 */
	public static final int ATTRIB_TEXTURES = 3;


	/**
	 * Atrav�s do �ndice um {@link Shader} sabe qual atributo � atribu�do a cada vari�vel de entrada no programa de v�rtices.
	 * Para cada modelo � poss�vel definir at� 16 atributos diferentes do qual cada um s� deve armazenar um tipo de informa��o.
	 * @return aquisi��o do �ndice que identifica 
	 */

	int getIndex();

	/**
	 * O comprimento do atributo determina quantos dados s�o necess�rios para completar as informa��es do atributo.
	 * Esse comprimento est� relacionado a quantidade de v�rtices do modelo, mas n�o necessariamente ao seu total.
	 * @return aquisi��o da quantidade de dados armazenados no atributo.
	 */

	int length();

	/**
	 * O tamanho do atributo determina como o {@link Shader} ir� receber o atributo como entrada no programa de v�rtices.
	 * Quando o tamanho do atributo for um ser� um valor primitivo, dois define um vetor de dois e assim em diante.
	 * @return aquisi��o da quantidade da valores necess�rio para formar cada dado respectivo ao v�rtice.
	 */

	int size();

	/**
	 * Procedimento usado por {@link ModelLoader} para salvar os valores desse atributo dentro de um {@link VAO}.
	 * Assim, cada tipo de atributo poder� especificar o tipo do atributo dinamicamente e quanto valores distintos.
	 * @param vao intermedi�rio para referenciar um objeto de vetor de v�rtices do qual ser� atribu�do os valores.
	 */

	void storeInVAO(VAO vao);

	/**
	 * Procedimento chamado para liberar as informa��es tempor�rias de dados do modelo.
	 * Neste ponto o modelo j� foi carregado no OpenGL, portanto os dados n�o s�o mais necess�rios.
	 */

	void release();
}
