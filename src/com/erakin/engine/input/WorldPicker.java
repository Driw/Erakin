package com.erakin.engine.input;

import static com.erakin.engine.EngineUtil.newVector3f;
import static org.diverproject.util.Util.nameOf;

import org.diverproject.util.ObjectDescription;
import org.lwjgl.util.vector.Vector3f;

import com.erakin.api.render.TerrainRender;
import com.erakin.api.render.WorldRender;
import com.erakin.api.resources.world.WorldRuntimeException;
import com.erakin.engine.EngineUtil;
import com.erakin.engine.camera.Camera;

/**
 * <h1>Seletor da Projeção do Mouse no Mundo</h1>
 *
 * <p>Classe responsável por criar usar uma projeção em direção ao cursor do mouse com relação a posição da câmera.
 * Através dessa projeção e com duas variáveis é calculado em que lugar do mundo (terreno) o mouse está apontado.
 * Das duas variáveis, uma permite definir o nível de precisão do cursor no espaço e outra a distância limite.</p>
 *
 * <p>Aumentar a precisão também aumenta o tempo de processamento, porém torna a posição do cursor no espaço preciso.
 * Já a distância limite não influencia no tempo de processamento mas sim para encontrar a localização do cursor.</p>
 *
 * @see MousePicker
 * @see Camera
 * @see WorldRender
 *
 * @author Andrew
 */

public class WorldPicker extends MousePicker
{
	/**
	 * Valor padrão da precisão para localizar o cursor no mundo.
	 */
	public static final int DEFAULT_RECURSION_COUNT = 200;

	/**
	 * Valor padrão da distância limite entre a câmera e o cursor no mundo.
	 */
	public static final float DEFAULT_RAY_RANGE = 30f;


	/**
	 * Referência do mundo renderizável que será considerado.
	 */
	private WorldRender world;

	/**
	 * Vetor contendo a localização no espaço do cursos no mundo.
	 */
	private Vector3f currentTerrainPoint;

	/**
	 * Precisão para localizar o cursor no mundo.
	 */
	private int recursionCount;

	/**
	 * Distância limite entre a câmera e o cursor no mundo.
	 */
	private float rayRange;

	/**
	 * Cria uma nova instância de um seletor da projeção do mouse feita no mundo renderizado.
	 * @param camera referência da câmera usada para visualização do mundo na tela.
	 */

	public WorldPicker(Camera camera)
	{
		super(camera, EngineUtil.getProjectionMatrix());

		setRayRange(DEFAULT_RAY_RANGE);
		setRecursionCount(DEFAULT_RECURSION_COUNT);
	}

	@Override
	public void update()
	{
		super.update();

		if (intersectionInRange(0, rayRange, getRay()))
			currentTerrainPoint = binarySearch(0, 0, rayRange, getRay());
		else
			currentTerrainPoint = null;
	}

	/**
	 * Verifica se a superfície de um terreno está entre uma determinada faixa da projeção.
	 * @param start distância inicial da busca pela intersecção da projeção com o terreno.
	 * @param finish distância final da busca pela intersecção da projeção com o terreno.
	 * @param ray vetor com a direção da projeção do cursor do mouse em relação ao espaço.
	 * @return true se o terreno estiver nessa faixa de projeção ou false caso contrário.
	 */

	private boolean intersectionInRange(float start, float finish, Vector3f ray)
	{
		Vector3f startPoint = getPointOnRay(ray, start);
		Vector3f endPoint = getPointOnRay(ray, finish);

		return !isUnderGround(startPoint) && isUnderGround(endPoint);
	}

	/**
	 * Calcula um ponto no espaço de acordo com a linha de projeção do cursor no espaço e uma distância.
	 * @param ray vetor com a direção da projeção do cursor do mouse em relação ao espaço.
	 * @param distance a que distância a projeção está sendo emitida para definir o ponto no espaço.
	 * @return aquisição da localização do ponto projetado no espaço conforme parâmetros.
	 */

	private Vector3f getPointOnRay(Vector3f ray, float distance)
	{
		Vector3f start = newVector3f(getCamera());
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);

