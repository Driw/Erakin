package com.erakin.engine;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Tarefa Padrão</h1>
 *
 * <p>Classe usada para simplificar a utilização de uma nova tarefa no sistema.
 * Essa tarefa terá uma variável de inicialização e outra para indicar o seu fim.</p>
 *
 * <p>Será necessário implementar alguns métodos que irão garantir melhor uso de tarefas.
 * Esses métodos serão chamados de acordo com o estado em que a tarefa se encontrar.
 * Cada um dos métodos deve possuir instruções adequadas para que a tarefa funcione.</p>
 *
 * <p>Primeiramente o sistema faz a inicialização através de <code>initiate</code> e indica que foi iniciado.
 * No tick seguinte as instruções da tarefa poderão ser processadas através de <code>subTick</code>.
 * Por fim, se durante um subTick for chamado <code>over</code> então a tarefa terá chego ao seu fim.
 * No tick seguinte será processado as instruções designadas para o método <code>terminate</code>.</p>
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
	 * Quando chamado indica que a tarefa chegou ao seu fim, portanto será encerrada.
	 * O encerramento será feito no mesmo tick porém somente após o término de <code>subTick</code>.
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
	 * Quando a tarefa for adicionada a lista de tarefas na engine esse será o primeiro método.
	 * No tick seguinte de sua inserção, ao ser chamado irá redirecionar para esse método.
	 * Somente no tick seguinte que outro método poderá ser chamado na tarefa.
	 * @throws Exception exceção que pode ocorrer durante a inicialização da tarefa.
	 */
	public abstract void initiate() throws Exception;

	/**
	 * Se em algum momento for definido para efetuar o encerramento esse será o método a ser chamado.
	 * Deverá garantir que todos os objetos na tarefa sejam liberados do sistema adequadamente.
	 * Se ainda necessário notificar visualmente o encerramento da atividade em questão.
	 * @throws Exception exceção que pode ocorrer durante o encerramento da tarefa.
	 */
	public abstract void terminate() throws Exception;

	/**
	 * Procedimento interno chamado sempre que um novo tick estiver a chamar essa tarefa.
	 * Antes desse método ser chamado, o sistema chama a inicialização da tarefa.
	 * Deverá garantir que todas as funcionalidades da tarefa sejam atendidas no mesmo.
	 * @param delay quantos milissegundos se passaram desde a última chamada.
	 * @throws Exception exceção que pode ocorrer durante o processamento da tarefa.
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
