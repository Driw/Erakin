package com.erakin.api.resources.shader;

import org.diverproject.util.FileUtil;
import org.diverproject.util.collection.Map;
import org.diverproject.util.collection.abstraction.StringSimpleMap;

/**
 * <h1>Fábrica para Leitor de Shader</h1>
 *
 * <p>Essa classe deverá conter métodos que permitem a criação de objetos contendo
 * a programação para computação gráfica de acordo com o caminho do arquivo especificado
 * em disco, para assim lê-lo e convertê-lo para que funcione corretamente no OpenGL.</p>
 *
 * <p>Tratamento tal que deverá verificar a extensão do arquivo para adequar a forma
 * em que os dados serão lidos afim de entender como o programa funciona e onde e
 * como as suas informações estão armazenados dentro do arquivo em questão.</p>
 *
 * @see Map
 * @see ShaderReader
 *
 * @author Andrew
 */

public class ShaderReaderFactory
{
	/**
	 * Referência do objeto ShaderReaderFactory para adaptar ao padrão de projetos Singleton.
	 */
	private static final ShaderReaderFactory SINGLETON = new ShaderReaderFactory();

	/**
	 * Lista contendo todas os leitores das extensões de programas para computações gráficas.
	 */
	private Map<String, ShaderReader> readers;

	/**
	 * Construtor privado para respeitar o padrão de projetos Singleton.
	 * Inicializa o mapeamento de leitores disponíveis adicionando os padrões.
	 */

	private ShaderReaderFactory()
	{
		readers = new StringSimpleMap<ShaderReader>();
	}

	/**
	 * Através do caminho do arquivo carregado, identifica a extensão do mesmo.
	 * Após identificar a extensão irá procurar se existe um leitor para tal.
	 * @param path caminho contendo a extensão do arquivo a ser lido.
	 * @return aquisição do leitor de computação gráfica da extensão.
	 */

	public ShaderReader getShaderReaderOf(String path)
	{
		String extension = FileUtil.getExtension(path).toLowerCase();
		ShaderReader reader = readers.get(extension);

		if (reader == null)
			throw new ShaderRuntimeException("extensão '%s' não suportada", extension);

		return reader;
	}

	/**
	 * Adiciona um novo leitor para programas de computação gráfica como opção de leitura.
	 * Caso um leitor para a extensão passada já exista ele será substituído pela nova.
	 * @param extension tipo de extensão de arquivo do qual será suportado a leitura.
	 * @param reader referência do leitor que será usado para a extensão acima.
	 */

	public void addShaderReader(String extension, ShaderReader reader)
	{
		if (readers.containsKey(extension))
			readers.update(extension, reader);
		else
			readers.add(extension, reader);
	}

	/**
	 * ShaderReaderFactory utiliza o padrão de projetos Singleton que permite a criação de apenas um objeto do tipo.
	 * Esse padrão é usado para que não seja possível criar mais do que uma fábrica para dados de computações gráfica.
	 * Não há necessidade na existência de dois objetos deste, procedimentos não estáticos pois não deve
	 * funcionar como um utilitário, mas sim com uma espécie de gerenciador, gerenciado programas em disco.
	 * @return aquisição da referência do objeto fábrica para leitores de programas para computação gráfica em disco.
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
