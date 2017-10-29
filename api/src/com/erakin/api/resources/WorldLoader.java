package com.erakin.api.resources;

import static org.diverproject.log.LogSystem.logDebug;
import static org.diverproject.log.LogSystem.logWarning;

import java.io.File;

import org.diverproject.util.FileUtil;
import org.diverproject.util.ObjectDescription;

import com.erakin.api.resources.world.TerrainDimension;
import com.erakin.api.resources.world.WorldData;
import com.erakin.api.resources.world.WorldException;
import com.erakin.api.resources.world.WorldReader;
import com.erakin.api.resources.world.WorldReaderFactory;
import com.erakin.api.resources.world.WorldRuntimeException;

/**
 * <h1>Carregador de Mundos</h1>
 *
 * <p>Usada para fazer um gerenciamento avançado dos mundos/terrenos carregadas a partir de arquivos.
 * Além de carregar, salva as informações em uma pasta virtual em memória para que não seja carregada duas vezes.</p>
 *
 * <p>Por exemplo, um mundo chamado <b>terra</b> irá verificar se o mundo já foi carregado no sistema,
 * caso ele já tenha sido carregado, irá usar as informações do mesmo ao invés de carregar novamente.
 * Esse sistema permite além de economizar tempo de processamento evitar consumo de memória desnecessário.</p>
 *
 * <p>Por padrão o formato dos dados básicos de um mundo são armazenados em um arquivo <b>wdd</b>.
 * Esse arquivo deve conter o nome do mundo, quantidade de terrenos e a forma como será feita a leitura.
 * Podendo variar de acordo com a preferência ou necessidade do usuário em relação a informações do mundo.</p>
 *
 * @see ResourceMap
 * @see WorldReader
 * @see WorldReaderFactory
 *
 * @author Andrew Mello
 */

public class WorldLoader extends DefaultLoader<World>
{
	/**
	 * Instância para carregador de mundos no padrão de projetos Singleton.
	 */
	private static final WorldLoader INSTANCE = new WorldLoader();

	/**
	 * Dimensão mínima para definir o tamanho de latitude ou longitude em terrenos de um mundo.
	 */
	private static final int MIN_MAP_SIZE = 1;

	/**
	 * Dimensão máxima para definir o tamanho de latitude ou longitude em terrenos de um mundo.
	 */
	private static final int MAX_MAP_SIZE = 1024;


	/**
	 * Quantidade de mundos já carregados.
	 */
	private int count;

	/**
	 * Construtor privado para evitar múltiplas instâncias para o carregador de mapas.
	 * Inicializa o mapeador de recursos definindo o seu nome de acordo com as preferências.
	 * A preferência aqui utilizada é <code>worlds</code>, pasta para arquivos de mapas.
	 */

	private WorldLoader()
	{
		super("worlds");
	}

	/**
	 * Permite obter um determinado mapa já carregado ou então forçar o carregamento deste.
	 * Se o mapa existir irá retornar um mapa temporária dessa raíz caso contrário irá
	 * criar uma nova raíz carregando as informações do arquivo de acordo com o nome do mapa.
	 * Utiliza as propriedades de preferências que definem o caminho para arquivos de mapas.
	 * @param name nome do qual foi dado ao mapa, em outras palavras o nome do arquivo,
	 * caso não seja definido nenhuma extensão para esse, será considerado <b>map</b> por padrão.
	 * @return aquisição do objeto de mapa temporário gerado da raíz de acordo com o nome.
	 * @throws WorldException falha durante a leitura do arquivo ou arquivo com dados corrompidos.
	 */

	public World getMap(String name) throws WorldException
	{
		if (!name.contains("."))
			name += ".xml";

		ResourceRoot<World> resourceRoot = selectResource(name);

		if (resourceRoot != null)
		{
			WorldRoot mapRoot = (WorldRoot) resourceRoot;
			return mapRoot.genResource();
		}

		String path = getPathname() + name;
		WorldReaderFactory factory = WorldReaderFactory.getInstance();
		WorldReader reader = factory.getMapReaderOf(path);

		try {

			WorldData data = reader.readWorld(new File(path));
			World map = createWorld(path, data);

			return map;

		} catch (Exception e) {
			throw new WorldException(e);
		}
	}

