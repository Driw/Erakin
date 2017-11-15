package com.erakin.engine;

import java.awt.Canvas;

import org.lwjgl.opengl.DisplayMode;

/**
 * <h1>Escuta para Exibi��o</h1>
 *
 * <p>Atrav�s dessa escuta � poss�vel que determinadas a��es no {@link DisplayManager} possam ser repassadas para outras classes.
 * Nesse caso sempre que o modo de exibi��o for alterado, outras classes podem ficar sabendo dessa altera��o internamente.
 * Para isso, � necess�rio que a classe implemente essa interface, por em quanto somente executado por um {@link Canvas} definido.</p>
 *
 * @see DisplayMode
 *
 * @author Andrew
 */

public interface DisplayListener
{
	void setDisplayMode(DisplayMode displayMode);
}
