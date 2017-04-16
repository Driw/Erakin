package com.erakin.engine.entity;

import static com.erakin.common.Utilities.objectString;

import org.diverproject.util.ObjectDescription;
import org.lwjgl.util.vector.Vector3f;

import com.erakin.engine.resource.Model;

/**
 * <h1>Entidade Padr�o</h1>
 *
 * <p>A entidade padr�o � uma implementa��o b�sica para uma entidade, guardado atributos b�sicos do mesmo.
 * Esses atributos s�o referentes as propriedades posicionamento, rotacionamento e escalamento da entidade.
 * Os atributos poder�o ser trabalhados atrav�s de algumas opera��es �teis e din�micas.</p>
 *
 * @see Entity
 * @see Model
 *
 * @author Andrew Mello
 */

public abstract class EntityDefault implements Entity
{
	/**
	 * Modelagem tri-dimensional usada para visualizar a entidade.
	 */
	private Model model;

	/**
	 * Vetor contendo os dados de posicionamento no espa�o.
	 */
	private Vector3f position;

	/**
	 * Vetor contendo as propriedades de rota��o.
	 */
	private Vector3f rotation;

	/**
	 * Vetor contendo as propriedades de escalas.
	 */
	private Vector3f scale;

	/**
	 * Constr�i uma nova entidade padr�o inicializando alguns atributos para que possam trabalhar.
	 * Esses atributos s�o os vetores que armazenam os dados do posicionamento, rota��o e escala.
	 * Para esse caso ser� considerado que a escala tenha como valor padr�o sempre 1.0f (todos eixos).
	 */

	public EntityDefault()
	{
		position = new Vector3f();
		rotation = new Vector3f();
		scale = new Vector3f(1.0f, 1.0f, 1.0f);
	}

	/**
	 * Define os valores dos quais o posicionamento dever� assumir como seus novos valores.
	 * @param x dist�ncia do ponto central em rela��o ao eixo da latitude.
	 * @param y dist�ncia do ponto central em rela��o ao eixo da altura.
	 * @param z dist�ncia do ponto central em rela��o ao eixo da longitude.
	 */

	public void setPosition(float x, float y, float z)
	{
		position.x = x;
		position.y = y;
		position.z = z;
	}

	/**
	 * Aumenta os valores do posicionamento que est�o atualmente definidos de acordo com:
	 * @param x quanto dever� aumentar o eixo da latitude ao ser visualizada.
	 * @param y quanto dever� aumentar o eixo da altura ao ser visualizada.
	 * @param z quanto dever� aumentar o eixo da longitude ao ser visualizada.
	 */

	public void increasePosition(float x, float y, float z)
	{
		position.x += x;
		position.y += y;
		position.z += z;
	}

	/**
	 * Define valores dos quais a rota��o dever� assumir como seus novos valores.
	 * @param pitch �ngulo que ir� assumir a rota��o no eixo da inclina��o.
	 * @param yaw �ngulo que ir� assumir a rota��o no eixo do rolamento.
	 * @param roll �ngulo que ir� assumir a rota��o no eixo da guinada.
	 */

	public void setRotation(float pitch, float yaw, float roll)
	{
		rotation.x = yaw % 360;
		rotation.y = pitch % 360;
		rotation.z = roll % 360;
	}

	/**
	 * Aumenta os valores das rota��es que est�o atualmente definidas de acordo com:
	 * @param yaw quanto dever� aumentar o eixo da guinada ao ser visualizada.
	 * @param pitch quanto dever� aumentar o eixo de rolamento ao ser visualizada.
	 * @param roll quanto dever� aumentar o eixo da inclina��o ao ser visualizada.
	 */

	public void increaseRotation(float pitch, float yaw, float roll)
	{
		rotation.x = (rotation.x + yaw) % 360;
		rotation.y = (rotation.y + pitch) % 360;
		rotation.z = (rotation.z + roll) % 360;
	}

	/**
	 * Define valores dos quais a escala dever� assumir como seus novos valores.
	 * @param x quanto ter� a largura da entidade ao ser visualizada.
	 * @param y quanto ter� o comprimento da entidade ao ser visualizada.
	 * @param z quanto ter� a altura da entidade ao ser visualizada.
	 */

	public void setScale(float x, float y, float z)
	{
		scale.x = x;
		scale.y = y;
		scale.z = z;
	}

	/**
	 * Aumenta os valores das escalas que est�o atualmente definidas de acordo com:
	 * @param x quanto dever� aumentar a largura da entidade ao ser visualizada.
	 * @param y quanto dever� aumentar o comprimento da entidade ao ser visualizada.
	 * @param z quanto dever� aumentar a altura da entidade ao ser visualizada.
	 */

	public void increaseScale(float x, float y, float z)
	{
		scale.x += x;
		scale.y += y;
		scale.z += z;
	}

	/**
	 * A modelagem tri-dimensional � usada para indicar como ser� a sua forma ao ser visualizada.
	 * Devendo ainda possuir uma textura para ser usada durante a sua renderiza��o e outros afins.
	 * @param model refer�ncia do novo modelo tri-dimensional para visualiza��o da entidade.
	 */

	public void setModel(Model model)
	{
		this.model = model;
	}

	@Override
	public Model getModel()
	{
		return model;
	}

	@Override
	public Vector3f getPosition()
	{
		return position;
	}

	@Override
	public Vector3f getRotation()
	{
		return rotation;
	}

	@Override
	public Vector3f getScale()
	{
		return scale;
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		if (model != null)
			description.append("model", model.getID());

		description.append("position", objectString(position));
		description.append("rotation", objectString(rotation));
		description.append("scale", objectString(scale));

		return description.toString();
	}
}
