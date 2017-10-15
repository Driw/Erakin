package com.erakin.api.input;

import org.diverproject.jni.input.KeyEvent;

/**
 * <h1>Leitura de Tecla</h1>
 *
 * <p>Objetos com essa interface podem ser adicionadas ao teclado para desenvolvedor.
 * Quando o teclado receber eventos de teclas irá repassar esse através dos métodos
 * implementados por essa interface, cada tipo de evento em um único método.</p>
 *
 * <p>Uma observação que deverá ser feita em relação aos eventos desencadeados.
 * Sempre que um evento de pressionar for detectado um de digitar também será.
 * Apenas os eventos de digitar terão um caracter vinculado ao mesmo.</p>
 *
 * @see KeyTypedListener
 * @see KeyPressedListener
 * @see KeyReleasedListener
 * @see KeyEvent
 *
 * @author Andrew
 */

public interface KeyListener extends KeyTypedListener, KeyPressedListener, KeyReleasedListener
{

}
