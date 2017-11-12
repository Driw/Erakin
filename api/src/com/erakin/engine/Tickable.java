package com.erakin.engine;

/**
 * <h1>Tickable</h1>
 *
 * N�o h� uma tradu��o para o nome dado a classe, mas sua fun��o e dizer que pode ser atualizado e renderizado.
 * Usado para simplificar a codifica��o de algumas classes que podem tanto ser atualizadas como renderizadas.
 * Algumas outras classes as vezes podem ser apenas atualizadas, em alguns poucos casos apenas renderizadas.
 *
 * @author Andrew Mello
 */

public interface Tickable extends Updatable, Renderable
{

}
