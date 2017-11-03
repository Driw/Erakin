package com.erakin.api.resources.model;

import org.diverproject.util.ObjectDescription;

import com.erakin.api.lwjgl.VAO;
import com.erakin.api.resources.ResourceRoot;

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

public class ModelRoot extends ResourceRoot<Model>
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
	 * Vetor especificando os atributos usados pelo modelo.
	 */
	int attributes[];

	/**
	 * Construtor em package para permitir apenas que ModelLoader construa um.
	 * Isso irá garantir que um Modelo Raíz inválido possa ser criado na engine.
	 * @param filepath caminho do arquivo em disco com os dados do modelo carregado.
	 */

	ModelRoot(String filepath)
	{
		super(filepath);

		attributes = new int[0];
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
		Model model = new Model(this);
		addReference(model);

		return model;
	}

	@Override
	public void toString(ObjectDescription description)
	{
		description.append("vao", vao == null ? null : vao.getID());
		description.append("reflectivity", defaultReflectivity);
		description.append("shineDamping", defaultShineDamping);
	}
}
