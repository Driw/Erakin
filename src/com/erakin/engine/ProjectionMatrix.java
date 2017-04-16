package com.erakin.engine;

import static com.erakin.common.Utilities.sizeOfCollection;
import static com.erakin.engine.Maths.createProjectionMatrix;
import static org.diverproject.log.LogSystem.logDebug;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.collection.List;
import org.diverproject.util.collection.abstraction.DynamicList;
import org.lwjgl.util.vector.Matrix4f;

/**
 * <h1>Matriz de Projeção</h1>
 *
 * <p>Classe criada para representar a matriz de projeção do qual será usada globalmente no engine.
 * Assim é possível alterar seus valores aqui e todos que usarem a projeção serão atualizados também.</p>
 * 
 * <p>Permite definir separadamente cada valor da matriz ou então definir todos ao mesmo tempo.
 * Redefinir uma propriedade da matriz não significa que ela atualizará a projeção também.
 * Para isso é necessário ou atualizar passando todas as propriedades ou atualização simples.</p>
 *
 * <p>Atualização simples irá usar os valores definidos nas propriedades internamente da classe.
 * Já para a atualização com parâmetros, além de utilizar esses parâmetros para calcular a projeção,
 * irá redefinir os atributos da projeção respectivamente ao nome dos parâmetros passados.</p>
 *
 * @see ProjectionListener
 *
 * @author Andrew Mello
 */

public class ProjectionMatrix
{
	/**
	 * Instância para matriz de projeção no padrão de projetos Singleton.
	 */
	private static final ProjectionMatrix INSTANCE = new ProjectionMatrix();


	/**
	 * O quão grande será o campo de visão.
	 */
	private float fieldOfView;

	/**
	 * O quão próximo será iniciado o campo de visão.
	 */
	private float nearPlane;

	/**
	 * O quão distante será encerrado o campo de visão.
	 */
	private float farPlane;

	/**
	 * Indica se a matriz está atualizada de acordo com seus atributos.
	 */
	private boolean updated;

	/**
	 * Matriz usada para salvar os dados para projeções.
	 */
	private Matrix4f matrix;

	/**
	 * Lista contendo todos os ouvintes de mudanças na matriz de projeção.
	 */
	private List<ProjectionListener> listeners;

	/**
	 * Construtor privado para atender ao padrão de projetos singleton.
	 * Inicializa a matriz utilizando os atributos com valores padrões.
	 * <code>FOV<code>, <code>NEAR_PLANE</code> e <code>FAR_PLANE</code>.
	 */

	private ProjectionMatrix()
	{
		matrix = new Matrix4f();
		listeners = new DynamicList<ProjectionListener>();

		Preferences preferences = PreferencesSettings.getVideoPreferences();
		float fieldOfView = preferences.getOptionFloat("fieldOfView");
		float nearPlane = preferences.getOptionFloat("nearPlane");
		float farPlane = preferences.getOptionFloat("farPlane");

		updateWith(fieldOfView, nearPlane, farPlane);
	}

	/**
	 * Campo de visão é a extensão angular de um ambiente que é vista por uma câmera.
	 * Por padrão matriz de projeções irão usar a constante <code>FOV</code> para tal.
	 * @return aquisição para o tamanho do campo de visão em projeções.
	 */

	public float getFieldOfView()
	{
		return fieldOfView;
	}

	/**
	 * Campo de visão é a extensão angular de um ambiente que é vista por uma câmera.
	 * Quanto maior for o campo de visão mais aos lados da visão poderão ser vistos.
	 * @param fieldOfView novo valor que será considerado como campo de visão.
	 */

	public void setFieldOfView(float fieldOfView)
	{
		this.fieldOfView = fieldOfView;
		this.updated = false;
	}

	/**
	 * Plano próximo indica o quão distante do centro de visão será iniciada o campo de visão.
	 * Por padrão matriz de projeções irão usar a constante <code>NEAR_PLANE</code> para tal.
	 * @return aquisição do quão próximo será iniciado o campo de visão, plano próximo.
	 */

