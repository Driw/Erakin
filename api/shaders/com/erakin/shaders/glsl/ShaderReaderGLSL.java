package com.erakin.shaders.glsl;

import org.diverproject.util.stream.Input;

import com.erakin.api.resources.shader.ShaderData;
import com.erakin.api.resources.shader.ShaderDataDefault;
import com.erakin.api.resources.shader.ShaderException;
import com.erakin.api.resources.shader.ShaderReader;

/**
 * <h1>Leitor de Programação GLSL</h1>
 *
 * <p>Esse leitor permite ler arquivos GLSL e a partir da leitura criar uma String com o código de programação GLSL.
 * O código fica descrito em dois momentos que são especificados por linhas que comecem com um valor específico.
 * Num primeiro momento o OpenGL considera o para vértices (vertex shader) seguido do fragmentado (fragment shader).</p>
 *
 * <p>O programa para vértices ficará dentro de <code>START_VERTEX_PROGRAM</code> até <code>END_VERTEX_PROGRAM</code>.
 * O programa para fragmentos ficará dentro de <code>START_FRAGMENT_PROGRAM</code> até <code>END_FRAGMENT_PROGRAM</code>.</p>
 *
 * <p><b>Exemplo de Vertex/Fragment Program:<br>
 * <code>START_VERTEX_PROGRAM</code><br>
 * { ... codificação do programa para computação gráfica ... }<br>
 * <code>END_VERTEX_PROGRAM</code><br>
 * <code>START_FRAGMENT_PROGRAM</code><br>
 * { ... codificação do programa para computação gráfica ... }<br>
 * <code>END_FRAGMENT_PROGRAM</code></p>
 *
 * @see ShaderReader
 *
 * @author Andrew Mello
 */

public class ShaderReaderGLSL implements ShaderReader
{
	/**
	 * Extensão dos arquivos que utilizarão este reader.
	 */
	public static final String EXTENSION = "glsl";

	/**
	 * Tag para que indica o inicio do programa para vértices.
	 */
	public static final String START_VERTEX_PROGRAM = "#StartVertexProgram";

	/**
	 * Tag para que indica o fim do programa para vértices.
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
	 * Procedimento interno que vai preparar a leitura da codificação do programa para vértices.
	 * @param input referência da stream para entrada de dados do arquivo GLSL lido.
	 * @param vertexProgram objeto do qual será salvo todo a codificação lida.
	 */

	private void readVertexProgram(Input input, StringBuilder vertexProgram)
	{
		readProgram(input, vertexProgram, START_VERTEX_PROGRAM, END_VERTEX_PROGRAM);
	}

	/**
	 * Procedimento interno que vai preparar a leitura da codificação do programa para fragmentos.
	 * @param input referência da stream para entrada de dados do arquivo GLSL lido.
	 * @param fragmentProgram objeto do qual será salvo todo a codificação lida.
	 */

	private void readFragmentProgram(Input input, StringBuilder fragmentProgram)
	{
		readProgram(input, fragmentProgram, START_VERTEX_PROGRAM, END_FRAGMENT_PROGRAM);
	}

	/**
	 * Procedimento interno usado para fazer a leitura da codificação de um programa (vértice ou fragmento).
	 * É lido linha por linha até encontrar a tag que indica o inicio do programa.
	 * Após o inicio, salva as linhas seguintes como parte do programa até a tag de fim do mesmo.
	 * @param input referência da stream para entrada de dados do arquivo GLSL lido.
	 * @param program objeto do qual será salvo todo a codificação lida.
	 * @param start tag do qual indica que o código do programa será iniciado.
	 * @param end tag do qual indica que o código do programa foi encerrado.
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
