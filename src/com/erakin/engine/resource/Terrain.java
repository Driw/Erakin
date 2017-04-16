package com.erakin.engine.resource;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.lang.IntUtil;

import com.erakin.engine.lwjgl.GLBind;

/**
 * <h1>Terreno</h1>
 *
 * <p>Um terreno nada mais � do que um objeto que possui uma modelagem tri-dimensional.
 * A modelagem tri-dimensional ser� usada para representar todo o terreno respectivo.
 * Cada terreno tem uma identifica��o diferente de acordo com o mundo que pertence.
 * Tamb�m ser� registrado em um atributo a identifica��o do mundo que pertence.<p>
 *
 * @see GLBind
 * @see Model
 *
 * @author Andrew
 */

public class Terrain
{
	/**
	 * Identifica��o do mundo no sistema.
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
	 * Quantidade de c�lulas no eixo da longitude.
	 */
	int width;

	/**
	 * Quantidade de c�lulas no eixo da latitude.
	 */
	int length;

	/**
	 * N�vel de eleva��o do terreno.
	 */
	float heights[][];

	/**
	 * Modelagem tri-dimensional do terreno.
	 */
	Model model;

	/**
	 * Constr�i um novo terreno, sendo necess�rio definir suas coordenadas no mundo.
	 * As coordenadas s�o referentes a grade de terrenos que um mundo possui.
	 * @param x coordenada no eixo da longitude do terreno no mundo.
	 * @param y coordenada no eixo da latitude do terreno no mundo.
	 * @param width quantidade de c�lulas que ir� compor o terreno na largura.
	 * @param length quantidade de c�lulas que ir� compor o terreno no comprimento.
	 */

	public Terrain(int x, int y, int width, int length)
	{
		this.x = x;
		this.z = y;

		setSize(width, length);
	}

	/**
	 * Terrenos s�o armazenados dentro de mundos, cada mundo possui uma identifica��o �nica.
	 * @return aquisi��o da identifica��o do mundo a qual esse terreno pertence.
	 */

	public int getWorldID()
	{
		return worldID;
	}

	/**
	 * Terrenos s�o formados atrav�s de uma modelagem tri-dimensiona e usados para serem visualizados.
	 * @return aquisi��o da modelagem tri-dimensional respectiva a esse terreno.
	 */

	public Model getModel()
	{
		return model;
	}

	/**
	 * Permite definir qual ser� a modelagem tri-dimensional que o terreno ir� usar.
	 * @param model refer�ncia da nova modelagem tri-dimensional a ser usada pelo terreno.
	 */

	public void setModel(Model model)
	{
		this.model = model;
	}

	/**
	 * Um mundo � formado por uma grande de terrenos, onde todos os terrenos possuem o mesmo tamanho.
	 * Coordenadas de um terreno permitem saber em que posi��o da grade de terrenos ele se encontra.
	 * @return aquisi��o da coordenada na longitude do terreno na grade de terrenos do mundo.
	 */

	public int getX()
	{
		return x;
	}

	/**
	 * Um mundo � formado por uma grande de terrenos, onde todos os terrenos possuem o mesmo tamanho.
	 * Coordenadas de um terreno permitem saber em que posi��o da grade de terrenos ele se encontra.
	 * @return aquisi��o da coordenada na latitude do terreno na grade de terrenos do mundo.
	 */

	public int getZ()
	{
		return z;
	}

	/**
	 * Terrenos s�o formados por diversas c�lulas (geralmente quadrados) formados por duas faces.
	 * @return aquisi��o do tamanho do terreno em c�lulas no eixo da longitude.
	 */

	public int getWidth()
	{
		return width;
	}

	/**
	 * Terrenos s�o formados por diversas c�lulas (geralmente quadrados) formados por duas faces.
	 * @return aquisi��o do tamanho do terreno em c�lulas no eixo da latitude.
	 */

	public int getLength()
	{
		return length;
	}

	/**
	 * Permite definir qual o tamanho que o terreno ir� possuir em c�lulas.
	 * @param width quantidade de c�lulas que ir� compor o terreno na largura.
	 * @param length quantidade de c�lulas que ir� compor o terreno no comprimento.
	 */

	void setSize(int width, int length)
	{
		this.width = width;
		this.length = length;
		this.heights = new float[width][length];
	}

	/**
	 * O n�vel de eleva��o permite que os terrenos se tornem mais reais.
	 * Al�m disso ajuda na cria��o da normaliza��o para ser iluminado corretamente.
	 * @param x coordenada relativa ao terreno no eixo da longitude.
	 * @param z coordenada relativa ao terreno no eixo da latitude.
	 * @return aquisi��o do n�vel de eleva��o do terreno na coordenada passada.
	 */

	public float getHeight(int x, int z)
	{
		if (IntUtil.interval(x, 0, width) && IntUtil.interval(z, 0, length))
			return heights[x][z];

		return 0f;
	}

	/**
	 * Define o n�vel de eleva��o de uma determinada c�lula do terreno.
	 * @param x coordenada relativa ao terreno no eixo da longitude.
	 * @param z coordenada relativa ao terreno no eixo da latitude.
	 * @param height valor de nivelamento a ser aplicado na c�lula.
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
	 * Terrenos podem usar uma textura �nica que permite visualiz�-lo com cores que define sua altitude.
	 * Por exemplo: o n�vel mais baixo ter� um tom azul em quanto o mais alto um tom vermelho.
	 * Logo todos os n�veis do terreno ter�o uma cor relativa ao seu n�vel em degrad� de azul e vermelho.
	 * @return aquisi��o da textura que ir� representar o nivelamento do terreno em degrad�.
	 */

	public Texture getHeightTexture()
	{
		return model == null ? null : model.getTexture();
	}
}
