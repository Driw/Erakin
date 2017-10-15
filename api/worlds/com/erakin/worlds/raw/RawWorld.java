package com.erakin.worlds.raw;

import static com.erakin.api.lwjgl.math.MathsWorld.calcInTerrainCoord;
import static com.erakin.api.lwjgl.math.MathsWorld.calcTerrainCoord;
import static org.diverproject.log.LogSystem.logWarning;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.SizeUtil;
import org.diverproject.util.lang.FloatUtil;
import org.diverproject.util.lang.IntUtil;

import com.erakin.api.lwjgl.math.MathsWorld;
import com.erakin.api.render.TerrainRender;
import com.erakin.api.render.WorldRender;
import com.erakin.api.resources.Texture;
import com.erakin.api.resources.world.TerrainDimension;
import com.erakin.api.resources.world.TerrainRuntimeException;

/**
 * <h1>Mundo Bruto</h1>
 *
 * <p>Esse tipo de mundo n�o se baseia em dados carregados a partir de um arquivo, mas sim da engine.
 * Os dados s�o criados ap�s especificar quantos terrenos o mundo ter� e qual o tamanho deles.</p>
 *
 * <p>Por ser um terreno bruto e criado pela engine, atrav�s dele poderemos modificar a forma dos terrenos.
 * Portanto, ser� a partir deste tipo de mundo que um editor de mundos/terrenos ir� utilizar.
 * Al�m de gerar o terreno plano � poss�vel alterar o tamanho de cada unidade do terreno.</p>
 *
 * @see MathsWorld
 * @see TerrainRender
 * @see WorldRender
 * @see Texture
 * @see TerrainDimension
 *
 * @author Andrew
 */

public class RawWorld implements WorldRender
{
	/**
	 * Tamanho b�sico de um mundo bruto em bytes (n�o considera terrenos).
	 */
	public static final int BASE_BYTES = (Integer.BYTES * 2) + (Float.BYTES * 1) + TerrainDimension.BYTES;

	/**
	 * Quantidade m�nima de terrenos permitidos na longitude (eixo X).
	 */
	public static final int MIN_WIDTH = 0;

	/**
	 * Quantidade m�xima de terrenos permitidos na longitude (eixo X).
	 */
	public static final int MAX_WIDTH = 512;

	/**
	 * Quantidade m�nima de terrenos permitidos na longitude (eixo Z).
	 */
	public static final int MIN_LENGTH = 0;

	/**
	 * Quantidade m�xima de terrenos permitidos na longitude (eixo Z).
	 */
	public static final int MAX_LENGTH = 512;

	/**
	 * Tamanho m�nimo para se definir a uma unidade de terreno.
	 */
	public static final float MIN_UNIT_SIZE = 0.1f;

	/**
	 * Tamanho m�ximo para se definir a uma unidade de terreno.
	 */
	public static final float MAX_UNIT_SIZE = 100f;

	/**
	 * Tamanho padr�o para cada unidade do terreno (largura e altura).
	 */
	public static final float DEFAULT_UNIT_SIZE = 1f;


	/**
	 * Pr�-fixo para identifica��o do mundo na engine.
	 */
	private String prefix;

	/**
	 * Quantidade de terrenos no eixo da longitude.
	 */
	private int width;

	/**
	 * Quantidade de terrenos no eixo da latitude.
	 */
	private int length;

	/**
	 * Tamanho de cada unidade do terreno no espa�o.
	 */
	private float unitSize;

	/**
	 * Dimensionamento dos terrenos desse mundo.
	 */
	private TerrainDimension terrainDimension;

	/**
	 * Matriz para aloca��o dos terrenos do mundo.
	 */
	private RawTerrain terrains[][];

	/**
	 * Cria um novo mundo bruto em branco, portanto n�o ter� terrenos/espa�o.
	 * @param prefix pr�-fixo para identifica��o do mundo pela engine.
	 */

	public RawWorld(String prefix)
	{
		this.prefix = prefix;

		unitSize = DEFAULT_UNIT_SIZE;
		terrains = new RawTerrain[0][0];
		terrainDimension = new TerrainDimension();
	}

	/**
	 * Calcula o tamanho base do mundo bruto mais a somat�ria dos terrenos.
	 * @return aquisi��o do espa�o aproximado de ocupa��o em mem�ria do objeto.
	 */

	public int sizeof()
	{
		int sizeof = 0;

		for (int x = 0; x < width; x++)
			for (int z = 0; z < length; z++)
				sizeof += terrains[x][z].sizeof();

		return sizeof + BASE_BYTES;
	}

