package com.erakin.api.resources.shader;

import org.diverproject.util.FileUtil;
import org.diverproject.util.collection.Map;
import org.diverproject.util.collection.abstraction.StringSimpleMap;

/**
 * <h1>F�brica para Leitor de Shader</h1>
 *
 * <p>Essa classe dever� conter m�todos que permitem a cria��o de objetos contendo
 * a programa��o para computa��o gr�fica de acordo com o caminho do arquivo especificado
 * em disco, para assim l�-lo e convert�-lo para que funcione corretamente no OpenGL.</p>
 *
 * <p>Tratamento tal que dever� verificar a extens�o do arquivo para adequar a forma
 * em que os dados ser�o lidos afim de entender como o programa funciona e onde e
 * como as suas informa��es est�o armazenados dentro do arquivo em quest�o.</p>
 *
 * @see Map
 * @see ShaderReader
 *
 * @author Andrew
 */

public class ShaderReaderFactory
{
	/**
	 * Refer�ncia do objeto ShaderReaderFactory para adaptar ao padr�o de projetos Singleton.
	 */
	private static final ShaderReaderFactory SINGLETON = new ShaderReaderFactory();

	/**
	 * Lista contendo todas os leitores das extens�es de programas para computa��es gr�ficas.
	 */
	private Map<String, ShaderReader> readers;

	/**
	 * Construtor privado para respeitar o padr�o de projetos Singleton.
	 * Inicializa o mapeamento de leitores dispon�veis adicionando os padr�es.
	 */

	private ShaderReaderFactory()
	{
		readers = new StringSimpleMap<ShaderReader>();
	}

	/**
	 * Atrav�s do caminho do arquivo carregado, identifica a extens�o do mesmo.
	 * Ap�s identificar a extens�o ir� procurar se existe um leitor para tal.
	 * @param path caminho contendo a extens�o do arquivo a ser lido.
	 * @return aquisi��o do leitor de computa��o gr�fica da extens�o.
	 */

	public ShaderReader getShaderReaderOf(String path)
	{
		String extension = FileUtil.getExtension(path).toLowerCase();
		ShaderReader reader = readers.get(extension);

		if (reader == null)
			throw new ShaderRuntimeException("extens�o '%s' n�o suportada", extension);

		return reader;
	}

	/**
	 * Adiciona um novo leitor para programas de computa��o gr�fica como op��o de leitura.
	 * Caso um leitor para a extens�o passada j� exista ele ser� substitu�do pela nova.
	 * @param extension tipo de extens�o de arquivo do qual ser� suportado a leitura.
	 * @param reader refer�ncia do leitor que ser� usado para a extens�o acima.
	 */

	public void addShaderReader(String extension, ShaderReader reader)
	{
		if (readers.containsKey(extension))
			readers.update(extension, reader);
		else
			readers.add(extension, reader);
	}

	/**
	 * ShaderReaderFactory utiliza o padr�o de projetos Singleton que permite a cria��o de apenas um objeto do tipo.
	 * Esse padr�o � usado para que n�o seja poss�vel criar mais do que uma f�brica para dados de computa��es gr�fica.
	 * N�o h� necessidade na exist�ncia de dois objetos deste, procedimentos n�o est�ticos pois n�o deve
	 * funcionar como um utilit�rio, mas sim com uma esp�cie de gerenciador, gerenciado programas em disco.
	 * @return aquisi��o da refer�ncia do objeto f�brica para leitores de programas para computa��o gr�fica em disco.
	 */

	public static ShaderReaderFactory getInstance()
	{
		return SINGLETON;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName();
	}
}
