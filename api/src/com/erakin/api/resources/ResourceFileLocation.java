package com.erakin.api.resources;

/**
 * <h1>Localiza��o do Arquivo de Recurso</h1>
 *
 * <p>Interface usada para determinar os m�todos que podem ser �teis para localiza��o de um arquivo de recurso em disco.
 * Atrav�s desses m�todos � poss�vel saber o tipo de arquivo (extens�o), nome do arquivo, diret�rio e caminho em disco.</p>
 *
 * @author Andrew
 */

public interface ResourceFileLocation
{
	/**
	 * A extens�o do arquivo de recurso determina como os dados do arquivo se encontram para serem lidos.
	 * Para o seguinte arquivo em disco "/data/resource_type/filename.extension" ser� retornado "extension".
	 * @return aquisi��o do tipo de arquivo (extens�o) de recurso.
	 */

	String getFileExtension();

	/**
	 * O nome do arquivo � usado para um reconhecimento do usu�rio, o nome define boa parte do seu conte�do.
	 * Para o seguinte arquivo em disco "/data/resource_type/filename.extension" ser� retornado "filename".
	 * @return aquisi��o do nome do arquivo de recurso.
	 */

	String getFileName();

	/**
	 * O nome completo do arquivo � composto pelo nome do arquivo em si e o tipo de arquivo (extens�o).
	 * Para o seguinte arquivo em disco "/data/resource_type/filename.extension" ser� retornado "filename.extension".
	 * @return aquisi��o do nome completo (nome + extens�o) do arquivo de recurso.
	 */

	String getFileFullName();

	/**
	 * O diret�rio determina o local em disco onde pode ser encontrado outros arquivos com a mesma finalidade ou tipo.
	 * Para o seguinte arquivo em disco "/data/resource_type/filename.extension" ser� retornado "/data/resource_type/".
	 * @return aquisi��o do caminho de diret�rio do arquivo em disco.
	 */

	String getFileDirectory();

	/**
	 * O caminho do arquivo de recurso pode ser utilizado para recarregar as informa��es do recurso.
	 * Para o seguinte arquivo em disco "/data/resource_type/filename.extension" ser� retornado o mesmo valor.
	 * @return aquisi��o do caminho completo do arquivo em disco.
	 */

	String getFilePath();
}
