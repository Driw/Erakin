package com.erakin.engine.lwjgl;

/**
 * <p>Objeto de Vinculação</p>
 *
 * <p>Interface usada para permitir as classes possuírem procedimentos básicos.
 * Esses procedimentos são necessário para classes que usam partes do OpenGL
 * em relação a métodos bind e unbind, como também uma liberação de informações.</p>
 *
 * @author Andrew
 */

public interface GLBind extends Releasable
{
	/**
	 * Código para desvincular qualquer objeto OpenGL.
	 */
	public static final int UNBIND_CODE = 0;

	/**
	 * Identificação deve ser referente ao objeto no sistema OpenGL e não ao engine.
	 * Esse ID será usado para vincular, desvincular e liberar o objeto no OpenGL.
	 * @return aquisição para o código de identificação do objeto no OpenGL.
	 */

	int getID();

	/**
	 * Deve fazer a vinculação desse objeto com o OpenGL para ser utilizado.
	 * Uma vez que tenha sido vinculado, todas interações no OpenGL será com esse objeto.
	 * Funciona de forma diferente para cada objeto, de acordo com a identificação e tipo.
	 */

	void bind();

	/**
	 * Quando chamado deve fazer desfazer a vinculação de qualquer objeto usado.
	 * Uma vez que tenha sido desvinculado, não terá interação com o OpenGL.
	 * Funciona da mesma forma que qualquer outro objeto desse tipo.
	 */

	void unbind();

	/**
	 * Irá conversar com o OpenGL para verificar se esse objeto ainda é válido.
	 * Se inválido, significa que já foi liberado da memória e não é mais usado.
	 * @return true se for válido ou false caso contrário.
	 */

	boolean valid();
}
