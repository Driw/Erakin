package com.erakin.engine;

import org.diverproject.jni.input.KeyboardDispatcher;

import com.erakin.engine.input.KeyListener;
import com.erakin.engine.input.KeyPressedListener;
import com.erakin.engine.input.KeyReleasedListener;
import com.erakin.engine.input.KeyTypedListener;
import com.erakin.engine.input.VirtualKeyboard;

/**
 * <h1>Entrada</h1>
 *
 * <p>Classe para centralizar as inst�ncias dos servi�os para entrada de informa��es/a��es na Engine.
 * Assim ser� poss�vel que um input seja visto de forma diferente para a engine e desenvolvedor.
 * Teclado podemos ver ele de duas formas: como despachante (engine) e teclado virtual (desenvolvedor).</p>
 *
 * @see Keyboard
 * @see KeyboardDispatcher
 * @see VirtualKeyboard
 *
 * @author Andrew
 */

public interface Input
{
	/**
	 * Refer�ncia da inst�ncia do teclado virtual que ser� considerado.
	 */
	static final VirtualKeyboard VIRTUAL_KEYBOARD = new VirtualKeyboard();

	/**
	 * Despachante para Teclado � usado internamente no n�cleo da engine para usar como servi�o.
	 * Esse servi�o � usado atrav�s da biblioteca JNI Input e precisa ser especificado.
	 * Sua visibilidade � protegida de forma que apenas a Engine possa saber de sua exist�ncia.
	 * @return aquisi��o do teclado virtual da engine com vis�o para teclado despachante.
	 */

	static KeyboardDispatcher getDispatcher()
	{
		return VIRTUAL_KEYBOARD;
	}

	/**
	 * A forma de visualiza��o das funcionalidades do teclado para o desenvolvimento do jogo,
	 * � o teclado virtual que permite adicionar escutas espec�ficas ou obter detalhes do teclado.
	 * Os detalhes s�o referente a estados espec�ficos de determinadas teclas ou a��es.
	 * @return aquisi��o do teclado virtual da engine com vis�o para usar o teclado.
	 */

	public static VirtualKeyboard getVirtualKeyboard()
	{
		return VIRTUAL_KEYBOARD;
	}

	boolean wasClicked(int... key);
	boolean isPressed(int... key);
	void addListener(KeyListener listener);
	void removeListener(KeyListener listener);
	void addTypedListener(KeyTypedListener listener);
	void removeTypedListener(KeyTypedListener listener);
	void addPressedListener(KeyPressedListener listener);
	void removePressedListener(KeyPressedListener listener);
	void addReleasedListener(KeyReleasedListener listener);
	void removeReleasedListener(KeyReleasedListener listener);
}