		return Vector3f.add(start, scaledRay, null);
	}

	/**
	 * Realiza uma busca binária para localização da projeção do mouse no espaço do mundo conforme especificações.
	 * A busca é feita de forma recursiva de acordo com o nível de precisão e distância limite definidas.
	 * @param count quantas vezes a já foi feita a busca binária recursiva para verificar o limite.
	 * @param start distância inicial da busca binária em relação a posição da câmera.
	 * @param finish distância final da busca binária em relação a posição da câmera.
	 * @param ray vetor com a direção da projeção do cursor do mouse em relação ao espaço.
	 * @return aquisição da localização de proximidade do cursor no espaço feito na busca.
	 */

	private Vector3f binarySearch(int count, float start, float finish, Vector3f ray)
	{
		float half = start + ((finish - start) / 2f);

		if (count >= recursionCount)
		{
			Vector3f endPoint = getPointOnRay(ray, half);
			TerrainRender terrain = getTerrain(endPoint.getX(), endPoint.getZ());

			return terrain != null ? endPoint : null;
		}

		return	intersectionInRange(start, half, ray) ?
				binarySearch(++count, start, half, ray) :
				binarySearch(++count, half, finish, ray);
	}

	/**
	 * Verifica se uma determinada coordenada no espaço está acima ou abaixo do espaço do terreno renderizado.
	 * Caso não haja nenhum terreno na direção da projeção será considerado que não á relevo (altura zero).
	 * @param espaceCoord coordenadas no espaço do qual deve ser feito o teste.
	 * @return true se as coordenadas estão abaixo do terreno ou true se estiver acima.
	 */

	private boolean isUnderGround(Vector3f espaceCoord)
	{
		float height = 0;
		TerrainRender terrain = getTerrain(espaceCoord.getX(), espaceCoord.getZ());

		if (terrain != null)
			height = terrain.getHeightOfTerrain(espaceCoord.getX(), espaceCoord.getZ());

		return espaceCoord.y < height;
	}

	/**
	 * Procedimento interno usado para selecionar qual o terreno que está sendo apontado pelo cursor.
	 * @param worldX coordenada no espaço do mundo em relação ao eixo da longitude (X).
	 * @param worldZ coordenada no espaço do mundo em relação ao eixo da latitude (Z).
	 * @return aquisição do terreno respectivo as coordenadas ou null caso não encontrado.
	 */

	private TerrainRender getTerrain(float worldX, float worldZ)
	{
		int xTerrain = (int) (worldX / world.getTerrainWidth());
		int zTerrain = (int) (worldZ / world.getTerrainLength());

		try {
			return world.getTerrain(xTerrain, zTerrain);
		} catch (WorldRuntimeException e) {
			return null;
		}
	}

	/**
	 * O objeto desse seletor é criar um vetor com a localização no espaço de onde o cursor do mouse está apontado.
	 * Nesse caso a localização espera ser projetada para alguma superfície de um terreno do mundo renderizado.
	 * @return aquisição da localização do cursor no espaço ou null caso não esteja apontado ou no alcance.
	 */

	public Vector3f getTerrainPoint()
	{
		return currentTerrainPoint;
	}

	/**
	 * A localização do cursor no espaço é feito dividindo a projeção em duas partes iguais até encontrar o cursor.
	 * Cada uma das duas partes da projeção é verificado se o cursor está na primeira ou segunda parte da projeção.
	 * Quanto maior for a precisão mais iterações serão feitas recursivamente par encontrar o cursor do mouse.
	 * @return aquisição da precisão para localizar o cursor no mundo.
	 */

	public int getRecursionCount()
	{
		return recursionCount;
	}

	/**
	 * A localização do cursor no espaço é feito dividindo a projeção em duas partes iguais até encontrar o cursor.
	 * Cada uma das duas partes da projeção é verificado se o cursor está na primeira ou segunda parte da projeção.
	 * Quanto maior for a precisão mais iterações serão feitas recursivamente par encontrar o cursor do mouse.
	 * @param recursionCount precisão para localizar o cursor no mundo.
	 */

	public void setRecursionCount(int recursionCount)
	{
		this.recursionCount = recursionCount;
	}

	/**
	 * Para que a projeção possa ser feita é necessário definir um limite de distância do qual ela percorrerá.
	 * Quanto maior a distância, mais longe a câmera pode ficar do terreno e manter o cursor localizado.
	 * @return aquisição da distância limite entre a câmera e o cursor no mundo.
	 */

	public float getRayRange()
	{
		return rayRange;
	}

	/**
	 * Para que a projeção possa ser feita é necessário definir um limite de distância do qual ela percorrerá.
	 * Quanto maior a distância, mais longe a câmera pode ficar do terreno e manter o cursor localizado.
	 * @param rayRange distância limite entre a câmera e o cursor no mundo.
	 */

	public void setRayRange(float rayRange)
	{
		this.rayRange = rayRange;
	}

	/**
	 * Define o mundo que a partir de seus terrenos poderá ser localizado a projeção do ponto no seu espaço.
	 * <i>Para que funcione adequadamente o mundo aqui usado deve ser o mesmo mundo renderizado na tela.</i>
	 * @param world referência do mundo renderizável a se considerar na projeção do mouse.
	 */

	public void setWorld(WorldRender world)
	{
		this.world = world;
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("terrainPoint", getTerrainPoint());
		description.append("recursionCount", getRecursionCount());
		description.append("rayRange", getRayRange());
		description.append("ray", getRay());
		description.append("camera", nameOf(getCamera()));

		return description.toString();
	}
}
