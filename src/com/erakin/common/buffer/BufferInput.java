package com.erakin.common.buffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.lang.ByteUtil;

/**
 * <h1>Buffer de Input</h1>
 *
 * <p>Esse buffer funciona diretamente com bytes armazenados em vetores ou algo do g�nero.
 * � poss�vel criar buffers desse tipo de duas maneiras, a primeira atrav�s de arquivos especificados.
 * Em quanto a outra � atrav�s de objetos do tipo InputStream que podem de arquivos ou outro.</p>
 *
 * <p>Possui todos os m�todos necess�rios para funcionamento do gerenciamento com bytes em mem�ria.
 * Usufrui das funcionalidades pr�-definidas no N�cleo para Buffer para simplificar a implementa��o.</p>
 *
 * @see BufferCore
 *
 * @author Andrew Mello
 */

public class BufferInput extends BufferCore
{
	/**
	 * Vetor usado para armazenamento dos bytes salvos quando solicitado.
	 */
	private byte saved[];

	/**
	 * Vetor usado para fazer a leitura dos bytes armazenados como dados do buffer.
	 */
	private byte data[];

	/**
	 * Constr�i um novo buffer para trabalhar com os bytes de um arquivo qualquer.
	 * @param path caminho do arquivo do qual ser� aberto e lido os bytes.
	 */

	public BufferInput(String path)
	{
		this(new File(path));
	}

	/**
	 * Constr�i um novo buffer para trabalhar com os bytes de um arquivo qualquer.
	 * @param file refer�ncia do arquivo do qual ser� aberto e lido os bytes.
	 */

	public BufferInput(File file)
	{
		try {
			parse(new FileInputStream(file));
		} catch (FileNotFoundException e) {
		}
	}

	/**
	 * Constr�i um novo buffer para trabalhar com os bytes de um InputStream qualquer.
	 * @param input refer�ncia da stream como entrada de dados para o buffer.
	 */

	public BufferInput(InputStream input)
	{
		parse(input);
	}

	/**
	 * Procedimento interno usado para analisar um InputStream para fazer a leitura dos dados.
	 * L� completamente o arquivo, armazena os dados deste em mem�ria e o transforma em buffer.
	 * @param input refer�ncia da stream como entrada de dados para o buffer.
	 */

	private void parse(InputStream input)
	{
		try {

			if (input.available() == 0)
				data = new byte[0];

			else
			{
				byte inputData[] = new byte[input.available()];
				int read = input.read(inputData);

				if (read == inputData.length)
					data = inputData;
				else
					data = ByteUtil.subarray(inputData, 0, read);
			}

		} catch (IOException e) {
		}
	}

	@Override
	public byte read()
	{
		if (offset == data.length)
			return -1;

		byte read = data[offset++];

		return read;
	}

	@Override
	public void close()
	{
		data = null;
	}

	@Override
	public void reset()
	{
		offset = 0;
	}

	@Override
	public void read(byte[] array, int offset, int length)
	{
		for (int i = 0; i < length; i++)
			array[i] = data[offset + i];
	}

	@Override
	public byte[] getSaved()
	{
		return saved;
	}

	@Override
	public byte[] getDate()
	{
		return data;
	}

	@Override
	public void skip(int length)
	{
		offset += length;
	}

	@Override
	public boolean isClose()
	{
		return data == null;
	}

	@Override
	public void save(int bytes)
	{
		saved = ByteUtil.subarray(data, offset, bytes);
	}

	@Override
	public int length()
	{
		return data == null ? 0 : data.length;
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("offset", offset);
		description.append("inverted", invert);
		description.append("closed", data == null);
		description.append("space", space() == 0 ? "EOF" : space());

		if (saved != null)
			description.append("saved", saved.length);

		return description.toString();
	}
}
