package com.erakin.api.resources;

import org.diverproject.util.ObjectDescription;

import com.erakin.api.lwjgl.GLBind;

/**
 * <h1>Recurso</h1>
 *
 * <p>Um recurso � apenas uma refer�ncia que possui um indicador interno para o
 * recurso <i>"verdadeiro"</i>, no engine esse objeto � denominado como recurso ra�z.
 * O recurso serve como objeto trabalh�vel na codifica��o do jogo desenvolvido.
 *
 * <p>Ele pode ser usado em qualquer momento, por�m deve ser usado de forma correta.
 * Caso a sua utiliza��o n�o seja apropriada, a finalidade de separa recursos em um
 * recurso ra�z e outro recurso fict�cio (esse) perde toda a utilidade (mem�ria).</p>
 *
 * <p>Portanto, para que um recurso possa ser usado apropriadamente como esperado,
 * ao terminar de usar qualquer recurso, independente de qual seja este, dever� chamar
 * <code>release()</code> para fazer a libera��o das informa��es da mem�ria.</p>
 *
 * <p>Se a ra�z identificar que n�o h� mais refer�ncia desse recurso ir� remover o
 * mesmo tamb�m da mem�ria, para n�o consumir o mesmo, caso contr�rio ir� permanecer
 * at� que o �ltimo recurso fict�cio possa para ser liberado.</p>
 *
 * <p>De modo geral, os recursos fict�cios n�o possuem informa��es relacionadas aos
 * recursos diretamente, por�m sua refer�ncia da ra�z permita que eles possam
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
	 * Refer�ncia do recurso ra�z que ser� usado por esse recurso.
	 */
	T root;

	/**
	 * Constr�i um novo recurso sendo necess�rio definir sua ra�z.
	 * @param root refer�ncia do recurso ra�z que ser� usado.
	 */

	Resource(T root)
	{
		this.root = root;
	}

	/**
	 * Procedimento que deve ser chamado quando for necess�rio fazer sua libera��o.
	 * Internamente � chamado apenas quando a ra�z perder todas suas refer�ncias.
	 * Deve ser implementado pelos tipos de recursos especificando libera��o de mem�ria.
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
	 * O posicionamento do conte�do � sempre feito apenas ao final da descri��o.
	 * @param description refer�ncia da descri��o desse objeto que ser� usado.
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
