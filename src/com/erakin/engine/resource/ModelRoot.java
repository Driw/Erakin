package com.erakin.engine.resource;

import org.diverproject.util.ObjectDescription;

import com.erakin.engine.lwjgl.VAO;

/**
 * <h1>Modelo Raíz</h1>
 *
 * <p>Para essa raíz será de responsabilidade o armazenamento do VAO do modelo.
 * Esse VAO irá conter as seguintes informações abaixo conforme ordenado:</p>
 *
 * <p><b>[Attribute]</b>: <i>Especificação do conteúdo no atributo.</i><br>
 * <b>[0]</b>: Posição dos vértices no espaço;<br>
 * <b>[1]</b>: Coordenada da textura para cada vértice;<br>
 * <b>[2]</b>: Normalização TODO;<br></p>
 *
 * @see ResourceRoot
 * @see VAO
 *
 * @author Andrew
 */

public class ModelRoot extends ResourceRoot
{
	/**
	 * Identificação do Vertex-Array Object usado.
	 */
	VAO vao;

	/**
	 * Nível padrão da força para refletir iluminações.
	 */
	float defaultReflectivity;

	/**
	 * Nível padrão da redução para brilhos refletidos.
	 */
	float defaultShineDamping;

	/**
	 * Construtor em package para permitir apenas que ResourceManager construa um.
	 * Isso irá garantir que um Modelo Raíz inválido possa ser criado no engine.
	 */

	ModelRoot()
	{
		defaultReflectivity = 0f;
		defaultShineDamping = 1f;
	}

	/**
	 * Refletividade indica o quanto será refletida a iluminação ao entrar em contado com o modelo.
	 * Por padrão o valor para esse é definido como 0 a fim de manter um reflexo leve e suave.
	 * @param reflectivity nível de refletividade do modelo, quanto maior mais reflexo terá.
	 */

	public void setDefaultReflectivity(float reflectivity)
	{
		this.defaultReflectivity = reflectivity;
	}

	/**
	 * Redução do Brilho indica o quanto o reflexo será reduzido em relação a visão da câmera.
	 * Por padrão o valor para esse é definido como 1 a fim de manter um brilho leve e suave.
	 * @param shineDamping nível de redução do brilho, quanto maior mais redução terá.
	 */

	public void setDefaultShineDamping(float shineDamping)
	{
		this.defaultShineDamping = shineDamping;
	}

	@Override
	public Model genResource()
	{
		return new Model(this);
	}

	@Override
	public void toString(ObjectDescription description)
	{
		description.append("vao", vao == null ? null : vao.getID());
		description.append("reflectivity", defaultReflectivity);
		description.append("shineDamping", defaultShineDamping);
	}
}
