package com.erakin.engine.resource.textures.pixel;

/**
 * <h1>Expans�o de Pixels</h1>
 *
 * <p>Interface usada para determinar o padr�o de funcionalidades na expans�o de pixels.
 * A expans�o de pixels � feita de acordo com os bytes especificados de uma image.
 * Devendo considerar ainda algumas outras propriedades que ir�o determinar como � feito.</p>
 *
 * <p>Por padr�o, todas expans�es dever�o possuir duas formas de an�lise, onde a primeira
 * tem como finalidade verificar o pixel que ser� analisado. Em quanto o segundo
 * ter� a finalidade de obter o valor em byte respectivo a um pixel analisado.</p>
 *
 * @author Andrew
 */

public interface PixelExpand
{
	/**
	 * Procedimento que ir� fazer a an�lise para a expans�o dos pixels.
	 * @param line linha de varredura de pixels que est� sendo analisada.
	 * @param palette vetor contendo informa��es da paleta de cores.
	 */

	void parse(byte[] line, byte[] palette);

	/**
	 * Procedimento que ir� fazer a analise para a expans�o de um pixel especifico.
	 * @param index �ndice do pixel do qual est� sendo analisado para expandir.
	 * @param value qual o valor que foi calculado pelo expansor (dado analisado).
	 * @return byte respectivo ao valor e �ndice passado da tonalidade do pixel.
	 */

	byte parse(int index, int value);
}
