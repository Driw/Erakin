package com.erakin.engine.resource.textures.pixel;

import java.nio.ByteBuffer;

/**
 * <h1>Ajuste de Pixels por Paleta</h1>
 *
 * <p>Interface que irá determinar os procedimentos necessários para fazer o ajuste dos
 * pixels de uma imagem de acordo com as informações armazenadas na paleta dessa imagem.
 * Uma paleta determina uma lista de cores, e para cada cor um índice será definido.</p>
 *
 * <p>Ao invés de um pixel conter as informações do pixel, irá conter as informações
 * das cores armazenadas na paleta economizando 3 bytes por cada cor, quando uma imagem
 * utiliza um limite de cores baixo, a utilização de uma paleta de cores é viável.</p>
 *
 * @author Andrew
 */

public interface PixelPaletteCast
{
	/**
	 * Procedimento que irá fazer o ajuste para uma linha de varredura de pixels.
	 * @param buffer referência do buffer que será gravado o ajuste feito.
	 * @param line referência da linha de varredura contendo informações dos pixels.
	 * @param palette vetor contendo a lista de cores do qual será considerada.
	 * @param transparency vetor contendo a cor considerada como transparente.
	 */

	void cast(ByteBuffer buffer, byte[] line, byte palette[], byte[] transparency);
}
