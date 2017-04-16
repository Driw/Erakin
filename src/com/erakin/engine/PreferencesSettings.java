package com.erakin.engine;

import com.erakin.common.ErakinException;

/**
 * <h1>Configura��es para Prefer�ncias</h1>
 *
 * <p>Classe usada apenas para determinar os objetos contendo os tipos de prefer�ncias que podem ser usados.
 * Sua finalidade � permitir que o desenvolvimento da aplica��o permita determinar como funcionar� as
 * prefer�ncias do seu jeito, e a partir disso, definir essas aqui para que o engine possa utilizar.</p>
 *
 * <p>Para garantir o seu funcionamento adequadamente, o engine verifica se todos os tipos de prefer�ncias
 * foram definidos, e de modo a garantir que nenhuma delas sejam perdidas no meio da execu��o n�o ir�
 * permitir que valores nulos sejam passados para essas prefer�ncias, de modo que n�o parem de funcionar.</p>
 *
 * @author Andrew Mello
 */

public class PreferencesSettings
{
	/**
	 * Prefer�ncias de v�deo usadas.
	 */
	private static Preferences videoPreferences = new PreferencesDefault();

	/**
	 * Prefer�ncias de diret�rios para arquivos.
	 */
	private static Preferences folderPreferences = new PreferencesDefault();

	/**
	 * Construtor privado para evitar inst�ncias desnecess�rias dessa classe.
	 */

	private PreferencesSettings()
	{
		
	}

	/**
	 * Prefer�ncias de v�deo s�o usadas para determinar algumas funcionalidades relacionadas ao v�deo.
	 * Como tamanho da tela, taxa de fps, sincroniza��o vertical, qualidade de renderiza��o e outros.
	 * @return aquisi��o do objeto contendo as prefer�ncias de v�deo usadas.
	 */

	public static Preferences getVideoPreferences()
	{
		return videoPreferences;
	}

	/**
	 * Permite definir qual ser� o objeto contendo as prefer�ncias de v�deo a ser usada.
	 * Caso uma prefer�ncia nula seja definida ser� mantida a antiga prefer�ncia.
	 * @param videoPreferences novas prefer�ncias de v�deo que ser�o utilizadas.
	 */

	public static void setVideoPreferences(Preferences videoPreferences)
	{
		if (videoPreferences == null)
			return;

		PreferencesSettings.videoPreferences = videoPreferences;
	}

	/**
	 * Prefer�ncia de pastas s�o usadas para possuir uma padr�o e melhorar a forma de como ler os arquivos.
	 * Assim � poss�vel alterar o diret�rio sem a necessidade de modificar linhas de c�digo do programa.
	 * @return aquisi��o do objeto contendo as prefer�ncias de pastas usadas.
	 */

	public static Preferences getFolderPreferences()
	{
		return folderPreferences;
	}

	/**
	 * Permite definir qual ser� o objeto contendo as prefer�ncias de pasta a ser usado.
	 * Caso uma prefer�ncia nula seja definida ser� mantida a antiga prefer�ncia.
	 * @param folderPreferences novas prefer�ncias de pasta que ser�o utilizadas.
	 */

	public static void setFolderPreferences(Preferences folderPreferences)
	{
		if (folderPreferences == null)
			return;

		PreferencesSettings.folderPreferences = folderPreferences;
	}

	/**
	 * Usado internamente pelo Engine para garantir que todas as prefer�ncias foram definidas na aplica��o.
	 * Essas prefer�ncias s�o utilizadas internamente no engine e devem estar definidas para funcionarem.
	 * @throws ErakinException apenas se alguma das prefer�ncias n�o tiverem sido definidas.
	 */

	public static void initiate() throws ErakinException
	{
		if (folderPreferences == null)
			throw new ErakinException("nenhuma prefer�ncia de diret�rios foi definida");

		if (videoPreferences == null)
			throw new ErakinException("nenhuma prefer�ncia de v�deo foi definida");
	}
}
