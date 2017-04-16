package com.erakin.engine.resource;

import org.diverproject.util.ObjectDescription;

import com.erakin.engine.Preferences;
import com.erakin.engine.PreferencesSettings;

/**
 * <h1>Carregador Padr�o</h1>
 *
 * <p>Classe usada para implementar algumas funcionalidades padr�es entre todos os carregadores.
 * Protege a visibilidade de alguns objetos de forma a manter melhor a seguran�a de seu uso.
 * Possui como responsabilidade um mapeamento dos recursos carregados e o caminho a carregar.</p>
 *
 * @see PreferencesSettings
 *
 * @author Andre Mello
 */

public class DefaultLoader
{
	/**
	 * Caminho parcial ou completo da localiza��o dos arquivos que ser�o carregados.
	 */
	private String pathname;

	/**
	 * Mapeador de recursos para permitir o gerenciamento de adicionar, remover e selecionar.
	 */
	private ResourceMap resources;

	/**
	 * Cria um novo carregador padr�o inicializando algumas defini��es b�sicas do seu funcionamento.
	 * Deve definir adequadamente o caminho base que ser� usado quando um arquivo for carregado.
	 * @param property nome da propriedade que cont�m o caminho parcial ou completo da pasta.
	 */

	public DefaultLoader(String property)
	{
		this(property, property);
	}

	/**
	 * Cria um novo carregador padr�o inicializando algumas defini��es b�sicas do seu funcionamento.
	 * Deve definir adequadamente o caminho base que ser� usado quando um arquivo for carregado.
	 * @param property nome da propriedade que cont�m o caminho parcial ou completo da pasta.
	 * @param name nome que ser� dado para esse carregador de recursos e tamb�m como pr�-fixo.
	 */

	public DefaultLoader(String property, String name)
	{
		Preferences preferences = PreferencesSettings.getFolderPreferences();
		String path = preferences.getOptionString(property);

		pathname = path;
		resources = new ResourceMap(name);
	}

	/**
	 * Insere um novo recurso ra�z para ser armazenado pelo carregador padr�o.
	 * Deve ser usado apenas quando um recurso ra�z tiver sido carregado completamente.
	 * Tendo ainda em mente que este dever� ter sido verificado e validado pelo sistema.
	 * @param resource refer�ncia do recurso ra�z que est� pronto para ser listado.
	 * @return true se conseguir inserir o recurso ra�z ou false caso contr�rio.
	 */

	protected boolean insertResource(ResourceRoot resource)
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

	protected ResourceRoot selectResource(String pathname)
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
	 * Nome dos recursos � usado como nome da pasta virtual que ir� armazenar os recursos.
	 * Esse ser� usado como pr�-fixo de todos os recursos que forem adicionados ao carregador.
	 * @return aquisi��o do nome do tipo de recursos que est�o sendo armazenados no carregador.
	 */

	protected String getResourceName()
	{
		return resources.getName();
	}

	protected void toString(ObjectDescription description)
	{
		
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("pathname", pathname);
		description.append("resources", resources.length());

		toString(description);

		return super.toString();
	}
}
