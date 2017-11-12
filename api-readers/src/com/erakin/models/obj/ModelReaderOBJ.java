package com.erakin.models.obj;

import static com.erakin.api.ErakinAPIUtil.objectString;

import java.io.InputStream;
import java.util.Scanner;

import org.diverproject.util.collection.List;
import org.diverproject.util.collection.Queue;
import org.diverproject.util.collection.abstraction.DynamicList;
import org.diverproject.util.collection.abstraction.DynamicQueue;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.erakin.api.lwjgl.math.Vector3i;
import com.erakin.api.resources.model.ModelDataDefault;
import com.erakin.api.resources.model.ModelException;
import com.erakin.api.resources.model.ModelReaderDefault;

/**
 * <h1>Leitor de Modelagem OBJ</h1>
 *
 * <p>Tem como finalidade a implementa��o da forma de como ser� feito a leitura.
 * Para este caso, o carregamento � feito atrav�s de um Scanner que ir� analisar as
 * informa��es de um InputStream contendo os dados em strings (linhas).</p>
 *
 * @see ModelReaderDefault
 *
 * @author Andrew
 */

public class ModelReaderOBJ extends ModelReaderDefault
{
	/**
	 * Extens�o dos arquivos que utilizar�o este reader.
	 */
	public static final String FILE_EXTENSION = "obj";

	/**
	 * Conte�do atual da linha que est� sendo lida do arquivo.
	 */
	private String line;

	/**
	 * Lista contendo todos os v�rtices que podem ser lidos.
	 */
	private List<Vector3f> vertices;

	/**
	 * Lista contendo todos as coordenadas de textura que podem ser lidas.
	 */
	private List<Vector2f> uvTextures;

	/**
	 * Lista contendo todos as normaliza��es que podem ser lidos.
	 */
	private List<Vector3f> normals;

	/**
	 * Lista contendo todos os �ndices para formar faces que podem ser lidos.
	 */
	private Queue<Vector3i> faces;

	/**
	 * Constr�i um novo leitor de modelagens armazenados em um arquivo OBJ.
	 * Dever� iniciar as listas para armazenar v�rtices, coordenadas de textura,
	 * normaliza��es e faces para quando um arquivo OBJ for passado para se ler.
	 */

	public ModelReaderOBJ()
	{
		vertices = new DynamicList<Vector3f>();
		uvTextures = new DynamicList<Vector2f>();
		normals = new DynamicList<Vector3f>();
		faces = new DynamicQueue<Vector3i>();
	}

	@Override
	protected void subLoadModel(InputStream stream) throws ModelException
	{
		Scanner scanner = new Scanner(stream);

		int i = 0;

		try {

			for (; scanner.hasNext(); i++)
			{
				line = scanner.nextLine();

				if (line.startsWith("#") || line.startsWith("o"))
					continue;

				if (line.startsWith("v "))
					parseVertice(line);

				else if (line.startsWith("vt "))
					parseTexturePosition(line);

				else if (line.startsWith("vn "))
					parseNormal(line);

				else if (line.startsWith("f "))
					parseFace(line);
			}

			scanner.close();
			initiateModel();

		} catch (ModelException e) {
			throw e;
		} catch (Exception e) {
			scanner.close();
			throw new ModelException("erro na linha %d (%s)", i, e.getMessage());
		}
	}

	/**
	 * Chamado sempre que uma linha lida do arquivo OBJ conter dados de um v�rtice.
	 * @param line string contendo todo o conte�do da linha lida do arquivo.
	 */

	private void parseVertice(String line)
	{
		String columns[] = line.split(" ");

		Vector3f vertice = new Vector3f();
		vertice.x = Float.parseFloat(columns[1]);
		vertice.y = Float.parseFloat(columns[2]);
		vertice.z = Float.parseFloat(columns[3]);
		vertices.add(vertice);
	}

	/**
	 * Chamado sempre que uma linha lida do arquivo OBJ conter dados de uma coordenada de textura.
	 * @param line string contendo todo o conte�do da linha lida do arquivo.
	 */

	private void parseTexturePosition(String line)
	{
		String columns[] = line.split(" ");

		Vector2f texture = new Vector2f();
		texture.x = Float.parseFloat(columns[1]);
		texture.y = Float.parseFloat(columns[2]);
		uvTextures.add(texture);
	}

	/**
	 * Chamado sempre que uma linha lida do arquivo OBJ conter dados de uma normaliza��o.
	 * @param line string contendo todo o conte�do da linha lida do arquivo.
	 */

	private void parseNormal(String line)
	{
		String columns[] = line.split(" ");

		Vector3f normal = new Vector3f();
		normal.x = Float.parseFloat(columns[1]);
		normal.y = Float.parseFloat(columns[2]);
		normal.z = Float.parseFloat(columns[3]);
		normals.add(normal);
	}

	/**
	 * Chamado sempre que uma linha lida do arquivo OBJ conter dados de uma face.
	 * @param line string contendo todo o conte�do da linha lida do arquivo.
	 */

	private void parseFace(String line)
	{
		String columns[] = line.split(" ");

		for (int i = 1; i <= 3; i++)
		{
			String data[] = columns[i].split("/");

			Vector3i vector = new Vector3i();
			vector.x = Integer.parseInt(data[0]);
			vector.y = Integer.parseInt(data[1]);
			vector.z = Integer.parseInt(data[2]);

			faces.offer(vector);
		}
	}

	/**
	 * Procedimento chamado somente ap�s o final da an�lise do arquivo OBJ que foi lido.
	 * Nesse momento todos os dados ter�o sido lidos e devem ser organizados em uma modelagem.
	 * Para isso esse m�todo ir� garantir a cria��o de um objeto para armazen�-los e ajust�-los.
	 * @return aquisi��o de um objeto contendo os dados para se criar a modelagem lida.
	 * @throws ModelException ocorre quando h� dados incoerentes na textura, normaliza��o ou face.
	 */

	private ModelDataDefault initiateModel() throws ModelException
	{
		data.initVertices(vertices.size());
		data.initUVTextures(vertices.size());
		data.initNormals(vertices.size());
		data.initIndexes(faces.size());

		for (int i = 0; i < vertices.size(); i++)
		{
			Vector3f vertex = vertices.get(i);
			data.setVertice(i, vertex.x, vertex.y, vertex.z);
		}

		for (int i = 0; faces.size() > 0; i++)
		{
			Vector3i face = faces.poll();

			int vertexIndex = face.x - 1;
			int textureIndex = face.y - 1;
			int normalIndex = face.z - 1;

			Vector2f texture = uvTextures.get(textureIndex);

			if (texture == null)
				throw new ModelException("coordenada de textura n�o encontrada (%d)", textureIndex);

			Vector3f normal = normals.get(normalIndex);

			if (normal == null)
				throw new ModelException("normaliza��o n�o encontrada (%d)", normalIndex);

			try {

				data.setIndice(i, vertexIndex);
				data.setUVTexture(vertexIndex, texture.x, 1 - texture.y);
				data.setNormal(vertexIndex, normal.x, normal.y, normal.z);

			} catch (Exception e) {
				throw new ModelException("falha ao definir face (%s)", objectString(face));
			}
		}

		return data;
	}
}
