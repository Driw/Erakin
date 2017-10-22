package com.erakin.api.input;

import static org.diverproject.log.LogSystem.logNotice;
import static org.diverproject.util.service.SystemBase.PROPERTIE_USE_LOG;

import org.diverproject.jni.input.InputException;
import org.diverproject.jni.input.InputSystem;
import org.diverproject.util.service.ServiceSystem;

/**
 * <h1>Gerenciador de Entrada</h1>
 *
 * <p>Classe utilitária para implementação de métodos que permitem facilitar a utilização do teclado.</p>
 *
 * @author Andrew
 */

public class InputManager
{
	/**
	 * Define as propriedades padrões <code>PROPERTIE_USE_LOG</code> para ser usado pelo sistema de Input (teclado).
	 */

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
		ServiceSystem.getInstance().add(VirtualKeyboard.getInstance());

		InputSystem system = InputSystem.getInstance();
		system.setKeyboardDispatcher(VirtualKeyboard.getInstance());
		system.initialize();

		logNotice("serviços inicializados.\n");
	}

	/**
	 * Construtor privado para evitar instâncias desnecessárias
	 */

	private InputManager()
	{
		
	}
}
