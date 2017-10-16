package com.erakin.worlds.raw;

import static com.erakin.api.Constants.VECTOR3F_BYTES;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Unidade de Terreno Bruta</h1>
 *
 * <p>Terrenos são formados por diversas unidades de terreno que armazenam informações de sua forma.
 * Essa unidade de terreno é considerada bruta, já que os dados não são obrigatoriamente lidos de arquivos.
 * Eles podem ser gerados através da criação de um mundo bruto que gera tanto terrenos como unidades brutas.</p>
 *
 * <p>A principal diferente em ser bruto é que podemos alterar o posicionamento dos vértices diretamente.
 * Dessa forma, é possível que na Engine exista um editor de mundos/terrenos, já que outros terrenos possuem
 * as informações estáticas após serem carregados de um arquivo, não permitindo mudanças no mesmo.</p>
 *
 * <p>Unidades de terrenos são quadrados perfeitos (largura e comprimento igual).
 * Formados a partir de duas triangulações de vértices onde dois vértices se repetem nas triangulações.</p>
 *
 * <p><b>Vértices</b> - 0: sudoeste, 1: sudeste, 2: nordeste e 3: noroeste.<br>
 * <b>Exemplo de Triangulação</b> - 0: (sudoeste, sudeste e noroeste), 1: (noroeste, nordeste e sudeste)</p>
 *
 * @see Vector3fTriggerable
 * @see Vector3fListener
 *
 * @author Andrew
 */

public class RawTerrainUnit
{
	/**
	 * Tamanho de uma unidade de terreno em memória.
	 */
	public static final int BYTES = (VECTOR3F_BYTES * 4);


	/**
	 * Posicionamento da unidade na lista de unidades do terreno..
	 * Utilizado para saber em que lugar os dados da unidade serão posicionadas (OpenGL).
	 */
	private int offset;

	/**
	 * Vetor com o posicionamento do vértice localizado no sudoeste da unidade.
	 */
	private Vector3fTriggerable southWest;

	/**
	 * Vetor com o posicionamento do vértice localizado no sudeste da unidade.
	 */
	private Vector3fTriggerable southEast;

	/**
	 * Vetor com o posicionamento do vértice localizado no nordeste da unidade.
	 */
	private Vector3fTriggerable northEast;

	/**
	 * Vetor com o posicionamento do vértice localizado no noroeste da unidade.
	 */
	private Vector3fTriggerable northWest;

	/**
	 * Cria uma nova instância de uma unidade de terreno bruto.
	 * Necessário especificar um offset da unidade em relação a outras.
	 * @param cellOffset posicionamento da unidade na lista de unidades do terreno.
	 * @param listener necessário caso deseje uma atualização de modelo mais ágil.
	 */

	public RawTerrainUnit(int cellOffset, Vector3fListener listener)
	{
		offset = cellOffset;
		southWest = new Vector3fTriggerable(this, listener);
		southEast = new Vector3fTriggerable(this, listener);
		northEast = new Vector3fTriggerable(this, listener);
		northWest = new Vector3fTriggerable(this, listener);
	}

	/**
	 * O posicionamento da unidade nos permite saber exatamente em que lugar a unidade se encontra no OpenGL.
	 * <b>Por exemplo</b>, os vértices são armazenados em um vetor de floats onde cada vértice possui 3 informações (x,y,z).
	 * Se o offset dessa unidade for <i>5</i>, sabemos que os dados dessa unidade será de índice:
	 * <i>5 * 4 * 3 = 60</i> ({offset},{vértices por unidade},{valor por vértice}).
	 * @return posicionamento da unidade na lista de unidades do terreno.
	 */

	public int getOffset()
	{
		return offset;
	}

	/**
	 * @return aquisição das informações do vértice do sudoeste da unidade.
	 */

	public Vector3fTriggerable getSouthWest()
	{
		return southWest;
	}

	/**
	 * @return aquisição das informações do vértice do sudeste da unidade.
	 */

	public Vector3fTriggerable getSouthEast()
	{
		return southEast;
	}

	/**
	 * @return aquisição das informações do vértice do nordeste da unidade.
	 */

	public Vector3fTriggerable getNorthEast()
	{
		return northEast;
	}

	/**
	 * @return aquisição das informações do vértice do noroeste da unidade.
	 */

	public Vector3fTriggerable getNorthWest()
	{
		return northWest;
	}

	/**
	 * @return aquisição da altura média dessa unidade.
	 */

	public float avarageHeight()
	{
		return (southWest.getY() + southEast.getY() + northEast.getY() + northWest.getY()) / 4;
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("southWest", southWest);
		description.append("southEast", southEast);
		description.append("northEast", northEast);
		description.append("northWest", northWest);

		return description.toString();
	}
}
