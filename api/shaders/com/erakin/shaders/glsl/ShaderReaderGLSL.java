package com.erakin.shaders.glsl;

import org.diverproject.util.stream.Input;

import com.erakin.api.resources.shader.ShaderData;
import com.erakin.api.resources.shader.ShaderDataDefault;
import com.erakin.api.resources.shader.ShaderException;
import com.erakin.api.resources.shader.ShaderReader;

/**
 * <h1>Leitor de Programa��o GLSL</h1>
 *
 * <p>Esse leitor permite ler arquivos GLSL e a partir da leitura criar uma String com o c�digo de programa��o GLSL.
 * O c�digo fica descrito em dois momentos que s�o especificados por linhas que comecem com um valor espec�fico.
 * Num primeiro momento o OpenGL considera o para v�rtices (vertex shader) seguido do fragmentado (fragment shader).</p>
 *
 * <p>O programa para v�rtices ficar� dentro de <code>START_VERTEX_PROGRAM</code> at� <code>END_VERTEX_PROGRAM</code>.
 * O programa para fragmentos ficar� dentro de <code>START_FRAGMENT_PROGRAM</code> at� <code>END_FRAGMENT_PROGRAM</code>.</p>
 *
 * <p><b>Exemplo de Vertex/Fragment Program:<br>
 * <code>START_VERTEX_PROGRAM</code><br>
 * { ... codifica��o do programa para computa��o gr�fica ... }<br>
 * <code>END_VERTEX_PROGRAM</code><br>
 * <code>START_FRAGMENT_PROGRAM</code><br>
 * { ... codifica��o do programa para computa��o gr�fica ... }<br>
 * <code>END_FRAGMENT_PROGRAM</code></p>
 *
 * @see ShaderReader
 *
 * @author Andrew Mello
 */

public class ShaderReaderGLSL implements ShaderReader
{
	/**
	 * Extens�o dos arquivos que utilizar�o este reader.
	 */
	public static final String EXTENSION = "glsl";

	/**
	 * Tag para que indica o inicio do programa para v�rtices.
	 */
	public static final String START_VERTEX_PROGRAM = "#StartVertexProgram";

	/**
	 * Tag para que indica o fim do programa para v�rtices.
	 */
	public static final String END_VERTEX_PROGRAM = "#EndVertexProgram";

	/**
	 * Tag para que indica o inicio do programa para fragmentos.
	 */
	public static final String START_FRAGMENT_PROGRAM = "#StartFragmentProgram";

	/**
	 * Tag para que indica o fim do programa para fragmentos.
	 */
	public static final String END_FRAGMENT_PROGRAM = "#EndFragmentProgram";

	@Override
	public ShaderData readShader(Input input) throws ShaderException
	{
		ShaderDataDefault data = new ShaderDataDefault();

		readVertexProgram(input, data.getVertexProgram());
		readFragmentProgram(input, data.getFragmentProgram());

		return data;
	}

	/**
	 * Procedimento interno que vai preparar a leitura da codifica��o do programa para v�rtices.
	 * @param input refer�ncia da stream para entrada de dados do arquivo GLSL lido.
	 * @param vertexProgram objeto do qual ser� salvo todo a codifica��o lida.
	 */

	private void readVertexProgram(Input input, StringBuilder vertexProgram)
	{
		readProgram(input, vertexProgram, START_VERTEX_PROGRAM, END_VERTEX_PROGRAM);
	}

	/**
	 * Procedimento interno que vai preparar a leitura da codifica��o do programa para fragmentos.
	 * @param input refer�ncia da stream para entrada de dados do arquivo GLSL lido.
	 * @param fragmentProgram objeto do qual ser� salvo todo a codifica��o lida.
	 */

	private void readFragmentProgram(Input input, StringBuilder fragmentProgram)
	{
		readProgram(input, fragmentProgram, START_VERTEX_PROGRAM, END_FRAGMENT_PROGRAM);
	}

	/**
	 * Procedimento interno usado para fazer a leitura da codifica��o de um programa (v�rtice ou fragmento).
	 * � lido linha por linha at� encontrar a tag que indica o inicio do programa.
	 * Ap�s o inicio, salva as linhas seguintes como parte do programa at� a tag de fim do mesmo.
	 * @param input refer�ncia da stream para entrada de dados do arquivo GLSL lido.
	 * @param program objeto do qual ser� salvo todo a codifica��o lida.
	 * @param start tag do qual indica que o c�digo do programa ser� iniciado.
	 * @param end tag do qual indica que o c�digo do programa foi encerrado.
	 */

	private void readProgram(Input input, StringBuilder program, String start, String end)
	{
		boolean found = false;

		while (input.space() > 0)
		{
			String line = input.readLine();

			if (!found && line.startsWith(start))
			{
				found = true;
				continue;
			}

			if (line.startsWith(end))
				break;

			program.append(line);
			program.append("\n");
		}
	}
}
