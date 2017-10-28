package com.erakin.api.resources.shader;

import com.erakin.api.lwjgl.GLBind;

/**
 * <h1>Computação Gráfica para Renderizar</h1>
 *
 * <p>Essa interface define que há uma programação para realizar as computações gráficas na renderização.
 * Essas computações gráficas podem adicionar cores, texturas, iluminações ou outros efeitos visuais.
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

public interface ShaderRender extends GLBind
{
	/**
	 * Deve garantir que todos os atributos usado sejam vinculados.
	 */

	void bindAttributes();

	/**
	 * Chamado para obter a localização de todas variáveis uniformes usadas no programa.
	 * Essas variáveis são respectivas apenas aquelas que podem ser definidas dinamicamente.
	 */

	void getAllUniformLocation();
}
