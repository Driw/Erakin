package com.erakin.api.resources;

import org.diverproject.util.collection.abstraction.VirtualFolder;

/**
 * Mapeamento de Recursos
 *
 * <p>O mapeamento de recursos funciona como uma espécie de pasta virtual para armazenamento de recursos.
 * A finalidade dele é permitir verificar a existência de um recurso e ao invés de carregá-lo duas vezes,
 * simplesmente utilizar a raíz do recurso do qual já foi carregada, para isso usamos uma <i>pasta virtual</i>.</p>
 *
 * @author Andrew Mello
 *
 * @param <T> tipo de recurso que poderá ser mapeado.
 */

public class ResourceMap<T extends Resource<?>> extends VirtualFolder<ResourceRoot<T>>
{
	/**
	 * Constrói um novo mapeador de recursos para alocação de novos recursos.
	 * Nesse caso indica que esse mapeador será a raíz principal da estrutura.
	 * @param name nome do qual será dado ao mapa, para validar caminhos.
	 */

	public ResourceMap(String name)
	{
		super(name);
	}

	/**
	 * Constrói um novo mapeador de recursos originado de outro mapeador.
	 * @param name nome do qual será dado ao mapa, para validar caminhos.
	 * @param parent referência do mapeador que original a criação deste.
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
	 * Isso será usado quando um recurso for liberado e não houver mais referências ou seu tempo de vida acabar.
	 * Assim será garantido que um recurso raíz possa ser removido do mapeador quando parar de ser usado.
	 * @param root referência recurso raíz do qual está sendo revido do mapeador.
	 * @param map referência do mapeador para recursos do qual deverá removê-lo.
	 * @return aquisição do listener criado com as informações acima.
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
