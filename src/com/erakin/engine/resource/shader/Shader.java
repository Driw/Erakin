package com.erakin.engine.resource.shader;

import com.erakin.api.lwjgl.GLBind;

/**
 * <h1>Programação Shader</h1>
 *
 * <p>Essa programação permite que durante renderizações, sejam feitas computações gráficas.
 * Essas computações gráficas podem adicionar cores, texturas, iluminações ou outros efeitos.
 * Cada tipo de efeito deverá ser programado, e de acordo com a forma os resultados mudam.</p>
 *
 * <p>Além de ser uma Programação Shader deverá também utilizas as funcionalidades de Objeto de Vinculação.
 * Garantindo que este seja possível ser utilizado na renderização (bind), cancelar o uso da computação
 * gráfica durante a renderização (unbind) e liberar quando parar de usar (release)</p>
 *
 * @see GLBind
 *
 * @author Andrew Mello
 */

public interface Shader extends GLBind
{
	/**
	 * Vincula um determinada variável a um determinado atributo de entrada no shader.
	 * @param attribute código de identificação do atributo que será vinculado.
	 * @param variable nome da variável que irá assumir o atributo acima no glsl.
	 */

	void bindAttribute(int attribute, String variable);

	/**
	 * Obtém o índice de uma variável uniforme associado ao nome com a programação shader feita.
	 * @param name nome do qual foi dado a variável, não pode ser utilizado com pré-fixo <b>gl_</b>.
	 * @return índice da variável no nome acima ou -1 se não encontrar ou for reservada ao programa.
	 */

	int getUniformLocation(String name);
}
