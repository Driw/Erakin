package com.erakin.api.input;

import org.diverproject.jni.input.KeyEvent;

/**
 * <h1>Leitura de Tecla Liberada</h1>
 *
 * <p>Objetos com essa interface podem ser adicionadas ao teclado para desenvolvedor.
 * Teclas liberadas são chamada uma única vez e somente depois que parar de pressioná-la.
 * Para esta será vinculada apenas a identificação da tecla porém sem caracter no mesmo.</p>
 *
 * @see KeyEvent
 *
 * @author Andrew
 */

public interface KeyReleasedListener
{
	/**
	 * Chamado sempre que uma tecla parar de ser pressionado, apenas uma vez.
	 * @param event objeto contendo as informações do evento detectado no teclado.
	 */

	void keyReleased(KeyEvent event);
}
