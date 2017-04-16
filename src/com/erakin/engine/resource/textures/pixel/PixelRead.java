package com.erakin.engine.resource.textures.pixel;

import java.nio.ByteBuffer;

/**
 * <h1>Ler Pixel</h1>
 *
 * <p>Interface que implementa um m�todo para facilitar a leitura de pixels.
 * Passando como refer�ncia as informa��es de uma linha de varredura dos pixels.
 * Podendo ainda trabalhar com propriedade de transpar�ncia na leitura.</p>
 *
 * @author Andrew
 */

public interface PixelRead
{
	/**
	 * Chamado para fazer a leitura de pixels de acordo com as informa��es abaixo.
	 * @param buffer refer�ncia do buffer que ser� escrito os pixels lidos.
	 * @param line linha contendo as informa��es da varredura dos pixels.
	 * @param transparency vetor contendo as informa��es para transpar�ncia.
	 * @param offset a partir de qual �ndice da linha ser� feito a leitura.
	 * @param length quantos bytes dever�o ser lidos a partir do offset.
	 */

	void read(ByteBuffer buffer, byte[] line, byte[] transparency, int offset, int length);
}
