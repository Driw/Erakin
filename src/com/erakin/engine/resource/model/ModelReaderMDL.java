package com.erakin.engine.resource.model;

import java.io.InputStream;

import com.erakin.common.buffer.Buffer;
import com.erakin.common.buffer.BufferInput;

/**
 * <h1>Leitor de Modelagem MDL</h1>
 *
 * <p>Tem como finalidade a implementação da forma de como será feito a leitura.
 * Para este caso, a leitura é feito através de um Buffer que irá decodificar as
 * informações de um InputStream contendo os bytes dos dados desse modelo.</p>
 *
 * @see ModelReaderDefault
 *
 * @author Andrew
 */

public class ModelReaderMDL extends ModelReaderDefault
{
	@Override
	public ModelDataDefault readModel(InputStream stream) throws ModelException
	{
		ModelDataDefault model = new ModelDataDefault();
		Buffer buffer = new BufferInput(stream);

		if (buffer.getChar() != 'M' && buffer.getChar() != 'D')
			throw new ModelException("formato inválido");

		int vertexCount = buffer.getInt();
		int textureCount = buffer.getInt();
		int normalCount = buffer.getInt();
		int indiceCount = buffer.getInt();

		model.init(vertexCount, textureCount, normalCount, indiceCount);

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

		for (int i = 0; i < indiceCount; i++)
		{
			int vertex = buffer.getInt();

			model.setIndice(i, vertex);
		}

		return model;
	}
}
