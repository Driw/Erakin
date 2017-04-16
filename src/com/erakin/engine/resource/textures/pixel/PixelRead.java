package com.erakin.engine.resource.textures.pixel;

import java.nio.ByteBuffer;

/**
 * <h1>Ler Pixel</h1>
 *
 * <p>Interface que implementa um método para facilitar a leitura de pixels.
 * Passando como referência as informações de uma linha de varredura dos pixels.
 * Podendo ainda trabalhar com propriedade de transparência na leitura.</p>
 *
 * @author Andrew
 */

public interface PixelRead
{
	/**
	 * Chamado para fazer a leitura de pixels de acordo com as informações abaixo.
	 * @param buffer referência do buffer que será escrito os pixels lidos.
	 * @param line linha contendo as informações da varredura dos pixels.
	 * @param transparency vetor contendo as informações para transparência.
	 * @param offset a partir de qual índice da linha será feito a leitura.
	 * @param length quantos bytes deverão ser lidos a partir do offset.
	 */

	void read(ByteBuffer buffer, byte[] line, byte[] transparency, int offset, int length);
}
