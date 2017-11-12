package com.erakin.engine;

/**
 * <h1>Escuta para Engine</h1>
 *
 * <p>As escutas para Engine, permitem que determinados procedimentos possam ser executados.
 * Esses procedimentos ser�o chamados automaticamente pela engine se tiverem sido adicionados.
 * O momento em que ser�o executados varia de acordo com o m�todo que for utilizado.</p>
 *
 * @author Andrew
 */

public interface EngineListener
{
	/**
	 * Chamado quando a engine solicitar a inicializa��o do sistema tal como servi�os.
	 * Esse m�todo � chamado at� antes mesmo da inicializa��o da tela ou do teclado.
	 */

	void onInitiate();

	/**
	 * Chamado quando a engine tiver detectado uma solicita��o para fechamento do cliente.
	 * Ele dever� ser chamado apenas ap�s o encerramento de um loop ap�s a solicita��o.
	 */

	void onClosed();

	/**
	 * Chamado quando a engine j� tiver sido fechada, ou seja, sistema e servi�os foram desligados.
	 * A �nica coisa chamado ap�s esse m�todo � o Sistema do Java para sair da aplica��o.
	 */

	void onShutdown();

	/**
	 * Esse procedimento ser� chamada por cada quadro de renderiza��o da Engine.
	 * O mas comum � a utiliza��o de tarefas, por�m tarefas s�o executadas depois.
	 * @param delay tempo em milissegundos passados desde a �ltima atualiza��o.
	 */

	void tick(long delay);
}
