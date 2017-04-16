package com.erakin.engine.input;

import org.diverproject.jni.input.KeyEvent;

/**
 * <h1>Leitura de Tecla Digitada</h1>
 *
 * <p>Objetos com essa interface podem ser adicionadas ao teclado para desenvolvedor.
 * Teclas digitadas possuem um evento que identifica a tecla como também o caracter.
 * Além disso são chamadas de tempo em tempo em quanto a tecla estiver pressionada.</p>
 *
 * @see KeyEvent
 *
 * @author Andrew
 */

public interface KeyTypedListener
{
	/**
	 * Chamado sempre que uma tecla estiver sendo digitada, em quanto pressionado.
	 * @param event objeto contendo as informações do evento detectado no teclado.
	 */

	void keyTyped(KeyEvent event);
}
