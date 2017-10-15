package com.erakin.api.resources.world;

import org.diverproject.util.FileUtil;
import org.diverproject.util.collection.Map;
import org.diverproject.util.collection.abstraction.StringSimpleMap;

import com.erakin.worlds.wds.WorldReaderWDS;

/**
 * <h1>F�brica para Leitor de Mapas</h1>
 *
 * <p>Essa classe dever� conter m�todos que permitem a cria��o de objetos contendo
 * os dados dos mapas passadas de acordo com o caminho do arquivo especificado
 * em disco, para assim carreg�-lo e trat�-lo da maneira mais apropriada.</p>
 *
 * <p>Tratamento tal que dever� verificar a extens�o do arquivo para adequar a forma
 * em que os dados ser�o lidos afim de entender como o modelo funciona e onde e
 * como as suas informa��es est�o armazenados dentro do arquivo em quest�o.</p>
 *
 * @see Map
 * @see WorldReader
 * @see WorldReaderWDS
 *
 * @author Andrew
 */

public class WorldReaderFactory
{
	/**
	 * Refer�ncia do objeto MapReaderFactory para adaptar ao padr�o de projetos Singleton.
	 */
	private static final WorldReaderFactory INSTANCE = new WorldReaderFactory();

	/**
	 * Lista contendo todas os leitores das extens�es de mapas.
	 */
	private Map<String, WorldReader> readers;

	/**
	 * Construtor privado para respeitar o padr�o de projetos Singleton.
	 * Inicializa o mapeamento de leitores dispon�veis adicionando os padr�es.
	 * Para esse ser� considerado os como padr�o apenas o formato MFS.
	 */

	private WorldReaderFactory()
	{
		readers = new StringSimpleMap<>();
	}

	/**
	 * Atrav�s do caminho do arquivo carregado, identifica a extens�o do mesmo.
	 * Ap�s identificar a extens�o ir� procurar se existe um leitor para tal.
	 * @param path caminho contendo a extens�o do arquivo a ser lido.
	 * @return aquisi��o do leitor de mapa a partir da extens�o.
	 */

	public WorldReader getMapReaderOf(String path)
	{
		String extension = FileUtil.getExtension(path).toLowerCase();
		WorldReader reader = readers.get(extension);

		if (reader == null)
			throw new WorldRuntimeException("extens�o '%s' n�o suportada", extension);

		return reader;
	}

	/**
	 * Adiciona um novo leitor para carregamento de mapas como op��o de leitura.
	 * Caso um leitor para a extens�o passada j� exista ele ser� substitu�do pela nova.
	 * @param extension tipo de extens�o de arquivo do qual ser� suportado a leitura.
	 * @param reader refer�ncia do leitor que ser� usado para a extens�o acima.
	 */

	public void addMapReader(String extension, WorldReader reader)
	{
		if (readers.containsKey(extension))
			readers.update(extension, reader);
		else
			readers.add(extension, reader);
	}

	/**
	 * MapReaderFactory utiliza o padr�o de projetos Singleton que permite a cria��o de apenas um objeto do tipo.
	 * Esse padr�o � usado para que n�o seja poss�vel criar mais do que uma f�brica para dados de mapas.
	 * N�o h� necessidade na exist�ncia de dois objetos deste, procedimentos n�o est�ticos pois n�o deve
	 * funcionar como um utilit�rio, mas sim com uma esp�cie de gerenciador, gerenciado mapas em disco.
	 * @return aquisi��o da refer�ncia do objeto f�brica para leitores de mapas em disco.
	 */

	public static WorldReaderFactory getInstance()
	{
		return INSTANCE;
	}
}
