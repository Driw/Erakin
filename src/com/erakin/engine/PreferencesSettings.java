package com.erakin.engine;

import com.erakin.common.ErakinException;

/**
 * <h1>Configurações para Preferências</h1>
 *
 * <p>Classe usada apenas para determinar os objetos contendo os tipos de preferências que podem ser usados.
 * Sua finalidade é permitir que o desenvolvimento da aplicação permita determinar como funcionará as
 * preferências do seu jeito, e a partir disso, definir essas aqui para que o engine possa utilizar.</p>
 *
 * <p>Para garantir o seu funcionamento adequadamente, o engine verifica se todos os tipos de preferências
 * foram definidos, e de modo a garantir que nenhuma delas sejam perdidas no meio da execução não irá
 * permitir que valores nulos sejam passados para essas preferências, de modo que não parem de funcionar.</p>
 *
 * @author Andrew Mello
 */

public class PreferencesSettings
{
	/**
	 * Preferências de vídeo usadas.
	 */
	private static Preferences videoPreferences = new PreferencesDefault();

	/**
	 * Preferências de diretórios para arquivos.
	 */
	private static Preferences folderPreferences = new PreferencesDefault();

	/**
	 * Construtor privado para evitar instâncias desnecessárias dessa classe.
	 */

	private PreferencesSettings()
	{
		
	}

	/**
	 * Preferências de vídeo são usadas para determinar algumas funcionalidades relacionadas ao vídeo.
	 * Como tamanho da tela, taxa de fps, sincronização vertical, qualidade de renderização e outros.
	 * @return aquisição do objeto contendo as preferências de vídeo usadas.
	 */

	public static Preferences getVideoPreferences()
	{
		return videoPreferences;
	}

	/**
	 * Permite definir qual será o objeto contendo as preferências de vídeo a ser usada.
	 * Caso uma preferência nula seja definida será mantida a antiga preferência.
	 * @param videoPreferences novas preferências de vídeo que serão utilizadas.
	 */

	public static void setVideoPreferences(Preferences videoPreferences)
	{
		if (videoPreferences == null)
			return;

		PreferencesSettings.videoPreferences = videoPreferences;
	}

	/**
	 * Preferência de pastas são usadas para possuir uma padrão e melhorar a forma de como ler os arquivos.
	 * Assim é possível alterar o diretório sem a necessidade de modificar linhas de código do programa.
	 * @return aquisição do objeto contendo as preferências de pastas usadas.
	 */

	public static Preferences getFolderPreferences()
	{
		return folderPreferences;
	}

	/**
	 * Permite definir qual será o objeto contendo as preferências de pasta a ser usado.
	 * Caso uma preferência nula seja definida será mantida a antiga preferência.
	 * @param folderPreferences novas preferências de pasta que serão utilizadas.
	 */

	public static void setFolderPreferences(Preferences folderPreferences)
	{
		if (folderPreferences == null)
			return;

		PreferencesSettings.folderPreferences = folderPreferences;
	}

	/**
	 * Usado internamente pelo Engine para garantir que todas as preferências foram definidas na aplicação.
	 * Essas preferências são utilizadas internamente no engine e devem estar definidas para funcionarem.
	 * @throws ErakinException apenas se alguma das preferências não tiverem sido definidas.
	 */

	public static void initiate() throws ErakinException
	{
		if (folderPreferences == null)
			throw new ErakinException("nenhuma preferência de diretórios foi definida");

		if (videoPreferences == null)
			throw new ErakinException("nenhuma preferência de vídeo foi definida");
	}
}
