package com.erakin.engine;

/**
 * <h1>Tarefa</h1>
 *
 * <p>Tarefas podem ser adicionadas ao engine, para que estas possam ser processadas continuadamente.
 * Como toda tarefa possui um fim, tamb�m ser� necess�rio determinar um m�todo para verificar isso.
 * Elas s�o processadas apenas uma vez a cada tick que ocorrer no processamento (quadro por segundo).</p>
 *
 * <p>Toda e qualquer exce��o que for gerado durante a execu��o da tarefa, pode ser redirecionada
 * para que o engine fa�a o tratamento adequado, que por padr�o � simplesmente fazer o registro desta.
 * Quando uma exce��o for causada, automaticamente ser� considerado que a tarefa acabou, para evitar
 * problemas durante a execu��o ou ainda ent�o encher o registro de eventos do engine com o mesmo.</p>
 *
 * @see Exception
 *
 * @author Andrew Mello
 */

public interface Task
{
	/**
	 * Chamado uma �nica vez por cada tick processado pelo engine.
	 * @param delay quantos milissegundos se passaram desde o �ltimo tick.
	 * @throws Exception ocorre apenas por irregularidade na execu��o.
	 */

	void tick(long delay) throws Exception;

	/**
	 * Verifica se a tarefa j� chegou ao seu fim, e se chegou ser� eliminada.
	 * Uma vez que a tarefa seja eliminada, n�o ser� mais processada no engine.
	 * @return true se tiver chegado ao fim ou false caso contr�rio.
	 */

	boolean isOver();
}