	public float getNearPlane()
	{
		return nearPlane;
	}

	/**
	 * Plano próximo indica o quão distante do centro de visão será iniciada o campo de visão.
	 * Isso pode ser visto como uma espécie de zoom, quanto maior for mais próximo fica.
	 * @param nearPlane novo valor que será considerado como inicio do campo de visão.
	 */

	public void setNearPlane(float nearPlane)
	{
		this.nearPlane = nearPlane;
		this.updated = false;
	}

	/**
	 * Plano distante indica o quão distante do centro de visão será encerrado o campo de visão.
	 * Por padrão matriz de projeções irão usar a constante <code>FAR_PLANE</code> para tal.
	 * @return aquisição do quão distante será terminado o campo de visão, plano máximo.
	 */

	public float getFarPlane()
	{
		return farPlane;
	}

	/**
	 * Plano distante indica o quão distante do centro de visão será encerrado o campo de visão.
	 * Isso pode ser visto como a distância em profundidade em que o campo de visão terá.
	 * @param farPlane novo valor que será considerado como encerramento do campo de visão.
	 */

	public void setFarPlane(float farPlane)
	{
		this.farPlane = farPlane;
		this.updated = false;
	}

	/**
	 * Atualiza a matriz de projeção utilizando os seguintes valores a baixo:
	 * @param fieldOfView campo de visão indica o quanto será visível pela câmera.
	 * @param nearPlane indica onde será iniciado o campo de visão respectivo a câmera.
	 * @param farPlane indica onde será terminado o campo de visão respectivo a câmera.
	 */

	public void updateWith(float fieldOfView, float nearPlane, float farPlane)
	{
		setFieldOfView(fieldOfView);
		setNearPlane(nearPlane);
		setFarPlane(farPlane);

		update();
	}

	/**
	 * Atualiza a matriz de projeção usando os atributos atualmente definidos.
	 */

	public void update()
	{
		createProjectionMatrix(matrix, fieldOfView, nearPlane, farPlane);

		updated = true;

		for (ProjectionListener listener : listeners)
			listener.changeProjection(matrix);

		logDebug("matriz autalizada (fov: %.2f, near: %.2f, far: %.2f)\n", fieldOfView, nearPlane, farPlane);
	}

	/**
	 * A matriz é usada para guardar os valores calculados com <code>createProjectionMatrix()</code>.
	 * Os valores dessa matriz são dependentes dos atributos como campo de visão e distância.
	 * @return aquisição da matriz contendo os valores para cálculos de projeções.
	 */

	public Matrix4f getMatrix()
	{
		return matrix;
	}

	/**
	 * Ao adicionar um listeners a matriz de projeção, o método nela será chamado quando a matriz mudar.
	 * Por exemplo, no caso de um shader usa-se dentro de <code>changeProjection</code> para atualizar o mesmo.
	 * @param listener referência do listener ou de um objeto que possua/implemente esse serviço.
	 * @return true se conseguir adicionar com êxito ou false caso já tenha sido adicionado.
	 */

	public boolean addListener(ProjectionListener listener)
	{
		if (!listeners.contains(listener))
			return listeners.add(listener);

		return false;
	}

	/**
	 * Remover um listeners da matriz de projeção, significa que não será mais chamado quando a matriz mudar.
	 * @param listener referência do listener ou objeto que possua/implemente o serviço a ser removido.
	 * @return true se conseguir remover com êxito ou false caso não tenha encontrado.
	 */

	public boolean removeListener(ProjectionListener listener)
	{
		return listeners.remove(listener);
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("fov", fieldOfView);
		description.append("near", nearPlane);
		description.append("far", farPlane);
		description.append("updated", updated);
		description.append("listeners", sizeOfCollection(listeners));

		return description.toString();
	}

	/**
	 * Procedimento que permite obter a única instância da matriz de projeção.
	 * Utiliza o padrão Singleton para evitar a existência de mais instâncias.
	 * @return aquisição da instância para utilização da matriz de projeção.
	 */

	public static ProjectionMatrix getInstance()
	{
		return INSTANCE;
	}
}
