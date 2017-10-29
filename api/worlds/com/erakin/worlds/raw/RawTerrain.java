package com.erakin.worlds.raw;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.SizeUtil;
import org.diverproject.util.collection.List;
import org.diverproject.util.lang.IntUtil;

import com.erakin.api.render.ModelRender;
import com.erakin.api.render.TerrainRender;
import com.erakin.api.render.WorldRender;
import com.erakin.api.resources.Texture;
import com.erakin.api.resources.world.TerrainRuntimeException;

/**
 * <h1>Terreno Bruto</h1>
 *
 * <h1>Um terreno bruto possui informações de um terreno e pode ser renderizado quando o modelo for gerado.
 * Diferente de outros terrenos, esse terreno não precisa de dados de um arquivo para ser criado.
 * Ele pode ser criado pela Engine a partir das configurações especificadas em um mundo bruto.</h1>
 *
 * </p>A ideia desta classe é permitir a criação de um editor de mundos/terrenos na Engine.
 * Outras formas de leitura de mundos foram feitas para testar a renderização dos mundos.
 * Através desse objeto será possível criar do zero e modificar as informações de um mundo.</h1>
 *
 * @see TerrainRender
 * @see WorldRender
 * @see RawTerrainModel
 * @see Texture
 * @see RawTerrainUnit
 *
 * @author Andrew
 */

public class RawTerrain implements TerrainRender
{
	/**
	 * Tamanho mínimo permitido na largura de um terreno.
	 */
	private static final int MIN_WIDTH = 0;

	/**
	 * Tamanho máximo permitido na largura de um terreno.
	 */
	private static final int MAX_WIDTH = 512;

	/**
	 * Tamanho mínimo permitido no comprimento de um terreno.
	 */
	private static final int MIN_LENGTH = 0;

	/**
	 * Tamanho máximo permitido no comprimento de um terreno.
	 */
	private static final int MAX_LENGTH = 512;


	/**
	 * Posicionamento do terreno no eixo da longitude (x) do mundo.
	 */
	private int x;

	/**
	 * Posicionamento do terreno no eixo da latitude (z) do mundo.
	 */
	private int z;

	/**
	 * Largura do terreno em unidades.
	 */
	private int width;

	/**
	 * Comprimento do terreno em unidades.
	 */
	private int length;

	/**
	 * Modelo do terreno (necessário gerar).
	 */
	private RawTerrainModel model;

	/**
	 * Mundo renderizável que detém esse terreno.
	 */
	WorldRender world;

	/**
	 * Textura usada no modo de visualização da elevação do terreno.
	 */
	Texture heightTexture;

	/**
	 * Matriz para posicionamento dos dados de cada unidade do terreno.
	 */
	RawTerrainUnit tiles[][];

	/**
	 * Cria uma nova instância de um terreno bruto com base nas informações abaixo:
	 * Por padrão esses terrenos são criados sem nenhum espaço (width: 0, length: 0).
	 * @param world referência do mundo renderizável que detém o terreno.
	 * @param x posicionamento do terreno no mundo no eixo da longitude (x).
	 * @param z posicionamento do terreno no mundo no eixo da latitude (z).
	 */

	public RawTerrain(WorldRender world, int x, int z)
	{
		this.x = x;
		this.z = z;
		this.world = world;
		this.model = new RawTerrainModel(this);
		this.tiles = new RawTerrainUnit[0][0];
	}

	/**
	 * Calcula o tamanho do objeto em bytes com base no tamanho do modelo e unidades.
	 * Vale lembrar que os dados do modelo ficam no OpenGL e unidades no Java,
	 * por tanto um terreno com modelo terá informações duplicadas (tamanho em dobro).
	 * @return aquisição do tamanho aproximado de ocupação em memória do terreno.
	 */

	public int sizeof()
	{
		return model.sizeof() + (width * length * RawTerrainUnit.BYTES) + 20;
	}

