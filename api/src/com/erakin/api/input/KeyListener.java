package com.erakin.api.input;

import org.diverproject.jni.input.KeyEvent;

/**
 * <h1>Leitura de Tecla</h1>
 *
 * <p>Objetos com essa interface podem ser adicionadas ao teclado para desenvolvedor.
 * Quando o teclado receber eventos de teclas ir� repassar esse atrav�s dos m�todos
 * implementados por essa interface, cada tipo de evento em um �nico m�todo.</p>
 *
 * <p>Uma observa��o que dever� ser feita em rela��o aos eventos desencadeados.
 * Sempre que um evento de pressionar for detectado um de digitar tamb�m ser�.
 * Apenas os eventos de digitar ter�o um caracter vinculado ao mesmo.</p>
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
