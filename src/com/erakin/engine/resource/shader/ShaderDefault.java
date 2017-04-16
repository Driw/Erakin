package com.erakin.engine.resource.shader;

import static org.diverproject.log.LogSystem.logDebug;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glIsProgram;
import static org.lwjgl.opengl.GL20.glIsShader;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.util.Scanner;

import org.diverproject.util.ObjectDescription;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.erakin.engine.Preferences;
import com.erakin.engine.PreferencesSettings;

/**
 * <h1>Programação Shader Padrão</h1>
 *
 * <p>Classe para implementação básica das funcionalidades para a criação de uma Programação Shader.
 * Utiliza arquivos para ser feito a leitura da codificação que irá determinar como será a programação.
 * Essa programação será vinculado a dois tipos de programas, um para vértices e outro para fragmentos.</p>
 *
 * <p>A programação para vértices irá fazer um computação gráfica definindo propriedades ao vértice.
 * Em quanto a programação para fragmentos, irá fazer fazer a computação gráfica de um face de vértices.
 * Primeiramente é definido as propriedades ao vértice em seguida fazer o mesmo para as faces.</p>
 *
 * <p>Utiliza das preferências para obter a pasta do qual contém os arquivos shader em <code>PreferencesSetting</code>.
 * Utiliza o nome dado a essa Programação Shader para obter o nome dos arquivos contendo a codificação do programa.
 * Os dois arquivos serão lidos da seguinte maneira: "{folder}{name}_vertex.glsl" e "{folder}{name}_fragment.glsl".</p>
 *
 * @see Shader
 *
 * @author Andrew Mello
 */

public abstract class ShaderDefault implements Shader
{
	/**
	 * Identificação do programa para computação gráfica.
	 */
	private int id;

	/**
	 * Identificação do programa para computação gráfica de vértices.
	 */
	private int vertex;

	/**
	 * Identificação do programa para computação gráfica de faces.
	 */
	private int fragment;

	/**
	 * Nome da programação.
	 */
	private String name;

	/**
	 * Constrói um novo Programa Shader, carregando dois arquivos que devem conter o código da computação gráfica.
	 * Após fazer o carregamento dos arquivos para computação gráfica de vértices e faces os unem em um só programa.
	 * @param name nome que será dado a programação, usado também para localização dos arquivos necessários.
	 */

	public ShaderDefault(String name)
	{
		this.name = name;

		Preferences preferences = PreferencesSettings.getFolderPreferences();

		String vertexName = String.format("%s%s_vertex.glsl", preferences.getOptionString("shaders"), name);
		String fragmentName = String.format("%s%s_fragment.glsl", preferences.getOptionString("shaders"), name);

		vertex = createShaderProgram(vertexName, GL_VERTEX_SHADER);
		fragment = createShaderProgram(fragmentName, GL_FRAGMENT_SHADER);

		id = glCreateProgram();
		glAttachShader(id, vertex);
		glAttachShader(id, fragment);

		bindAttributes();

		glLinkProgram(id);
		glValidateProgram(id);

		getAllUniformLocation();

		logDebug("shader carregado (id: %d, name: %s)\n", id, name);
	}

	/**
	 * Deve garantir que todos os atributos usado sejam vinculados.
	 */

	protected abstract void bindAttributes();

	/**
	 * Chamado para obter a localização de todas variáveis uniformes usadas no programa.
	 * Essas variáveis são respectivas apenas aquelas que podem ser definidas dinamicamente.
	 */

	protected abstract void getAllUniformLocation();

	/**
	 * Permite definir o valor de uma variável uniforme através da identificação da sua localização.
	 * @param location índice para localização da variável uniforme para definir um valor dinâmico.
	 * @param value qual será o valor assumido pela programação quando esta for executada.
	 */

	protected void setFloat(int location, float value)
	{
		glUniform1f(location, value);
	}

	/**
	 * Permite definir o valor de uma variável uniforme através da identificação da sua localização.
	 * @param location índice para localização da variável uniforme para definir um valor dinâmico.
	 * @param vector qual será o vetor assumido pela programação quando esta for executada.
	 */

