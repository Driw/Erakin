package com.erakin.api.resources;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Carregador Padrão</h1>
 *
 * <p>Classe usada para implementar algumas funcionalidades padrões entre todos os carregadores.
 * Protege a visibilidade de alguns objetos de forma a manter melhor a segurança de seu uso.
 * Possui como responsabilidade um mapeamento dos recursos carregados e o caminho a carregar.</p>
 *
 * @author Andre Mello
 *
 * @param <T> tipo de recurso que poderá ser carregado.
 */

public class DefaultLoader<T extends Resource<?>>
{
	/**
	 * Caminho parcial ou completo da localização dos arquivos que serão carregados.
	 */
	private String pathname;

	/**
	 * Mapeador de recursos para permitir o gerenciamento de adicionar, remover e selecionar.
	 */
	private ResourceMap<T> resources;

	/**
	 * Cria um novo carregador padrão inicializando algumas definições básicas do seu funcionamento.
	 * Deve definir adequadamente o caminho base que será usado quando um arquivo for carregado.
	 * @param name nome que será dado para esse carregador de recursos e também como pré-fixo.
	 */

	public DefaultLoader(String name)
	{
		resources = new ResourceMap<T>(name);
	}

	/**
	 * Insere um novo recurso raíz para ser armazenado pelo carregador padrão.
	 * Deve ser usado apenas quando um recurso raíz tiver sido carregado completamente.
	 * Tendo ainda em mente que este deverá ter sido verificado e validado pelo sistema.
	 * @param resource referência do recurso raíz que está pronto para ser listado.
	 * @return true se conseguir inserir o recurso raíz ou false caso contrário.
	 */

	protected boolean insertResource(ResourceRoot<T> resource)
	{
		return resources.add(resource);
	}

	/**
	 * Remove um recurso raíz através da especificação do seu caminho dentro do carregador padrão.
	 * Deve ser usado apenas quando um recurso raíz tiver sido descarregado completamente.
	 * Tendo ainda em mente que uma vez descarregado para ser usado novamente é preciso carregá-lo.
	 * @param pathname nome de identificação do recurso raíz dentro do carregador a ser removido.
	 * @return true se conseguir remover o recurso raíz ou false caso contrário.
	 */

	protected boolean removeResource(String pathname)
	{
		return resources.remove(pathname);
	}

	/**
	 * Seleciona um recurso raíz através do seu nome de identificação dentro do carregador padrão.
	 * @param pathname nome de identificação do recurso raíz dentro do carregador a selecionar.
	 * @return referência do recurso raíz de acordo com o nome de identificação passado.
	 */

	protected ResourceRoot<T> selectResource(String pathname)
	{
		if (!pathname.startsWith(getPathname()))
			pathname = String.format("%s/%s", getResourceName(), pathname);

		return resources.get(pathname);
	}

	/**
	 * Verifica se um determinado recurso raíz está inserido dentro do carregador padrão.
	 * @param pathname nome de identificação do recurso raíz a ser verificado.
	 * @return true se encontrar um recurso raíz ou false caso contrário.
	 */

	protected boolean containResource(String pathname)
	{
		return selectResource(pathname) != null;
	}

	/**
	 * Cada carregador padrão terá um configuração na parte de configurações padrões de pastas.
	 * @return aquisição do caminho parcial ou completo especificado por <code>PreferenceSettings</code>.
	 */

	public String getPathname()
	{
		return pathname;
	}

	/**
	 * Define qual será o caminho padrão para leitura dos arquivos de recursos.
	 * @param pathname caminho parcial ou completo do diretório contendo os arquivos.
	 */

	public void setPathname(String pathname)
	{
		this.pathname = pathname;
	}

	/**
	 * Nome dos recursos é usado como nome da pasta virtual que irá armazenar os recursos.
	 * Esse será usado como pré-fixo de todos os recursos que forem adicionados ao carregador.
	 * @return aquisição do nome do tipo de recursos que estão sendo armazenados no carregador.
	 */

	protected String getResourceName()
	{
		return resources.getName();
	}

	/**
	 * Itera cada um dos recursos raízes salvos no carregador para que seja atualizado o seu tempo de vida útil.
	 * Caso seu tempo de vida útil tenha acabo o seu conteúdo será liberado completamente do carregador.
	 * <i>Um recurso raíz removido não terá mais utilidade pra nenhum recurso referente a ele, porém pode ser recarregado.</i>.
	 * @param delay quantos milissegundos se passou desde a última atualização.
	 */

	public void update(long delay)
	{
		for (ResourceRoot<T> resource : resources)
		{
			resource.update(delay);

			if (!resource.isAlive())
			{
				resource.release();
				removeResource(resource.getFilePath());
			}
		}
	}

	protected void toString(ObjectDescription description)
	{
		
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("pathname", pathname);
		description.append("resources", resources.size());

		toString(description);

		return description.toString();
	}
}
