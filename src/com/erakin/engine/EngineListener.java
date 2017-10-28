package com.erakin.engine;

/**
 * <h1>Escuta para Engine</h1>
 *
 * <p>As escutas para Engine, permitem que determinados procedimentos possam ser executados.
 * Esses procedimentos serão chamados automaticamente pela engine se tiverem sido adicionados.
 * O momento em que serão executados varia de acordo com o método que for utilizado.</p>
 *
 * @author Andrew
 */

public interface EngineListener
{
	/**
	 * Chamado quando a engine solicitar a inicialização do sistema tal como serviços.
	 * Esse método é chamado até antes mesmo da inicialização da tela ou do teclado.
	 */

	void onInitiate();

	/**
	 * Chamado quando a engine tiver detectado uma solicitação para fechamento do cliente.
	 * Ele deverá ser chamado apenas após o encerramento de um loop após a solicitação.
	 */

	void onClosed();

	/**
	 * Chamado quando a engine já tiver sido fechada, ou seja, sistema e serviços foram desligados.
	 * A única coisa chamado após esse método é o Sistema do Java para sair da aplicação.
	 */

	void onShutdown();

	/**
	 * Esse procedimento será chamada por cada quadro de renderização da Engine.
	 * O mas comum é a utilização de tarefas, porém tarefas são executadas depois.
	 * @param delay tempo em milissegundos passados desde a última atualização.
	 */

	void tick(long delay);
}
