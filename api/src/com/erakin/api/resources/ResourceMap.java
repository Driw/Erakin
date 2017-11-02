package com.erakin.api.resources;

import org.diverproject.util.collection.abstraction.VirtualFolder;

/**
 * Mapeamento de Recursos
 *
 * <p>O mapeamento de recursos funciona como uma esp�cie de pasta virtual para armazenamento de recursos.
 * A finalidade dele � permitir verificar a exist�ncia de um recurso e ao inv�s de carreg�-lo duas vezes,
 * simplesmente utilizar a ra�z do recurso do qual j� foi carregada, para isso usamos uma <i>pasta virtual</i>.</p>
 *
 * @author Andrew Mello
 *
 * @param <T> tipo de recurso que poder� ser mapeado.
 */

public class ResourceMap<T extends Resource<?>> extends VirtualFolder<ResourceRoot<T>>
{
	/**
	 * Constr�i um novo mapeador de recursos para aloca��o de novos recursos.
	 * Nesse caso indica que esse mapeador ser� a ra�z principal da estrutura.
	 * @param name nome do qual ser� dado ao mapa, para validar caminhos.
	 */

	public ResourceMap(String name)
	{
		super(name);
	}

	/**
	 * Constr�i um novo mapeador de recursos originado de outro mapeador.
	 * @param name nome do qual ser� dado ao mapa, para validar caminhos.
	 * @param parent refer�ncia do mapeador que original a cria��o deste.
	 */

	private ResourceMap(String name, ResourceMap<T> parent)
	{
		super(name, parent);
	}

	@Override
	protected void subAdd(ResourceRoot<T> file)
	{
		prepareListener(file, this);
	}

	@Override
	protected void subRemove(ResourceRoot<T> file)
	{
		file.listener = null;
		file.release();
	}

	/**
	 * Usado internamente para construir um listener para garantir que recursos sejam removidos adequadamente.
	 * Isso ser� usado quando um recurso for liberado e n�o houver mais refer�ncias ou seu tempo de vida acabar.
	 * Assim ser� garantido que um recurso ra�z possa ser removido do mapeador quando parar de ser usado.
	 * @param root refer�ncia recurso ra�z do qual est� sendo revido do mapeador.
	 * @param map refer�ncia do mapeador para recursos do qual dever� remov�-lo.
	 * @return aquisi��o do listener criado com as informa��es acima.
	 */

	private ResourceListener prepareListener(final ResourceRoot<T> root, final ResourceMap<T> map)
	{
		return new ResourceListener()
		{
			@Override
			public void resourceRelease()
			{
				map.remove(root.getFilePath());
			}
		};
	}
}
