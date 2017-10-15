package com.erakin.engine.resource.shader;

import com.erakin.api.lwjgl.GLBind;

/**
 * <h1>Programa��o Shader</h1>
 *
 * <p>Essa programa��o permite que durante renderiza��es, sejam feitas computa��es gr�ficas.
 * Essas computa��es gr�ficas podem adicionar cores, texturas, ilumina��es ou outros efeitos.
 * Cada tipo de efeito dever� ser programado, e de acordo com a forma os resultados mudam.</p>
 *
 * <p>Al�m de ser uma Programa��o Shader dever� tamb�m utilizas as funcionalidades de Objeto de Vincula��o.
 * Garantindo que este seja poss�vel ser utilizado na renderiza��o (bind), cancelar o uso da computa��o
 * gr�fica durante a renderiza��o (unbind) e liberar quando parar de usar (release)</p>
 *
 * @see GLBind
 *
 * @author Andrew Mello
 */

public interface Shader extends GLBind
{
	/**
	 * Vincula um determinada vari�vel a um determinado atributo de entrada no shader.
	 * @param attribute c�digo de identifica��o do atributo que ser� vinculado.
	 * @param variable nome da vari�vel que ir� assumir o atributo acima no glsl.
	 */

	void bindAttribute(int attribute, String variable);

	/**
	 * Obt�m o �ndice de uma vari�vel uniforme associado ao nome com a programa��o shader feita.
	 * @param name nome do qual foi dado a vari�vel, n�o pode ser utilizado com pr�-fixo <b>gl_</b>.
	 * @return �ndice da vari�vel no nome acima ou -1 se n�o encontrar ou for reservada ao programa.
	 */

	int getUniformLocation(String name);
}
