package com.erakin.engine;

import org.lwjgl.util.vector.Matrix4f;

/**
 * Listener para Proje��o
 *
 * Usado pela Matriz de Proje��o permitir que outros objetos sejam atualizados junto com a matriz.
 * Por exemplo, no caso de um shader que use a matriz de proje��o, quando ela for alterada o shader
 * n�o ficar� sabendo dessa mudan�a, sendo necess�rio o uso de um ouvinte para quando esse ocorrer.
 *
 * Internamente a Matriz de Proje��o (classe) ir� usar uma lista para armazenar esses listeners.
 * Sempre que for solicitado ao mesmo para atualizar sua matriz de acordo com os seus atributos,
 * ir� percorrer essa lista chamando todos os listeners avisando a mudan�a com <code>changeProjection</code>.
 *
 * @see ProjectionMatrix
 *
 * @author Andrew Mello
 */

public interface ProjectionListener
{
	/**
	 * Chamado sempre que a Matriz de Proje��o detectar uma solicita��o para atualizar o mesmo.
	 * @param newProjectionMatrix matriz contendo os novos dados para a proje��o feita.
	 */

	void changeProjection(Matrix4f newProjectionMatrix);
}
