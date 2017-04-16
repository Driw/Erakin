package com.erakin.engine.scene;

import com.erakin.engine.Tickable;

/**
 * <h1>Gerenciador de Cenários</h1>
 *
 * <p>São usados para garantir que a utilização dos cenários seja feita adequadamente de acordo com suas especificações.
 * Devem permitir determinar qual cenário deverá ser usado, podendo ainda se desejável usar múltiplos cenários.
 * Utiliza de ticks para manter a atualização e também da renderização do conteúdo dos cenários ativos.</p>
 *
 * <p>O gerenciador poderá trabalhar com um ou mais cenários ativos ao mesmo tempo desde que seja programado para tal.
 * Podendo ainda armazenar da forma que desejar, caso múltiplos cenários ativos recomenda-se usar coleções.
 * Para cada tipo de gerenciamento uma coleção será requisitada, <b>podendo ser</b> indexada por prioridade ou pilha.</p>
 *
 * @see Tickable
 *
 * @author Andrew Mello
 */

public interface SceneManager extends Tickable
{
	
}
