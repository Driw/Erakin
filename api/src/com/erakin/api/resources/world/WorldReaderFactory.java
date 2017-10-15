package com.erakin.api.resources.world;

import org.diverproject.util.FileUtil;
import org.diverproject.util.collection.Map;
import org.diverproject.util.collection.abstraction.StringSimpleMap;

import com.erakin.worlds.wds.WorldReaderWDS;

/**
 * <h1>Fábrica para Leitor de Mapas</h1>
 *
 * <p>Essa classe deverá conter métodos que permitem a criação de objetos contendo
 * os dados dos mapas passadas de acordo com o caminho do arquivo especificado
 * em disco, para assim carregá-lo e tratá-lo da maneira mais apropriada.</p>
 *
 * <p>Tratamento tal que deverá verificar a extensão do arquivo para adequar a forma
 * em que os dados serão lidos afim de entender como o modelo funciona e onde e
 * como as suas informações estão armazenados dentro do arquivo em questão.</p>
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
	 * Referência do objeto MapReaderFactory para adaptar ao padrão de projetos Singleton.
	 */
	private static final WorldReaderFactory INSTANCE = new WorldReaderFactory();

	/**
	 * Lista contendo todas os leitores das extensões de mapas.
	 */
	private Map<String, WorldReader> readers;

	/**
	 * Construtor privado para respeitar o padrão de projetos Singleton.
	 * Inicializa o mapeamento de leitores disponíveis adicionando os padrões.
	 * Para esse será considerado os como padrão apenas o formato MFS.
	 */

	private WorldReaderFactory()
	{
		readers = new StringSimpleMap<>();
	}

	/**
	 * Através do caminho do arquivo carregado, identifica a extensão do mesmo.
	 * Após identificar a extensão irá procurar se existe um leitor para tal.
	 * @param path caminho contendo a extensão do arquivo a ser lido.
	 * @return aquisição do leitor de mapa a partir da extensão.
	 */

	public WorldReader getMapReaderOf(String path)
	{
		String extension = FileUtil.getExtension(path).toLowerCase();
		WorldReader reader = readers.get(extension);

		if (reader == null)
			throw new WorldRuntimeException("extensão '%s' não suportada", extension);

		return reader;
	}

	/**
	 * Adiciona um novo leitor para carregamento de mapas como opção de leitura.
	 * Caso um leitor para a extensão passada já exista ele será substituído pela nova.
	 * @param extension tipo de extensão de arquivo do qual será suportado a leitura.
	 * @param reader referência do leitor que será usado para a extensão acima.
	 */

	public void addMapReader(String extension, WorldReader reader)
	{
		if (readers.containsKey(extension))
			readers.update(extension, reader);
		else
			readers.add(extension, reader);
	}

	/**
	 * MapReaderFactory utiliza o padrão de projetos Singleton que permite a criação de apenas um objeto do tipo.
	 * Esse padrão é usado para que não seja possível criar mais do que uma fábrica para dados de mapas.
	 * Não há necessidade na existência de dois objetos deste, procedimentos não estáticos pois não deve
	 * funcionar como um utilitário, mas sim com uma espécie de gerenciador, gerenciado mapas em disco.
	 * @return aquisição da referência do objeto fábrica para leitores de mapas em disco.
	 */

	public static WorldReaderFactory getInstance()
	{
		return INSTANCE;
	}
}
