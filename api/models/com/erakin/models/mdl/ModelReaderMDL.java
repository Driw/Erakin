package com.erakin.models.mdl;

import java.io.InputStream;

import com.erakin.api.buffer.Buffer;
import com.erakin.api.buffer.BufferInput;
import com.erakin.api.resources.model.ModelDataDefault;
import com.erakin.api.resources.model.ModelException;
import com.erakin.api.resources.model.ModelReaderDefault;

/**
 * <h1>Leitor de Modelagem MDL</h1>
 *
 * <p>Tem como finalidade a implementa��o da forma de como ser� feito a leitura.
 * Para este caso, a leitura � feito atrav�s de um Buffer que ir� decodificar as
 * informa��es de um InputStream contendo os bytes dos dados desse modelo.</p>
 *
 * @see ModelReaderDefault
 *
 * @author Andrew
 */

public class ModelReaderMDL extends ModelReaderDefault
{
	/**
	 * Extens�o dos arquivos que utilizar�o este reader.
	 */
	public static final String FILE_EXTENSION = "mdl";

	@Override
	public ModelDataDefault readModel(InputStream stream) throws ModelException
	{
		ModelDataDefault model = new ModelDataDefault();
		Buffer buffer = new BufferInput(stream);

		if (buffer.getChar() != 'M' && buffer.getChar() != 'D')
			throw new ModelException("formato inv�lido");

		int vertexCount = buffer.getInt();
		int uvTextureCount = buffer.getInt();
		int normalCount = buffer.getInt();
		int indexCount = buffer.getInt();

		model.initIndexes(indexCount);
		model.initVertices(vertexCount);
		model.initUVTextures(uvTextureCount);
		model.initNormals(normalCount);

		for (int i = 0; i < vertexCount; i++)
		{
			float x = buffer.getFloat();
			float y = buffer.getFloat();
			float z = buffer.getFloat();

			model.setVertice(i, x, y, z);
		}

		for (int i = 0; i < vertexCount; i++)
		{
			float x = buffer.getFloat();
			float y = buffer.getFloat();

			model.setTexture(i, x, y);
		}

		for (int i = 0; i < normalCount; i++)
		{
			float x = buffer.getFloat();
			float y = buffer.getFloat();
			float z = buffer.getFloat();

			model.setNormal(i, x, y, z);
		}

		for (int i = 0; i < indexCount; i++)
		{
			int vertex = buffer.getInt();

			model.setIndice(i, vertex);
		}

		return model;
	}
}
