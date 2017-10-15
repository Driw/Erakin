package com.erakin.api.input;

import org.diverproject.jni.input.KeyEvent;

/**
 * <h1>Leitura de Tecla Pressionada</h1>
 *
 * <p>Objetos com essa interface podem ser adicionadas ao teclado para desenvolvedor.
 * Teclas pressionadas possuem um evento que identifica a tecla porém não tem caracter.
 * Além disso são chamadas de tempo em tempo em quanto a tecla estiver pressionada.</p>
 *
 * @see KeyEvent
 *
 * @author Andrew
 */

public interface KeyPressedListener
{
	/**
	 * Chamado sempre que uma tecla estiver sendo pressionado, em quanto pressionado.
	 * @param event objeto contendo as informações do evento detectado no teclado.
	 */

	void keyPressed(KeyEvent event);
}
