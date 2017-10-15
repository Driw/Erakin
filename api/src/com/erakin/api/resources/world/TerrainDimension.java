package com.erakin.api.resources.world;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Dimensão de Terreno</h1>
 *
 * <p>Classe usada para funcionar para determinar a dimensão, o tamanho de um terreno.
 * Além de determinar o tamanho deste, possui algumas funcionalidades extras e úteis.
 * Possui getters e setters para trabalhar os tamanhos e construtores variados.</p>
 *
 * <p>A mais importante é bloquear a alteração do seu tamanho para evitar problemas.
 * Por exemplo, um mapa com 10x10 de unidades é criado, uma vez gerado não irá permitir,
 * que outros terrenos além deste tamanho passam ser aceitos, se assim for definido.</p>
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
	 * Constrói um novo dimensionar de terreno sem determinar o seu tamanho.
	 * Nesse caso será possível determinar especificadamente pelos setters.
	 */

	public TerrainDimension()
	{
		
	}

	/**
	 * Constrói um novo dimensionar de terreno sem determinar o seu tamanho.
	 * Nesse caso o tamanho passado será aplicado em ambos os tamanhos dos eixos.
	 * @param size tamanho tanto do eixo da largura quanto do comprimento.
	 */

	public TerrainDimension(int size)
	{
		width = length = size;
	}

	/**
	 * Constrói um novo dimensionar de terreno sem determinar o seu tamanho.
	 * Nesse caso será possível definir especificadamente o tamanho de cada eixo.
	 * @param width tamanho que será definido para o eixo da largura.
	 * @param length tamanho que será definido para o eixo do comprimento.
	 */

	public TerrainDimension(int width, int length)
	{
		this.width = width;
		this.length = length;
	}

	/**
	 * Dimensão de terreno possui uma medida que irá indicar o tamanho da largura.
	 * Esta não possui uma unidade de medida padrão, depende do renderizador/mundo.
	 * @return aquisição da tamanho da dimensão do terreno no eixo da largura.
	 */

	public int getWidth()
	{
		return width;
	}

	/**
	 * Permite definir qual será o tamanho da dimensão do terreno de acordo com:
	 * A modificação só poderá ser aplicada no caso da dimensão estar desbloqueada.
	 * @param width novo tamanho da dimensão do terreno no eixo da largura.
	 */

	public void setWidth(int width)
	{
		if (!locked)
			this.width = width;
	}

	/**
	 * Dimensão de terreno possui uma medida que irá indicar o tamanho do comprimento.
	 * Esta não possui uma unidade de medida padrão, depende do renderizador/mundo.
	 * @return aquisição da tamanho da dimensão do terreno no eixo do comprimento.
	 */

	public int getLength()
	{
		return length;
	}

	/**
	 * Permite definir qual será o tamanho da dimensão do terreno de acordo com:
	 * A modificação só poderá ser aplicada no caso da dimensão estar desbloqueada.
	 * @param length novo tamanho da dimensão do terreno no eixo do comprimento.
	 */

	public void setLength(int length)
	{
		if (!locked)
			this.length = length;
	}

	/**
	 * O dimensionamento por padrão vem desbloqueado, esse método irá bloqueá-lo.
	 * Bloquear o dimensionamento significa que seu tamanho não poderá ser modificado.
	 * Uma vez que ele venha a ser bloqueado não poderá ser desbloqueado.
	 * @return aquisição do mesmo objeto com a definição de bloqueado.
	 */

	public TerrainDimension lock()
	{
		locked = true;

		return this;
	}

	/**
	 * Verifica se o dimensionamento do terreno já está bloqueado.
	 * Estar bloqueado significa não poder alterar seu tamanho já definido.
	 * @return true se estiver bloqueado ou false caso contrário.
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
