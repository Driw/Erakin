package com.erakin.api.resources.shader;

/**
 * <h1>Dados para Computa��o Gr�fica</h1>
 *
 * <p>Interface que ir� implementar as informa��es b�sicas que toda computa��o gr�fica possui.
 * Esses dados ir�o auxiliar na cria��o dos programas que realizam a computa��o gr�fica no jogo.
 * A �nica informa��es necess�ria para tal � uma String contendo o c�digo da programa��o.</p>
 *
 * <p>Outra finalidade dessa interface � permitir a exist�ncia de outras semelhantes.
 * De modo que essas determinem novas funcionalidades de acordo com o seu prop�sito.</p>
 *
 * @author Andrew
 */

public interface ShaderData
{
	/**
	 * O programa determina sequ�ncia de comandos para realiza��o da computa��o gr�fica.
	 * @return aquisi��o do programa para computa��o gr�fica.
	 */

	StringBuilder getVertexProgram();

	/**
	 * O programa determina sequ�ncia de comandos para realiza��o da computa��o gr�fica.
	 * @return aquisi��o do programa para computa��o gr�fica.
	 */

	StringBuilder getFragmentProgram();
}
