package com.erakin.api.resources.shader;

import com.erakin.api.lwjgl.GLBind;

/**
 * <h1>Computa��o Gr�fica para Renderizar</h1>
 *
 * <p>Essa interface define que h� uma programa��o para realizar as computa��es gr�ficas na renderiza��o.
 * Essas computa��es gr�ficas podem adicionar cores, texturas, ilumina��es ou outros efeitos visuais.
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

public interface ShaderRender extends GLBind
{
	/**
	 * Deve garantir que todos os atributos usado sejam vinculados.
	 */

	void bindAttributes();

	/**
	 * Chamado para obter a localiza��o de todas vari�veis uniformes usadas no programa.
	 * Essas vari�veis s�o respectivas apenas aquelas que podem ser definidas dinamicamente.
	 */

	void getAllUniformLocation();
}
