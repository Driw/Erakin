package com.erakin.engine;

/**
 * <h1>Renderizável</h1>
 *
 * <p>Quando uma classe implementa essa interface, significa que ela possui conteúdo para ser renderizado.
 * É usado por diversas classes para facilitar a codificação e documentação dessas classes.
 * Possui apenas um método, que é chamado sempre que um novo tick ocorrer no loop do engine.</p>
 *
 * <p>Um grande ponto que será muito utilizado é a questão do intervalo entre os loops, <b>delay</b>.
 * Esse intervalo é definido em milissegundos e pode ser obtido através de cada loop pelo método
 * <code>render</code> do qual será implementado por essa interface, e tem como objeto renderizar o objeto.</p>
 *
 * @author Andrew Mello
 */

public interface Renderable
{
	/**
	 * Deve possui operações que renderize todos os componentes necessárias do objeto em questão.
	 * @param delay quantos milissegundos se passaram desde a última atualização feita.
	 */

	void render(long delay);
}
