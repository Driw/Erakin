package org.erakin.api.input;

import org.diverproject.jni.input.KeyEvent;
import org.diverproject.jni.input.KeyListener;

/**
 * <p><h1>Adaptador de Tecla</h1>
 *
 * <p>Classe usada para facilitar a codificação durante a utilização de um KeyListener.
 * Tendo como finalidade apenas a redução de código durante o desenvolvimento da aplicação.
 * Não deve ser usado como herança, para isso basta implementar KeyListener.</p>
 *
 * <p>As vezes será necessário usar um KeyListener diretamente no código ao invés de uma
 * classe que o possua, usando essa classe, basta sobrescrever apenas os métodos que
 * realmente serão necessários para aquela codificação em questão.</p>
 *
 * @see KeyListener
 * @see KeyTypedListener
 * @see KeyPressedListener
 * @see KeyReleasedListener
 * @see KeyEvent
 *
 * @author Andrew
 */

public final class KeyAdapter implements KeyListener
{
	@Override
	public void keyTyped(KeyEvent event)
	{
		
	}

	@Override
	public void keyPressed(KeyEvent event)
	{
		
	}

	@Override
	public void keyReleased(KeyEvent event)
	{
		
	}
}
