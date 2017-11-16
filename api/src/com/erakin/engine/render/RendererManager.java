package com.erakin.engine.render;

import com.erakin.engine.Engine;
import com.erakin.engine.Tickable;

/**
 * <h1>Gerenciador de Renderização</h1>
 *
 * <p>Toda e qualquer renderização que for feita durante a execução deverá estar exclusivamente ligada a partir daqui.
 * Dentro do engine ou gerenciador de exibição não se é feito nenhuma renderização de qualquer objeto existente.
 * As classes que necessitam de uma renderização poderão conter, mas não serão renderizados se não definidos para tal.</p>
 *
 * <p>Suas finalidades como um gerenciador é primeiramente determinar como as renderizações devem ser feitas.
 * Em seguida garantir um gerenciamento dos objetos como entidades, terrenos, iluminações dentre outros ativos ou não.
 * Garantir que esses elementos sejam usados apenas quando necessários e removidos também apenas quando necessário.</p>
 *
 * <p>Possui uma relação bem semelhante as tarefas do engine, onde há um procedimento para inicialização e verificação.
 * Isso é feito para garantir que não haverá problemas durante a inicialização do renderizador devido ao uso do OpenGL.</p>
 *
 * @see Tickable
 *
 * @author Andrew Mello
 */

public interface RendererManager extends Tickable
{
	/**
	 * Chamado somente quando o cliente for finalizado ou ainda então quando for feito uma troca de gerenciador.
	 * Deverá liberar toda e qualquer informação referente a computação gráfica (shader) e outros objetos usados.
	 */

	void cleanup();

	/**
	 * Para prevenir problemas de instâncias por utilização do OpenGL durante a criação dos renderizadores,
	 * esse método é usado para conter todo o conteúdo do construtor que use OpenGL ou tudo preferencialmente.
	 * Será chamado somente quando o loop do engine começar a funcionar, assim o OpenGL já terá sido iniciado.
	 */

	void initiate();

	/**
	 * Verifica se o renderizador já foi iniciado, uma vez que ele tenha sido iniciado o engine irá ignorar a operação.
	 * @return true somente se já tiver sido iniciado ou false caso ainda não tenha sido.
	 */

	boolean isInitiate();

	/**
	 * Quando o renderizador estiver parado ele não será chamado para renderizar pela {@link Engine}.
	 * @return true se estiver parado e não deve renderizar ou false caso contrário e deve renderizar.
	 */

	boolean isStoped();

	/**
	 * Ao parar o renderizador, não será chamado pela {@link Engine} para efetuar a sua renderização.
	 */

	void pause();

	/**
	 * Ao resumir o renderizador, volta a ser chamado pela {@link Engine} para efetuar sua renderização.
	 */

	void resume();
}
