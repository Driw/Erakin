package com.erakin.api;

import static org.diverproject.log.LogSystem.logError;
import static org.diverproject.util.MessageUtil.showError;

import java.util.Random;

import org.diverproject.util.collection.Collection;
import org.diverproject.util.lang.HexUtil;
import org.diverproject.util.lang.StringUtil;
import org.diverproject.util.stream.Input;

/**
 * <h1>Utilitários</h1>
 *
 * <p>Classe destina a funcionar como utilitário, portanto possui apenas procedimentos públicos e estáticos.
 * Esses procedimentos são utilizadas internamente pelo engine, porém podem ser úteis para o desenvolvimento
 * de um jogo baseado no engine, contendo diversas funcionalidades que irão facilitar o desenvolvimento.</p>
 *
 * @author Andrew Mello
 */

public final class ErakinAPIUtil
{
	/**
	 * Objeto randorizador.
	 */
	private static final Random random = new Random();

	/**
	 * Construtor privado para evitar instâncias desnecessárias.
	 */

	private ErakinAPIUtil()
	{
		
	}

	/**
	 * Usado para obter o nome da classe, reduzindo assim o código com chamada da classe e nome simples.
	 * No caso da classe chamada de núcleo do engine (Engine.java) seria retornado apenas <b>Engine</b>.
	 * @param object objeto do qual deseja obter o nome da classe.
	 * @return nome da classe independente de estar sendo visto como uma interface ou como herança.
	 */

	public static String nameOf(Object object)
	{
		if (object == null)
			return null;

		return object.getClass().getSimpleName();
	}

	/**
	 * Faz com que uma thread durma (fique em espera) por um determinado tempo sem necessidade de Try Catch.
	 * Caso haja uma exception durante essa espera, não será tratado, portanto deve ter bom uso.
	 * @param millis quantos milissegundos a thread deverá ficar em espera.
	 */

