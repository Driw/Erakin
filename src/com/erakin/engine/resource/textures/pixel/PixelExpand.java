package com.erakin.engine.resource.textures.pixel;

/**
 * <h1>Expansão de Pixels</h1>
 *
 * <p>Interface usada para determinar o padrão de funcionalidades na expansão de pixels.
 * A expansão de pixels é feita de acordo com os bytes especificados de uma image.
 * Devendo considerar ainda algumas outras propriedades que irão determinar como é feito.</p>
 *
 * <p>Por padrão, todas expansões deverão possuir duas formas de análise, onde a primeira
 * tem como finalidade verificar o pixel que será analisado. Em quanto o segundo
 * terá a finalidade de obter o valor em byte respectivo a um pixel analisado.</p>
 *
 * @author Andrew
 */

public interface PixelExpand
{
	/**
	 * Procedimento que irá fazer a análise para a expansão dos pixels.
	 * @param line linha de varredura de pixels que está sendo analisada.
	 * @param palette vetor contendo informações da paleta de cores.
	 */

	void parse(byte[] line, byte[] palette);

	/**
	 * Procedimento que irá fazer a analise para a expansão de um pixel especifico.
	 * @param index índice do pixel do qual está sendo analisado para expandir.
	 * @param value qual o valor que foi calculado pelo expansor (dado analisado).
	 * @return byte respectivo ao valor e índice passado da tonalidade do pixel.
	 */

	byte parse(int index, int value);
}
