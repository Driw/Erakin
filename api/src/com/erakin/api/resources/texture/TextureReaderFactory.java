package com.erakin.api.resources.texture;

import org.diverproject.util.FileUtil;
import org.diverproject.util.collection.Map;
import org.diverproject.util.collection.abstraction.StringSimpleMap;

/**
 * <h1>F�brica para Leitor de Texturas</h1>
 *
 * <p>Essa classe dever� conter m�todos que permitem a cria��o de objetos contendo
 * os dados das texturas passadas de acordo com o caminho do arquivo especificado
 * em disco, para assim carreg�-lo e trat�-lo da maneira mais apropriada.</p>
 *
 * <p>Tratamento tal que dever� verificar a extens�o do arquivo para adequar a forma
 * em que os dados ser�o lidos afim de entender como o modelo funciona e onde e
 * como as suas informa��es est�o armazenados dentro do arquivo em quest�o.</p>
 *
 * @see Map
 * @see TextureReader
 * @see TextureReaderBMP
 * @see TextureReaderJPG
 * @see TextureReaderPNG
 *
 * @author Andrew
 */

public class TextureReaderFactory
{
	/**
	 * Refer�ncia do objeto TextureReaderFactory para adaptar ao padr�o de projetos Singleton.
	 */
	private static final TextureReaderFactory INSTANCE = new TextureReaderFactory();

	/**
	 * Lista contendo todas os leitores das extens�es de texturas.
	 */
	private Map<String, TextureReader> readers;

	/**
	 * Construtor privado para respeitar o padr�o de projetos Singleton.
	 * Inicializa o mapeamento de leitores dispon�veis adicionando os padr�es.
	 * Para esse ser� considerado os como padr�o os formatos PNG, BMP e JPG.
	 */

	private TextureReaderFactory()
	{
		readers = new StringSimpleMap<TextureReader>();
	}

	/**
	 * Atrav�s do caminho do arquivo carregado, identifica a extens�o do mesmo.
	 * Ap�s identificar a extens�o ir� procurar se existe um leitor para tal.
	 * @param path caminho contendo a extens�o do arquivo a ser lido.
	 * @return aquisi��o do leitor de textura a partir da extens�o.
	 */

	public TextureReader getTextureReaderOf(String path)
	{
		String extension = FileUtil.getExtension(path).toLowerCase();
		TextureReader reader = readers.get(extension);

		if (reader == null)
			throw new TextureRuntimeException("extens�o '%s' n�o suportada", extension);

		return reader;
	}

	/**
	 * Adiciona um novo leitor para carregamento de texturas como op��o de leitura.
	 * Caso um leitor para a extens�o passada j� exista ele ser� substitu�do pela nova.
	 * @param extension tipo de extens�o de arquivo do qual ser� suportado a leitura.
	 * @param reader refer�ncia do leitor que ser� usado para a extens�o acima.
	 */

	public void addTextureReader(String extension, TextureReader reader)
	{
		if (readers.containsKey(extension))
			readers.update(extension, reader);
		else
			readers.add(extension, reader);
	}

	/**
	 * TextureReaderFactory utiliza o padr�o de projetos Singleton que permite a cria��o de apenas um objeto do tipo.
	 * Esse padr�o � usado para que n�o seja poss�vel criar mais do que uma f�brica para dados de texturas.
	 * N�o h� necessidade na exist�ncia de dois objetos deste, procedimentos n�o est�ticos pois n�o deve
	 * funcionar como um utilit�rio, mas sim com uma esp�cie de gerenciador, gerenciado texturas em disco.
	 * @return aquisi��o da refer�ncia do objeto f�brica para leitores de texturas em disco.
	 */

	public static TextureReaderFactory getInstance()
	{
		return INSTANCE;
	}
}
