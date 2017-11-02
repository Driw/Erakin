package com.erakin.api.resources;

import static org.diverproject.util.Util.format;

import org.diverproject.util.FileUtil;

/**
 * <h1>Arquivo de Recurso</h1>
 *
 * <p>Classe utilizada para separar cada informação de um arquivo de recurso carregado em disco.
 * As informações consistem em definir o tipo de arquivo (extensão), nome e diretório.</p>
 *
 * @author Andrew
 */

public class ResourceFile implements ResourceFileLocation
{
	/**
	 * Tipo do arquivo de recurso.
	 */
	private String extension;

	/**
	 * Nome do recurso do qual foi carregado.
	 */
	private String name;

	/**
	 * Caminho do recurso em disco sem considerar o diretório para recursos.
	 */
	private String directory;

	/**
	 * Define todas as informações necessárias para localização de um arquivo de recurso.
	 * Através do caminho especificado será identificado a extensão, nome e diretório.
	 * @param path caminho parcial ou completo do arquivo em disco do recurso.
	 */

	public void setFilePath(String path)
	{
		name = FileUtil.getFileName(path);
		extension = FileUtil.getExtension(path);
		directory = FileUtil.getParentPath(path);
	}

	@Override
	public String getFileExtension()
	{
		return extension;
	}

	@Override
	public String getFileName()
	{
		return name;
	}

	@Override
	public String getFileFullName()
	{
		if (extension == null)
			return name;

		return format("%s.%s", name, extension);
	}

	@Override
	public String getFileDirectory()
	{
		return directory == null ? "" : directory;
	}

	@Override
	public String getFilePath()
	{
		return format("%s/%s", getFileDirectory(), getFileFullName());
	}

	@Override
	public String toString()
	{
		return getFilePath();
	}
}
