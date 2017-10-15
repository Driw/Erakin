package com.erakin.api.resources.model;

import org.diverproject.util.FileUtil;
import org.diverproject.util.collection.Map;
import org.diverproject.util.collection.abstraction.StringSimpleMap;

/**
 * <h1>F�brica para Leitor de Modelos</h1>
 *
 * <p>Essa classe dever� conter m�todos que permitem a cria��o de objetos contendo
 * os dados dos modelos passadas de acordo com o caminho do arquivo especificado
 * em disco, para assim l�-lo e trat�-lo da maneira mais apropriada.</p>
 *
 * <p>Tratamento tal que dever� verificar a extens�o do arquivo para adequar a forma
 * em que os dados ser�o lidos afim de entender como o modelo funciona e onde e
 * como as suas informa��es est�o armazenados dentro do arquivo em quest�o.</p>
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
	 * Refer�ncia do objeto ModelReaderFactory para adaptar ao padr�o de projetos Singleton.
	 */
	private static final ModelReaderFactory SINGLETON = new ModelReaderFactory();

	/**
	 * Lista contendo todas os leitores das extens�es de modelos tri-dimensionais.
	 */
	private Map<String, ModelReader> readers;

	/**
	 * Construtor privado para respeitar o padr�o de projetos Singleton.
	 * Inicializa o mapeamento de leitores dispon�veis adicionando os padr�es.
	 * Para esse ser� considerado os como padr�o apenas os formatos MDL e OBJ.
	 */

	private ModelReaderFactory()
	{
		readers = new StringSimpleMap<ModelReader>();
	}

	/**
	 * Atrav�s do caminho do arquivo carregado, identifica a extens�o do mesmo.
	 * Ap�s identificar a extens�o ir� procurar se existe um leitor para tal.
	 * @param path caminho contendo a extens�o do arquivo a ser lido.
	 * @return aquisi��o do leitor de modelagem tri-dimensional da extens�o.
	 */

	public ModelReader getModelReaderOf(String path)
	{
		String extension = FileUtil.getExtension(path).toLowerCase();
		ModelReader reader = readers.get(extension);

		if (reader == null)
			throw new ModelRuntimeException("extens�o '%s' n�o suportada", extension);

		return reader;
	}

	/**
	 * Adiciona um novo leitor para modelos tri-dimensionais como op��o de leitura.
	 * Caso um leitor para a extens�o passada j� exista ele ser� substitu�do pela nova.
	 * @param extension tipo de extens�o de arquivo do qual ser� suportado a leitura.
	 * @param reader refer�ncia do leitor que ser� usado para a extens�o acima.
	 */

	public void addModelReader(String extension, ModelReader reader)
	{
		readers.update(extension, reader);
	}

	/**
	 * ModelReaderFactory utiliza o padr�o de projetos Singleton que permite a cria��o de apenas um objeto do tipo.
	 * Esse padr�o � usado para que n�o seja poss�vel criar mais do que uma f�brica para dados de modelos.
	 * N�o h� necessidade na exist�ncia de dois objetos deste, procedimentos n�o est�ticos pois n�o deve
	 * funcionar como um utilit�rio, mas sim com uma esp�cie de gerenciador, gerenciado modelos em disco.
	 * @return aquisi��o da refer�ncia do objeto f�brica para leitores de modelagens em disco.
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
