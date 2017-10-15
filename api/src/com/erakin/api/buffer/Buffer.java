package com.erakin.api.buffer;

/**
 * <h1>Buffer</h1>
 *
 * <p>Buffer pode ser usado de diferentes formas de acordo com o necessário no desenvolvimento.
 * Pode ser usado a partir de um arquivo em disco, lendo os bytes e armazenados temporariamente.
 * Ou ainda então como o tradicional, reservar um espaço de bytes em memória e utilizá-los.
 *
 * <p>Essa utilização permite obter diversos tipos de dados primitivos através dos bytes.
 * Os dados possíveis de se obter são char, short, int, long, float, double e string.
 * Apenas string não é um dado primitivo, usando também uma forma diferente para ler.
 *
 * <p>Além de obter dados possui algumas informações extras relacionadas ao buffer que podem ajudar.
 * Como fechar o buffer, restabelecer para o inicio, salvar ou pular uma quantidade de bytes.
 * Algo que pode ser considerado um problema na utilização, é que não se gera Exceptions ao usá-lo.</p>
 *
 * @author Andrew Mello
 */

public interface Buffer
{
	/**
	 * Tamanho de inicialização padrão para o buffer em bytes (8kb).
	 */
	public static final int DEFAULT_SIZE = 8096;

	/**
	 * Buffers possuem um procedimento que permite salvar uma quantidade de bytes.
	 * @return aquisição dos bytes que foram salvos da última vez.
	 */

	byte[] getSaved();

	/**
	 * Buffers são formados por um vetor de bytes, onde armazena os dados lidos.
	 * @return aquisição de todos os bytes lidos desde o inicio do buffer.
	 */

	byte[] getDate();

	/**
	 * Permite ler um único byte sem converter o tipo de dado.
	 * @return aquisição do próximo byte que deve ser lido.
	 */

	byte read();

	/**
	 * Permite ler um único byte para e convertê-los para o formato char.
	 * @return aquisição do próximo byte que deve ser lido.
	 */

	char getChar();

	/**
	 * Permite ler dois bytes seguidos convertê-los para o formato short.
	 * @return aquisição dos próximos dois bytes que devem ser lidos.
	 */

	short getShort();

	/**
	 * Permite ler quatro bytes seguidos e convertê-los para o formato int.
	 * @return aquisição dos próximos quatro bytes que devem ser lidos.
	 */

	int getInt();

	/**
	 * Permite ler oito bytes seguidos e convertê-los para o formato long.
	 * @return aquisição dos próximos oito bytes que devem ser lidos.
	 */

	long getLong();

	/**
	 * Permite ler quatro bytes seguidos e convertê-los para o formato float.
	 * @return aquisição dos próximos quatro bytes que devem ser lidos.
	 */

	float getFloat();

	/**
	 * Permite ler oito bytes seguidos e convertê-los para o formato double.
	 * @return aquisição dos próximos oito bytes que devem ser lidos.
	 */

	double getDouble();

	/**
	 * Permite ler os próximos n bytes e convertê-los para o formato string.
	 * @return aquisição de uma string com n bytes onde n é o próximo byte lido.
	 */

	String getString();

	/**
	 * Permite ler os próximos n bytes e convertê-los para o formato string.
	 * @param bytes quantos bytes terá a string do qual deseja obter.
	 * @return aquisição da string contendo os próximos bytes.
	 */

	String getString(int bytes);

	/**
	 * Constrói um vetor de bytes internamente em seguida irá
	 * carrega-lo com os próximos bytes disponível para leitura.
	 * @param lenght quantidade de bytes que serão lidos.
	 * @return vetor contendo a quantiadde de bytes acima.
	 */

	byte[] read(int lenght);

	/**
	 * Carrega um vetor de bytes com os próximos bytes lidos.
	 * @param array vetor que terá os bytes carregados.
	 */

	void read(byte[] array);

	/**
	 * Carrega uma determinada quantidade de bytes de acordo com as informações abaixo.
	 * @param array vetor do qual terá os bytes carregados do buffer.
	 * @param offset a partir de qual índice do vetor será carregado.
	 * @param length quantos bytes deverão ser carregados a partir do offset.
	 */

	void read(byte[] array, int offset, int length);

	/**
	 * Fecha o buffer de modo que este não possa ler mais nenhum dado.
	 */

	void close();

	/**
	 * Restabelece o buffer para o ponto inicial do mesmo como se tive sido instanciado novamente.
	 */

	void reset();

	/**
	 * Salva uma determinada quantidade de bytes enviado estes para um buffer secundário interno.
	 * @param bytes quantos bytes deverão ser armazenados no buffer interno.
	 */

	void save(int bytes);

	/**
	 * Permite que o ponteiro que indica onde o buffer deverá ler avançar para os próximos bytes.
	 * @param length quantidade de bytes que o ponteiro deverá pular na stream.
	 */

	void skip(int length);

	/**
	 * Offset determina o quão longe do inicio da stream está sendo lido os bytes, ponteiro.
	 * @return aquisição do número de bytes que já foram lidos ou local do ponteiro na stream.
	 */

	int offset();

	/**
	 * Comprimento irá indicar o tamanho do atual buffer interno que armazena os bytes.
	 * @return aquisição da capacidade atual do buffer para armazenar bytes.
	 */

	int length();

	/**
	 * Tamanho irá indicar uma diferença entre o tamanho do buffer e o ponteiro.
	 * @return quantidade de bytes que ainda podem ser lidos.
	 */

	int space();

	/**
	 * Verifica se o buffer já foi solicitado para ser fechado, pode ser também de buffer inválido.
	 * @return true se estiver fechado ou for inválido ou false caso contrário.
	 */

	boolean isClose();

	/**
	 * Habilita a inversão de bytes quando for feita a leitura de um dado primitivo.
	 * Para esse caso será considerado apenas short, int, long, float e double.
	 * @param enable true para habilitar a inversão ou false para desabilitar.
	 */

	void invert(boolean enable);

	/**
	 * Permite fazer a leitura de um vetor de números inteiros em um buffer.
	 * @param buffer referência do buffer para fizer a leitura dos números.
	 * @param array vetor que será usado para armazenar os números lidos.
	 */

	public static void load(Buffer buffer, int[] array)
	{
		for (int i = 0; i < array.length; i++)
			array[i] = buffer.getInt();
	}

	/**
	 * Permite fazer a leitura de um vetor de números flutuantes em um buffer.
	 * @param buffer referência do buffer para fazer a leitura dos números.
	 * @param array vetor que será usado para armazenar os números lidos.
	 */

	public static void load(Buffer buffer, float[] array)
	{
		for (int i = 0; i < array.length; i++)
			array[i] = buffer.getFloat();
	}

	/**
	 * Permite fazer a leitura de uma matriz de números inteiros em um buffer.
	 * @param buffer referência do buffer para fazer a leitura dos números.
	 * @param matrix matriz que será usada para armazenar os números lidos.
	 */

	public static void load(Buffer buffer, int[][] matrix)
	{
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[i].length; j++)
				matrix[i][j] = buffer.getInt();
	}

	/**
	 * Permite fazer a leitura de uma matriz de números flutuantes em um buffer.
	 * @param buffer referência do buffer para fazer a leitura dos números.
	 * @param matrix matriz que será usada para armazenar os números lidos.
	 */

	public static void load(Buffer buffer, float[][] matrix)
	{
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[i].length; j++)
				matrix[i][j] = buffer.getFloat();
	}
}
