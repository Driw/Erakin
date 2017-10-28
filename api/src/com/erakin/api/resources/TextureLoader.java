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
 * <p>Usada para fazer um gerenciamento avan�ado das texturas/imagens carregadas a partir de arquivos.
 * Al�m de carregar, salva as informa��es em uma pasta virtual em mem�ria para que n�o seja carregada duas vezes.</p>
 *
 * <p>Por exemplo, uma textura chamado <b>casa.png</b> ir� verificar se este objeto j� foi carregado no sistema,
 * caso ele j� tenha sido carregado, ir� usar as informa��es deste objeto ao inv�s de ler os dados do arquivo.
 * Esse sistema permite al�m de economizar tempo de processamento evitar consumo de mem�ria desnecess�rio.</p>
 *
 * <p>Por padr�o os objetos ser�o considerados no formato <b>png</b> que � o utilizado pela biblioteca Erakin.
 * Por�m � poss�vel tamb�m utilizar outras formata��es como <b>bmp</b> e <b>jpg</b> ou ainda adicionar a sua � lista.
 * Sendo poss�vel aumentar a diversidade de formas para trabalhar com os dados de imagens texturas.</p>
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
	 * Tamanho m�nimo que uma textura ter�.
	 */
	private static final int MIN_TEXTURE_SIZE = 16;

	/**
	 * Tamanho m�ximo que uma textura ter�.
	 */
	private static final int MAX_TEXTURE_SIZE = glMaxTextureSize();

	/**
	 * Inst�ncia para carregador de texturas no padr�o de projetos Singleton.
	 */
	private static final TextureLoader instance = new TextureLoader();

	/**
	 * Construtor privado para evitar m�ltiplas inst�ncias para o carregador de texturas.
	 * Inicializa o mapeador de recursos definindo o seu nome de acordo com as prefer�ncias.
	 * A prefer�ncia aqui utilizada � <code>textures</code>, pasta para arquivos de texturas.
	 */

	private TextureLoader()
	{
		super("textures");
	}

	/**
	 * Permite obter uma determinada textura j� carregada ou ent�o for�ar o carregamento desta.
	 * Se a textura existir ir� retornar uma textura tempor�ria dessa ra�z caso contr�rio ir�
	 * criar uma nova ra�z carregando as informa��es do arquivo de acordo com o nome da textura.
	 * @param name nome do qual foi dado a textura, em outras palavras o nome do arquivo,
	 * caso n�o seja definido nenhuma extens�o para esse, ser� considerado <b>png</b> por padr�o.
	 * @return aquisi��o do objeto de textura tempor�ria gerado da ra�z de acordo com o nome.
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
	 * Permite construir uma nova textura a partir das informa��es abaixo.
	 * Caso o caminho de aloca��o j� esteja sendo usado por outra n�o ser� poss�vel criar.
	 * Se for poss�vel criar, a textura ra�z ser� armazenada e gerada uma tempor�ria.
	 * @param path caminho onde foi localizado a textura, onde deve ser alocada.
	 * @param data objeto contendo os dados da textura para armazenamento.
	 * @return aquisi��o de uma textura de uso tempor�rio.
	 */

	public Texture createTexture(String path, TextureData data)
	{
		if (path == null)
			throw new TextureRuntimeException("caminho n�o definido");

		if (data == null)
			throw new TextureRuntimeException("dados da textura n�o definido");

		if (!path.startsWith(getResourceName()))
		{
			if (path.contains(getResourceName()+ "/"))
				path = path.substring(path.indexOf(getResourceName()), path.length());
			else
				path = String.format("%s/%s", getResourceName(), path);
		}

		if (containResource(path))
			throw new TextureRuntimeException("textura j� existente (%s)", path);

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

		logDebug("textura '%s' lida com �xito (width: %d, height: %d, depth: %d, alpha: %s).\n",
				root.fileName, root.width, root.height, root.depth, root.alpha);

		if (!insertResource(root))
			logWarning("n�o foi poss�vel salvar a textura '%s'.\n", root.fileName);

		return root.genResource();
	}

	/**
	 * Procedimento que ir� fazer a verifica��o da validade dos dados de uma ra�z para textura.
	 * As verifica��es consistem em verificar se o tamanho da imagem est� dentro dos limites.
	 * Como tamb�m averiguar se o depth da imagem possui um valor aceit�vel.
	 * @param root refer�ncia da ra�z de textura do qual ser� validada.
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
			throw new TextureRuntimeException("depth inv�lido (depth: %d)", root.depth);
	}

	/**
	 * Procedimento que permite obter a �nica inst�ncia do carregador de texturas.
	 * Utiliza o padr�o Singleton para evitar a exist�ncia de mais inst�ncias.
	 * @return aquisi��o da inst�ncia para utiliza��o do carregador de texturas.
	 */

	public static TextureLoader getIntance()
	{
		return instance;
	}
}