	protected void setVector2f(int location, Vector2f vector)
	{
		glUniform2f(location, vector.x, vector.y);
	}

	/**
	 * Permite definir o valor de uma variável uniforme através da identificação da sua localização.
	 * @param location índice para localização da variável uniforme para definir um valor dinâmico.
	 * @param vector qual será o vetor assumido pela programação quando esta for executada.
	 */

	protected void setVector3f(int location, Vector3f vector)
	{
		glUniform3f(location, vector.x, vector.y, vector.z);
	}

	/**
	 * Permite definir o valor de uma variável uniforme através da identificação da sua localização.
	 * @param location índice para localização da variável uniforme para definir um valor dinâmico.
	 * @param value qual será o valor assumido pela programação quando esta for executada.
	 */

	protected void setInt(int location, int value)
	{
		glUniform1i(location, value);
	}

	/**
	 * Permite definir o valor de uma variável uniforme através da identificação da sua localização.
	 * @param location índice para localização da variável uniforme para definir um valor dinâmico.
	 * @param enable qual será o valor assumido pela programação quando esta for executada.
	 */

	protected void setBoolean(int location, boolean enable)
	{
		glUniform1f(location, enable ? 1 : 0);
	}

	/**
	 * Permite definir o valor de uma variável uniforme através da identificação da sua localização.
	 * @param location índice para localização da variável uniforme para definir um valor dinâmico.
	 * @param matrix qual será a matriz assumida pela programação quando esta for executada.
	 */

	protected void setMatrix4f(int location, Matrix4f matrix)
	{
		FloatBuffer values = BufferUtils.createFloatBuffer(16);

		matrix.store(values);
		values.flip();

		glUniformMatrix4(location, false, values);
	}

	@Override
	public int getUniformLocation(String name)
	{
		return glGetUniformLocation(id, name);
	}

	@Override
	public int getID()
	{
		return id;
	}

	@Override
	public boolean valid()
	{
		return glIsShader(vertex) && glIsShader(fragment) && glIsProgram(id);
	}

	@Override
	public void bind()
	{
		glUseProgram(id);
	}

	@Override
	public void unbind()
	{
		glUseProgram(0);
	}

	@Override
	public void release()
	{
		unbind();

		glDetachShader(id, vertex);
		glDetachShader(id, fragment);
		glDeleteShader(fragment);
		glDeleteShader(vertex);
		glDeleteShader(id);
	}

	@Override
	public void bindAttribute(int attribute, String variable)
	{
		glBindAttribLocation(id, attribute, variable);
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("name", name);
		description.append("id", id);
		description.append("vertex", vertex);
		description.append("fragment", fragment);

		return description.toString();
	}

	/**
	 * Faz o carregamento de um arquivo que é esperado ser de codificação para computação gráfica.
	 * @param path caminho parcial ou completo do arquivo onde se encontra o arquivo a ser lido.
	 * @param type programação do qual está sendo feita, <code>GL_VERTEX_SHADER</code> e <code>GL_FRAGMENT_SHADER</code>.
	 * @return identificação da programação de computação gráfica do qual foi criada.
	 * @throws ShaderRuntimeException arquivo não encontrado, acesso não permitido e codificação inválida.
	 */

	public static int createShaderProgram(String path, int type) throws ShaderRuntimeException
	{
		StringBuilder program = new StringBuilder();

		try {

		File source = new File(path);
		Scanner scanner = new Scanner(source);

		while (scanner.hasNextLine())
			program.append(scanner.nextLine()).append("\n");

		scanner.close();

		} catch (FileNotFoundException e) {
			throw new ShaderRuntimeException(e.getMessage());
		}

		int id = glCreateShader(type);

		glShaderSource(id, program);
		glCompileShader(id);

		if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE)
			throw new ShaderRuntimeException(glGetShaderInfoLog(id, 1024));

		return id;
	}
}
