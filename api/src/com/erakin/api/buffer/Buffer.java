package com.erakin.api.buffer;

/**
 * <h1>Buffer</h1>
 *
 * <p>Buffer pode ser usado de diferentes formas de acordo com o necess�rio no desenvolvimento.
 * Pode ser usado a partir de um arquivo em disco, lendo os bytes e armazenados temporariamente.
 * Ou ainda ent�o como o tradicional, reservar um espa�o de bytes em mem�ria e utiliz�-los.
 *
 * <p>Essa utiliza��o permite obter diversos tipos de dados primitivos atrav�s dos bytes.
 * Os dados poss�veis de se obter s�o char, short, int, long, float, double e string.
 * Apenas string n�o � um dado primitivo, usando tamb�m uma forma diferente para ler.
 *
 * <p>Al�m de obter dados possui algumas informa��es extras relacionadas ao buffer que podem ajudar.
 * Como fechar o buffer, restabelecer para o inicio, salvar ou pular uma quantidade de bytes.
 * Algo que pode ser considerado um problema na utiliza��o, � que n�o se gera Exceptions ao us�-lo.</p>
 *
 * @author Andrew Mello
 */

public interface Buffer
{
	/**
	 * Tamanho de inicializa��o padr�o para o buffer em bytes (8kb).
	 */
	public static final int DEFAULT_SIZE = 8096;

	/**
	 * Buffers possuem um procedimento que permite salvar uma quantidade de bytes.
	 * @return aquisi��o dos bytes que foram salvos da �ltima vez.
	 */

	byte[] getSaved();

	/**
	 * Buffers s�o formados por um vetor de bytes, onde armazena os dados lidos.
	 * @return aquisi��o de todos os bytes lidos desde o inicio do buffer.
	 */

	byte[] getDate();

	/**
	 * Permite ler um �nico byte sem converter o tipo de dado.
	 * @return aquisi��o do pr�ximo byte que deve ser lido.
	 */

	byte read();

	/**
	 * Permite ler um �nico byte para e convert�-los para o formato char.
	 * @return aquisi��o do pr�ximo byte que deve ser lido.
	 */

	char getChar();

	/**
	 * Permite ler dois bytes seguidos convert�-los para o formato short.
	 * @return aquisi��o dos pr�ximos dois bytes que devem ser lidos.
	 */

	short getShort();

	/**
	 * Permite ler quatro bytes seguidos e convert�-los para o formato int.
	 * @return aquisi��o dos pr�ximos quatro bytes que devem ser lidos.
	 */

	int getInt();

	/**
	 * Permite ler oito bytes seguidos e convert�-los para o formato long.
	 * @return aquisi��o dos pr�ximos oito bytes que devem ser lidos.
	 */

	long getLong();

	/**
	 * Permite ler quatro bytes seguidos e convert�-los para o formato float.
	 * @return aquisi��o dos pr�ximos quatro bytes que devem ser lidos.
	 */

	float getFloat();

	/**
	 * Permite ler oito bytes seguidos e convert�-los para o formato double.
	 * @return aquisi��o dos pr�ximos oito bytes que devem ser lidos.
	 */

	double getDouble();

	/**
	 * Permite ler os pr�ximos n bytes e convert�-los para o formato string.
	 * @return aquisi��o de uma string com n bytes onde n � o pr�ximo byte lido.
	 */

	String getString();

	/**
	 * Permite ler os pr�ximos n bytes e convert�-los para o formato string.
	 * @param bytes quantos bytes ter� a string do qual deseja obter.
	 * @return aquisi��o da string contendo os pr�ximos bytes.
	 */

	String getString(int bytes);

	/**
	 * Constr�i um vetor de bytes internamente em seguida ir�
	 * carrega-lo com os pr�ximos bytes dispon�vel para leitura.
	 * @param lenght quantidade de bytes que ser�o lidos.
	 * @return vetor contendo a quantiadde de bytes acima.
	 */

	byte[] read(int lenght);

	/**
	 * Carrega um vetor de bytes com os pr�ximos bytes lidos.
	 * @param array vetor que ter� os bytes carregados.
	 */

