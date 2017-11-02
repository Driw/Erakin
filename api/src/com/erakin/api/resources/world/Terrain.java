package com.erakin.api.resources.world;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.collection.List;
import org.diverproject.util.lang.IntUtil;

import com.erakin.api.lwjgl.GLBind;
import com.erakin.api.render.ModelRender;
import com.erakin.api.render.TerrainRender;
import com.erakin.api.resources.texture.Texture;

/**
 * <h1>Terreno</h1>
 *
 * <p>Um terreno nada mais é do que um objeto que possui uma modelagem tri-dimensional.
 * A modelagem tri-dimensional será usada para representar todo o terreno respectivo.
 * Cada terreno tem uma identificação diferente de acordo com o mundo que pertence.
 * Também será registrado em um atributo a identificação do mundo que pertence.<p>
 *
 * @see GLBind
 * @see ModelRender
 *
 * @author Andrew
 */

public class Terrain implements TerrainRender
{
	/**
	 * Identificação do mundo no sistema.
	 */
	int worldID;

	/**
	 * Coordenada do terreno no mundo no eixo da longitude.
	 */
	int x;

	/**
	 * Coordenada do terreno no mundo no eixo da latitude.
	 */
	int z;

	/**
	 * Quantidade de células no eixo da longitude.
	 */
	int width;

	/**
	 * Quantidade de células no eixo da latitude.
	 */
	int length;

	/**
	 * Nível de elevação do terreno.
	 */
	float heights[][];

	/**
	 * Modelagem tri-dimensional do terreno.
	 */
	ModelRender model;

	/**
	 * Constrói um novo terreno, sendo necessário definir suas coordenadas no mundo.
	 * As coordenadas são referentes a grade de terrenos que um mundo possui.
	 * @param x coordenada no eixo da longitude do terreno no mundo.
	 * @param y coordenada no eixo da latitude do terreno no mundo.
	 * @param width quantidade de células que irá compor o terreno na largura.
	 * @param length quantidade de células que irá compor o terreno no comprimento.
	 */

	public Terrain(int x, int y, int width, int length)
	{
		this.x = x;
		this.z = y;

		setSize(width, length);
	}

	/**
	 * Terrenos são armazenados dentro de mundos, cada mundo possui uma identificação única.
	 * @return aquisição da identificação do mundo a qual esse terreno pertence.
	 */

	public int getWorldID()
	{
		return worldID;
	}

	@Override
	public ModelRender getModel()
	{
		return model;
	}

	/**
	 * Permite definir qual será a modelagem tri-dimensional que o terreno irá usar.
	 * @param model referência da nova modelagem tri-dimensional a ser usada pelo terreno.
	 */

	public void setModel(ModelRender model)
	{
		this.model = model;
	}

	@Override
	public int getX()
	{
		return x;
	}

	@Override
	public int getZ()
	{
		return z;
	}

	@Override
	public int getWidth()
	{
		return width;
	}

	@Override
	public int getLength()
	{
		return length;
	}

	@Override
	public Texture getHeightTexture()
	{
		return model == null ? null : model.getTexture();
	}

	@Override
	public boolean isHeightTexture()
	{
		return true; // FIXME por enquanto forçar textura de relevo para funcionar como antes
	}

	@Override
	public List<Texture> getTextures()
	{
		return null; // FIXME será necessário listar a textura dos terrenos
	}

	/**
	 * Permite definir qual o tamanho que o terreno irá possuir em células.
	 * @param width quantidade de células que irá compor o terreno na largura.
	 * @param length quantidade de células que irá compor o terreno no comprimento.
	 */

	void setSize(int width, int length)
	{
		this.width = width;
		this.length = length;
		this.heights = new float[width][length];
	}

	/**
	 * O nível de elevação permite que os terrenos se tornem mais reais.
	 * Além disso ajuda na criação da normalização para ser iluminado corretamente.
	 * @param x coordenada relativa ao terreno no eixo da longitude.
	 * @param z coordenada relativa ao terreno no eixo da latitude.
	 * @return aquisição do nível de elevação do terreno na coordenada passada.
	 */

	public float getHeight(int x, int z)
	{
		if (IntUtil.interval(x, 0, width) && IntUtil.interval(z, 0, length))
			return heights[x][z];

		return 0f;
	}

	/**
	 * Define o nível de elevação de uma determinada célula do terreno.
	 * @param x coordenada relativa ao terreno no eixo da longitude.
	 * @param z coordenada relativa ao terreno no eixo da latitude.
	 * @param height valor de nivelamento a ser aplicado na célula.
	 */

	public void setHeight(int x, int z, float height)
	{
		if (IntUtil.interval(x, 0, width - 1) && IntUtil.interval(z, 0, length - 1))
			heights[x][z] = height;
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("worldID", worldID);
		description.append("x", x);
		description.append("z", z);
		description.append("width", width);
		description.append("length", length);
		description.append("hasModel", model != null);

		return description.toString();
	}
}
