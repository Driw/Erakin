package com.erakin.api.resources;

import static com.erakin.api.lwjgl.GLUtil.glMaxTextureSize;
import static com.erakin.api.lwjgl.math.Maths.fold;
import static com.erakin.api.resources.texture.PixelFormat.FORMAT_RGBA;
import static org.diverproject.log.LogSystem.logDebug;
import static org.diverproject.log.LogSystem.logWarning;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.io.FileInputStream;
import java.nio.ByteBuffer;

import org.diverproject.util.FileUtil;
import org.diverproject.util.lang.IntUtil;

import com.erakin.api.resources.texture.TextureData;
import com.erakin.api.resources.texture.TextureException;
import com.erakin.api.resources.texture.TextureReader;
import com.erakin.api.resources.texture.TextureReaderFactory;
import com.erakin.api.resources.texture.TextureRuntimeException;

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

public final class TextureLoader extends DefaultLoader
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
	 * Instância para carregador de texturas no padrão de projetos Singleton.
	 */
	private static final TextureLoader instance = new TextureLoader();

	/**
	 * Construtor privado para evitar múltiplas instâncias para o carregador de texturas.
	 * Inicializa o mapeador de recursos definindo o seu nome de acordo com as preferências.
	 * A preferência aqui utilizada é <code>textures</code>, pasta para arquivos de texturas.
	 */

	private TextureLoader()
	{
		super("textures");
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

		ResourceRoot resourceRoot = selectResource(name);

		if (resourceRoot != null)
		{
			TextureRoot textureRoot = (TextureRoot) resourceRoot;
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

		TextureRoot root = new TextureRoot();
		root.id = glGenTextures();
		root.fileName = FileUtil.getFileName(path);
		root.filePath = FileUtil.getParentPath(path);
		root.fileExtension = FileUtil.getExtension(path);
		root.alpha = data.getDepth() == 32;
		root.depth = data.getDepth();
		root.width = data.getWidth();
		root.height = data.getHeight();

		validateLimits(root);

		ByteBuffer pixels = data.getPixels();
		pixels.flip();

		int format = data.getDepth() == 32 ? GL_RGBA : GL_RGB;

		int width = fold(data.getTexWidth());
		int height = fold(data.getTexHeight());

		glBindTexture(GL_TEXTURE_2D, root.id);
//		glGenerateMipmap(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4f);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, format, GL_UNSIGNED_BYTE, pixels);

		logDebug("textura '%s' lida com êxito (width: %d, height: %d, depth: %d, alpha: %s).\n",
				root.fileName, root.width, root.height, root.depth, root.alpha);

		if (!insertResource(root))
			logWarning("não foi possível salvar a textura '%s'.\n", root.fileName);

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
