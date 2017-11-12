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
 * <h1>Seletor da Proje��o do Mouse no Mundo</h1>
 *
 * <p>Classe respons�vel por criar usar uma proje��o em dire��o ao cursor do mouse com rela��o a posi��o da c�mera.
 * Atrav�s dessa proje��o e com duas vari�veis � calculado em que lugar do mundo (terreno) o mouse est� apontado.
 * Das duas vari�veis, uma permite definir o n�vel de precis�o do cursor no espa�o e outra a dist�ncia limite.</p>
 *
 * <p>Aumentar a precis�o tamb�m aumenta o tempo de processamento, por�m torna a posi��o do cursor no espa�o preciso.
 * J� a dist�ncia limite n�o influencia no tempo de processamento mas sim para encontrar a localiza��o do cursor.</p>
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
	 * Valor padr�o da precis�o para localizar o cursor no mundo.
	 */
	public static final int DEFAULT_RECURSION_COUNT = 200;

	/**
	 * Valor padr�o da dist�ncia limite entre a c�mera e o cursor no mundo.
	 */
	public static final float DEFAULT_RAY_RANGE = 30f;


	/**
	 * Refer�ncia do mundo renderiz�vel que ser� considerado.
	 */
	private WorldRender world;

	/**
	 * Vetor contendo a localiza��o no espa�o do cursos no mundo.
	 */
	private Vector3f currentTerrainPoint;

	/**
	 * Precis�o para localizar o cursor no mundo.
	 */
	private int recursionCount;

	/**
	 * Dist�ncia limite entre a c�mera e o cursor no mundo.
	 */
	private float rayRange;

	/**
	 * Cria uma nova inst�ncia de um seletor da proje��o do mouse feita no mundo renderizado.
	 * @param camera refer�ncia da c�mera usada para visualiza��o do mundo na tela.
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
	 * Verifica se a superf�cie de um terreno est� entre uma determinada faixa da proje��o.
	 * @param start dist�ncia inicial da busca pela intersec��o da proje��o com o terreno.
	 * @param finish dist�ncia final da busca pela intersec��o da proje��o com o terreno.
	 * @param ray vetor com a dire��o da proje��o do cursor do mouse em rela��o ao espa�o.
	 * @return true se o terreno estiver nessa faixa de proje��o ou false caso contr�rio.
	 */

	private boolean intersectionInRange(float start, float finish, Vector3f ray)
	{
		Vector3f startPoint = getPointOnRay(ray, start);
		Vector3f endPoint = getPointOnRay(ray, finish);

		return !isUnderGround(startPoint) && isUnderGround(endPoint);
	}

	/**
	 * Calcula um ponto no espa�o de acordo com a linha de proje��o do cursor no espa�o e uma dist�ncia.
	 * @param ray vetor com a dire��o da proje��o do cursor do mouse em rela��o ao espa�o.
	 * @param distance a que dist�ncia a proje��o est� sendo emitida para definir o ponto no espa�o.
	 * @return aquisi��o da localiza��o do ponto projetado no espa�o conforme par�metros.
	 */

	private Vector3f getPointOnRay(Vector3f ray, float distance)
	{
		Vector3f start = newVector3f(getCamera());
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);

		return Vector3f.add(start, scaledRay, null);
	}

	/**
	 * Realiza uma busca bin�ria para localiza��o da proje��o do mouse no espa�o do mundo conforme especifica��es.
	 * A busca � feita de forma recursiva de acordo com o n�vel de precis�o e dist�ncia limite definidas.
	 * @param count quantas vezes a j� foi feita a busca bin�ria recursiva para verificar o limite.
	 * @param start dist�ncia inicial da busca bin�ria em rela��o a posi��o da c�mera.
	 * @param finish dist�ncia final da busca bin�ria em rela��o a posi��o da c�mera.
	 * @param ray vetor com a dire��o da proje��o do cursor do mouse em rela��o ao espa�o.
	 * @return aquisi��o da localiza��o de proximidade do cursor no espa�o feito na busca.
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
	 * Verifica se uma determinada coordenada no espa�o est� acima ou abaixo do espa�o do terreno renderizado.
	 * Caso n�o haja nenhum terreno na dire��o da proje��o ser� considerado que n�o � relevo (altura zero).
	 * @param espaceCoord coordenadas no espa�o do qual deve ser feito o teste.
	 * @return true se as coordenadas est�o abaixo do terreno ou true se estiver acima.
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
	 * Procedimento interno usado para selecionar qual o terreno que est� sendo apontado pelo cursor.
	 * @param worldX coordenada no espa�o do mundo em rela��o ao eixo da longitude (X).
	 * @param worldZ coordenada no espa�o do mundo em rela��o ao eixo da latitude (Z).
	 * @return aquisi��o do terreno respectivo as coordenadas ou null caso n�o encontrado.
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
	 * O objeto desse seletor � criar um vetor com a localiza��o no espa�o de onde o cursor do mouse est� apontado.
	 * Nesse caso a localiza��o espera ser projetada para alguma superf�cie de um terreno do mundo renderizado.
	 * @return aquisi��o da localiza��o do cursor no espa�o ou null caso n�o esteja apontado ou no alcance.
	 */

	public Vector3f getTerrainPoint()
	{
		return currentTerrainPoint;
	}

	/**
	 * A localiza��o do cursor no espa�o � feito dividindo a proje��o em duas partes iguais at� encontrar o cursor.
	 * Cada uma das duas partes da proje��o � verificado se o cursor est� na primeira ou segunda parte da proje��o.
	 * Quanto maior for a precis�o mais itera��es ser�o feitas recursivamente par encontrar o cursor do mouse.
	 * @return aquisi��o da precis�o para localizar o cursor no mundo.
	 */

	public int getRecursionCount()
	{
		return recursionCount;
	}

	/**
	 * A localiza��o do cursor no espa�o � feito dividindo a proje��o em duas partes iguais at� encontrar o cursor.
	 * Cada uma das duas partes da proje��o � verificado se o cursor est� na primeira ou segunda parte da proje��o.
	 * Quanto maior for a precis�o mais itera��es ser�o feitas recursivamente par encontrar o cursor do mouse.
	 * @param recursionCount precis�o para localizar o cursor no mundo.
	 */

	public void setRecursionCount(int recursionCount)
	{
		this.recursionCount = recursionCount;
	}

	/**
	 * Para que a proje��o possa ser feita � necess�rio definir um limite de dist�ncia do qual ela percorrer�.
	 * Quanto maior a dist�ncia, mais longe a c�mera pode ficar do terreno e manter o cursor localizado.
	 * @return aquisi��o da dist�ncia limite entre a c�mera e o cursor no mundo.
	 */

	public float getRayRange()
	{
		return rayRange;
	}

	/**
	 * Para que a proje��o possa ser feita � necess�rio definir um limite de dist�ncia do qual ela percorrer�.
	 * Quanto maior a dist�ncia, mais longe a c�mera pode ficar do terreno e manter o cursor localizado.
	 * @param rayRange dist�ncia limite entre a c�mera e o cursor no mundo.
	 */

	public void setRayRange(float rayRange)
	{
		this.rayRange = rayRange;
	}

	/**
	 * Define o mundo que a partir de seus terrenos poder� ser localizado a proje��o do ponto no seu espa�o.
	 * <i>Para que funcione adequadamente o mundo aqui usado deve ser o mesmo mundo renderizado na tela.</i>
	 * @param world refer�ncia do mundo renderiz�vel a se considerar na proje��o do mouse.
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
