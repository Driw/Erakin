package com.erakin.engine;

/**
 * <h1>Tarefa</h1>
 *
 * <p>Tarefas podem ser adicionadas ao engine, para que estas possam ser processadas continuadamente.
 * Como toda tarefa possui um fim, também será necessário determinar um método para verificar isso.
 * Elas são processadas apenas uma vez a cada tick que ocorrer no processamento (quadro por segundo).</p>
 *
 * <p>Toda e qualquer exceção que for gerado durante a execução da tarefa, pode ser redirecionada
 * para que o engine faça o tratamento adequado, que por padrão é simplesmente fazer o registro desta.
 * Quando uma exceção for causada, automaticamente será considerado que a tarefa acabou, para evitar
 * problemas durante a execução ou ainda então encher o registro de eventos do engine com o mesmo.</p>
 *
 * @see Exception
 *
 * @author Andrew Mello
 */

public interface Task
{
	/**
	 * Chamado uma única vez por cada tick processado pelo engine.
	 * @param delay quantos milissegundos se passaram desde o último tick.
	 * @throws Exception ocorre apenas por irregularidade na execução.
	 */

	void tick(long delay) throws Exception;

	/**
	 * Verifica se a tarefa já chegou ao seu fim, e se chegou será eliminada.
	 * Uma vez que a tarefa seja eliminada, não será mais processada no engine.
	 * @return true se tiver chegado ao fim ou false caso contrário.
	 */

	boolean isOver();
}
