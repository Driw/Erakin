package com.erakin.api.resources.model;

import com.erakin.api.lwjgl.VAO;
import com.erakin.api.resources.shader.Shader;

/**
 * <h1>Atributo para Modelo</h1>
 *
 * <p>Um atributo do modelo contém informações necessárias para que sua renderização seja feita corretamente.
 * Por exemplo, a posição dos vértices representa um atributo em quanto as coordenadas de textura outro.
 * Para definir um atributo primeiro é necessário especificar a posição do atributo e tamanho da informação.</p>
 *
 * @see VAO
 *
 * @author Andrew
 */

public interface ModelAttribute
{
	/**
	 * Índice que define um atributo para identificação do posicionamento dos vértices.
	 */
	public static final int ATTRIB_VERTEX = 0;

	/**
	 * Índice que define um atributo para identificação das coordenadas de texturas.
	 */
	public static final int ATTRIB_UV_TEXTURE = 1;

	/**
	 * Índice que define um atributo para identificação da normalização.
	 */
	public static final int ATTRIB_NORMAL = 2;

	/**
	 * Índice que define um atributo para identificação de textura por vértice.
	 */
	public static final int ATTRIB_TEXTURES = 3;


	/**
	 * Através do índice um {@link Shader} sabe qual atributo é atribuído a cada variável de entrada no programa de vértices.
	 * Para cada modelo é possível definir até 16 atributos diferentes do qual cada um só deve armazenar um tipo de informação.
	 * @return aquisição do índice que identifica 
	 */

	int getIndex();

	/**
	 * O comprimento do atributo determina quantos dados são necessários para completar as informações do atributo.
	 * Esse comprimento está relacionado a quantidade de vértices do modelo, mas não necessariamente ao seu total.
	 * @return aquisição da quantidade de dados armazenados no atributo.
	 */

	int length();

	/**
	 * O tamanho do atributo determina como o {@link Shader} irá receber o atributo como entrada no programa de vértices.
	 * Quando o tamanho do atributo for um será um valor primitivo, dois define um vetor de dois e assim em diante.
	 * @return aquisição da quantidade da valores necessário para formar cada dado respectivo ao vértice.
	 */

	int size();

	/**
	 * Procedimento usado por {@link ModelLoader} para salvar os valores desse atributo dentro de um {@link VAO}.
	 * Assim, cada tipo de atributo poderá especificar o tipo do atributo dinamicamente e quanto valores distintos.
	 * @param vao intermediário para referenciar um objeto de vetor de vértices do qual será atribuído os valores.
	 */

	void storeInVAO(VAO vao);

	/**
	 * Procedimento chamado para liberar as informações temporárias de dados do modelo.
	 * Neste ponto o modelo já foi carregado no OpenGL, portanto os dados não são mais necessários.
	 */

	void release();
}
