package com.erakin.engine;

/**
 * <h1>Tickable</h1>
 *
 * Não há uma tradução para o nome dado a classe, mas sua função e dizer que pode ser atualizado e renderizado.
 * Usado para simplificar a codificação de algumas classes que podem tanto ser atualizadas como renderizadas.
 * Algumas outras classes as vezes podem ser apenas atualizadas, em alguns poucos casos apenas renderizadas.
 *
 * @author Andrew Mello
 */

public interface Tickable extends Updatable, Renderable
{

}
