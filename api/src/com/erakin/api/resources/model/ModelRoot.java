package com.erakin.api.resources.model;

import org.diverproject.util.ObjectDescription;

import com.erakin.api.lwjgl.VAO;
import com.erakin.api.resources.ResourceRoot;

/**
 * <h1>Modelo Ra�z</h1>
 *
 * <p>Para essa ra�z ser� de responsabilidade o armazenamento do VAO do modelo.
 * Esse VAO ir� conter as seguintes informa��es abaixo conforme ordenado:</p>
 *
 * <p><b>[Attribute]</b>: <i>Especifica��o do conte�do no atributo.</i><br>
 * <b>[0]</b>: Posi��o dos v�rtices no espa�o;<br>
 * <b>[1]</b>: Coordenada da textura para cada v�rtice;<br>
 * <b>[2]</b>: Normaliza��o TODO;<br></p>
 *
 * @see ResourceRoot
 * @see VAO
 *
 * @author Andrew
 */

public class ModelRoot extends ResourceRoot<Model>
{
	/**
	 * Identifica��o do Vertex-Array Object usado.
	 */
	VAO vao;

	/**
	 * N�vel padr�o da for�a para refletir ilumina��es.
	 */
	float defaultReflectivity;

	/**
	 * N�vel padr�o da redu��o para brilhos refletidos.
	 */
	float defaultShineDamping;

	/**
	 * Vetor especificando os atributos usados pelo modelo.
	 */
	int attributes[];

	/**
	 * Construtor em package para permitir apenas que ModelLoader construa um.
	 * Isso ir� garantir que um Modelo Ra�z inv�lido possa ser criado na engine.
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
	 * Refletividade indica o quanto ser� refletida a ilumina��o ao entrar em contado com o modelo.
	 * Por padr�o o valor para esse � definido como 0 a fim de manter um reflexo leve e suave.
	 * @param reflectivity n�vel de refletividade do modelo, quanto maior mais reflexo ter�.
	 */

	public void setDefaultReflectivity(float reflectivity)
	{
		this.defaultReflectivity = reflectivity;
	}

	/**
	 * Redu��o do Brilho indica o quanto o reflexo ser� reduzido em rela��o a vis�o da c�mera.
	 * Por padr�o o valor para esse � definido como 1 a fim de manter um brilho leve e suave.
	 * @param shineDamping n�vel de redu��o do brilho, quanto maior mais redu��o ter�.
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