	public static void sleep(long millis)
	{
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Método usado para facilitar a codificação quando ocorrer um erro fatal no sistema.
	 * Irá exibir uma janela com título da exceção e contendo uma mensagem adicional para tal.
	 * Além disso irá finalizar forçadamente a aplicação sem encerrar o engine adequadamente.
	 * @param e referência da exceção do qual foi gerada durante a execução do engine.
	 * @param format formato que terá a mensagem adicional além da mensagem da exceção.
	 * @param args argumentos respectivos ao formato da mensagem passada acima.
	 */

	public static void fatalError(Exception e, String format, Object... args)
	{
		String formated = String.format(format, args);
		String message = String.format("%s\n%s", formated, e.getMessage());

		logError(formated+ "\n");

		e.printStackTrace();

		showError(e.getMessage(), message);

		System.exit(0);
	}

	/**
	 * Converte um objeto para string conforme o seu toString, obtendo apenas o conteúdo dos dados.
	 * O conteúdo dos dados que serão retornados será tudo aquilo que estiver entre chaves <b>[]</b>.
	 * @param object referência do objeto do qual será obtido os dados em <code>toString()</code>.
	 * @return aquisição de uma string contendo apenas os dados do <code>toString()</code>.
	 */

	public static String objectString(Object object)
	{
		if (object == null)
			return "null";

		String str = object.toString();

		return str.substring(str.indexOf('['), str.lastIndexOf(']') + 1);
	}

	/**
	 * <p>Permite recortar uma string do um ponto especificado até o final do mesmo.
	 * O ponto especificado será o índice do final de um conjunto de caracteres.</p>
	 * <p>Por exemplo: <code>subAt("0123456789", "345")</code>: <b>"6789"</b></p>
	 * @param source string do qual deverá ter seu conteúdo recortado.
	 * @param locate sequência de caracteres para indicar o final do corte.
	 * @return string com seu inicio cortado até encontrar <b>locate</b>,
	 * caso não seja encontrado retorna o conteúdo completo de <b>source</b>.
	 */

	public static String subAt(String source, String locate)
	{
		return subAt(source, locate, false);
	}

	/**
	 * <p>Permite recortar uma string do um ponto especificado até o final do mesmo.
	 * O ponto especificado será o índice do final de um conjunto de caracteres.</p>
	 * <p>Por exemplo: <code>subAt("0123456789", "345")</code>: <b>"6789"</b></p>
	 * @param source string do qual deverá ter seu conteúdo recortado.
	 * @param locate sequência de caracteres para indicar o final do corte.
	 * @param useLocate usar conteúdo de locate junto do recorte feito.
	 * @return string com seu inicio cortado até encontrar <b>locate</b>,
	 * caso não seja encontrado retorna o conteúdo completo de <b>source</b>.
	 */

	public static String subAt(String source, String locate, boolean useLocate)
	{
		int index = source.lastIndexOf(locate);

		if (index < 0)
			return source;

		if (!useLocate)
			index += locate.length();

		return source.substring(index, source.length());
	}

	/**
	 * <p>Permite recortar uma string do inicio até um ponto especificado do mesmo.
	 * O ponto especificado será o índice do inicio de um conjunto de caracteres.</p>
	 * <p>Por exemplo: <code>subAt("0123456789", "345")</code>: <b>"012"</b></p>
	 * @param source string do qual deverá ter seu conteúdo recortado.
	 * @param locate sequência de caracteres para indicar o inicio do corte.
	 * @return string com seu inicio cortado até encontrar <b>locate</b>,
	 * caso não seja encontrado retorna o conteúdo completo de <b>source</b>.
	 */

	public static String subAtEnd(String source, String locate)
	{
		return subAtEnd(source, locate, false);
	}

	/**
	 * <p>Permite recortar uma string do inicio até um ponto especificado do mesmo.
	 * O ponto especificado será o índice do inicio de um conjunto de caracteres.</p>
	 * <p>Por exemplo: <code>subAt("0123456789", "345")</code>: <b>"012"</b></p>
	 * @param source string do qual deverá ter seu conteúdo recortado.
	 * @param locate sequência de caracteres para indicar o inicio do corte.
	 * @param useLocate usar conteúdo de locate junto do recorte feito.
	 * @return string com seu inicio cortado até encontrar <b>locate</b>,
	 * caso não seja encontrado retorna o conteúdo completo de <b>source</b>.
	 */

	public static String subAtEnd(String source, String locate, boolean useLocate)
	{
		int index = source.indexOf(locate);

		if (index < 0)
			return source;

		if (useLocate)
			index += locate.length();

		return source.substring(0, index);
	}

	/**
	 * Usado apenas para obter um entendimento mais rápido durante a leitura do código.
	 * Irá verificar se uma dada coleção é válida (não é null) e retornar seu tamanho.
	 * @param collection referência da coleção do qual deseja obter o seu tamanho.
	 * @return tamanho respectivo da coleção passada ou -1 caso seja nulo.
	 */

	public static int sizeOfCollection(Collection<?> collection)
	{
		return collection == null ? -1 : collection.size();
	}

	/**
	 * Permite obter um valor positivo aleatório qualquer de acordo com as especificações abaixo.
	 * Para esse caso o valor não possui um mínimo ou máximo especificado (limites do java).
	 * @return aquisição de um valor de número inteiro randômico (aleatório).
	 */

	public static int rand()
	{
		return random.nextInt();
	}

	/**
	 * Permite obter um valor aleatório qualquer de acordo com as especificações abaixo.
	 * @param limit qual o maior valor que poderá ser obtido nessa randomização.
	 * @return aquisição um valor randômico (aleatório) de acordo com o limite acima.
	 */

	public static int rand(int limit)
	{
		return random.nextInt(limit);
	}

	/**
	 * Permite obter um valor aleatório qualquer de acordo com as especificações abaixo.
	 * @param min qual o menor valor que poderá ser obtido durante a randomização.
	 * @param max qual o maior valor que poderá ser obtido durante a randomização.
	 * @return aquisição um valor randômico (aleatório) de acordo com o limite acima.
	 */

	public static int rand(int min, int max)
	{
		return random.nextInt(max - min) + min;
	}

	/**
	 * Permite obter um valor positivo aleatório qualquer de acordo com as especificações abaixo.
	 * Para esse caso o valor não possui um mínimo ou máximo especificado (limites do java).
	 * @return aquisição de um valor de ponto flutuante randômico (aleatório).
	 */

	public static float randf()
	{
		return random.nextFloat();
	}

	/**
	 * Permite obter um valor negativo aleatório qualquer de acordo com as especificações abaixo.
	 * Para esse caso o valor não possui um mínimo ou máximo especificado (limites do java).
	 * @return aquisição de um valor de ponto flutuante randômico (aleatório).
	 */

	public static float randfn()
	{
		return random.nextInt(2) == 0 ? random.nextFloat() : -random.nextFloat();
	}

	/**
	 * Converte um tipo de valor numérico para um String contendo seu valor em hexadecimal de 2 caracteres.
	 * @param value valor numérico do tipo byte do qual será convertido para hexadecimal.
	 * @return aquisição da String com o valor hexadecimal obtido a partir do valor especificado.
	 */

	public static String hex(byte value)
	{
		return StringUtil.addEndWhile("0", HexUtil.parseByte(value), 2);
	}

	/**
	 * Converte um tipo de valor numérico para um String contendo seu valor em hexadecimal de 4 caracteres.
	 * @param value valor numérico do tipo short do qual será convertido para hexadecimal.
	 * @return aquisição da String com o valor hexadecimal obtido a partir do valor especificado.
	 */

	public static String hex(short value)
	{
		return StringUtil.addEndWhile("0", HexUtil.parseShort(value), 4);
	}

	/**
	 * Converte um tipo de valor numérico para um String contendo seu valor em hexadecimal de 8 caracteres.
	 * @param value valor numérico do tipo int do qual será convertido para hexadecimal.
	 * @return aquisição da String com o valor hexadecimal obtido a partir do valor especificado.
	 */

	public static String hex(int value)
	{
		return StringUtil.addEndWhile("0", HexUtil.parseInt(value), 8);
	}

	/**
	 * Converte um tipo de valor numérico para um String contendo seu valor em hexadecimal de 16 caracteres.
	 * @param value valor numérico do tipo long do qual será convertido para hexadecimal.
	 * @return aquisição da String com o valor hexadecimal obtido a partir do valor especificado.
	 */

	public static String hex(long value)
	{
		return StringUtil.addEndWhile("0", HexUtil.parseLong(value), 16);
	}

	/**
	 * Procedimento para validação do tipo de arquivo com base nas informações "magic" (dados inicias padrão).
	 * O magic corresponde a uma sequência de dados fixo que todo arquivo de um tipo especifico deve possuir.
	 * @param input referência da stream para entrada de dados criado a partir do arquivo em disco.
	 * @param realMagic vetor com os bytes referentes a validação do valor magic do arquivo.
	 * @return true se o valor for válido ou false caso contrário.
	 */

	public static boolean magicFileValidade(Input input, byte realMagic[])
	{
		for (byte b : realMagic)
			if (input.getByte() != b)
				return false;

		return true;
	}
}
