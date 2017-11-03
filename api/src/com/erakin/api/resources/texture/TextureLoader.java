package com.erakin.api.resources.texture;

import static com.erakin.api.lwjgl.GLUtil.glMaxTextureSize;
import static com.erakin.api.lwjgl.math.Maths.fold;
import static com.erakin.api.resources.texture.PixelFormat.FORMAT_RGBA;
import static com.erakin.api.resources.texture.TextureTarget.TT_2D;
import static com.erakin.api.resources.texture.TextureTarget.TT_CUBE_MAP;
import static org.diverproject.log.LogSystem.logDebug;
import static org.diverproject.log.LogSystem.logWarning;
import static org.diverproject.util.Util.format;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.io.FileInputStream;

import org.diverproject.util.FileUtil;
import org.diverproject.util.lang.IntUtil;

import com.erakin.api.resources.DefaultLoader;
import com.erakin.api.resources.ResourceMap;
import com.erakin.api.resources.ResourceRoot;

/**
 * <h1>Carregador de Texturas</h1>
 *
 * <p>Usada para fazer um gerenciamento avançado das texturas/imagens carregadas a partir de arquivos.
 * Além de carregar, salva as informações em uma pasta virtual em memória para que não seja carregada duas vezes.</p>
 *
 * <p>Por exemplo, uma textura chamado <b>casa.png</b> irá verificar se este objeto já foi carregado no sistema,
 * caso ele já tenha sido carregado, irá usar as informações deste objeto ao invés de ler os dados do arquivo.
 * Esse sistema permite além de economizar tempo de processamento evitar consumo de memória desnecessário.</p>
 *
 * <p>Por padrão os objetos serão considerados no formato <b>png</b> que é o utilizado pela biblioteca Erakin.
 * Porém é possível também utilizar outras formatações como <b>bmp</b> e <b>jpg</b> ou ainda adicionar a sua à lista.
 * Sendo possível aumentar a diversidade de formas para trabalhar com os dados de imagens texturas.</p>
 *
 * @see ResourceMap
 * @see TextureReader
 * @see TextureReaderFactory
 *
 * @author Andrew Mello
 */

public final class TextureLoader extends DefaultLoader<Texture>
{
	/**
	 * Tamanho mínimo que uma textura terá.
	 */
	private static final int MIN_TEXTURE_SIZE = 16;

	/**
	 * Tamanho máximo que uma textura terá.
	 */
	private static final int MAX_TEXTURE_SIZE = glMaxTextureSize();

	/**
	 * Nome padrão para mapeamento de mundos.
	 */
	public static final String DEFAULT_PATH = "textures";

	/**
	 * Índice da imagem para a face esquerda do cubo.
	 */
	public static final int CUBE_LEFT_FACE = 0;

	/**
	 * Índice da imagem para a face direita do cubo.
	 */
	public static final int CUBE_RIGHT_FACE = 1;
	/**
	 * Índice da imagem para a face superior do cubo.
	 */
	public static final int CUBE_TOP_FACE = 2;

	/**
	 * Índice da imagem para a face inferior do cubo.
	 */
	public static final int CUBE_BOTTOM_FACE = 3;

	/**
	 * Índice da imagem para a face frontal do cubo.
	 */
	public static final int CUBE_BACK_FACE = 4;

	/**
	 * Índice da imagem para a face posterior do cubo.
	 */
	public static final int CUBE_FRONT_FACE = 5;

	/**
	 * Quantidade de imagens necessárias para as faces de um cubo.
	 */
	public static final int CUBE_FACE_COUNT = 6;

	/**
	 * Instância para carregador de texturas no padrão de projetos Singleton.
	 */
	private static final TextureLoader instance = new TextureLoader();

	/**
	 * Construtor privado para evitar múltiplas instâncias para o carregador de texturas.
	 * Inicializa o mapeador de recursos definindo o seu nome por padrão <code>DEFAULT_PATH</code>.
	 */

	private TextureLoader()
	{
		super(DEFAULT_PATH);
	}

	/**
	 * Permite obter uma determinada textura já carregada ou então forçar o carregamento desta.
	 * Se a textura existir irá retornar uma textura temporária dessa raíz caso contrário irá
	 * criar uma nova raíz carregando as informações do arquivo de acordo com o nome da textura.
	 * @param name nome do qual foi dado a textura, em outras palavras o nome do arquivo,
	 * caso não seja definido nenhuma extensão para esse, será considerado <b>png</b> por padrão.
	 * @return aquisição do objeto de textura temporária gerado da raíz de acordo com o nome.
	 * @throws TextureException falha durante a leitura do arquivo ou arquivo com dados corrompidos.
	 */

	public Texture getTexture(String name) throws TextureException
	{
		if (!name.contains("."))
			name += ".png";

		ResourceRoot<Texture> resourceRoot = selectResource(name);

		if (resourceRoot != null)
		{
			TextureRoot textureRoot = (TextureRoot) resourceRoot;

			if (textureRoot.target == TT_2D)
				return textureRoot.genResource();

			return textureRoot.genResource();
		}

		String path = getPathname() + name;
		TextureReaderFactory factory = TextureReaderFactory.getInstance();
		TextureReader reader = factory.getTextureReaderOf(path);

		try {

			TextureData data = reader.readTexture(new FileInputStream(path), FORMAT_RGBA);
			Texture texture = createTexture(path, data);

			return texture;

		} catch (Exception e) {
			throw new TextureException(e);
		}
	}

