package com.erakin.engine.scene;

import com.erakin.engine.Tickable;

/**
 * <h1>Gerenciador de Cen�rios</h1>
 *
 * <p>S�o usados para garantir que a utiliza��o dos cen�rios seja feita adequadamente de acordo com suas especifica��es.
 * Devem permitir determinar qual cen�rio dever� ser usado, podendo ainda se desej�vel usar m�ltiplos cen�rios.
 * Utiliza de ticks para manter a atualiza��o e tamb�m da renderiza��o do conte�do dos cen�rios ativos.</p>
 *
 * <p>O gerenciador poder� trabalhar com um ou mais cen�rios ativos ao mesmo tempo desde que seja programado para tal.
 * Podendo ainda armazenar da forma que desejar, caso m�ltiplos cen�rios ativos recomenda-se usar cole��es.
 * Para cada tipo de gerenciamento uma cole��o ser� requisitada, <b>podendo ser</b> indexada por prioridade ou pilha.</p>
 *
 * @see Tickable
 *
 * @author Andrew Mello
 */

public interface SceneManager extends Tickable
{
	
}
