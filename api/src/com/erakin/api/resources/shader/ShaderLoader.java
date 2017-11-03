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
 * <p>Usada para fazer um gerenciamento avançado dos programas de computação gráfica carregados a partir de arquivos.
 * Além de carregar, salva as informações em uma pasta virtual em memória para que não seja carregada duas vezes.</p>
 *
 * <p>Por exemplo, um programa chamado <b>effect</b> irá verificar se o programa já foi carregado no sistema,
 * caso ele já tenha sido carregado, irá usar as informações do mesmo ao invés de carregar novamente.
 * Esse sistema permite além de economizar tempo de processamento evitar consumo de memória desnecessário.</p>
 *
 * <p>Por padrão o formato dos dados básicos de um programa são armazenados em um arquivo <b>glsl</b>.
 * Esse arquivo deve conter o código do programa de vértices e programa de fragmentos, separado por tags.</p>
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
	 * Instância para carregador de programas no padrão de projetos Singleton.
	 */
	private static final ShaderLoader INSTANCE = new ShaderLoader();

	/**
	 * Nome padrão para mapeamento de mundos.
	 */
	public static final String DEFAULT_PATH = "shaders";

	/**
	 * Construtor privado para evitar múltiplas instâncias para o carregador de programas.
	 * Inicializa o mapeador de recursos definindo o seu nome por padrão <code>DEFAULT_PATH</code>.
	 */

	private ShaderLoader()
	{
		super(DEFAULT_PATH);
	}

	/**
	 * Permite obter um determinado programa para computação gráfica já carregada ou então forçar o carregamento desta.
	 * Se o programa já existir irá retornar um programa temporária dessa raíz caso contrário irá
	 * criar uma nova raíz carregando as informações do arquivo de acordo com o nome do programa.
	 * @param name nome do qual foi dado ao programa, em outras palavras o nome do arquivo,
	 * caso não seja definido nenhuma extensão para esse, será considerado <b>glsl</b> por padrão.
	 * @return aquisição do objeto de programação temporária gerado da raíz de acordo com o nome.
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
	 * Permite construir um novo programa para computação gráfica a partir das informações abaixo.
	 * Caso o caminho de alocação já esteja sendo usado por outro não será possível criar.
	 * Se for possível criar, o programa raíz será armazenado e gerado um temporário.
	 * @param path caminho onde foi localizado o programa, onde deve ser alocado.
	 * @param data objeto contendo o código do programa para armazenamento.
	 * @return aquisição de um programa de uso temporário.
	 */

	public Shader createShader(String path, ShaderData data)
	{
		if (path == null)
			throw new ShaderRuntimeException("caminho não definido");

		if (data == null)
			throw new ShaderRuntimeException("dados do programa não definido");

		if (!path.startsWith(getResourceName()))
		{
			if (path.contains(getResourceName()+ "/"))
				path = path.substring(path.indexOf(getResourceName()), path.length());
			else
				path = String.format("%s/%s", getResourceName(), path);
		}

		if (containResource(path))
			throw new ShaderRuntimeException("programa já existente (%s)", path);

		ShaderRoot root = new ShaderRoot(path);
		root.id = glCreateProgram();
		root.vertex = createShaderProgram(data.getVertexProgram(), GL_VERTEX_SHADER);
		root.fragment = createShaderProgram(data.getFragmentProgram(), GL_FRAGMENT_SHADER);

		glAttachShader(root.id, root.vertex);
		glAttachShader(root.id, root.fragment);

		logDebug("shader '%s' lida com êxito (id: %d, vertex: %d, fragment: %d).\n", root.getFileName(), root.id, root.vertex, root.fragment);

		if (!insertResource(root))
			logWarning("não foi possível o shader '%s'.\n", root.getFileName());

		return root.genResource();
	}

	/**
	 * Faz o carregamento de um arquivo que é esperado ser de codificação para computação gráfica.
	 * @param program objeto contendo todo o código da programação de computação gráfica à ser compilada.
	 * @param type programação do qual está sendo feita, <code>GL_VERTEX_SHADER</code> e <code>GL_FRAGMENT_SHADER</code>.
	 * @return identificação da programação de computação gráfica do qual foi criada.
	 * @throws ShaderRuntimeException código da programação inválida.
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
	 * Procedimento que permite obter a única instância do carregador de programas.
	 * Utiliza o padrão Singleton para evitar a existência de mais instâncias.
	 * @return aquisição da instância para utilização do carregador de programas.
	 */

	public static ShaderLoader getInstance()
	{
		return INSTANCE;
	}
}