	/**
	 * Permite construir um novo mapa a partir das informações abaixo.
	 * Caso o caminho de alocação já esteja sendo usado por outra não será possível criar.
	 * Se for possível criar, o mapa raíz será armazenada e gerada uma temporária.
	 * @param path caminho onde foi localizado o mapa, onde deve ser alocada.
	 * @param data objeto contendo os dados do mapa para armazenamento.
	 * @return aquisição de um mapa de uso temporário.
	 */

	public World createWorld(String path, WorldData data)
	{
		if (path == null)
			throw new WorldRuntimeException("caminho não definido");

		if (data == null)
			throw new WorldRuntimeException("dados do mapa não definido");

		String folderPathname = FileUtil.getParentPath(path);
		String folder = data.getFolder();

		if (folder.contains(":"))
			folderPathname = folder;
		else
			folderPathname = FileUtil.adaptPath(String.format("%s/%s", folderPathname, folder));

		if (!path.startsWith(getResourceName()))
		{
			if (path.contains(getResourceName()+ "/"))
				path = path.substring(path.indexOf(getResourceName()), path.length());
			else
				path = String.format("%s/%s", getResourceName(), path);
		}

		if (containResource(path))
			throw new WorldRuntimeException("mundo já existente (%s)", path);

		WorldRoot root = new WorldRoot();
		root.id = ++count;
		root.fileName = FileUtil.getFileName(path);
		root.filePath = FileUtil.getParentPath(path);
		root.fileExtension = FileUtil.getExtension(path);
		root.prefix = data.getPrefix();
		root.width = data.getWidth();
		root.length = data.getLength();
		root.terrainDimension = new TerrainDimension(data.getTerrainWidth(), data.getTerrainLength());
		root.terrainLoader = data.getTerrainLoader();
		root.folder = new File(folderPathname);
		root.unit = data.getUnit();

		validateLimits(root);

		logDebug("mapa '%s' lido com êxito (id: %d, width: %d, length: %d).\n",
				root.fileName, root.id, root.width, root.length);

		if (!insertResource(root))
			logWarning("não foi possível salvar o mapa '%s'.\n", root.fileName);

		return root.genResource();
	}

	/**
	 * Procedimento que irá fazer a verificação da validade dos dados de uma raíz para mapa.
	 * As verificações consistem em verificar se o tamanho do mapa está dentro dos limites.
	 * @param root referência da raíz do mapa do qual será validada.
	 */

	private void validateLimits(WorldRoot root)
	{
		if (root.width < MIN_MAP_SIZE)
			throw new WorldRuntimeException("falha ao alocar mapa (width: %d, min: %d)", root.width, MIN_MAP_SIZE);

		if (root.width > MAX_MAP_SIZE)
			throw new WorldRuntimeException("falha ao alocar mapa (width: %d, max: %d)", root.width, MAX_MAP_SIZE);

		if (root.length < MIN_MAP_SIZE)
			throw new WorldRuntimeException("falha ao alocar mapa (length: %d, min: %d)", root.length, MIN_MAP_SIZE);

		if (root.length > MAX_MAP_SIZE)
			throw new WorldRuntimeException("falha ao alocar mapa (length: %d, max: %d)", root.length, MAX_MAP_SIZE);
	}

	@Override
	protected void toString(ObjectDescription description)
	{
		description.append("count", count);
	}

	/**
	 * Procedimento que permite obter a única instância do carregador de mundos.
	 * Utiliza o padrão Singleton para evitar a existência de mais instâncias.
	 * @return aquisição da instância para utilização do carregador de mundos.
	 */

	public static WorldLoader getInstance()
	{
		return INSTANCE;
	}
}