	void read(byte[] array);

	/**
	 * Carrega uma determinada quantidade de bytes de acordo com as informa��es abaixo.
	 * @param array vetor do qual ter� os bytes carregados do buffer.
	 * @param offset a partir de qual �ndice do vetor ser� carregado.
	 * @param length quantos bytes dever�o ser carregados a partir do offset.
	 */

	void read(byte[] array, int offset, int length);

	/**
	 * Fecha o buffer de modo que este n�o possa ler mais nenhum dado.
	 */

	void close();

	/**
	 * Restabelece o buffer para o ponto inicial do mesmo como se tive sido instanciado novamente.
	 */

	void reset();

	/**
	 * Salva uma determinada quantidade de bytes enviado estes para um buffer secund�rio interno.
	 * @param bytes quantos bytes dever�o ser armazenados no buffer interno.
	 */

	void save(int bytes);

	/**
	 * Permite que o ponteiro que indica onde o buffer dever� ler avan�ar para os pr�ximos bytes.
	 * @param length quantidade de bytes que o ponteiro dever� pular na stream.
	 */

	void skip(int length);

	/**
	 * Offset determina o qu�o longe do inicio da stream est� sendo lido os bytes, ponteiro.
	 * @return aquisi��o do n�mero de bytes que j� foram lidos ou local do ponteiro na stream.
	 */

	int offset();

	/**
	 * Comprimento ir� indicar o tamanho do atual buffer interno que armazena os bytes.
	 * @return aquisi��o da capacidade atual do buffer para armazenar bytes.
	 */

	int length();

	/**
	 * Tamanho ir� indicar uma diferen�a entre o tamanho do buffer e o ponteiro.
	 * @return quantidade de bytes que ainda podem ser lidos.
	 */

	int space();

	/**
	 * Verifica se o buffer j� foi solicitado para ser fechado, pode ser tamb�m de buffer inv�lido.
	 * @return true se estiver fechado ou for inv�lido ou false caso contr�rio.
	 */

	boolean isClose();

	/**
	 * Habilita a invers�o de bytes quando for feita a leitura de um dado primitivo.
	 * Para esse caso ser� considerado apenas short, int, long, float e double.
	 * @param enable true para habilitar a invers�o ou false para desabilitar.
	 */

	void invert(boolean enable);

	/**
	 * Permite fazer a leitura de um vetor de n�meros inteiros em um buffer.
	 * @param buffer refer�ncia do buffer para fizer a leitura dos n�meros.
	 * @param array vetor que ser� usado para armazenar os n�meros lidos.
	 */

	public static void load(Buffer buffer, int[] array)
	{
		for (int i = 0; i < array.length; i++)
			array[i] = buffer.getInt();
	}

	/**
	 * Permite fazer a leitura de um vetor de n�meros flutuantes em um buffer.
	 * @param buffer refer�ncia do buffer para fazer a leitura dos n�meros.
	 * @param array vetor que ser� usado para armazenar os n�meros lidos.
	 */

	public static void load(Buffer buffer, float[] array)
	{
		for (int i = 0; i < array.length; i++)
			array[i] = buffer.getFloat();
	}

	/**
	 * Permite fazer a leitura de uma matriz de n�meros inteiros em um buffer.
	 * @param buffer refer�ncia do buffer para fazer a leitura dos n�meros.
	 * @param matrix matriz que ser� usada para armazenar os n�meros lidos.
	 */

	public static void load(Buffer buffer, int[][] matrix)
	{
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[i].length; j++)
				matrix[i][j] = buffer.getInt();
	}

	/**
	 * Permite fazer a leitura de uma matriz de n�meros flutuantes em um buffer.
	 * @param buffer refer�ncia do buffer para fazer a leitura dos n�meros.
	 * @param matrix matriz que ser� usada para armazenar os n�meros lidos.
	 */

	public static void load(Buffer buffer, float[][] matrix)
	{
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[i].length; j++)
				matrix[i][j] = buffer.getFloat();
	}
}
