package com.erakin.api.lwjgl;

/**
 * <h1>Liberar</h1>
 *
 * <p>Objetos que implementem essa interface devem ser permitidos liberar.
 * Essa libera��o ir� variar de acordo o tipo de objeto que ir� especificar.
 * De modo geral, a libera��o � feito removendo dados internos do objeto,
 * for�ando uma libera��o de mem�ria "mais r�pida" para tal.</p>
 *
 * <p>Em rela��o ao OpenGL, tamb�m haver� uma libera��o nos dados do mesmo.
 * Essa libera��o s� ir� ocorrer de fato se houver necessidade pelo objeto.
 * Caso o objeto n�o possua rela��o direta com o OpenGL n�o haver� o mesmo.</p>
 *
 * @author Andrew
 */

public interface Releasable
{
	/**
	 * Quando chamado dever� fazer a libera��o dos dados desse objeto.
	 * Essa libera��o deve funcionar tanto para os dados internos se
	 * necess�rio, quanto para informa��es associadas ao OpenGL.
	 */

	void release();
}
