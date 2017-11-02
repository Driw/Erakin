package com.erakin.api.resources;

/**
 * <h1>Localização do Arquivo de Recurso</h1>
 *
 * <p>Interface usada para determinar os métodos que podem ser úteis para localização de um arquivo de recurso em disco.
 * Através desses métodos é possível saber o tipo de arquivo (extensão), nome do arquivo, diretório e caminho em disco.</p>
 *
 * @author Andrew
 */

public interface ResourceFileLocation
{
	/**
	 * A extensão do arquivo de recurso determina como os dados do arquivo se encontram para serem lidos.
	 * Para o seguinte arquivo em disco "/data/resource_type/filename.extension" será retornado "extension".
	 * @return aquisição do tipo de arquivo (extensão) de recurso.
	 */

	String getFileExtension();

	/**
	 * O nome do arquivo é usado para um reconhecimento do usuário, o nome define boa parte do seu conteúdo.
	 * Para o seguinte arquivo em disco "/data/resource_type/filename.extension" será retornado "filename".
	 * @return aquisição do nome do arquivo de recurso.
	 */

	String getFileName();

	/**
	 * O nome completo do arquivo é composto pelo nome do arquivo em si e o tipo de arquivo (extensão).
	 * Para o seguinte arquivo em disco "/data/resource_type/filename.extension" será retornado "filename.extension".
	 * @return aquisição do nome completo (nome + extensão) do arquivo de recurso.
	 */

	String getFileFullName();

	/**
	 * O diretório determina o local em disco onde pode ser encontrado outros arquivos com a mesma finalidade ou tipo.
	 * Para o seguinte arquivo em disco "/data/resource_type/filename.extension" será retornado "/data/resource_type/".
	 * @return aquisição do caminho de diretório do arquivo em disco.
	 */

	String getFileDirectory();

	/**
	 * O caminho do arquivo de recurso pode ser utilizado para recarregar as informações do recurso.
	 * Para o seguinte arquivo em disco "/data/resource_type/filename.extension" será retornado o mesmo valor.
	 * @return aquisição do caminho completo do arquivo em disco.
	 */

	String getFilePath();
}