	/**
	 * Verifica se uma determinada coordenada de unidade de terreno é válida.
	 * @param x coordenada da unidade no terreno em relação ao eixo da longitude (x).
	 * @param z coordenada da unidade no terreno em relação ao eixo da longitude (z).
	 * @return true se existir a unidade de terreno ou false caso contrário.
	 */

	public boolean isCoordinate(int x, int z)
	{
		return	IntUtil.interval(x, 0, width - 1) &&
				IntUtil.interval(z, 0, length - 1);
	}

	/**
	 * Procedimento interno para realizar a validação da quantidade de unidades no terreno.
	 * @param width largura do terreno, quantidade de unidades no eixo da longitude (x).
	 * @param length comprimento do terreno, quantidade de unidades no eixo da latitude (z).
	 */

	private void validateSize(int width, int length)
	{
		if (!IntUtil.interval(width, MIN_WIDTH, MAX_WIDTH))
			throw new TerrainRuntimeException("width %d não suportado (min: %d, max: %d)", width, MIN_WIDTH, MAX_WIDTH);

		if (!IntUtil.interval(length, MIN_LENGTH, MAX_LENGTH))
			throw new TerrainRuntimeException("length %d não suportado (min: %d, max: %d)", length, MIN_LENGTH, MAX_LENGTH);		
	}

	/**
	 * Altera o tamanho do terreno, determinando o número de unidades em cada eixo.
	 * Ao alterar o tamanho do terrenos, unidades antigas são apagados e uma nova é gerado.
	 * @param size quantidade de unidades em ambos os eixos, longitude (x) e latitude (z).
	 */

	public void setSize(int size)
	{
		setSize(size, size);
	}

	/**
	 * Altera o tamanho do terreno, determinando o número de unidades em cada eixo.
	 * Ao alterar o tamanho do terrenos, unidades antigas são apagados e uma nova é gerado.
	 * @param width largura do terreno, quantidade de unidades no eixo da longitude (x).
	 * @param length comprimento do terreno, quantidade de unidades no eixo da latitude (z).
	 */

	public void setSize(int width, int length)
	{
		validateSize(width, length);
		freeTiles();

		tiles = new RawTerrainUnit[(this.width = width)][(this.length = length)];

		newTiles();
	}

	/**
	 * Procedimento interno que itera cada unidade do terreno e libera o mesmo.
	 * Após liberar os tiles também libera a ocupação da unidade neste terreno.
	 * A liberação desses objetos consiste em definir o valor como null.
	 * <i>A ideia é que seja usado em terrenos grandes, seguido de um <b>Garbage Collector</b></i>
	 */

	void freeTiles()
	{
		for (int x = 0; x < width; x++)
			for (int z = 0; z < length; z++)
				tiles[x][z] = null;
	}

	/**
	 * Procedimento interno que cria as unidades, porém não gera o modelo.
	 */

	void newTiles()
	{
		int offset = 0;

		for (int x = 0; x < width; x++)
			for (int z = 0; z < length; z++)
				tiles[x][z] = new RawTerrainUnit(offset++, CELL_CHANGED);
	}

	/**
	 * Gera um terreno completamente plano de forma que possa ser renderizado na Engine.
	 * As unidades são alocadas em matriz, com tamanho <code>unitSize</code>.
	 */

	public void genFlatTerrain()
	{
		for (int x = 0; x < width; x++)
			for (int z = 0; z < length; z++)
			{
				float baseX = x * world.getUnitSize();
				float baseZ = z * world.getUnitSize();

				RawTerrainUnit cell = tiles[x][z];
				cell.getSouthWest().set(baseX, 0f, baseZ);
				cell.getSouthEast().set(baseX + world.getUnitSize(), 0f, baseZ);
				cell.getNorthEast().set(baseX + world.getUnitSize(), 0f, baseZ + world.getUnitSize());
				cell.getNorthWest().set(baseX, 0f, baseZ + world.getUnitSize());
			}

		model.generateFullModel();
	}

