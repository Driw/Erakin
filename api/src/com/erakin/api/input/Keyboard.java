package com.erakin.api.input;

import org.diverproject.jni.input.KeyEvent;
import org.diverproject.jni.input.KeyboardDispatcher;
import org.diverproject.util.service.ServiceException;

/**
 * <h1>Teclado Virtual</h1>
 *
 * <p>JNI Input é uma das bibliotecas utilizadas pelo engine, permite detectar ações para mouse e teclado.
 * Nesse caso será considerado apenas as funcionalidades de teclado na classe, que funciona como despachante.</p>
 * 
 * <p>A biblioteca JNI deverá detectar as ações do teclado e repassar para o sistema de entradas e repassar.
 * Quando repassado, o sistema irá verificar qual o despachante de teclado usado e repassar para este,
 * determinar o que será feito com o evento que foi construído a partir da detecção da ação acionada.</p>
 *
 * <p>A finalidade do despachante de teclado é receber os eventos detectados e determinar o que fazer.
 * Para cada evento de tecla detectado há um tipo de ação no mesmo e alguns atributos informativos sobre.</p>
 *
 * <p>Essa classe teclado irá despachar adequadamente cada evento para suas ações respectivas.
 * As ações que poderão ser encontradas pelos eventos de teclado são de tecla pressionada, clicada ou solta.
 * Todos os eventos irão possuir informações sobre qual a tecla que foi usada na ação e alguns outros afins.</p>
 *
 * <p>Tem como alta prioridade no sistema do engine, pois garante metade da interação do jogador com o jogo.
 * Pode ser usado para identificar qual ação usar ou funcionalidade que deve ser ativada de acordo com o que
 * foi programado para tal, como por exemplo quando a tecla Escape for pressionada exibir o menu principal.</p>
 *
 * @author Andrew Mello
 */

public class Keyboard implements KeyboardDispatcher
{
	/**
	 * Instância para teclado virtual no padrão de projetos Singleton.
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
	 * Procedimento que permite obter a única instância do teclado virtual.
	 * Utiliza o padrão Singleton para evitar a existência de mais instâncias.
	 * @return aquisição da instância para utilização das preferências de vídeo.
	 */

	public static KeyboardDispatcher getInstance()
	{
		return INSTANCE;
	}
}
