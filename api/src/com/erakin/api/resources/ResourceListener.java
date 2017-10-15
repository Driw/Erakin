package com.erakin.api.resources;

/**
 * <h1>Listener para Recurso</h1>
 *
 * <p>Usado pelo recurso raíz para que um mapeador possa identificar quando um recurso for liberado.</p>
 *
 * @author Andrew Mello
 */

public interface ResourceListener
{
	/**
	 * Chamado sempre que o recurso foi liberado do sistema.
	 */

	void resourceRelease();
}
