package com.erakin.engine;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Tarefa Padr�o</h1>
 *
 * <p>Classe usada para simplificar a utiliza��o de uma nova tarefa no sistema.
 * Essa tarefa ter� uma vari�vel de inicializa��o e outra para indicar o seu fim.</p>
 *
 * <p>Ser� necess�rio implementar alguns m�todos que ir�o garantir melhor uso de tarefas.
 * Esses m�todos ser�o chamados de acordo com o estado em que a tarefa se encontrar.
 * Cada um dos m�todos deve possuir instru��es adequadas para que a tarefa funcione.</p>
 *
 * <p>Primeiramente o sistema faz a inicializa��o atrav�s de <code>initiate</code> e indica que foi iniciado.
 * No tick seguinte as instru��es da tarefa poder�o ser processadas atrav�s de <code>subTick</code>.
 * Por fim, se durante um subTick for chamado <code>over</code> ent�o a tarefa ter� chego ao seu fim.
 * No tick seguinte ser� processado as instru��es designadas para o m�todo <code>terminate</code>.</p>
 *
 * @author Andre Mello
 */

public abstract class TaskDefault implements Task
{
	/**
	 * Determina se a tarefa foi inicializada.
	 */
	private boolean initialized;

	/**
	 * Determina se a tarefa foi encerrada.
	 */
	private boolean over;

	@Override
	public final void tick(long delay) throws Exception
	{
		if (!initialized)
		{
			initiate();
			initialized = true;
		}

		else
			subTick(delay);

		if (over)
			terminate();
	}

	/**
	 * Quando chamado indica que a tarefa chegou ao seu fim, portanto ser� encerrada.
	 * O encerramento ser� feito no mesmo tick por�m somente ap�s o t�rmino de <code>subTick</code>.
	 */

	public void over()
	{
		over = true;
	}

	@Override
	public final boolean isOver()
	{
		return over;
	}

	/**
	 * Quando a tarefa for adicionada a lista de tarefas na engine esse ser� o primeiro m�todo.
	 * No tick seguinte de sua inser��o, ao ser chamado ir� redirecionar para esse m�todo.
	 * Somente no tick seguinte que outro m�todo poder� ser chamado na tarefa.
	 * @throws Exception exce��o que pode ocorrer durante a inicializa��o da tarefa.
	 */
	public abstract void initiate() throws Exception;

	/**
	 * Se em algum momento for definido para efetuar o encerramento esse ser� o m�todo a ser chamado.
	 * Dever� garantir que todos os objetos na tarefa sejam liberados do sistema adequadamente.
	 * Se ainda necess�rio notificar visualmente o encerramento da atividade em quest�o.
	 * @throws Exception exce��o que pode ocorrer durante o encerramento da tarefa.
	 */
	public abstract void terminate() throws Exception;

	/**
	 * Procedimento interno chamado sempre que um novo tick estiver a chamar essa tarefa.
	 * Antes desse m�todo ser chamado, o sistema chama a inicializa��o da tarefa.
	 * Dever� garantir que todas as funcionalidades da tarefa sejam atendidas no mesmo.
	 * @param delay quantos milissegundos se passaram desde a �ltima chamada.
	 * @throws Exception exce��o que pode ocorrer durante o processamento da tarefa.
	 */
	public abstract void subTick(long delay) throws Exception;

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("initialized", initialized);
		description.append("over", over);

		return description.toString();
	}
}
