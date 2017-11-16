package com.erakin.api.input;

import java.awt.Canvas;

import javax.swing.JFrame;

import org.lwjgl.opengl.Display;

/**
 * <h1>Listener para Habilitar {@link VirtualKeyboard}</h1>
 *
 * <p>Possuí um único método e através do mesmo será possível saber se {@link VirtualKeyboard} será habilitado ou não.
 * Caso seja um sistema de cliente sendo usado isso provavelmente vai considerar se o {@link Display} está criado e ativo.
 * Entretanto, se for uma outra ferramenta usando um {@link Canvas} por exemplo, pode ser necessário {@link JFrame} focado.</p>
 *
 * @author Andrew
 */

public interface VirtualKeyBoardActiveListener
{
	/**
	 * Determina se {@link VirtualKeyboard} deve ou não despachar os eventos de teclado detectados.
	 * @return true para continuar despachando ou false para interromper temporariamente.
	 */

	boolean canDispatch();
}
