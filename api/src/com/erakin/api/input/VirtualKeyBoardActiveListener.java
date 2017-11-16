package com.erakin.api.input;

import java.awt.Canvas;

import javax.swing.JFrame;

import org.lwjgl.opengl.Display;

/**
 * <h1>Listener para Habilitar {@link VirtualKeyboard}</h1>
 *
 * <p>Possu� um �nico m�todo e atrav�s do mesmo ser� poss�vel saber se {@link VirtualKeyboard} ser� habilitado ou n�o.
 * Caso seja um sistema de cliente sendo usado isso provavelmente vai considerar se o {@link Display} est� criado e ativo.
 * Entretanto, se for uma outra ferramenta usando um {@link Canvas} por exemplo, pode ser necess�rio {@link JFrame} focado.</p>
 *
 * @author Andrew
 */

public interface VirtualKeyBoardActiveListener
{
	/**
	 * Determina se {@link VirtualKeyboard} deve ou n�o despachar os eventos de teclado detectados.
	 * @return true para continuar despachando ou false para interromper temporariamente.
	 */

	boolean canDispatch();
}
