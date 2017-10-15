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
 * <p>Esse tipo de mundo não se baseia em dados carregados a partir de um arquivo, mas sim da engine.
 * Os dados são criados após especificar quantos terrenos o mundo terá e qual o tamanho deles.</p>
 *
 * <p>Por ser um terreno bruto e criado pela engine, através dele poderemos modificar a forma dos terrenos.
 * Portanto, será a partir deste tipo de mundo que um editor de mundos/terrenos irá utilizar.
 * Além de gerar o terreno plano é possível alterar o tamanho de cada unidade do terreno.</p>
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
	 * Tamanho básico de um mundo bruto em bytes (não considera terrenos).
	 */
	public static final int BASE_BYTES = (Integer.BYTES * 2) + (Float.BYTES * 1) + TerrainDimension.BYTES;

	/**
	 * Quantidade mínima de terrenos permitidos na longitude (eixo X).
	 */
	public static final int MIN_WIDTH = 0;

	/**
	 * Quantidade máxima de terrenos permitidos na longitude (eixo X).
	 */
	public static final int MAX_WIDTH = 512;

	/**
	 * Quantidade mínima de terrenos permitidos na longitude (eixo Z).
	 */
	public static final int MIN_LENGTH = 0;

	/**
	 * Quantidade máxima de terrenos permitidos na longitude (eixo Z).
	 */
	public static final int MAX_LENGTH = 512;

	/**
	 * Tamanho mínimo para se definir a uma unidade de terreno.
	 */
	public static final float MIN_UNIT_SIZE = 0.1f;

	/**
	 * Tamanho máximo para se definir a uma unidade de terreno.
	 */
	public static final float MAX_UNIT_SIZE = 100f;

	/**
	 * Tamanho padrão para cada unidade do terreno (largura e altura).
	 */
	public static final float DEFAULT_UNIT_SIZE = 1f;


	/**
	 * Pré-fixo para identificação do mundo na engine.
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
	 * Tamanho de cada unidade do terreno no espaço.
	 */
	private float unitSize;

	/**
	 * Dimensionamento dos terrenos desse mundo.
	 */
	private TerrainDimension terrainDimension;

	/**
	 * Matriz para alocação dos terrenos do mundo.
	 */
	private RawTerrain terrains[][];

	/**
	 * Cria um novo mundo bruto em branco, portanto não terá terrenos/espaço.
	 * @param prefix pré-fixo para identificação do mundo pela engine.
	 */

	public RawWorld(String prefix)
	{
		this.prefix = prefix;

		unitSize = DEFAULT_UNIT_SIZE;
		terrains = new RawTerrain[0][0];
		terrainDimension = new TerrainDimension();
	}

	/**
	 * Calcula o tamanho base do mundo bruto mais a somatória dos terrenos.
	 * @return aquisição do espaço aproximado de ocupação em memória do objeto.
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
	 * Verifica se uma determinada posição de terreno existe nesse mundo.
	 * @param xTerrain posição do terreno no eixo da longitude (x).
	 * @param zTerrain posição do terreno no eixo da latitude (z).
	 * @return true se existir o terreno na posição ou false caso contrário.
	 */

	public boolean isTerrainCoordinate(int xTerrain, int zTerrain)
	{
		return MathsWorld.isTerrainCoordinate(width, length, xTerrain, zTerrain);
	}

	/**
	 * Verifica se para uma determinada coordenada do mundo existe uma terreno.
	 * @param xWorld coordenada do mundo no eixo da longitude (x).
	 * @param zWorld coordenada do mundo no eixo da latitude (z).
	 * @return true se existir um terreno nas coordenadas ou false caso contrário.
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
	 * Procedimento interno para realizar a validação da quantidade de terrenos no mundo.
	 * @param width largura do mundo, quantidade de terrenos no eixo da longitude (x).
	 * @param length comprimento do mundo, quantidade de terrenos no eixo da latitude (z).
	 */

	private void validateSize(int width, int length)
	{
		if (!IntUtil.interval(width, MIN_WIDTH, MAX_WIDTH))
			throw new TerrainRuntimeException("width %d não suportado (min: %d, max: %d)", width, MIN_WIDTH, MAX_WIDTH);

		if (!IntUtil.interval(length, MIN_LENGTH, MAX_LENGTH))
			throw new TerrainRuntimeException("length %d não suportado (min: %d, max: %d)", length, MIN_LENGTH, MAX_LENGTH);		
	}

	/**
	 * Altera o tamanho do mundo, determinando o número de terrenos em cada eixo.
	 * Ao alterar o tamanho do mundo, terrenos antigos são apagados e um novo é gerado.
	 * @param size quantidade de terrenos em ambos os eixos, longitude (x) e latitude (z).
	 */

	public void setSize(int size)
	{
		setSize(size, size);
	}

	/**
	 * Altera o tamanho do mundo, determinando o número de terrenos em cada eixo.
	 * Ao alterar o tamanho do mundo, terrenos antigos são apagados e um novo é gerado.
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
	 * Após liberar os tiles também libera a ocupação do terreno neste mundo.
	 * A liberação desses objetos consiste em definir o valor como null.
	 * <i>A ideia é que seja usado em mundos grandes, seguido de um <b>Garbage Collector</b></i>
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
	 * Procedimento interno que cria os terrenos, porém não gera o modelo.
	 * Após criar o terreno, especifica o tamanho de cada unidade e quantas unidades.
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
	 * Irá iterar todos os terrenos do mundo e para cada um será gerado um terreno plano.
	 * Aqui o modelo do terreno será gerado e então poderá ser renderizado na Engine.
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
	 * Irá alterar os vértices XYZ proporcionalmente, recalcula a normalização e mantém as coordenadas de textura.
	 * Mantém a referência do modelo do terreno, enviando os dados ao OpenGL para serem atualizados.
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
	 * Permite definir o tamanho de cada unidade de terreno no espaço.
	 * <b>Necessário</b> usar <code>genFlatTerrain()</code> ou <code>updateUnitSize()</code>.
	 * @param unitSize
	 */

	public void setUnitSize(float unitSize)
	{
		if (FloatUtil.interval(unitSize, MIN_UNIT_SIZE, MAX_UNIT_SIZE))
			this.unitSize = unitSize;
		else
			logWarning("tamanho para uniade de terreno não suportado (size: %.2f, min: %.2f, max: %.2f)", unitSize, MIN_UNIT_SIZE, MAX_UNIT_SIZE);
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
	 * O dimensionamento dos terrenos são repassados para todos os terrenos.
	 * Por tanto, se o mundo tiver o dimensionamento alterado todos os terrenos vão ser.
	 * Entretanto deverá ser gerado novamente um modelo para que funcione corretamente.
	 * @return aquisição do tamanho dos terrenos desse mundo (quantidade de unidades).
	 */

	public TerrainDimension getTerrainDimension()
	{
		return terrainDimension;
	}

	/**
	 * Um SimpleFlatTerrain é a representação de um terreno bruto, portanto não é carregado de um arquivo,
	 * mas sim gerado a partir das informações da engine, salvos nesse mundo.
	 * @param xTerrain posicionamento do terreno desejado no eixo da longitude (x).
	 * @param zTerrain posicionamento do terreno desejado no eixo da latitude (z).
	 * @return aquisição do terreno correspondente ao posicionamento especificado.
	 * @throws TerrainRuntimeException ocorre apenas se as coordenadas forem inválida.
	 */

	public RawTerrain getSimpleFlatTerrain(int xTerrain, int zTerrain)
	{
		if (MathsWorld.isTerrainCoordinate(width, length, xTerrain, zTerrain))
			return terrains[xTerrain][zTerrain];

		throw new TerrainRuntimeException("%d,%d não é um TerrainCell válido (width: %d, length: %d)", xTerrain, zTerrain, width, length);
	}

	/**
	 * Permite definir uma textura para ser utilizada na renderização do mundo.
	 * Essa textura será usada quando for desejado ver a elevação do terreno.
	 * @param texture referência de uma textura já carregada no sistema.
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
