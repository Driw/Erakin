package com.erakin.api.resources;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Carregador Padr�o</h1>
 *
 * <p>Classe usada para implementar algumas funcionalidades padr�es entre todos os carregadores.
 * Protege a visibilidade de alguns objetos de forma a manter melhor a seguran�a de seu uso.
 * Possui como responsabilidade um mapeamento dos recursos carregados e o caminho a carregar.</p>
 *
 * @author Andre Mello
 *
 * @param <T> tipo de recurso que poder� ser carregado.
 */

public class DefaultLoader<T extends Resource<?>>
{
	/**
	 * Caminho parcial ou completo da localiza��o dos arquivos que ser�o carregados.
	 */
	private String pathname;

	/**
	 * Mapeador de recursos para permitir o gerenciamento de adicionar, remover e selecionar.
	 */
	private ResourceMap<T> resources;

	/**
	 * Cria um novo carregador padr�o inicializando algumas defini��es b�sicas do seu funcionamento.
	 * Deve definir adequadamente o caminho base que ser� usado quando um arquivo for carregado.
	 * @param name nome que ser� dado para esse carregador de recursos e tamb�m como pr�-fixo.
	 */

	public DefaultLoader(String name)
	{
		resources = new ResourceMap<T>(name);
	}

	/**
	 * Insere um novo recurso ra�z para ser armazenado pelo carregador padr�o.
	 * Deve ser usado apenas quando um recurso ra�z tiver sido carregado completamente.
	 * Tendo ainda em mente que este dever� ter sido verificado e validado pelo sistema.
	 * @param resource refer�ncia do recurso ra�z que est� pronto para ser listado.
	 * @return true se conseguir inserir o recurso ra�z ou false caso contr�rio.
	 */

	protected boolean insertResource(ResourceRoot<T> resource)
	{
		return resources.add(resource);
	}

	/**
	 * Remove um recurso ra�z atrav�s da especifica��o do seu caminho dentro do carregador padr�o.
	 * Deve ser usado apenas quando um recurso ra�z tiver sido descarregado completamente.
	 * Tendo ainda em mente que uma vez descarregado para ser usado novamente � preciso carreg�-lo.
	 * @param pathname nome de identifica��o do recurso ra�z dentro do carregador a ser removido.
	 * @return true se conseguir remover o recurso ra�z ou false caso contr�rio.
	 */

	protected boolean removeResource(String pathname)
	{
		return resources.remove(pathname);
	}

	/**
	 * Seleciona um recurso ra�z atrav�s do seu nome de identifica��o dentro do carregador padr�o.
	 * @param pathname nome de identifica��o do recurso ra�z dentro do carregador a selecionar.
	 * @return refer�ncia do recurso ra�z de acordo com o nome de identifica��o passado.
	 */

	protected ResourceRoot<T> selectResource(String pathname)
	{
		if (!pathname.startsWith(getPathname()))
			pathname = String.format("%s/%s", getResourceName(), pathname);

		return resources.get(pathname);
	}

	/**
	 * Verifica se um determinado recurso ra�z est� inserido dentro do carregador padr�o.
	 * @param pathname nome de identifica��o do recurso ra�z a ser verificado.
	 * @return true se encontrar um recurso ra�z ou false caso contr�rio.
	 */

	protected boolean containResource(String pathname)
	{
		return selectResource(pathname) != null;
	}

	/**
	 * Cada carregador padr�o ter� um configura��o na parte de configura��es padr�es de pastas.
	 * @return aquisi��o do caminho parcial ou completo especificado por <code>PreferenceSettings</code>.
	 */

	public String getPathname()
	{
		return pathname;
	}

	/**
	 * Define qual ser� o caminho padr�o para leitura dos arquivos de recursos.
	 * @param pathname caminho parcial ou completo do diret�rio contendo os arquivos.
	 */

	public void setPathname(String pathname)
	{
		this.pathname = pathname;
	}

	/**
	 * Nome dos recursos � usado como nome da pasta virtual que ir� armazenar os recursos.
	 * Esse ser� usado como pr�-fixo de todos os recursos que forem adicionados ao carregador.
	 * @return aquisi��o do nome do tipo de recursos que est�o sendo armazenados no carregador.
	 */

	protected String getResourceName()
	{
		return resources.getName();
	}

	/**
	 * Itera cada um dos recursos ra�zes salvos no carregador para que seja atualizado o seu tempo de vida �til.
	 * Caso seu tempo de vida �til tenha acabo o seu conte�do ser� liberado completamente do carregador.
	 * <i>Um recurso ra�z removido n�o ter� mais utilidade pra nenhum recurso referente a ele, por�m pode ser recarregado.</i>.
	 * @param delay quantos milissegundos se passou desde a �ltima atualiza��o.
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