	/**
	 * Verifica se uma determinada posi��o de terreno existe nesse mundo.
	 * @param xTerrain posi��o do terreno no eixo da longitude (x).
	 * @param zTerrain posi��o do terreno no eixo da latitude (z).
	 * @return true se existir o terreno na posi��o ou false caso contr�rio.
	 */

	public boolean isTerrainCoordinate(int xTerrain, int zTerrain)
	{
		return MathsWorld.isTerrainCoordinate(width, length, xTerrain, zTerrain);
	}

	/**
	 * Verifica se para uma determinada coordenada do mundo existe uma terreno.
	 * @param xWorld coordenada do mundo no eixo da longitude (x).
	 * @param zWorld coordenada do mundo no eixo da latitude (z).
	 * @return true se existir um terreno nas coordenadas ou false caso contr�rio.
	 */

	public boolean isWorldCoordinate(int xWorld, int zWorld)
	{
		int xTerrain = calcTerrainCoord(width, xWorld);
		int zTerrain = calcTerrainCoord(length, zWorld);

		if (!isTerrainCoordinate(xTerrain, zTerrain))
			return false;

		RawTerrain terrain = getSimpleFlatTerrain(xTerrain, zTerrain);

		if (terrain == null)
			return false;

		int xInTerrain = calcInTerrainCoord(terrainDimension.getWidth(), xWorld);
		int zInTerrain = calcInTerrainCoord(terrainDimension.getLength(), zWorld);

		return terrain.isCoordinate(xInTerrain, zInTerrain);
	}

	/**
	 * Procedimento interno para realizar a valida��o da quantidade de terrenos no mundo.
	 * @param width largura do mundo, quantidade de terrenos no eixo da longitude (x).
	 * @param length comprimento do mundo, quantidade de terrenos no eixo da latitude (z).
	 */

	private void validateSize(int width, int length)
	{
		if (!IntUtil.interval(width, MIN_WIDTH, MAX_WIDTH))
			throw new TerrainRuntimeException("width %d n�o suportado (min: %d, max: %d)", width, MIN_WIDTH, MAX_WIDTH);

		if (!IntUtil.interval(length, MIN_LENGTH, MAX_LENGTH))
			throw new TerrainRuntimeException("length %d n�o suportado (min: %d, max: %d)", length, MIN_LENGTH, MAX_LENGTH);		
	}

	/**
	 * Altera o tamanho do mundo, determinando o n�mero de terrenos em cada eixo.
	 * Ao alterar o tamanho do mundo, terrenos antigos s�o apagados e um novo � gerado.
	 * @param size quantidade de terrenos em ambos os eixos, longitude (x) e latitude (z).
	 */

	public void setSize(int size)
	{
		setSize(size, size);
	}

	/**
	 * Altera o tamanho do mundo, determinando o n�mero de terrenos em cada eixo.
	 * Ao alterar o tamanho do mundo, terrenos antigos s�o apagados e um novo � gerado.
	 * @param width largura do mundo, quantidade de terrenos no eixo da longitude (x).
	 * @param length comprimento do mundo, quantidade de terrenos no eixo da latitude (z).
	 */

	public void setSize(int width, int length)
	{
		validateSize(width, length);
		freeTerrains();

		terrains = new RawTerrain[(this.width = width)][(this.length = length)];

		newTerrains();
		genFlatTerrains();
	}

	/**
	 * Procedimento interno que itera cada terreno liberando cada Tile do mesmo.
	 * Ap�s liberar os tiles tamb�m libera a ocupa��o do terreno neste mundo.
	 * A libera��o desses objetos consiste em definir o valor como null.
	 * <i>A ideia � que seja usado em mundos grandes, seguido de um <b>Garbage Collector</b></i>
	 */

	private void freeTerrains()
	{
		for (int x = 0; x < width; x++)
			for (int z = 0; z < length; z++)
			{
				terrains[x][z].freeTiles();
				terrains[x][z] = null;
			}
	}

	/**
	 * Procedimento interno que cria os terrenos, por�m n�o gera o modelo.
	 * Ap�s criar o terreno, especifica o tamanho de cada unidade e quantas unidades.
	 */

	public void newTerrains()
	{
		for (int x = 0; x < width; x++)
			for (int z = 0; z < length; z++)
			{
				RawTerrain terrain = new RawTerrain(this, x, z);
				terrain.setSize(terrainDimension.getWidth(), terrainDimension.getLength());
				terrains[x][z] = terrain;
			}
	}

