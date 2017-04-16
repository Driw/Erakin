package com.erakin.engine.resource.textures;

import java.nio.ByteBuffer;

/**
 * <h1>Dados de Textura</h1>
 *
 * <p>Interface que ir� implementar as informa��es b�sicas que todo textura possui.
 * Esses dados ir�o auxiliar na cria��o das texturas dos objetos/entidades e afins no jogo.
 * As informa��es necess�rias � a quantidade de bits por pixel, dimensionamento da textura
 * tanto na largura quanto na altura e o mais importante os bytes dos pixels do mesmo.</p>
 *
 * <p>Outra finalidade dessa interface � permitir a exist�ncia de outras semelhantes.
 * De modo que essas determinem novas funcionalidades de acordo com o seu prop�sito.</p>
 * 
 * @see ByteBuffer
 *
 * @author Andrew
 */

public interface TextureData
{
	/**
	 * Depth determina a profundidade de cada pixel, ou seja, quantos bits ter� cada pixel.
	 * Por padr�o utilizamos as seguintes configura��es: Bit Color (8), RGB (24) e RGBA (32).
	 * @return aquisi��o do tamanho em bits de cada pixel dessa imagem.
	 */

	int getDepth();

	/**
	 * Toda imagem possui um tamanho pr�-determinado especificado em seus dados.
	 * Esses tamanho � definido em largura e altura representado em pixels.
	 * Onde cada pixel possui uma quantidade de bytes que define sua cor.
	 * @return aquisi��o do tamanho da largura da imagem em pixels.
	 */

	int getWidth();

	/**
	 * Toda imagem possui um tamanho pr�-determinado especificado em seus dados.
	 * Esses tamanho � definido em largura e altura representado em pixels.
	 * Onde cada pixel possui uma quantidade de bytes que define sua cor.
	 * @return aquisi��o do tamanho da altura da imagem em pixels.
	 */

	int getHeight();

	/**
	 * Imagens no OpenGL deve ser aceitos apenas se forem valores 2^n.
	 * Esse tamanho � respectivo a textura e n�o a imagem carregada.
	 * @return aquisi��o do tamanho da largura 2n da imagem em pixels.
	 */

	int getTexWidth();

	/**
	 * Imagens no OpenGL deve ser aceitos apenas se forem valores 2^n.
	 * Esse tamanho � respectivo a textura e n�o a imagem carregada.
	 * @return aquisi��o do tamanho da altura 2n da imagem em pixels.
	 */

	int getTexHeight();

	/**
	 * Os pixels dever�o ser armazenados em bytes correspondente ao formato da textura.
	 * @return aquisi��o de buffer contendo dados das cores de todos os pixels.
	 */

	ByteBuffer getPixels();
}
