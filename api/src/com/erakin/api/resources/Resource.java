package com.erakin.api.resources;

import org.diverproject.util.ObjectDescription;

import com.erakin.api.lwjgl.GLBind;

/**
 * <h1>Recurso</h1>
 *
 * <p>Um recurso é apenas uma referência que possui um indicador interno para o
 * recurso <i>"verdadeiro"</i>, no engine esse objeto é denominado como recurso raíz.
 * O recurso serve como objeto trabalhável na codificação do jogo desenvolvido.
 *
 * <p>Ele pode ser usado em qualquer momento, porém deve ser usado de forma correta.
 * Caso a sua utilização não seja apropriada, a finalidade de separa recursos em um
 * recurso raíz e outro recurso fictício (esse) perde toda a utilidade (memória).</p>
 *
 * <p>Portanto, para que um recurso possa ser usado apropriadamente como esperado,
 * ao terminar de usar qualquer recurso, independente de qual seja este, deverá chamar
 * <code>release()</code> para fazer a liberação das informações da memória.</p>
 *
 * <p>Se a raíz identificar que não há mais referência desse recurso irá remover o
 * mesmo também da memória, para não consumir o mesmo, caso contrário irá permanecer
 * até que o último recurso fictício possa para ser liberado.</p>
 *
 * <p>De modo geral, os recursos fictícios não possuem informações relacionadas aos
 * recursos diretamente, porém sua referência da raíz permita que eles possam
 * trabalhar de acordo com o que foram proposto ao definir seus tipos e dados.</p>
 *
 * @see GLBind
 * @see ResourceRoot
 *
 * @author Andrew
 */

public abstract class Resource<T extends ResourceRoot<?>> implements GLBind
{
	/**
	 * Referência do recurso raíz que será usado por esse recurso.
	 */
	T root;

	/**
	 * Constrói um novo recurso sendo necessário definir sua raíz.
	 * @param root referência do recurso raíz que será usado.
	 */

	Resource(T root)
	{
		this.root = root;
	}

	/**
	 * Procedimento que deve ser chamado quando for necessário fazer sua liberação.
	 * Internamente é chamado apenas quando a raíz perder todas suas referências.
	 * Deve ser implementado pelos tipos de recursos especificando liberação de memória.
	 */

	public void release()
	{
		if (root != null)
		{
			root.release();
			root = null;
		}
	}

	/**
	 * Procedimento chamado por toString a fim de preencher adequadamente o mesmo.
	 * O posicionamento do conteúdo é sempre feito apenas ao final da descrição.
	 * @param description referência da descrição desse objeto que será usado.
	 */

	public abstract void toString(ObjectDescription description);

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("path", root == null ? null : root.getFilePath());

		toString(description);

		return description.toString();
	}
}
