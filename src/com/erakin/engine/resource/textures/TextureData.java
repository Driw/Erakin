package com.erakin.engine.resource.textures;

import java.nio.ByteBuffer;

/**
 * <h1>Dados de Textura</h1>
 *
 * <p>Interface que irá implementar as informações básicas que todo textura possui.
 * Esses dados irão auxiliar na criação das texturas dos objetos/entidades e afins no jogo.
 * As informações necessárias é a quantidade de bits por pixel, dimensionamento da textura
 * tanto na largura quanto na altura e o mais importante os bytes dos pixels do mesmo.</p>
 *
 * <p>Outra finalidade dessa interface é permitir a existência de outras semelhantes.
 * De modo que essas determinem novas funcionalidades de acordo com o seu propósito.</p>
 * 
 * @see ByteBuffer
 *
 * @author Andrew
 */

public interface TextureData
{
	/**
	 * Depth determina a profundidade de cada pixel, ou seja, quantos bits terá cada pixel.
	 * Por padrão utilizamos as seguintes configurações: Bit Color (8), RGB (24) e RGBA (32).
	 * @return aquisição do tamanho em bits de cada pixel dessa imagem.
	 */

	int getDepth();

	/**
	 * Toda imagem possui um tamanho pré-determinado especificado em seus dados.
	 * Esses tamanho é definido em largura e altura representado em pixels.
	 * Onde cada pixel possui uma quantidade de bytes que define sua cor.
	 * @return aquisição do tamanho da largura da imagem em pixels.
	 */

	int getWidth();

	/**
	 * Toda imagem possui um tamanho pré-determinado especificado em seus dados.
	 * Esses tamanho é definido em largura e altura representado em pixels.
	 * Onde cada pixel possui uma quantidade de bytes que define sua cor.
	 * @return aquisição do tamanho da altura da imagem em pixels.
	 */

	int getHeight();

	/**
	 * Imagens no OpenGL deve ser aceitos apenas se forem valores 2^n.
	 * Esse tamanho é respectivo a textura e não a imagem carregada.
	 * @return aquisição do tamanho da largura 2n da imagem em pixels.
	 */

	int getTexWidth();

	/**
	 * Imagens no OpenGL deve ser aceitos apenas se forem valores 2^n.
	 * Esse tamanho é respectivo a textura e não a imagem carregada.
	 * @return aquisição do tamanho da altura 2n da imagem em pixels.
	 */

	int getTexHeight();

	/**
	 * Os pixels deverão ser armazenados em bytes correspondente ao formato da textura.
	 * @return aquisição de buffer contendo dados das cores de todos os pixels.
	 */

	ByteBuffer getPixels();
}
