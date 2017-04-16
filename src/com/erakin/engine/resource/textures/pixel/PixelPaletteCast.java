package com.erakin.engine.resource.textures.pixel;

import java.nio.ByteBuffer;

/**
 * <h1>Ajuste de Pixels por Paleta</h1>
 *
 * <p>Interface que ir� determinar os procedimentos necess�rios para fazer o ajuste dos
 * pixels de uma imagem de acordo com as informa��es armazenadas na paleta dessa imagem.
 * Uma paleta determina uma lista de cores, e para cada cor um �ndice ser� definido.</p>
 *
 * <p>Ao inv�s de um pixel conter as informa��es do pixel, ir� conter as informa��es
 * das cores armazenadas na paleta economizando 3 bytes por cada cor, quando uma imagem
 * utiliza um limite de cores baixo, a utiliza��o de uma paleta de cores � vi�vel.</p>
 *
 * @author Andrew
 */

public interface PixelPaletteCast
{
	/**
	 * Procedimento que ir� fazer o ajuste para uma linha de varredura de pixels.
	 * @param buffer refer�ncia do buffer que ser� gravado o ajuste feito.
	 * @param line refer�ncia da linha de varredura contendo informa��es dos pixels.
	 * @param palette vetor contendo a lista de cores do qual ser� considerada.
	 * @param transparency vetor contendo a cor considerada como transparente.
	 */

	void cast(ByteBuffer buffer, byte[] line, byte palette[], byte[] transparency);
}
