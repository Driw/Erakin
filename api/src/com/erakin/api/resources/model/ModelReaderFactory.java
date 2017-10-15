package com.erakin.api.resources.model;

import org.diverproject.util.FileUtil;
import org.diverproject.util.collection.Map;
import org.diverproject.util.collection.abstraction.StringSimpleMap;

/**
 * <h1>Fábrica para Leitor de Modelos</h1>
 *
 * <p>Essa classe deverá conter métodos que permitem a criação de objetos contendo
 * os dados dos modelos passadas de acordo com o caminho do arquivo especificado
 * em disco, para assim lê-lo e tratá-lo da maneira mais apropriada.</p>
 *
 * <p>Tratamento tal que deverá verificar a extensão do arquivo para adequar a forma
 * em que os dados serão lidos afim de entender como o modelo funciona e onde e
 * como as suas informações estão armazenados dentro do arquivo em questão.</p>
 *
 * @see Map
 * @see ModelReader
 * @see ModelReaderMDL
 * @see ModelReaderOBJ
 *
 * @author Andrew
 */

public class ModelReaderFactory
{
	/**
	 * Referência do objeto ModelReaderFactory para adaptar ao padrão de projetos Singleton.
	 */
	private static final ModelReaderFactory SINGLETON = new ModelReaderFactory();

	/**
	 * Lista contendo todas os leitores das extensões de modelos tri-dimensionais.
	 */
	private Map<String, ModelReader> readers;

	/**
	 * Construtor privado para respeitar o padrão de projetos Singleton.
	 * Inicializa o mapeamento de leitores disponíveis adicionando os padrões.
	 * Para esse será considerado os como padrão apenas os formatos MDL e OBJ.
	 */

	private ModelReaderFactory()
	{
		readers = new StringSimpleMap<ModelReader>();
	}

	/**
	 * Através do caminho do arquivo carregado, identifica a extensão do mesmo.
	 * Após identificar a extensão irá procurar se existe um leitor para tal.
	 * @param path caminho contendo a extensão do arquivo a ser lido.
	 * @return aquisição do leitor de modelagem tri-dimensional da extensão.
	 */

	public ModelReader getModelReaderOf(String path)
	{
		String extension = FileUtil.getExtension(path).toLowerCase();
		ModelReader reader = readers.get(extension);

		if (reader == null)
			throw new ModelRuntimeException("extensão '%s' não suportada", extension);

		return reader;
	}

	/**
	 * Adiciona um novo leitor para modelos tri-dimensionais como opção de leitura.
	 * Caso um leitor para a extensão passada já exista ele será substituído pela nova.
	 * @param extension tipo de extensão de arquivo do qual será suportado a leitura.
	 * @param reader referência do leitor que será usado para a extensão acima.
	 */

	public void addModelReader(String extension, ModelReader reader)
	{
		readers.update(extension, reader);
	}

	/**
	 * ModelReaderFactory utiliza o padrão de projetos Singleton que permite a criação de apenas um objeto do tipo.
	 * Esse padrão é usado para que não seja possível criar mais do que uma fábrica para dados de modelos.
	 * Não há necessidade na existência de dois objetos deste, procedimentos não estáticos pois não deve
	 * funcionar como um utilitário, mas sim com uma espécie de gerenciador, gerenciado modelos em disco.
	 * @return aquisição da referência do objeto fábrica para leitores de modelagens em disco.
	 */

	public static ModelReaderFactory getInstance()
	{
		return SINGLETON;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName();
	}
}
