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
 * <p>Classe para centralizar as instâncias dos serviços para entrada de informações/ações na Engine.
 * Assim será possível que um input seja visto de forma diferente para a engine e desenvolvedor.
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
	 * Referência da instância do teclado virtual que será considerado.
	 */
	static final VirtualKeyboard VIRTUAL_KEYBOARD = new VirtualKeyboard();

	/**
	 * Despachante para Teclado é usado internamente no núcleo da engine para usar como serviço.
	 * Esse serviço é usado através da biblioteca JNI Input e precisa ser especificado.
	 * Sua visibilidade é protegida de forma que apenas a Engine possa saber de sua existência.
	 * @return aquisição do teclado virtual da engine com visão para teclado despachante.
	 */

	static KeyboardDispatcher getDispatcher()
	{
		return VIRTUAL_KEYBOARD;
	}

	/**
	 * A forma de visualização das funcionalidades do teclado para o desenvolvimento do jogo,
	 * é o teclado virtual que permite adicionar escutas específicas ou obter detalhes do teclado.
	 * Os detalhes são referente a estados específicos de determinadas teclas ou ações.
	 * @return aquisição do teclado virtual da engine com visão para usar o teclado.
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
