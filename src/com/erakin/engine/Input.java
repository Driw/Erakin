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

	/**
	 * Verifica se uma ou mais teclas especificadas foram clicadas recentemente (configur�vel).
	 * @param key c�digo de todas as teclas do qual deseja verificar se foram clicadas.
	 * @return true se todas as teclas foram clicadas ou false se uma ou mais n�o foram.
	 */

	boolean wasClicked(int... key);

	/**
	 * Verifica se uma ou mais teclas especificadas est�o sendo pressionadas no momento.
	 * @param key c�digo de todas as teclas do qual deseja verificar se foram pressionadas.
	 * @return true se todas as teclas est�o pressionadas ou false se ao menos uma n�o estiver.
	 */

	boolean isPressed(int... key);

	/**
	 * Permite adicionar um listener completo para receber eventos do teclado detectados.
	 * @param listener refer�ncia do listener pare receber todos os tipos de eventos.
	 */

	void addListener(KeyListener listener);

	/**
	 * Permite remove rum listener especificado da lista de eventos de to teclado detectados.
	 * @param listener refer�ncia do listener que recebe todos os tipos de eventos.
	 */

	void removeListener(KeyListener listener);

	/**
	 * Permite adicionar um listener completo para receber eventos do teclado detectados.
	 * @param listener refer�ncia do listener pare receber os eventos do tipo digita��o.
	 */

	void addTypedListener(KeyTypedListener listener);

	/**
	 * Permite remove rum listener especificado da lista de eventos de to teclado detectados.
	 * @param listener refer�ncia do listener que recebe os eventos do tipo digita��o.
	 */

	void removeTypedListener(KeyTypedListener listener);

	/**
	 * Permite adicionar um listener completo para receber eventos do teclado detectados.
	 * @param listener refer�ncia do listener pare receber os eventos do tipo pressionado.
	 */

	void addPressedListener(KeyPressedListener listener);

	/**
	 * Permite remove rum listener especificado da lista de eventos de to teclado detectados.
	 * @param listener refer�ncia do listener que receberos eventos do tipo pressionado.
	 */

	void removePressedListener(KeyPressedListener listener);

	/**
	 * Permite adicionar um listener completo para receber eventos do teclado detectados.
	 * @param listener refer�ncia do listener pare receber os eventos do tipo liberado.
	 */

	void addReleasedListener(KeyReleasedListener listener);

	/**
	 * Permite remove rum listener especificado da lista de eventos de to teclado detectados.
	 * @param listener refer�ncia do listener que recebe os eventos do tipo liberado.
	 */

	void removeReleasedListener(KeyReleasedListener listener);
}
