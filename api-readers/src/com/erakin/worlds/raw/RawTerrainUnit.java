package com.erakin.worlds.raw;

import static com.erakin.api.Constants.VECTOR3F_BYTES;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Unidade de Terreno Bruta</h1>
 *
 * <p>Terrenos s�o formados por diversas unidades de terreno que armazenam informa��es de sua forma.
 * Essa unidade de terreno � considerada bruta, j� que os dados n�o s�o obrigatoriamente lidos de arquivos.
 * Eles podem ser gerados atrav�s da cria��o de um mundo bruto que gera tanto terrenos como unidades brutas.</p>
 *
 * <p>A principal diferente em ser bruto � que podemos alterar o posicionamento dos v�rtices diretamente.
 * Dessa forma, � poss�vel que na Engine exista um editor de mundos/terrenos, j� que outros terrenos possuem
 * as informa��es est�ticas ap�s serem carregados de um arquivo, n�o permitindo mudan�as no mesmo.</p>
 *
 * <p>Unidades de terrenos s�o quadrados perfeitos (largura e comprimento igual).
 * Formados a partir de duas triangula��es de v�rtices onde dois v�rtices se repetem nas triangula��es.</p>
 *
 * <p><b>V�rtices</b> - 0: sudoeste, 1: sudeste, 2: nordeste e 3: noroeste.<br>
 * <b>Exemplo de Triangula��o</b> - 0: (sudoeste, sudeste e noroeste), 1: (noroeste, nordeste e sudeste)</p>
 *
 * @see Vector3fTriggerable
 * @see Vector3fListener
 *
 * @author Andrew
 */

public class RawTerrainUnit
{
	/**
	 * Tamanho de uma unidade de terreno em mem�ria.
	 */
	public static final int BYTES = (VECTOR3F_BYTES * 4);


	/**
	 * Posicionamento da unidade na lista de unidades do terreno..
	 * Utilizado para saber em que lugar os dados da unidade ser�o posicionadas (OpenGL).
	 */
	private int offset;

	/**
	 * Vetor com o posicionamento do v�rtice localizado no sudoeste da unidade.
	 */
	private Vector3fTriggerable southWest;

	/**
	 * Vetor com o posicionamento do v�rtice localizado no sudeste da unidade.
	 */
	private Vector3fTriggerable southEast;

	/**
	 * Vetor com o posicionamento do v�rtice localizado no nordeste da unidade.
	 */
	private Vector3fTriggerable northEast;

	/**
	 * Vetor com o posicionamento do v�rtice localizado no noroeste da unidade.
	 */
	private Vector3fTriggerable northWest;

	/**
	 * Cria uma nova inst�ncia de uma unidade de terreno bruto.
	 * Necess�rio especificar um offset da unidade em rela��o a outras.
	 * @param cellOffset posicionamento da unidade na lista de unidades do terreno.
	 * @param listener necess�rio caso deseje uma atualiza��o de modelo mais �gil.
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
	 * <b>Por exemplo</b>, os v�rtices s�o armazenados em um vetor de floats onde cada v�rtice possui 3 informa��es (x,y,z).
	 * Se o offset dessa unidade for <i>5</i>, sabemos que os dados dessa unidade ser� de �ndice:
	 * <i>5 * 4 * 3 = 60</i> ({offset},{v�rtices por unidade},{valor por v�rtice}).
	 * @return posicionamento da unidade na lista de unidades do terreno.
	 */

	public int getOffset()
	{
		return offset;
	}

	/**
	 * @return aquisi��o das informa��es do v�rtice do sudoeste da unidade.
	 */

	public Vector3fTriggerable getSouthWest()
	{
		return southWest;
	}

	/**
	 * @return aquisi��o das informa��es do v�rtice do sudeste da unidade.
	 */

	public Vector3fTriggerable getSouthEast()
	{
		return southEast;
	}

	/**
	 * @return aquisi��o das informa��es do v�rtice do nordeste da unidade.
	 */

	public Vector3fTriggerable getNorthEast()
	{
		return northEast;
	}

	/**
	 * @return aquisi��o das informa��es do v�rtice do noroeste da unidade.
	 */

	public Vector3fTriggerable getNorthWest()
	{
		return northWest;
	}

	/**
	 * @return aquisi��o da altura m�dia dessa unidade.
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
