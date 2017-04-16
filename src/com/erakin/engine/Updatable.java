package com.erakin.engine;

/**
 * <h1>Atualizável</h1>
 *
 * <p>Quando uma classe implementa essa interface, significa que ela poderá ser atualizada no engine.
 * É usado por diversas classes para facilitar a codificação e documentação dessas classes.
 * Possui apenas um método, que é chamado sempre que um novo tick ocorrer no loop do engine.</p>
 *
 * <p>Um grande ponto que será muito utilizado é a questão do intervalo entre os loops, <b>delay</b>.
 * Esse intervalo é definido em milissegundos e pode ser obtido através de cada loop pelo método
 * <code>update</code> do qual será implementado por essa interface, e tem como objeto atualizar o objeto.</p>
 *
 * @author Andrew Mello
 */

public interface Updatable
{
	/**
	 * Deve possui operações que atualize todas as funcionalidades necessárias do objeto em questão.
	 * @param delay quantos milissegundos se passaram desde a última atualização feita.
	 */

	void update(long delay);
}
