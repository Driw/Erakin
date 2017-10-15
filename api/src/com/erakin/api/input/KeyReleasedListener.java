package com.erakin.api.input;

import org.diverproject.jni.input.KeyEvent;

/**
 * <h1>Leitura de Tecla Liberada</h1>
 *
 * <p>Objetos com essa interface podem ser adicionadas ao teclado para desenvolvedor.
 * Teclas liberadas s�o chamada uma �nica vez e somente depois que parar de pression�-la.
 * Para esta ser� vinculada apenas a identifica��o da tecla por�m sem caracter no mesmo.</p>
 *
 * @see KeyEvent
 *
 * @author Andrew
 */

public interface KeyReleasedListener
{
	/**
	 * Chamado sempre que uma tecla parar de ser pressionado, apenas uma vez.
	 * @param event objeto contendo as informa��es do evento detectado no teclado.
	 */

	void keyReleased(KeyEvent event);
}
