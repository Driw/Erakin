package com.erakin.engine;

import java.awt.Canvas;

import org.lwjgl.opengl.DisplayMode;

/**
 * <h1>Escuta para Exibição</h1>
 *
 * <p>Através dessa escuta é possível que determinadas ações no {@link DisplayManager} possam ser repassadas para outras classes.
 * Nesse caso sempre que o modo de exibição for alterado, outras classes podem ficar sabendo dessa alteração internamente.
 * Para isso, é necessário que a classe implemente essa interface, por em quanto somente executado por um {@link Canvas} definido.</p>
 *
 * @see DisplayMode
 *
 * @author Andrew
 */

public interface DisplayListener
{
	void setDisplayMode(DisplayMode displayMode);
}
