package com.erakin.engine;

import org.lwjgl.util.vector.Matrix4f;

/**
 * Listener para Projeção
 *
 * Usado pela Matriz de Projeção permitir que outros objetos sejam atualizados junto com a matriz.
 * Por exemplo, no caso de um shader que use a matriz de projeção, quando ela for alterada o shader
 * não ficará sabendo dessa mudança, sendo necessário o uso de um ouvinte para quando esse ocorrer.
 *
 * Internamente a Matriz de Projeção (classe) irá usar uma lista para armazenar esses listeners.
 * Sempre que for solicitado ao mesmo para atualizar sua matriz de acordo com os seus atributos,
 * irá percorrer essa lista chamando todos os listeners avisando a mudança com <code>changeProjection</code>.
 *
 * @see ProjectionMatrix
 *
 * @author Andrew Mello
 */

public interface ProjectionListener
{
	/**
	 * Chamado sempre que a Matriz de Projeção detectar uma solicitação para atualizar o mesmo.
	 * @param newProjectionMatrix matriz contendo os novos dados para a projeção feita.
	 */

	void changeProjection(Matrix4f newProjectionMatrix);
}
