package com.erakin.engine.render;

import com.erakin.engine.Engine;
import com.erakin.engine.Tickable;

/**
 * <h1>Gerenciador de Renderiza��o</h1>
 *
 * <p>Toda e qualquer renderiza��o que for feita durante a execu��o dever� estar exclusivamente ligada a partir daqui.
 * Dentro do engine ou gerenciador de exibi��o n�o se � feito nenhuma renderiza��o de qualquer objeto existente.
 * As classes que necessitam de uma renderiza��o poder�o conter, mas n�o ser�o renderizados se n�o definidos para tal.</p>
 *
 * <p>Suas finalidades como um gerenciador � primeiramente determinar como as renderiza��es devem ser feitas.
 * Em seguida garantir um gerenciamento dos objetos como entidades, terrenos, ilumina��es dentre outros ativos ou n�o.
 * Garantir que esses elementos sejam usados apenas quando necess�rios e removidos tamb�m apenas quando necess�rio.</p>
 *
 * <p>Possui uma rela��o bem semelhante as tarefas do engine, onde h� um procedimento para inicializa��o e verifica��o.
 * Isso � feito para garantir que n�o haver� problemas durante a inicializa��o do renderizador devido ao uso do OpenGL.</p>
 *
 * @see Tickable
 *
 * @author Andrew Mello
 */

public interface RendererManager extends Tickable
{
	/**
	 * Chamado somente quando o cliente for finalizado ou ainda ent�o quando for feito uma troca de gerenciador.
	 * Dever� liberar toda e qualquer informa��o referente a computa��o gr�fica (shader) e outros objetos usados.
	 */

	void cleanup();

	/**
	 * Para prevenir problemas de inst�ncias por utiliza��o do OpenGL durante a cria��o dos renderizadores,
	 * esse m�todo � usado para conter todo o conte�do do construtor que use OpenGL ou tudo preferencialmente.
	 * Ser� chamado somente quando o loop do engine come�ar a funcionar, assim o OpenGL j� ter� sido iniciado.
	 */

	void initiate();

	/**
	 * Verifica se o renderizador j� foi iniciado, uma vez que ele tenha sido iniciado o engine ir� ignorar a opera��o.
	 * @return true somente se j� tiver sido iniciado ou false caso ainda n�o tenha sido.
	 */

	boolean isInitiate();

	/**
	 * Quando o renderizador estiver parado ele n�o ser� chamado para renderizar pela {@link Engine}.
	 * @return true se estiver parado e n�o deve renderizar ou false caso contr�rio e deve renderizar.
	 */

	boolean isStoped();

	/**
	 * Ao parar o renderizador, n�o ser� chamado pela {@link Engine} para efetuar a sua renderiza��o.
	 */

	void pause();

	/**
	 * Ao resumir o renderizador, volta a ser chamado pela {@link Engine} para efetuar sua renderiza��o.
	 */

	void resume();
}
