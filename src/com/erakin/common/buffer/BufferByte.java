package com.erakin.common.buffer;

import java.nio.ByteBuffer;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Buffer de Bytes</h1>
 *
 * <p>Esse buffer funciona diretamente com bytes armazenados em vetores ou algo do g�nero.
 * A primeira op��o � a constru��o de um buffer em branco, sem dados para poder ler, apenas escrever.
 * Outra op��o � a partir de um vetor de bytes passado, utilizar� deste vetor ao inv�s de copiar.
 * Podendo ainda tamb�m trabalhar com o buffer usado pela linguagem JAVA denominado ByteBuffer.</p>
 *
 * <p>Possui todos os m�todos necess�rios para funcionamento do gerenciamento com bytes em mem�ria.
 * Usufrui das funcionalidades pr�-definidas no N�cleo para Buffer para simplificar a implementa��o.</p>
 *
 * @see BufferCore
 *
 * @author Andrew Mello
 */

public class BufferByte extends BufferCore
{
	/**
	 * Tamanho limite que determina o n�mero de bytes poss�veis para ler.
	 */
	private int savedOffset;

	/**
	 * Vetor usado para armazenamento dos bytes salvos quando solicitado.
	 */
	private byte saved[];

	/**
	 * Vetor usado para fazer a leitura dos bytes armazenados como dados do buffer.
	 */
	private byte data[];

	/**
	 * Constr�i um novo buffer para leitura de dados em branco, sem dados armazenados.
	 * @param size quantos bytes o buffer dever� ser capaz de segurar.
	 */

	public BufferByte(int size)
	{
		data = new byte[size];
	}

	/**
	 * Constr�i um novo buffer para leitura de dados a partir de bytes de um vetor de bytes.
	 * Nesse caso, o buffer ser� referenciado dentro do buffer ao inv�s de ser copiado.
	 * @param array refer�ncia do vetor que ser� usado para leitura de dados.
	 */

	public BufferByte(byte[] array)
	{
		data = array;
	}

	/**
	 * Constr�i um novo buffer para leitura de dados a partir de bytes de um buffer de bytes do java.
	 * @param byteBuffer refer�ncia do buffer do java contendo os bytes que ser�o usados.
	 */

	public BufferByte(ByteBuffer byteBuffer)
	{
		data = byteBuffer.array();
	}

	@Override
	public byte read()
	{
		if (offset == data.length)
			return -1;

		byte read = data[offset++];

		if (saved != null && savedOffset < saved.length)
			saved[savedOffset++] = read;

		return read;
	}

	@Override
	public boolean isClose()
	{
		return data == null;
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
	public byte[] getSaved()
	{
		return data;
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
	public void read(byte[] array, int offset, int length)
	{
		for (int i = 0; i < length; i++)
			array[i] = data[offset + i];
	}

	@Override
	public void save(int bytes)
	{
		saved = new byte[bytes];
		savedOffset = 0;
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
		description.append("closed", data == null);
		description.append("size", space() == 0 ? "EOF" : space());

		if (saved != null)
		{
			description.append("savedOffset", savedOffset);
			description.append("saved", saved.length);
		}

		return description.toString();
	}
}
