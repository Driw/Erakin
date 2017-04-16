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
 * <p>Esse buffer funciona diretamente com bytes armazenados em vetores ou algo do gênero.
 * É possível criar buffers desse tipo de duas maneiras, a primeira através de arquivos especificados.
 * Em quanto a outra é através de objetos do tipo InputStream que podem de arquivos ou outro.</p>
 *
 * <p>Possui todos os métodos necessários para funcionamento do gerenciamento com bytes em memória.
 * Usufrui das funcionalidades pré-definidas no Núcleo para Buffer para simplificar a implementação.</p>
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
	 * Constrói um novo buffer para trabalhar com os bytes de um arquivo qualquer.
	 * @param path caminho do arquivo do qual será aberto e lido os bytes.
	 */

	public BufferInput(String path)
	{
		this(new File(path));
	}

	/**
	 * Constrói um novo buffer para trabalhar com os bytes de um arquivo qualquer.
	 * @param file referência do arquivo do qual será aberto e lido os bytes.
	 */

	public BufferInput(File file)
	{
		try {
			parse(new FileInputStream(file));
		} catch (FileNotFoundException e) {
		}
	}

	/**
	 * Constrói um novo buffer para trabalhar com os bytes de um InputStream qualquer.
	 * @param input referência da stream como entrada de dados para o buffer.
	 */

	public BufferInput(InputStream input)
	{
		parse(input);
	}

	/**
	 * Procedimento interno usado para analisar um InputStream para fazer a leitura dos dados.
	 * Lê completamente o arquivo, armazena os dados deste em memória e o transforma em buffer.
	 * @param input referência da stream como entrada de dados para o buffer.
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
