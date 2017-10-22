package com.erakin.api.input;

import static org.diverproject.log.LogSystem.logNotice;
import static org.diverproject.util.service.SystemBase.PROPERTIE_USE_LOG;

import org.diverproject.jni.input.InputException;
import org.diverproject.jni.input.InputSystem;
import org.diverproject.util.service.ServiceSystem;

/**
 * <h1>Gerenciador de Entrada</h1>
 *
 * <p>Classe utilit�ria para implementa��o de m�todos que permitem facilitar a utiliza��o do teclado.</p>
 *
 * @author Andrew
 */

public class InputManager
{
	/**
	 * Define as propriedades padr�es <code>PROPERTIE_USE_LOG</code> para ser usado pelo sistema de Input (teclado).
	 */

	public static void setDefaultProperties()
	{
		InputSystem inputSystem = InputSystem.getInstance();
		inputSystem.setPropertie(PROPERTIE_USE_LOG, true);
	}

	/**
	 * Ao ser chamado define qual ser� o servi�o para receber o despache dos eventos de teclado.
	 * Al�m disso dever� tamb�m garantir que o servi�o de entrada seja inicializado no sistema.
	 * @throws InputException apenas se houver falha na inicializa��o do servi�o de entrada.
	 */

	public static void initiateInput() throws InputException
	{
		ServiceSystem.getInstance().add(VirtualKeyboard.getInstance());

		InputSystem system = InputSystem.getInstance();
		system.setKeyboardDispatcher(VirtualKeyboard.getInstance());
		system.initialize();

		logNotice("servi�os inicializados.\n");
	}

	/**
	 * Construtor privado para evitar inst�ncias desnecess�rias
	 */

	private InputManager()
	{
		
	}
}
