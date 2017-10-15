package com.erakin.api.lwjgl.math;

import org.diverproject.util.lang.IntUtil;

/**
 * <h1>Matem�tica para Mundos</h1>
 *
 * <p>Classe utilit�ria que � composta apenas de procedimentos est�ticos a fim de fazer contas matem�ticas.
 * Utilizado por diversas classes que precisam realizar as mesmas opera��es e muitas vezes complexas.
 * Assim � poss�vel reduzir quantidade de c�digos e facilitar o entendimento nessas outras classes.</p>
 *
 * <p>Aqui apenas os c�lculos relacionados a mundos ser�o especificados aqui, portanto n�o deve ser �til fora disso.
 * Outras classes de c�lculos podem existir, por�m ter� utilidade apenas para uma parte espec�fica do sistema.</p>
 *
 * @author Andrew
 */

public class MathsWorld
{
	/**
	 * Verifica se uma determinada coordenada est� presente no espa�o do terreno.
	 * Os limites da coordenadas devem estar entre (0,0) e (width-1,length-1).
	 * @param width tamanho do terreno na propor��o da largura.
	 * @param length tamanho do terreno na propor��o de comprimento.
	 * @param xTerrain coordenada � validar relativa ao eixo da largura (x).
	 * @param zTerrain coordenada � validar relativa ao eixo do comprimento (z).
	 * @return true se as coordenadas estiverem de acordo com o tamanho do terreno,
	 * caso contr�rio false.
	 */

	public static boolean isTerrainCoordinate(int width, int length, int xTerrain, int zTerrain)
	{
		return	IntUtil.interval(xTerrain, 0, width - 1) &&
				IntUtil.interval(zTerrain, 0, length - 1);
	}

	/**
	 * Calcula a posi��o ocupada por um terreno conforme as coordenadas relativas ao mundo.
	 * Esse calculo funciona tanto para posi��o X quanto Z, entretanto ao especificar
	 * a largura do mundo deve-se considerar a posi��o X, caso contr�rio latitude e posi��o Z.
	 * @param worldSize tamanho da largura ou comprimento do mundo em quest�o
	 * @param worldCoord coordenada relativa ao mundo (X para largura/longitude e Z para comprimento/latitude).
	 * @return aquisi��o do posicionamento X ou Z do terreno em rela��o ao mundo.
	 */

	public static int calcTerrainCoord(int worldSize, int worldCoord)
	{
		return worldCoord / worldSize;
	}

	/**
	 * Calcula a coordenada relativa ao terreno com base em uma coordenada relativa ao mundo.
	 * Esse calculo funciona tanto para latitude quanto longitude, entretanto ao especificar
	 * a largura do terreno deve-se considerar a coordenada X, caso contr�rio latitude e coordenada Z.
	 * @param terrainSize tamanho da largura ou comprimento do terreno em quest�o.
	 * @param worldCoord coordenada relativa ao mundo (X para largura/longitude e Z para comprimento/latitude).
	 * @return aquisi��o da coordenada X ou Z relativa ao espa�o ocupado pelo terreno.
	 */

	public static int calcInTerrainCoord(int terrainSize, int worldCoord)
	{
		return worldCoord % terrainSize;
	}

	/**
	 * Construtor privado para evitar inst�ncias desnecess�rias dessa classe utilit�ria.
	 */

	private MathsWorld()
	{
		
	}
}