	/**
	 * Permite construir uma nova textura a partir das informações abaixo.
	 * Caso o caminho de alocação já esteja sendo usado por outra não será possível criar.
	 * Se for possível criar, a textura raíz será armazenada e gerada uma temporária.
	 * @param path caminho onde foi localizado a textura, onde deve ser alocada.
	 * @param data objeto contendo os dados da textura para armazenamento.
	 * @return aquisição de uma textura de uso temporário.
	 */

	public Texture createTexture(String path, TextureData data)
	{
		if (path == null)
			throw new TextureRuntimeException("caminho não definido");

		if (data == null)
			throw new TextureRuntimeException("dados da textura não definido");

		if (!path.startsWith(getResourceName()))
		{
			if (path.contains(getResourceName()+ "/"))
				path = path.substring(path.indexOf(getResourceName()), path.length());
			else
				path = String.format("%s/%s", getResourceName(), path);
		}

		if (containResource(path))
			throw new TextureRuntimeException("textura já existente (%s)", path);

		TextureRoot root = new TextureRoot(path);
		root.id = glGenTextures();
		root.alpha = data.getDepth() == 32;
		root.depth = data.getDepth();
		root.width = data.getWidth();
		root.height = data.getHeight();
		root.target = TT_2D;

		validateLimits(root);

		int format = data.getDepth() == 32 ? GL_RGBA : GL_RGB;
		int width = fold(data.getTexWidth());
		int height = fold(data.getTexHeight());

		glBindTexture(root.target.GL_CODE, root.id);
		glTexImage2D(root.target.GL_CODE, 0, GL_RGBA, width, height, 0, format, GL_UNSIGNED_BYTE, data.getPixels());

		glGenerateMipmap(root.target.GL_CODE);
		glTexParameteri(root.target.GL_CODE, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(root.target.GL_CODE, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_NEAREST);
		glTexParameterf(root.target.GL_CODE, GL_TEXTURE_LOD_BIAS, -0.4f);

		logDebug("textura '%s' lida com êxito (width: %d, height: %d, depth: %d, alpha: %s).\n",
				root.getFileName(), root.width, root.height, root.depth, root.alpha);

		if (!insertResource(root))
			logWarning("não foi possível salvar a textura '%s'.\n", root.getFileName());

		return root.genResource();
	}

	/**
	 * <p>Permite obter uma determinada textura cúbica já carregada ou então forçar o carregamento desta.
	 * Se a textura existir irá retornar uma textura temporária dessa raíz caso contrário irá
	 * criar uma nova raíz carregando as informações do arquivo de acordo com o nome da textura.</p>
	 * Uma textura cúbica é formado por 6 texturas, portanto para a textura "cubo.png" por exemplo deve existir:
	 * "cubo_<b>right</b>.png", "cubo_<b>left</b>.png", "cubo_<b>top</b>.png", "cubo_<b>bottom</b>.png", "cubo_<b>back</b>.png", "cubo_<b>front</b>.png".
	 * @param name nome do qual foi dado a textura, em outras palavras o nome do arquivo,
	 * caso não seja definido nenhuma extensão para esse, será considerado <b>png</b> por padrão.
	 * @return aquisição do objeto de textura temporária gerado da raíz de acordo com o nome.
	 * @throws TextureException falha durante a leitura do arquivo ou arquivo com dados corrompidos.
	 */

	public Texture getCubeTexture(String name) throws TextureException
	{
		if (!name.contains("."))
			name += ".png";

		ResourceRoot<Texture> resourceRoot = selectResource(name);

		if (resourceRoot != null)
		{
			TextureRoot textureRoot = (TextureRoot) resourceRoot;

			if (textureRoot.target == TT_CUBE_MAP)
				return textureRoot.genResource();

			throw new TextureException("textura '%s' não é CUBE_MAP", name);
		}

		String path = getPathname() + name;
		TextureReaderFactory factory = TextureReaderFactory.getInstance();
		TextureReader reader = factory.getTextureReaderOf(path);

		try {

			String filepath = format("%s/%s", FileUtil.getParentPath(path), FileUtil.getFileName(path));
			String extension = FileUtil.getExtension(path);

			TextureData data[] = new TextureData[CUBE_FACE_COUNT];
			data[CUBE_RIGHT_FACE] = reader.readTexture(new FileInputStream(format("%s_right.%s", filepath, extension)), FORMAT_RGBA);
			data[CUBE_LEFT_FACE] = reader.readTexture(new FileInputStream(format("%s_left.%s", filepath, extension)), FORMAT_RGBA);
			data[CUBE_TOP_FACE] = reader.readTexture(new FileInputStream(format("%s_top.%s", filepath, extension)), FORMAT_RGBA);
			data[CUBE_BOTTOM_FACE] = reader.readTexture(new FileInputStream(format("%s_bottom.%s", filepath, extension)), FORMAT_RGBA);
			data[CUBE_BACK_FACE] = reader.readTexture(new FileInputStream(format("%s_back.%s", filepath, extension)), FORMAT_RGBA);
			data[CUBE_FRONT_FACE] = reader.readTexture(new FileInputStream(format("%s_front.%s", filepath, extension)), FORMAT_RGBA);

			Texture texture = createCubeTexture(path, data);

			return texture;

		} catch (Exception e) {
			throw new TextureException(e);
		}
	}

	/**
	 * <p>Permite construir uma nova textura cúbica a partir das informações abaixo.
	 * Caso o caminho de alocação já esteja sendo usado por outra não será possível criar.
	 * Se for possível criar, a textura raíz será armazenada e gerada uma temporária.</p>
	 * @param path caminho onde foi localizado a textura, onde deve ser alocada.
	 * @param data vetor contendo os dados da textura de cada face do cubo para armazenamento,
	 * deve possuir <code>CUBE_FACE_COUNT</code> faces e seguir a orgem de <code>CUBE_{i}_FACE</code>,
	 * onde i deve ser substituido pelo nome face (RIGTH, LEFT, TOP, BOTTOM, FRONT ou BACK).
	 * @return aquisição de uma textura cúbica de uso temporário.
	 */

	public Texture createCubeTexture(String path, TextureData[] data) throws TextureException
	{
		if (path == null)
			throw new TextureRuntimeException("caminho não definido");

		if (data == null)
			throw new TextureRuntimeException("dados da textura não definido");

		if (data.length != CUBE_FACE_COUNT)
			throw new TextureRuntimeException("texturas cúbicas precisam de %d (imagens) faces", CUBE_FACE_COUNT);

		for (TextureData tData : data)
			if (tData == null)
				throw new TextureRuntimeException("uma das faces da textura cúbida não foi definida");

		if (!path.startsWith(getResourceName()))
		{
			if (path.contains(getResourceName()+ "/"))
				path = path.substring(path.indexOf(getResourceName()), path.length());
			else
				path = String.format("%s/%s", getResourceName(), path);
		}

		if (containResource(path))
			throw new TextureRuntimeException("textura já existente (%s)", path);

		TextureData textureData = data[0];

		TextureRoot root = new TextureRoot(path);
		root.id = glGenTextures();
		root.alpha = textureData.getDepth() == 32;
		root.depth = textureData.getDepth();
		root.width = textureData.getWidth();
		root.height = textureData.getHeight();
		root.target = TT_CUBE_MAP;

		validateLimits(root);

		glActiveTexture(GL_TEXTURE0);
		glBindTexture(root.target.GL_CODE, root.id);
		{
			for (int i = 0; i < data.length; i++)
			{
				int format = data[i].getDepth() == 32 ? GL_RGBA : GL_RGB;
				int width = fold(data[i].getTexWidth());
				int height = fold(data[i].getTexHeight());

				glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA, width, height, 0, format, GL_UNSIGNED_BYTE, data[i].getPixels());
			}
		}
		glTexParameteri(root.target.GL_CODE, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(root.target.GL_CODE, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		logDebug("textura '%s' lida com êxito (width: %d, height: %d, depth: %d, alpha: %s).\n",
				root.getFileName(), root.width, root.height, root.depth, root.alpha);

		if (!insertResource(root))
			logWarning("não foi possível salvar a textura '%s'.\n", root.getFileName());

		return root.genResource();
	}

	/**
	 * Procedimento que irá fazer a verificação da validade dos dados de uma raíz para textura.
	 * As verificações consistem em verificar se o tamanho da imagem está dentro dos limites.
	 * Como também averiguar se o depth da imagem possui um valor aceitável.
	 * @param root referência da raíz de textura do qual será validada.
	 */

	private void validateLimits(TextureRoot root)
	{
		if (!IntUtil.interval(root.width, MIN_TEXTURE_SIZE, MAX_TEXTURE_SIZE))
			throw new TextureRuntimeException("falha ao alocar textura (width: %d, min: %d, max: %d)",
					root.width, MIN_TEXTURE_SIZE, MAX_TEXTURE_SIZE);

		if (!IntUtil.interval(root.height, MIN_TEXTURE_SIZE, MAX_TEXTURE_SIZE))
			throw new TextureRuntimeException("falha ao alocar textura (height: %d, min: %d, max: %d)",
					root.height, MIN_TEXTURE_SIZE, MAX_TEXTURE_SIZE);

		if (root.depth != 24 && root.depth != 32)
			throw new TextureRuntimeException("depth inválido (depth: %d)", root.depth);
	}

	/**
	 * Procedimento que permite obter a única instância do carregador de texturas.
	 * Utiliza o padrão Singleton para evitar a existência de mais instâncias.
	 * @return aquisição da instância para utilização do carregador de texturas.
	 */

	public static TextureLoader getIntance()
	{
		return instance;
	}
}
