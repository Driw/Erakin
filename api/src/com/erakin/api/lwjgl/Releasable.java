package com.erakin.api.lwjgl;

/**
 * <h1>Liberar</h1>
 *
 * <p>Objetos que implementem essa interface devem ser permitidos liberar.
 * Essa liberação irá variar de acordo o tipo de objeto que irá especificar.
 * De modo geral, a liberação é feito removendo dados internos do objeto,
 * forçando uma liberação de memória "mais rápida" para tal.</p>
 *
 * <p>Em relação ao OpenGL, também haverá uma liberação nos dados do mesmo.
 * Essa liberação só irá ocorrer de fato se houver necessidade pelo objeto.
 * Caso o objeto não possua relação direta com o OpenGL não haverá o mesmo.</p>
 *
 * @author Andrew
 */

public interface Releasable
{
	/**
	 * Quando chamado deverá fazer a liberação dos dados desse objeto.
	 * Essa liberação deve funcionar tanto para os dados internos se
	 * necessário, quanto para informações associadas ao OpenGL.
	 */

	void release();
}
