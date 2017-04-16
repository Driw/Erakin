package com.erakin.engine;

import static com.erakin.common.Utilities.sizeOfCollection;
import static com.erakin.engine.Maths.createProjectionMatrix;
import static org.diverproject.log.LogSystem.logDebug;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.collection.List;
import org.diverproject.util.collection.abstraction.DynamicList;
import org.lwjgl.util.vector.Matrix4f;

/**
 * <h1>Matriz de Proje��o</h1>
 *
 * <p>Classe criada para representar a matriz de proje��o do qual ser� usada globalmente no engine.
 * Assim � poss�vel alterar seus valores aqui e todos que usarem a proje��o ser�o atualizados tamb�m.</p>
 * 
 * <p>Permite definir separadamente cada valor da matriz ou ent�o definir todos ao mesmo tempo.
 * Redefinir uma propriedade da matriz n�o significa que ela atualizar� a proje��o tamb�m.
 * Para isso � necess�rio ou atualizar passando todas as propriedades ou atualiza��o simples.</p>
 *
 * <p>Atualiza��o simples ir� usar os valores definidos nas propriedades internamente da classe.
 * J� para a atualiza��o com par�metros, al�m de utilizar esses par�metros para calcular a proje��o,
 * ir� redefinir os atributos da proje��o respectivamente ao nome dos par�metros passados.</p>
 *
 * @see ProjectionListener
 *
 * @author Andrew Mello
 */

public class ProjectionMatrix
{
	/**
	 * Inst�ncia para matriz de proje��o no padr�o de projetos Singleton.
	 */
	private static final ProjectionMatrix INSTANCE = new ProjectionMatrix();


	/**
	 * O qu�o grande ser� o campo de vis�o.
	 */
	private float fieldOfView;

	/**
	 * O qu�o pr�ximo ser� iniciado o campo de vis�o.
	 */
	private float nearPlane;

	/**
	 * O qu�o distante ser� encerrado o campo de vis�o.
	 */
	private float farPlane;

	/**
	 * Indica se a matriz est� atualizada de acordo com seus atributos.
	 */
	private boolean updated;

	/**
	 * Matriz usada para salvar os dados para proje��es.
	 */
	private Matrix4f matrix;

	/**
	 * Lista contendo todos os ouvintes de mudan�as na matriz de proje��o.
	 */
	private List<ProjectionListener> listeners;

	/**
	 * Construtor privado para atender ao padr�o de projetos singleton.
	 * Inicializa a matriz utilizando os atributos com valores padr�es.
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
	 * Campo de vis�o � a extens�o angular de um ambiente que � vista por uma c�mera.
	 * Por padr�o matriz de proje��es ir�o usar a constante <code>FOV</code> para tal.
	 * @return aquisi��o para o tamanho do campo de vis�o em proje��es.
	 */

	public float getFieldOfView()
	{
		return fieldOfView;
	}

	/**
	 * Campo de vis�o � a extens�o angular de um ambiente que � vista por uma c�mera.
	 * Quanto maior for o campo de vis�o mais aos lados da vis�o poder�o ser vistos.
	 * @param fieldOfView novo valor que ser� considerado como campo de vis�o.
	 */

	public void setFieldOfView(float fieldOfView)
	{
		this.fieldOfView = fieldOfView;
		this.updated = false;
	}

	/**
	 * Plano pr�ximo indica o qu�o distante do centro de vis�o ser� iniciada o campo de vis�o.
	 * Por padr�o matriz de proje��es ir�o usar a constante <code>NEAR_PLANE</code> para tal.
	 * @return aquisi��o do qu�o pr�ximo ser� iniciado o campo de vis�o, plano pr�ximo.
	 */

	public float getNearPlane()
	{
		return nearPlane;
	}

	/**
	 * Plano pr�ximo indica o qu�o distante do centro de vis�o ser� iniciada o campo de vis�o.
	 * Isso pode ser visto como uma esp�cie de zoom, quanto maior for mais pr�ximo fica.
	 * @param nearPlane novo valor que ser� considerado como inicio do campo de vis�o.
	 */

	public void setNearPlane(float nearPlane)
	{
		this.nearPlane = nearPlane;
		this.updated = false;
	}

	/**
	 * Plano distante indica o qu�o distante do centro de vis�o ser� encerrado o campo de vis�o.
	 * Por padr�o matriz de proje��es ir�o usar a constante <code>FAR_PLANE</code> para tal.
	 * @return aquisi��o do qu�o distante ser� terminado o campo de vis�o, plano m�ximo.
	 */

	public float getFarPlane()
	{
		return farPlane;
	}

	/**
	 * Plano distante indica o qu�o distante do centro de vis�o ser� encerrado o campo de vis�o.
	 * Isso pode ser visto como a dist�ncia em profundidade em que o campo de vis�o ter�.
	 * @param farPlane novo valor que ser� considerado como encerramento do campo de vis�o.
	 */

	public void setFarPlane(float farPlane)
	{
		this.farPlane = farPlane;
		this.updated = false;
	}

	/**
	 * Atualiza a matriz de proje��o utilizando os seguintes valores a baixo:
	 * @param fieldOfView campo de vis�o indica o quanto ser� vis�vel pela c�mera.
	 * @param nearPlane indica onde ser� iniciado o campo de vis�o respectivo a c�mera.
	 * @param farPlane indica onde ser� terminado o campo de vis�o respectivo a c�mera.
	 */

	public void updateWith(float fieldOfView, float nearPlane, float farPlane)
	{
		setFieldOfView(fieldOfView);
		setNearPlane(nearPlane);
		setFarPlane(farPlane);

		update();
	}

	/**
	 * Atualiza a matriz de proje��o usando os atributos atualmente definidos.
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
	 * A matriz � usada para guardar os valores calculados com <code>createProjectionMatrix()</code>.
	 * Os valores dessa matriz s�o dependentes dos atributos como campo de vis�o e dist�ncia.
	 * @return aquisi��o da matriz contendo os valores para c�lculos de proje��es.
	 */

	public Matrix4f getMatrix()
	{
		return matrix;
	}

	/**
	 * Ao adicionar um listeners a matriz de proje��o, o m�todo nela ser� chamado quando a matriz mudar.
	 * Por exemplo, no caso de um shader usa-se dentro de <code>changeProjection</code> para atualizar o mesmo.
	 * @param listener refer�ncia do listener ou de um objeto que possua/implemente esse servi�o.
	 * @return true se conseguir adicionar com �xito ou false caso j� tenha sido adicionado.
	 */

	public boolean addListener(ProjectionListener listener)
	{
		if (!listeners.contains(listener))
			return listeners.add(listener);

		return false;
	}

	/**
	 * Remover um listeners da matriz de proje��o, significa que n�o ser� mais chamado quando a matriz mudar.
	 * @param listener refer�ncia do listener ou objeto que possua/implemente o servi�o a ser removido.
	 * @return true se conseguir remover com �xito ou false caso n�o tenha encontrado.
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
	 * Procedimento que permite obter a �nica inst�ncia da matriz de proje��o.
	 * Utiliza o padr�o Singleton para evitar a exist�ncia de mais inst�ncias.
	 * @return aquisi��o da inst�ncia para utiliza��o da matriz de proje��o.
	 */

	public static ProjectionMatrix getInstance()
	{
		return INSTANCE;
	}
}
