package com.erakin.api.resources.shader;

import static org.diverproject.log.LogSystem.logDebug;
import static org.diverproject.log.LogSystem.logWarning;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glShaderSource;

import java.io.File;

import org.diverproject.util.stream.implementation.input.InputByteArray;

import com.erakin.api.resources.DefaultLoader;
import com.erakin.api.resources.ResourceMap;
import com.erakin.api.resources.ResourceRoot;

/**
 * <h1>Carregador de Programas</h1>
 *
 * <p>Usada para fazer um gerenciamento avan�ado dos programas de computa��o gr�fica carregados a partir de arquivos.
 * Al�m de carregar, salva as informa��es em uma pasta virtual em mem�ria para que n�o seja carregada duas vezes.</p>
 *
 * <p>Por exemplo, um programa chamado <b>effect</b> ir� verificar se o programa j� foi carregado no sistema,
 * caso ele j� tenha sido carregado, ir� usar as informa��es do mesmo ao inv�s de carregar novamente.
 * Esse sistema permite al�m de economizar tempo de processamento evitar consumo de mem�ria desnecess�rio.</p>
 *
 * <p>Por padr�o o formato dos dados b�sicos de um programa s�o armazenados em um arquivo <b>glsl</b>.
 * Esse arquivo deve conter o c�digo do programa de v�rtices e programa de fragmentos, separado por tags.</p>
 *
 * @see ResourceMap
 * @see ShaderReader
 * @see ShaderReaderFactory
 *
 * @author Andrew Mello
 */

public class ShaderLoader extends DefaultLoader<Shader>
{
	/**
	 * Inst�ncia para carregador de programas no padr�o de projetos Singleton.
	 */
	private static final ShaderLoader INSTANCE = new ShaderLoader();

	/**
	 * Nome padr�o para mapeamento de mundos.
	 */
	public static final String DEFAULT_PATH = "shaders";

	/**
	 * Construtor privado para evitar m�ltiplas inst�ncias para o carregador de programas.
	 * Inicializa o mapeador de recursos definindo o seu nome por padr�o <code>DEFAULT_PATH</code>.
	 */

	private ShaderLoader()
	{
		super(DEFAULT_PATH);
	}

	/**
	 * Permite obter um determinado programa para computa��o gr�fica j� carregada ou ent�o for�ar o carregamento desta.
	 * Se o programa j� existir ir� retornar um programa tempor�ria dessa ra�z caso contr�rio ir�
	 * criar uma nova ra�z carregando as informa��es do arquivo de acordo com o nome do programa.
	 * @param name nome do qual foi dado ao programa, em outras palavras o nome do arquivo,
	 * caso n�o seja definido nenhuma extens�o para esse, ser� considerado <b>glsl</b> por padr�o.
	 * @return aquisi��o do objeto de programa��o tempor�ria gerado da ra�z de acordo com o nome.
	 * @throws ShaderException falha durante a leitura do arquivo ou arquivo com dados corrompidos.
	 */

	public Shader getShader(String name) throws ShaderException
	{
		if (!name.contains("."))
			name += ".glsl";

		ResourceRoot<Shader> resourceRoot = selectResource(name);

		if (resourceRoot != null)
		{
			ShaderRoot shaderRoot = (ShaderRoot) resourceRoot;
			return shaderRoot.genResource();
		}

		String path = getPathname() + name;
		ShaderReaderFactory factory = ShaderReaderFactory.getInstance();
		ShaderReader reader = factory.getShaderReaderOf(path);

		try {

			ShaderData data = reader.readShader(new InputByteArray(new File(path)));
			Shader shader = createShader(path, data);

			return shader;

		} catch (Exception e) {
			throw new ShaderException(e);
		}
	}

	/**
	 * Permite construir um novo programa para computa��o gr�fica a partir das informa��es abaixo.
	 * Caso o caminho de aloca��o j� esteja sendo usado por outro n�o ser� poss�vel criar.
	 * Se for poss�vel criar, o programa ra�z ser� armazenado e gerado um tempor�rio.
	 * @param path caminho onde foi localizado o programa, onde deve ser alocado.
	 * @param data objeto contendo o c�digo do programa para armazenamento.
	 * @return aquisi��o de um programa de uso tempor�rio.
	 */

	public Shader createShader(String path, ShaderData data)
	{
		if (path == null)
			throw new ShaderRuntimeException("caminho n�o definido");

		if (data == null)
			throw new ShaderRuntimeException("dados do programa n�o definido");

		if (!path.startsWith(getResourceName()))
		{
			if (path.contains(getResourceName()+ "/"))
				path = path.substring(path.indexOf(getResourceName()), path.length());
			else
				path = String.format("%s/%s", getResourceName(), path);
		}

		if (containResource(path))
			throw new ShaderRuntimeException("programa j� existente (%s)", path);

		ShaderRoot root = new ShaderRoot(path);
		root.id = glCreateProgram();
		root.vertex = createShaderProgram(data.getVertexProgram(), GL_VERTEX_SHADER);
		root.fragment = createShaderProgram(data.getFragmentProgram(), GL_FRAGMENT_SHADER);

		glAttachShader(root.id, root.vertex);
		glAttachShader(root.id, root.fragment);

		logDebug("shader '%s' lida com �xito (id: %d, vertex: %d, fragment: %d).\n", root.getFileName(), root.id, root.vertex, root.fragment);

		if (!insertResource(root))
			logWarning("n�o foi poss�vel o shader '%s'.\n", root.getFileName());

		return root.genResource();
	}

	/**
	 * Faz o carregamento de um arquivo que � esperado ser de codifica��o para computa��o gr�fica.
	 * @param program objeto contendo todo o c�digo da programa��o de computa��o gr�fica � ser compilada.
	 * @param type programa��o do qual est� sendo feita, <code>GL_VERTEX_SHADER</code> e <code>GL_FRAGMENT_SHADER</code>.
	 * @return identifica��o da programa��o de computa��o gr�fica do qual foi criada.
	 * @throws ShaderRuntimeException c�digo da programa��o inv�lida.
	 */

	private int createShaderProgram(StringBuilder program, int type)
	{
		int shaderID = glCreateShader(type);
		glShaderSource(shaderID, program);
		glCompileShader(shaderID);

		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE)
			if (type == GL_VERTEX_SHADER)
				throw new ShaderRuntimeException("falha ao criar vertex program - %s", glGetShaderInfoLog(shaderID, 1024));
			else
				throw new ShaderRuntimeException("falha ao criar fragment program - %s", glGetShaderInfoLog(shaderID, 1024));

		return shaderID;
	}

	/**
	 * Procedimento que permite obter a �nica inst�ncia do carregador de programas.
	 * Utiliza o padr�o Singleton para evitar a exist�ncia de mais inst�ncias.
	 * @return aquisi��o da inst�ncia para utiliza��o do carregador de programas.
	 */

	public static ShaderLoader getInstance()
	{
		return INSTANCE;
	}
}
