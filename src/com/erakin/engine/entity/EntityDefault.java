package com.erakin.engine.entity;

import static com.erakin.common.Utilities.objectString;

import org.diverproject.util.ObjectDescription;
import org.lwjgl.util.vector.Vector3f;

import com.erakin.engine.resource.Model;

/**
 * <h1>Entidade Padrão</h1>
 *
 * <p>A entidade padrão é uma implementação básica para uma entidade, guardado atributos básicos do mesmo.
 * Esses atributos são referentes as propriedades posicionamento, rotacionamento e escalamento da entidade.
 * Os atributos poderão ser trabalhados através de algumas operações úteis e dinâmicas.</p>
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
	 * Vetor contendo os dados de posicionamento no espaço.
	 */
	private Vector3f position;

	/**
	 * Vetor contendo as propriedades de rotação.
	 */
	private Vector3f rotation;

	/**
	 * Vetor contendo as propriedades de escalas.
	 */
	private Vector3f scale;

	/**
	 * Constrói uma nova entidade padrão inicializando alguns atributos para que possam trabalhar.
	 * Esses atributos são os vetores que armazenam os dados do posicionamento, rotação e escala.
	 * Para esse caso será considerado que a escala tenha como valor padrão sempre 1.0f (todos eixos).
	 */

	public EntityDefault()
	{
		position = new Vector3f();
		rotation = new Vector3f();
		scale = new Vector3f(1.0f, 1.0f, 1.0f);
	}

	/**
	 * Define os valores dos quais o posicionamento deverá assumir como seus novos valores.
	 * @param x distância do ponto central em relação ao eixo da latitude.
	 * @param y distância do ponto central em relação ao eixo da altura.
	 * @param z distância do ponto central em relação ao eixo da longitude.
	 */

	public void setPosition(float x, float y, float z)
	{
		position.x = x;
		position.y = y;
		position.z = z;
	}

	/**
	 * Aumenta os valores do posicionamento que estão atualmente definidos de acordo com:
	 * @param x quanto deverá aumentar o eixo da latitude ao ser visualizada.
	 * @param y quanto deverá aumentar o eixo da altura ao ser visualizada.
	 * @param z quanto deverá aumentar o eixo da longitude ao ser visualizada.
	 */

	public void increasePosition(float x, float y, float z)
	{
		position.x += x;
		position.y += y;
		position.z += z;
	}

	/**
	 * Define valores dos quais a rotação deverá assumir como seus novos valores.
	 * @param pitch ângulo que irá assumir a rotação no eixo da inclinação.
	 * @param yaw ângulo que irá assumir a rotação no eixo do rolamento.
	 * @param roll ângulo que irá assumir a rotação no eixo da guinada.
	 */

	public void setRotation(float pitch, float yaw, float roll)
	{
		rotation.x = yaw % 360;
		rotation.y = pitch % 360;
		rotation.z = roll % 360;
	}

	/**
	 * Aumenta os valores das rotações que estão atualmente definidas de acordo com:
	 * @param yaw quanto deverá aumentar o eixo da guinada ao ser visualizada.
	 * @param pitch quanto deverá aumentar o eixo de rolamento ao ser visualizada.
	 * @param roll quanto deverá aumentar o eixo da inclinação ao ser visualizada.
	 */

	public void increaseRotation(float pitch, float yaw, float roll)
	{
		rotation.x = (rotation.x + yaw) % 360;
		rotation.y = (rotation.y + pitch) % 360;
		rotation.z = (rotation.z + roll) % 360;
	}

	/**
	 * Define valores dos quais a escala deverá assumir como seus novos valores.
	 * @param x quanto terá a largura da entidade ao ser visualizada.
	 * @param y quanto terá o comprimento da entidade ao ser visualizada.
	 * @param z quanto terá a altura da entidade ao ser visualizada.
	 */

	public void setScale(float x, float y, float z)
	{
		scale.x = x;
		scale.y = y;
		scale.z = z;
	}

	/**
	 * Aumenta os valores das escalas que estão atualmente definidas de acordo com:
	 * @param x quanto deverá aumentar a largura da entidade ao ser visualizada.
	 * @param y quanto deverá aumentar o comprimento da entidade ao ser visualizada.
	 * @param z quanto deverá aumentar a altura da entidade ao ser visualizada.
	 */

	public void increaseScale(float x, float y, float z)
	{
		scale.x += x;
		scale.y += y;
		scale.z += z;
	}

	/**
	 * A modelagem tri-dimensional é usada para indicar como será a sua forma ao ser visualizada.
	 * Devendo ainda possuir uma textura para ser usada durante a sua renderização e outros afins.
	 * @param model referência do novo modelo tri-dimensional para visualização da entidade.
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
