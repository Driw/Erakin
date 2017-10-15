package com.erakin.api.input;

import org.diverproject.jni.input.KeyEvent;
import org.diverproject.jni.input.KeyboardDispatcher;
import org.diverproject.util.service.ServiceException;

/**
 * <h1>Teclado Virtual</h1>
 *
 * <p>JNI Input � uma das bibliotecas utilizadas pelo engine, permite detectar a��es para mouse e teclado.
 * Nesse caso ser� considerado apenas as funcionalidades de teclado na classe, que funciona como despachante.</p>
 * 
 * <p>A biblioteca JNI dever� detectar as a��es do teclado e repassar para o sistema de entradas e repassar.
 * Quando repassado, o sistema ir� verificar qual o despachante de teclado usado e repassar para este,
 * determinar o que ser� feito com o evento que foi constru�do a partir da detec��o da a��o acionada.</p>
 *
 * <p>A finalidade do despachante de teclado � receber os eventos detectados e determinar o que fazer.
 * Para cada evento de tecla detectado h� um tipo de a��o no mesmo e alguns atributos informativos sobre.</p>
 *
 * <p>Essa classe teclado ir� despachar adequadamente cada evento para suas a��es respectivas.
 * As a��es que poder�o ser encontradas pelos eventos de teclado s�o de tecla pressionada, clicada ou solta.
 * Todos os eventos ir�o possuir informa��es sobre qual a tecla que foi usada na a��o e alguns outros afins.</p>
 *
 * <p>Tem como alta prioridade no sistema do engine, pois garante metade da intera��o do jogador com o jogo.
 * Pode ser usado para identificar qual a��o usar ou funcionalidade que deve ser ativada de acordo com o que
 * foi programado para tal, como por exemplo quando a tecla Escape for pressionada exibir o menu principal.</p>
 *
 * @author Andrew Mello
 */

public class Keyboard implements KeyboardDispatcher
{
	/**
	 * Inst�ncia para teclado virtual no padr�o de projetos Singleton.
	 */
	private static final Keyboard INSTANCE = new Keyboard();

	@Override
	public void update(long delay)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() throws ServiceException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void terminate() throws ServiceException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void interrupted() throws ServiceException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getState()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getIdentificator()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dispatch(KeyEvent event)
	{
		// TODO Auto-generated method stub
		
	}

	/**
	 * Procedimento que permite obter a �nica inst�ncia do teclado virtual.
	 * Utiliza o padr�o Singleton para evitar a exist�ncia de mais inst�ncias.
	 * @return aquisi��o da inst�ncia para utiliza��o das prefer�ncias de v�deo.
	 */

	public static KeyboardDispatcher getInstance()
	{
		return INSTANCE;
	}
}
