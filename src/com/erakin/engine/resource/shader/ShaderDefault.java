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
 * <h1>Programa��o Shader Padr�o</h1>
 *
 * <p>Classe para implementa��o b�sica das funcionalidades para a cria��o de uma Programa��o Shader.
 * Utiliza arquivos para ser feito a leitura da codifica��o que ir� determinar como ser� a programa��o.
 * Essa programa��o ser� vinculado a dois tipos de programas, um para v�rtices e outro para fragmentos.</p>
 *
 * <p>A programa��o para v�rtices ir� fazer um computa��o gr�fica definindo propriedades ao v�rtice.
 * Em quanto a programa��o para fragmentos, ir� fazer fazer a computa��o gr�fica de um face de v�rtices.
 * Primeiramente � definido as propriedades ao v�rtice em seguida fazer o mesmo para as faces.</p>
 *
 * <p>Utiliza das prefer�ncias para obter a pasta do qual cont�m os arquivos shader em <code>PreferencesSetting</code>.
 * Utiliza o nome dado a essa Programa��o Shader para obter o nome dos arquivos contendo a codifica��o do programa.
 * Os dois arquivos ser�o lidos da seguinte maneira: "{folder}{name}_vertex.glsl" e "{folder}{name}_fragment.glsl".</p>
 *
 * @see Shader
 *
 * @author Andrew Mello
 */

public abstract class ShaderDefault implements Shader
{
	/**
	 * Identifica��o do programa para computa��o gr�fica.
	 */
	private int id;

	/**
	 * Identifica��o do programa para computa��o gr�fica de v�rtices.
	 */
	private int vertex;

	/**
	 * Identifica��o do programa para computa��o gr�fica de faces.
	 */
	private int fragment;

	/**
	 * Nome da programa��o.
	 */
	private String name;

	/**
	 * Constr�i um novo Programa Shader, carregando dois arquivos que devem conter o c�digo da computa��o gr�fica.
	 * Ap�s fazer o carregamento dos arquivos para computa��o gr�fica de v�rtices e faces os unem em um s� programa.
	 * @param name nome que ser� dado a programa��o, usado tamb�m para localiza��o dos arquivos necess�rios.
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
	 * Chamado para obter a localiza��o de todas vari�veis uniformes usadas no programa.
	 * Essas vari�veis s�o respectivas apenas aquelas que podem ser definidas dinamicamente.
	 */

	protected abstract void getAllUniformLocation();

	/**
	 * Permite definir o valor de uma vari�vel uniforme atrav�s da identifica��o da sua localiza��o.
	 * @param location �ndice para localiza��o da vari�vel uniforme para definir um valor din�mico.
	 * @param value qual ser� o valor assumido pela programa��o quando esta for executada.
	 */

	protected void setFloat(int location, float value)
	{
		glUniform1f(location, value);
	}

	/**
	 * Permite definir o valor de uma vari�vel uniforme atrav�s da identifica��o da sua localiza��o.
	 * @param location �ndice para localiza��o da vari�vel uniforme para definir um valor din�mico.
	 * @param vector qual ser� o vetor assumido pela programa��o quando esta for executada.
	 */

	protected void setVector2f(int location, Vector2f vector)
	{
		glUniform2f(location, vector.x, vector.y);
	}

	/**
	 * Permite definir o valor de uma vari�vel uniforme atrav�s da identifica��o da sua localiza��o.
	 * @param location �ndice para localiza��o da vari�vel uniforme para definir um valor din�mico.
	 * @param vector qual ser� o vetor assumido pela programa��o quando esta for executada.
	 */

	protected void setVector3f(int location, Vector3f vector)
	{
		glUniform3f(location, vector.x, vector.y, vector.z);
	}

	/**
	 * Permite definir o valor de uma vari�vel uniforme atrav�s da identifica��o da sua localiza��o.
	 * @param location �ndice para localiza��o da vari�vel uniforme para definir um valor din�mico.
	 * @param value qual ser� o valor assumido pela programa��o quando esta for executada.
	 */

	protected void setInt(int location, int value)
	{
		glUniform1i(location, value);
	}

	/**
	 * Permite definir o valor de uma vari�vel uniforme atrav�s da identifica��o da sua localiza��o.
	 * @param location �ndice para localiza��o da vari�vel uniforme para definir um valor din�mico.
	 * @param enable qual ser� o valor assumido pela programa��o quando esta for executada.
	 */

	protected void setBoolean(int location, boolean enable)
	{
		glUniform1f(location, enable ? 1 : 0);
	}

	/**
	 * Permite definir o valor de uma vari�vel uniforme atrav�s da identifica��o da sua localiza��o.
	 * @param location �ndice para localiza��o da vari�vel uniforme para definir um valor din�mico.
	 * @param matrix qual ser� a matriz assumida pela programa��o quando esta for executada.
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
	 * Faz o carregamento de um arquivo que � esperado ser de codifica��o para computa��o gr�fica.
	 * @param path caminho parcial ou completo do arquivo onde se encontra o arquivo a ser lido.
	 * @param type programa��o do qual est� sendo feita, <code>GL_VERTEX_SHADER</code> e <code>GL_FRAGMENT_SHADER</code>.
	 * @return identifica��o da programa��o de computa��o gr�fica do qual foi criada.
	 * @throws ShaderRuntimeException arquivo n�o encontrado, acesso n�o permitido e codifica��o inv�lida.
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