	/**
	 * Ir� iterar todos os terrenos do mundo e para cada um ser� gerado um terreno plano.
	 * Aqui o modelo do terreno ser� gerado e ent�o poder� ser renderizado na Engine.
	 */

	public void genFlatTerrains()
	{
		for (int x = 0; x < width; x++)
			for (int z = 0; z < length; z++)
			{
				RawTerrain terrain = terrains[x][z];

				if (terrain != null)
					terrain.genFlatTerrain();
			}
	}

	/**
	 * Atualiza todos os terrenos para que comecem a assumir um novo tamanho de unidade para terreno.
	 * Ir� alterar os v�rtices XYZ proporcionalmente, recalcula a normaliza��o e mant�m as coordenadas de textura.
	 * Mant�m a refer�ncia do modelo do terreno, enviando os dados ao OpenGL para serem atualizados.
	 */

	public void updateUnitSize()
	{
		for (int x = 0; x < width; x++)
			for (int z = 0; z < length; z++)
			{
				RawTerrain terrain = terrains[x][z];

				if (terrain != null)
					terrain.updateUnitSize();
			}
	}

	@Override
	public String getPrefix()
	{
		return prefix;
	}

	@Override
	public float getUnitSize()
	{
		return unitSize;
	}

	/**
	 * Permite definir o tamanho de cada unidade de terreno no espa�o.
	 * <b>Necess�rio</b> usar <code>genFlatTerrain()</code> ou <code>updateUnitSize()</code>.
	 * @param unitSize
	 */

	public void setUnitSize(float unitSize)
	{
		if (FloatUtil.interval(unitSize, MIN_UNIT_SIZE, MAX_UNIT_SIZE))
			this.unitSize = unitSize;
		else
			logWarning("tamanho para uniade de terreno n�o suportado (size: %.2f, min: %.2f, max: %.2f)", unitSize, MIN_UNIT_SIZE, MAX_UNIT_SIZE);
	}

	@Override
	public int getTerrainWidth()
	{
		return terrainDimension.getWidth();
	}

	@Override
	public int getTerrainLength()
	{
		return terrainDimension.getLength();
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
	public TerrainRender getTerrain(int xTerrain, int zTerrain)
	{
		return getSimpleFlatTerrain(xTerrain, zTerrain);
	}

	/**
	 * O dimensionamento dos terrenos s�o repassados para todos os terrenos.
	 * Por tanto, se o mundo tiver o dimensionamento alterado todos os terrenos v�o ser.
	 * Entretanto dever� ser gerado novamente um modelo para que funcione corretamente.
	 * @return aquisi��o do tamanho dos terrenos desse mundo (quantidade de unidades).
	 */

	public TerrainDimension getTerrainDimension()
	{
		return terrainDimension;
	}

	/**
	 * Um SimpleFlatTerrain � a representa��o de um terreno bruto, portanto n�o � carregado de um arquivo,
	 * mas sim gerado a partir das informa��es da engine, salvos nesse mundo.
	 * @param xTerrain posicionamento do terreno desejado no eixo da longitude (x).
	 * @param zTerrain posicionamento do terreno desejado no eixo da latitude (z).
	 * @return aquisi��o do terreno correspondente ao posicionamento especificado.
	 * @throws TerrainRuntimeException ocorre apenas se as coordenadas forem inv�lida.
	 */

	public RawTerrain getSimpleFlatTerrain(int xTerrain, int zTerrain)
	{
		if (MathsWorld.isTerrainCoordinate(width, length, xTerrain, zTerrain))
			return terrains[xTerrain][zTerrain];

		throw new TerrainRuntimeException("%d,%d n�o � um TerrainCell v�lido (width: %d, length: %d)", xTerrain, zTerrain, width, length);
	}

	/**
	 * Permite definir uma textura para ser utilizada na renderiza��o do mundo.
	 * Essa textura ser� usada quando for desejado ver a eleva��o do terreno.
	 * @param texture refer�ncia de uma textura j� carregada no sistema.
	 */

	public void setHeightTexture(Texture texture)
	{
		for (int x = 0; x < width; x++)
			for (int z = 0; z < length; z++)
				terrains[x][z].setHeightTexture(texture);
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("prefix", getPrefix());
		description.append("width", getWidth());
		description.append("length", getLength());
		description.append("unitSize", getUnitSize());
		description.append("sizeof", SizeUtil.toString(sizeof()));

		return description.toString();
	}
}
