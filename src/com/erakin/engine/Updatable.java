package com.erakin.engine;

/**
 * <h1>Atualiz�vel</h1>
 *
 * <p>Quando uma classe implementa essa interface, significa que ela poder� ser atualizada no engine.
 * � usado por diversas classes para facilitar a codifica��o e documenta��o dessas classes.
 * Possui apenas um m�todo, que � chamado sempre que um novo tick ocorrer no loop do engine.</p>
 *
 * <p>Um grande ponto que ser� muito utilizado � a quest�o do intervalo entre os loops, <b>delay</b>.
 * Esse intervalo � definido em milissegundos e pode ser obtido atrav�s de cada loop pelo m�todo
 * <code>update</code> do qual ser� implementado por essa interface, e tem como objeto atualizar o objeto.</p>
 *
 * @author Andrew Mello
 */

public interface Updatable
{
	/**
	 * Deve possui opera��es que atualize todas as funcionalidades necess�rias do objeto em quest�o.
	 * @param delay quantos milissegundos se passaram desde a �ltima atualiza��o feita.
	 */

	void update(long delay);
}
