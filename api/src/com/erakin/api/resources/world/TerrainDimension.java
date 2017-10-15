package com.erakin.api.resources.world;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Dimens�o de Terreno</h1>
 *
 * <p>Classe usada para funcionar para determinar a dimens�o, o tamanho de um terreno.
 * Al�m de determinar o tamanho deste, possui algumas funcionalidades extras e �teis.
 * Possui getters e setters para trabalhar os tamanhos e construtores variados.</p>
 *
 * <p>A mais importante � bloquear a altera��o do seu tamanho para evitar problemas.
 * Por exemplo, um mapa com 10x10 de unidades � criado, uma vez gerado n�o ir� permitir,
 * que outros terrenos al�m deste tamanho passam ser aceitos, se assim for definido.</p>
 *
 * @author Andre Mello
 */

public class TerrainDimension
{
	/**
	 * Tamanho do terreno no eixo da largura.
	 */
	private int width;

	/**
	 * Tamanho do terreno no eixo do comprimento.
	 */
	private int length;

	/**
	 * Determina se o dimensionado foi trancado.
	 */
	private boolean locked;

	/**
	 * Constr�i um novo dimensionar de terreno sem determinar o seu tamanho.
	 * Nesse caso ser� poss�vel determinar especificadamente pelos setters.
	 */

	public TerrainDimension()
	{
		
	}

	/**
	 * Constr�i um novo dimensionar de terreno sem determinar o seu tamanho.
	 * Nesse caso o tamanho passado ser� aplicado em ambos os tamanhos dos eixos.
	 * @param size tamanho tanto do eixo da largura quanto do comprimento.
	 */

	public TerrainDimension(int size)
	{
		width = length = size;
	}

	/**
	 * Constr�i um novo dimensionar de terreno sem determinar o seu tamanho.
	 * Nesse caso ser� poss�vel definir especificadamente o tamanho de cada eixo.
	 * @param width tamanho que ser� definido para o eixo da largura.
	 * @param length tamanho que ser� definido para o eixo do comprimento.
	 */

	public TerrainDimension(int width, int length)
	{
		this.width = width;
		this.length = length;
	}

	/**
	 * Dimens�o de terreno possui uma medida que ir� indicar o tamanho da largura.
	 * Esta n�o possui uma unidade de medida padr�o, depende do renderizador/mundo.
	 * @return aquisi��o da tamanho da dimens�o do terreno no eixo da largura.
	 */

	public int getWidth()
	{
		return width;
	}

	/**
	 * Permite definir qual ser� o tamanho da dimens�o do terreno de acordo com:
	 * A modifica��o s� poder� ser aplicada no caso da dimens�o estar desbloqueada.
	 * @param width novo tamanho da dimens�o do terreno no eixo da largura.
	 */

	public void setWidth(int width)
	{
		if (!locked)
			this.width = width;
	}

	/**
	 * Dimens�o de terreno possui uma medida que ir� indicar o tamanho do comprimento.
	 * Esta n�o possui uma unidade de medida padr�o, depende do renderizador/mundo.
	 * @return aquisi��o da tamanho da dimens�o do terreno no eixo do comprimento.
	 */

	public int getLength()
	{
		return length;
	}

	/**
	 * Permite definir qual ser� o tamanho da dimens�o do terreno de acordo com:
	 * A modifica��o s� poder� ser aplicada no caso da dimens�o estar desbloqueada.
	 * @param length novo tamanho da dimens�o do terreno no eixo do comprimento.
	 */

	public void setLength(int length)
	{
		if (!locked)
			this.length = length;
	}

	/**
	 * O dimensionamento por padr�o vem desbloqueado, esse m�todo ir� bloque�-lo.
	 * Bloquear o dimensionamento significa que seu tamanho n�o poder� ser modificado.
	 * Uma vez que ele venha a ser bloqueado n�o poder� ser desbloqueado.
	 * @return aquisi��o do mesmo objeto com a defini��o de bloqueado.
	 */

	public TerrainDimension lock()
	{
		locked = true;

		return this;
	}

	/**
	 * Verifica se o dimensionamento do terreno j� est� bloqueado.
	 * Estar bloqueado significa n�o poder alterar seu tamanho j� definido.
	 * @return true se estiver bloqueado ou false caso contr�rio.
	 */

	public boolean isLock()
	{
		return true;
	}

	@Override
	protected TerrainDimension clone()
	{
		TerrainDimension wd = new TerrainDimension();
		wd.width = width;
		wd.length = length;

		return wd;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof TerrainDimension)
		{
			TerrainDimension wd = (TerrainDimension) obj;

			return wd.width == width && wd.length == length;
		}

		return false;
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("width", width);
		description.append("length", length);

		if (locked)
			description.append("locked");

		return description.toString();
	}
}
