package com.erakin.api.buffer;

import static org.diverproject.util.lang.Bits.makeInt;
import static org.diverproject.util.lang.Bits.makeLong;
import static org.diverproject.util.lang.Bits.makeShort;
import static org.diverproject.util.lang.Bits.swap;

import org.diverproject.util.lang.IntUtil;

/**
 * <h1>Núcleo para Buffer</h1>
 *
 * <p>Essa classe deve funcionar como um núcleo, o que todos os buffers possuem de igual independente do tipo.
 * No caso, a forma como os dados primitivos são lidos são todas iguais, usando apenas o read() como diferente.
 * Todos os getters dos primitivos funcionam lendo byte por byte através de read() usando Bits para trabalhar.</p>
 *
 * <p>Cada buffer que for criado no sistema, recomenda-se utilizar essa classe como base para todos.
 * Sendo necessário implementar apenas alguns métodos que realmente variam pra cada tipo de buffer.
 * Como por exemplo a leitura do próximo byte varia com o tipo ou ainda então como ele é fechado.</p>.
 *
 * @author Andrew Mello
 */

public abstract class BufferCore implements Buffer
{
	/**
	 * Indicação do próximo byte do qual deve ser lido (quantidade de bytes lidos).
	 */
	int offset;

	/**
	 * Indica se os bytes dos dados primitivos devem ser invertidos.
	 */
	boolean invert;

	@Override
	public byte[] read(int lenght)
	{
		byte bytes[] = new byte[lenght];
		read(bytes);

		return bytes;
	}

	@Override
	public char getChar()
	{
		return (char) read();
	}

	@Override
	public short getShort()
	{
		short value = makeShort(read(), read());

		return invert ? swap(value) : value;
	}

	@Override
	public int getInt()
	{
		int value = makeInt(read(), read(), read(), read());

		return invert ? swap(value) : value;
	}

	@Override
	public long getLong()
	{
		long value = makeLong(read(), read(), read(), read(), read(), read(), read(), read());

		return invert ? swap(value) : value;
	}

	@Override
	public float getFloat()
	{
		int value = getInt();

		if (invert)
			value = swap(value);

		return Float.intBitsToFloat(value);
	}

	@Override
	public double getDouble()
	{
		long value = getLong();

		if (invert)
			value = swap(value);

		return Double.longBitsToDouble(value);
	}

	@Override
	public String getString()
	{
		int length = IntUtil.parseByte(read());
		byte array[] = new byte[length];
		read(array);

		return new String(array);
	}

	@Override
	public String getString(int bytes)
	{
		byte array[] = new byte[bytes];
		read(array);

		return new String(array);
	}

	@Override
	public void read(byte[] array)
	{
		for (int i = 0; i < array.length; i++)
			array[i] = read();
	}

	@Override
	public int offset()
	{
		return offset;
	}

	@Override
	public int space()
	{
		return isClose() ? 0 : length() - offset;
	}

	@Override
	public void invert(boolean enable)
	{
		invert = enable;
	}
}
