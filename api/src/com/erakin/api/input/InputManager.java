package com.erakin.api.input;

import static org.diverproject.log.LogSystem.logNotice;
import static org.diverproject.util.service.SystemBase.PROPERTIE_USE_LOG;

import org.diverproject.jni.input.InputException;
import org.diverproject.jni.input.InputSystem;

public class InputManager
{
	public static void setDefaultProperties()
	{
		InputSystem inputSystem = InputSystem.getInstance();
		inputSystem.setPropertie(PROPERTIE_USE_LOG, true);
	}

	/**
	 * Ao ser chamado define qual será o serviço para receber o despache dos eventos de teclado.
	 * Além disso deverá também garantir que o serviço de entrada seja inicializado no sistema.
	 * @throws InputException apenas se houver falha na inicialização do serviço de entrada.
	 */

	public static void initiateInput() throws InputException
	{
		InputSystem system = InputSystem.getInstance();
		system.setKeyboardDispatcher(Keyboard.getInstance());
		system.initialize();

		logNotice("serviços inicializados.\n");
	}
}
