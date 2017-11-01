package com.erakin.api.resources;

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

import org.diverproject.util.FileUtil;
import org.diverproject.util.stream.implementation.input.InputByteArray;

import com.erakin.api.resources.shader.ShaderData;
import com.erakin.api.resources.shader.ShaderException;
import com.erakin.api.resources.shader.ShaderReader;
import com.erakin.api.resources.shader.ShaderReaderFactory;
import com.erakin.api.resources.shader.ShaderRuntimeException;

public class ShaderLoader extends DefaultLoader<Shader>
{
	private static final ShaderLoader INSTANCE = new ShaderLoader();

	private ShaderLoader()
	{
		super("shaders");
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

		ShaderRoot root = new ShaderRoot();
		root.id = glCreateProgram();
		root.vertex = createShaderProgram(data.getVertexProgram(), GL_VERTEX_SHADER);
		root.fragment = createShaderProgram(data.getFragmentProgram(), GL_FRAGMENT_SHADER);
		root.fileName = FileUtil.getFileName(path);
		root.filePath = FileUtil.getParentPath(path);
		root.fileExtension = FileUtil.getExtension(path);

		glAttachShader(root.id, root.vertex);
		glAttachShader(root.id, root.fragment);

		logDebug("shader '%s' lida com êxito (id: %d, vertex: %d, fragment: %d).\n", root.fileName, root.id, root.vertex, root.fragment);

		if (!insertResource(root))
			logWarning("não foi possível o shader '%s'.\n", root.fileName);

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

	public static ShaderLoader getInstance()
	{
		return INSTANCE;
	}
}
