package com.erakin.api.resources.texture;

import java.nio.ByteBuffer;

/**
 * <h1>Escrever Pixel</h1>
 *
 * <p>Interface que implementa um m�todo para facilitar a escrita de um pixel.
 * Passando como refer�ncia as informa��es das tonalidade das cores RGB e
 * o valor da propriedade alfa, como tamb�m o buffer que ir� gravar.</p>
 *
 * @author Andrew
 */

public interface PixelWrite
{
	/**
	 * Deve escrever os dados de um determinado pixel em um buffer especificado.
	 * @param buffer refer�ncia do buffer do qual ter� os dados do pixel gravado.
	 * @param red n�vel de tonalidade para definir a cor vermelha.
	 * @param green n�vel de tonalidade para definir a cor azul.
	 * @param blue n�vel de tonalidade para definir a cor verde.
	 * @param alpha n�vel da propriedade para definir a transpar�ncia.
	 */

	void write(ByteBuffer buffer, byte red, byte green, byte blue, byte alpha);
}
