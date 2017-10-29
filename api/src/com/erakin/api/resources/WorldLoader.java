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
 * <p>Usada para fazer um gerenciamento avan�ado dos mundos/terrenos carregadas a partir de arquivos.
 * Al�m de carregar, salva as informa��es em uma pasta virtual em mem�ria para que n�o seja carregada duas vezes.</p>
 *
 * <p>Por exemplo, um mundo chamado <b>terra</b> ir� verificar se o mundo j� foi carregado no sistema,
 * caso ele j� tenha sido carregado, ir� usar as informa��es do mesmo ao inv�s de carregar novamente.
 * Esse sistema permite al�m de economizar tempo de processamento evitar consumo de mem�ria desnecess�rio.</p>
 *
 * <p>Por padr�o o formato dos dados b�sicos de um mundo s�o armazenados em um arquivo <b>wdd</b>.
 * Esse arquivo deve conter o nome do mundo, quantidade de terrenos e a forma como ser� feita a leitura.
 * Podendo variar de acordo com a prefer�ncia ou necessidade do usu�rio em rela��o a informa��es do mundo.</p>
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
	 * Inst�ncia para carregador de mundos no padr�o de projetos Singleton.
	 */
	private static final WorldLoader INSTANCE = new WorldLoader();

	/**
	 * Dimens�o m�nima para definir o tamanho de latitude ou longitude em terrenos de um mundo.
	 */
	private static final int MIN_MAP_SIZE = 1;

	/**
	 * Dimens�o m�xima para definir o tamanho de latitude ou longitude em terrenos de um mundo.
	 */
	private static final int MAX_MAP_SIZE = 1024;


	/**
	 * Quantidade de mundos j� carregados.
	 */
	private int count;

	/**
	 * Construtor privado para evitar m�ltiplas inst�ncias para o carregador de mapas.
	 * Inicializa o mapeador de recursos definindo o seu nome de acordo com as prefer�ncias.
	 * A prefer�ncia aqui utilizada � <code>worlds</code>, pasta para arquivos de mapas.
	 */

	private WorldLoader()
	{
		super("worlds");
	}

	/**
	 * Permite obter um determinado mapa j� carregado ou ent�o for�ar o carregamento deste.
	 * Se o mapa existir ir� retornar um mapa tempor�ria dessa ra�z caso contr�rio ir�
	 * criar uma nova ra�z carregando as informa��es do arquivo de acordo com o nome do mapa.
	 * Utiliza as propriedades de prefer�ncias que definem o caminho para arquivos de mapas.
	 * @param name nome do qual foi dado ao mapa, em outras palavras o nome do arquivo,
	 * caso n�o seja definido nenhuma extens�o para esse, ser� considerado <b>map</b> por padr�o.
	 * @return aquisi��o do objeto de mapa tempor�rio gerado da ra�z de acordo com o nome.
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
	 * Permite construir um novo mapa a partir das informa��es abaixo.
	 * Caso o caminho de aloca��o j� esteja sendo usado por outra n�o ser� poss�vel criar.
	 * Se for poss�vel criar, o mapa ra�z ser� armazenada e gerada uma tempor�ria.
	 * @param path caminho onde foi localizado o mapa, onde deve ser alocada.
	 * @param data objeto contendo os dados do mapa para armazenamento.
	 * @return aquisi��o de um mapa de uso tempor�rio.
	 */

	public World createWorld(String path, WorldData data)
	{
		if (path == null)
			throw new WorldRuntimeException("caminho n�o definido");

		if (data == null)
			throw new WorldRuntimeException("dados do mapa n�o definido");

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
			throw new WorldRuntimeException("mundo j� existente (%s)", path);

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

		logDebug("mapa '%s' lido com �xito (id: %d, width: %d, length: %d).\n",
				root.fileName, root.id, root.width, root.length);

		if (!insertResource(root))
			logWarning("n�o foi poss�vel salvar o mapa '%s'.\n", root.fileName);

		return root.genResource();
	}

	/**
	 * Procedimento que ir� fazer a verifica��o da validade dos dados de uma ra�z para mapa.
	 * As verifica��es consistem em verificar se o tamanho do mapa est� dentro dos limites.
	 * @param root refer�ncia da ra�z do mapa do qual ser� validada.
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
	 * Procedimento que permite obter a �nica inst�ncia do carregador de mundos.
	 * Utiliza o padr�o Singleton para evitar a exist�ncia de mais inst�ncias.
	 * @return aquisi��o da inst�ncia para utiliza��o do carregador de mundos.
	 */

	public static WorldLoader getInstance()
	{
		return INSTANCE;
	}
}
