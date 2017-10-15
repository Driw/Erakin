package com.erakin.api.lwjgl.math;

import org.diverproject.util.lang.IntUtil;

/**
 * <h1>Matemática para Mundos</h1>
 *
 * <p>Classe utilitária que é composta apenas de procedimentos estáticos a fim de fazer contas matemáticas.
 * Utilizado por diversas classes que precisam realizar as mesmas operações e muitas vezes complexas.
 * Assim é possível reduzir quantidade de códigos e facilitar o entendimento nessas outras classes.</p>
 *
 * <p>Aqui apenas os cálculos relacionados a mundos serão especificados aqui, portanto não deve ser útil fora disso.
 * Outras classes de cálculos podem existir, porém terá utilidade apenas para uma parte específica do sistema.</p>
 *
 * @author Andrew
 */

public class MathsWorld
{
	/**
	 * Verifica se uma determinada coordenada está presente no espaço do terreno.
	 * Os limites da coordenadas devem estar entre (0,0) e (width-1,length-1).
	 * @param width tamanho do terreno na proporção da largura.
	 * @param length tamanho do terreno na proporção de comprimento.
	 * @param xTerrain coordenada à validar relativa ao eixo da largura (x).
	 * @param zTerrain coordenada à validar relativa ao eixo do comprimento (z).
	 * @return true se as coordenadas estiverem de acordo com o tamanho do terreno,
	 * caso contrário false.
	 */

	public static boolean isTerrainCoordinate(int width, int length, int xTerrain, int zTerrain)
	{
		return	IntUtil.interval(xTerrain, 0, width - 1) &&
				IntUtil.interval(zTerrain, 0, length - 1);
	}

	/**
	 * Calcula a posição ocupada por um terreno conforme as coordenadas relativas ao mundo.
	 * Esse calculo funciona tanto para posição X quanto Z, entretanto ao especificar
	 * a largura do mundo deve-se considerar a posição X, caso contrário latitude e posição Z.
	 * @param worldSize tamanho da largura ou comprimento do mundo em questão
	 * @param worldCoord coordenada relativa ao mundo (X para largura/longitude e Z para comprimento/latitude).
	 * @return aquisição do posicionamento X ou Z do terreno em relação ao mundo.
	 */

	public static int calcTerrainCoord(int worldSize, int worldCoord)
	{
		return worldCoord / worldSize;
	}

	/**
	 * Calcula a coordenada relativa ao terreno com base em uma coordenada relativa ao mundo.
	 * Esse calculo funciona tanto para latitude quanto longitude, entretanto ao especificar
	 * a largura do terreno deve-se considerar a coordenada X, caso contrário latitude e coordenada Z.
	 * @param terrainSize tamanho da largura ou comprimento do terreno em questão.
	 * @param worldCoord coordenada relativa ao mundo (X para largura/longitude e Z para comprimento/latitude).
	 * @return aquisição da coordenada X ou Z relativa ao espaço ocupado pelo terreno.
	 */

	public static int calcInTerrainCoord(int terrainSize, int worldCoord)
	{
		return worldCoord % terrainSize;
	}

	/**
	 * Construtor privado para evitar instâncias desnecessárias dessa classe utilitária.
	 */

	private MathsWorld()
	{
		
	}
}
