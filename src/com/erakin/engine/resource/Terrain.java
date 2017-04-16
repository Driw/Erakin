package com.erakin.engine.resource;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.lang.IntUtil;

import com.erakin.engine.lwjgl.GLBind;

/**
 * <h1>Terreno</h1>
 *
 * <p>Um terreno nada mais é do que um objeto que possui uma modelagem tri-dimensional.
 * A modelagem tri-dimensional será usada para representar todo o terreno respectivo.
 * Cada terreno tem uma identificação diferente de acordo com o mundo que pertence.
 * Também será registrado em um atributo a identificação do mundo que pertence.<p>
 *
 * @see GLBind
 * @see Model
 *
 * @author Andrew
 */

public class Terrain
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
	Model model;

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

	/**
	 * Terrenos são formados através de uma modelagem tri-dimensiona e usados para serem visualizados.
	 * @return aquisição da modelagem tri-dimensional respectiva a esse terreno.
	 */

	public Model getModel()
	{
		return model;
	}

	/**
	 * Permite definir qual será a modelagem tri-dimensional que o terreno irá usar.
	 * @param model referência da nova modelagem tri-dimensional a ser usada pelo terreno.
	 */

	public void setModel(Model model)
	{
		this.model = model;
	}

	/**
	 * Um mundo é formado por uma grande de terrenos, onde todos os terrenos possuem o mesmo tamanho.
	 * Coordenadas de um terreno permitem saber em que posição da grade de terrenos ele se encontra.
	 * @return aquisição da coordenada na longitude do terreno na grade de terrenos do mundo.
	 */

	public int getX()
	{
		return x;
	}

	/**
	 * Um mundo é formado por uma grande de terrenos, onde todos os terrenos possuem o mesmo tamanho.
	 * Coordenadas de um terreno permitem saber em que posição da grade de terrenos ele se encontra.
	 * @return aquisição da coordenada na latitude do terreno na grade de terrenos do mundo.
	 */

	public int getZ()
	{
		return z;
	}

	/**
	 * Terrenos são formados por diversas células (geralmente quadrados) formados por duas faces.
	 * @return aquisição do tamanho do terreno em células no eixo da longitude.
	 */

	public int getWidth()
	{
		return width;
	}

	/**
	 * Terrenos são formados por diversas células (geralmente quadrados) formados por duas faces.
	 * @return aquisição do tamanho do terreno em células no eixo da latitude.
	 */

	public int getLength()
	{
		return length;
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

	/**
	 * Terrenos podem usar uma textura única que permite visualizá-lo com cores que define sua altitude.
	 * Por exemplo: o nível mais baixo terá um tom azul em quanto o mais alto um tom vermelho.
	 * Logo todos os níveis do terreno terão uma cor relativa ao seu nível em degradê de azul e vermelho.
	 * @return aquisição da textura que irá representar o nivelamento do terreno em degradê.
	 */

	public Texture getHeightTexture()
	{
		return model == null ? null : model.getTexture();
	}
}