	/**
	 * Atualiza todas as unidades para que comecem a assumir o novo tamanho especificado.
	 * Irá alterar os vértices XYZ proporcionalmente, recalcula a normalização e mantém as coordenadas de textura.
	 * Mantém a referência do modelo do terreno, enviando os dados ao OpenGL para serem atualizados.
	 */

	public void updateUnitSize()
	{
		for (int x = 0; x < width; x++)
			for (int z = 0; z < length; z++)
			{
				float baseX = x * world.getUnitSize();
				float baseZ = z * world.getUnitSize();

				RawTerrainUnit cell = tiles[x][z];
				cell.getSouthWest().setX(baseX);
				cell.getSouthWest().setZ(baseZ);
				cell.getSouthEast().setX(baseX + world.getUnitSize());
				cell.getSouthEast().setZ(baseZ);
				cell.getNorthEast().setX(baseX + world.getUnitSize());
				cell.getNorthEast().setZ(baseZ + world.getUnitSize());
				cell.getNorthWest().setX(baseX);
				cell.getNorthWest().setZ(baseZ + world.getUnitSize());
			}

		model.updateFullModel();
	}

	/**
	 * Procedimento interno para calcular o posicionamento de uma unidade.
	 * Utilizado por exemplo como índice base nos vetores de vértices.
	 * @param x coordenada na unidade de terreno no eixo da longitude (x).
	 * @param z coordenada na unidade de terreno no eixo da latitude (z).
	 * @return aquisição do posicionamento de índice base da unidade.
	 */

	int offset(int x, int z)
	{
		return (z * width) + x;
	}

	/**
	 * Listener interno para detectar quando os dados de uma unidade de terreno é alterada.
	 * Assim será possível informar ao OpenGL o mínimo de informações possíveis,
	 * reduzindo o tempo para atualizar o modelo do terreno que foi alterado.
	 */

	private Vector3fListener CELL_CHANGED = new Vector3fListener()
	{
		@Override
		public void onSet(RawTerrainUnit source, float x, float y, float z)
		{
			// TODO
		}
	};

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
	public ModelRender getModel()
	{
		return model;
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
	public Texture getHeightTexture()
	{
		return heightTexture;
	}

	/**
	 * Essa textura só será utilizado quando o terreno estiver no modo de visualização da elevação do terreno.
	 * @param texture referência da textura carregada na Engine para elevação do terreno.
	 */

	public void setHeightTexture(Texture texture)
	{
		this.heightTexture = texture;
	}

	@Override
	public boolean isHeightTexture()
	{
		return true; // FIXME por enquanto forçar textura de relevo para funcionar como antes
	}

	@Override
	public List<Texture> getTextures()
	{
		return null; // FIXME implementar com urgência!!
	}

	/**
	 * Terrenos são formados por diversas unidades, cada unidade possui informações como:
	 * posicionamento do vértices, normalização e coordenadas de textura.
	 * @param x coordenada da unidade de terreno no eixo da longitude (x).
	 * @param z coordenada da unidade de terreno no eixo da latitude (z).
	 * @return aquisição da unidade de terreno correspondente a coordenada.
	 * @throws TerrainRuntimeException ocorre apenas se as coordenadas forem inválidas.
	 */

	public RawTerrainUnit getUnit(int x, int z)
	{
		if (isCoordinate(x, z))
			return tiles[x][z];

		throw new TerrainRuntimeException("%d,%d não é um TerrainCell válido (width: %d, length: %d)", x, z, width, length);
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("z", getX());
		description.append("x", getZ());
		description.append("width", getWidth());
		description.append("length", getLength());
		description.append("unitSize", world.getUnitSize());
		description.append("generated", model.isGenerated());
		description.append("modelSizeof", SizeUtil.toString(model.sizeof()));
		description.append("sizeof", SizeUtil.toString(sizeof()));

		return description.toString();
	}
}
