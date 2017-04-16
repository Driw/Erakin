package com.erakin.engine.lwjgl;

/**
 * <p>Objeto de Vincula��o</p>
 *
 * <p>Interface usada para permitir as classes possu�rem procedimentos b�sicos.
 * Esses procedimentos s�o necess�rio para classes que usam partes do OpenGL
 * em rela��o a m�todos bind e unbind, como tamb�m uma libera��o de informa��es.</p>
 *
 * @author Andrew
 */

public interface GLBind extends Releasable
{
	/**
	 * C�digo para desvincular qualquer objeto OpenGL.
	 */
	public static final int UNBIND_CODE = 0;

	/**
	 * Identifica��o deve ser referente ao objeto no sistema OpenGL e n�o ao engine.
	 * Esse ID ser� usado para vincular, desvincular e liberar o objeto no OpenGL.
	 * @return aquisi��o para o c�digo de identifica��o do objeto no OpenGL.
	 */

	int getID();

	/**
	 * Deve fazer a vincula��o desse objeto com o OpenGL para ser utilizado.
	 * Uma vez que tenha sido vinculado, todas intera��es no OpenGL ser� com esse objeto.
	 * Funciona de forma diferente para cada objeto, de acordo com a identifica��o e tipo.
	 */

	void bind();

	/**
	 * Quando chamado deve fazer desfazer a vincula��o de qualquer objeto usado.
	 * Uma vez que tenha sido desvinculado, n�o ter� intera��o com o OpenGL.
	 * Funciona da mesma forma que qualquer outro objeto desse tipo.
	 */

	void unbind();

	/**
	 * Ir� conversar com o OpenGL para verificar se esse objeto ainda � v�lido.
	 * Se inv�lido, significa que j� foi liberado da mem�ria e n�o � mais usado.
	 * @return true se for v�lido ou false caso contr�rio.
	 */

	boolean valid();
}
