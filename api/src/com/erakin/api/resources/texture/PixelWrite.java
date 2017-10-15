package com.erakin.api.resources.texture;

import java.nio.ByteBuffer;

/**
 * <h1>Escrever Pixel</h1>
 *
 * <p>Interface que implementa um método para facilitar a escrita de um pixel.
 * Passando como referência as informações das tonalidade das cores RGB e
 * o valor da propriedade alfa, como também o buffer que irá gravar.</p>
 *
 * @author Andrew
 */

public interface PixelWrite
{
	/**
	 * Deve escrever os dados de um determinado pixel em um buffer especificado.
	 * @param buffer referência do buffer do qual terá os dados do pixel gravado.
	 * @param red nível de tonalidade para definir a cor vermelha.
	 * @param green nível de tonalidade para definir a cor azul.
	 * @param blue nível de tonalidade para definir a cor verde.
	 * @param alpha nível da propriedade para definir a transparência.
	 */

	void write(ByteBuffer buffer, byte red, byte green, byte blue, byte alpha);
}
