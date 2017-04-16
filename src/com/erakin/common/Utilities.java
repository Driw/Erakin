package com.erakin.common;

import static org.diverproject.log.LogSystem.logError;
import static org.diverproject.util.MessageUtil.showError;

import java.util.Random;

import org.diverproject.util.collection.Collection;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.erakin.common.vector.Vector3i;
import com.erakin.engine.ProjectionMatrix;

/**
 * <h1>Utilit�rios</h1>
 *
 * <p>Classe destina a funcionar como utilit�rio, portanto possui apenas procedimentos p�blicos e est�ticos.
 * Esses procedimentos s�o utilizadas internamente pelo engine, por�m podem ser �teis para o desenvolvimento
 * de um jogo baseado no engine, contendo diversas funcionalidades que ir�o facilitar o desenvolvimento.</p>
 *
 * @author Andrew Mello
 */

public final class Utilities
{
	/**
	 * Objeto randorizador.
	 */
	private static final Random random = new Random();

	/**
	 * Construtor privado para evitar inst�ncias desnecess�rias.
	 */

	private Utilities()
	{
		
	}

	/**
	 * Usado para obter o nome da classe, reduzindo assim o c�digo com chamada da classe e nome simples.
	 * No caso da classe chamada de n�cleo do engine (Engine.java) seria retornado apenas <b>Engine</b>.
	 * @param object objeto do qual deseja obter o nome da classe.
	 * @return nome da classe independente de estar sendo visto como uma interface ou como heran�a.
	 */

	public static String nameOf(Object object)
	{
		if (object == null)
			return null;

		return object.getClass().getSimpleName();
	}

	/**
	 * Faz com que uma thread durma (fique em espera) por um determinado tempo sem necessidade de Try Catch.
	 * Caso haja uma exception durante essa espera, n�o ser� tratado, portanto deve ter bom uso.
	 * @param millis quantos milissegundos a thread dever� ficar em espera.
	 */

	public static void sleep(long millis)
	{
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * M�todo usado para facilitar a codifica��o quando ocorrer um erro fatal no sistema.
	 * Ir� exibir uma janela com t�tulo da exce��o e contendo uma mensagem adicional para tal.
	 * Al�m disso ir� finalizar for�adamente a aplica��o sem encerrar o engine adequadamente.
	 * @param e refer�ncia da exce��o do qual foi gerada durante a execu��o do engine.
	 * @param format formato que ter� a mensagem adicional al�m da mensagem da exce��o.
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
	 * Converte um objeto para string conforme o seu toString, obtendo apenas o conte�do dos dados.
	 * O conte�do dos dados que ser�o retornados ser� tudo aquilo que estiver entre chaves <b>[]</b>.
	 * @param object refer�ncia do objeto do qual ser� obtido os dados em <code>toString()</code>.
	 * @return aquisi��o de uma string contendo apenas os dados do <code>toString()</code>.
	 */

	public static String objectString(Object object)
	{
		if (object == null)
			return "null";

		String str = object.toString();

		return str.substring(str.indexOf('['), str.lastIndexOf(']') + 1);
	}

	/**
	 * <p>Permite recortar uma string do um ponto especificado at� o final do mesmo.
	 * O ponto especificado ser� o �ndice do final de um conjunto de caracteres.</p>
	 * <p>Por exemplo: <code>subAt("0123456789", "345")</code>: <b>"6789"</b></p>
	 * @param source string do qual dever� ter seu conte�do recortado.
	 * @param locate sequ�ncia de caracteres para indicar o final do corte.
	 * @return string com seu inicio cortado at� encontrar <b>locate</b>,
	 * caso n�o seja encontrado retorna o conte�do completo de <b>source</b>.
	 */

	public static String subAt(String source, String locate)
	{
		return subAt(source, locate, false);
	}

	/**
	 * <p>Permite recortar uma string do um ponto especificado at� o final do mesmo.
	 * O ponto especificado ser� o �ndice do final de um conjunto de caracteres.</p>
	 * <p>Por exemplo: <code>subAt("0123456789", "345")</code>: <b>"6789"</b></p>
	 * @param source string do qual dever� ter seu conte�do recortado.
	 * @param locate sequ�ncia de caracteres para indicar o final do corte.
	 * @param useLocate usar conte�do de locate junto do recorte feito.
	 * @return string com seu inicio cortado at� encontrar <b>locate</b>,
	 * caso n�o seja encontrado retorna o conte�do completo de <b>source</b>.
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
	 * <p>Permite recortar uma string do inicio at� um ponto especificado do mesmo.
	 * O ponto especificado ser� o �ndice do inicio de um conjunto de caracteres.</p>
	 * <p>Por exemplo: <code>subAt("0123456789", "345")</code>: <b>"012"</b></p>
	 * @param source string do qual dever� ter seu conte�do recortado.
	 * @param locate sequ�ncia de caracteres para indicar o inicio do corte.
	 * @return string com seu inicio cortado at� encontrar <b>locate</b>,
	 * caso n�o seja encontrado retorna o conte�do completo de <b>source</b>.
	 */

	public static String subAtEnd(String source, String locate)
	{
		return subAtEnd(source, locate, false);
	}

	/**
	 * <p>Permite recortar uma string do inicio at� um ponto especificado do mesmo.
	 * O ponto especificado ser� o �ndice do inicio de um conjunto de caracteres.</p>
	 * <p>Por exemplo: <code>subAt("0123456789", "345")</code>: <b>"012"</b></p>
	 * @param source string do qual dever� ter seu conte�do recortado.
	 * @param locate sequ�ncia de caracteres para indicar o inicio do corte.
	 * @param useLocate usar conte�do de locate junto do recorte feito.
	 * @return string com seu inicio cortado at� encontrar <b>locate</b>,
	 * caso n�o seja encontrado retorna o conte�do completo de <b>source</b>.
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
	 * Usado apenas para obter um entendimento mais r�pido durante a leitura do c�digo.
	 * Ir� verificar se uma dada cole��o � v�lida (n�o � null) e retornar seu tamanho.
	 * @param collection refer�ncia da cole��o do qual deseja obter o seu tamanho.
	 * @return tamanho respectivo da cole��o passada ou -1 caso seja nulo.
	 */

	public static int sizeOfCollection(Collection<?> collection)
	{
		return collection == null ? -1 : collection.size();
	}

	/**
	 * Permite obter um valor positivo aleat�rio qualquer de acordo com as especifica��es abaixo.
	 * Para esse caso o valor n�o possui um m�nimo ou m�ximo especificado (limites do java).
	 * @return aquisi��o de um valor de n�mero inteiro rand�mico (aleat�rio).
	 */

	public static int rand()
	{
		return random.nextInt();
	}

	/**
	 * Permite obter um valor aleat�rio qualquer de acordo com as especifica��es abaixo.
	 * @param limit qual o maior valor que poder� ser obtido nessa randomiza��o.
	 * @return aquisi��o um valor rand�mico (aleat�rio) de acordo com o limite acima.
	 */

	public static int rand(int limit)
	{
		return random.nextInt(limit);
	}

	/**
	 * Permite obter um valor aleat�rio qualquer de acordo com as especifica��es abaixo.
	 * @param min qual o menor valor que poder� ser obtido durante a randomiza��o.
	 * @param max qual o maior valor que poder� ser obtido durante a randomiza��o.
	 * @return aquisi��o um valor rand�mico (aleat�rio) de acordo com o limite acima.
	 */

	public static int rand(int min, int max)
	{
		return random.nextInt(max - min) + min;
	}

	/**
	 * Permite obter um valor positivo aleat�rio qualquer de acordo com as especifica��es abaixo.
	 * Para esse caso o valor n�o possui um m�nimo ou m�ximo especificado (limites do java).
	 * @return aquisi��o de um valor de ponto flutuante rand�mico (aleat�rio).
	 */

	public static float randf()
	{
		return random.nextFloat();
	}

	/**
	 * Permite obter um valor negativo aleat�rio qualquer de acordo com as especifica��es abaixo.
	 * Para esse caso o valor n�o possui um m�nimo ou m�ximo especificado (limites do java).
	 * @return aquisi��o de um valor de ponto flutuante rand�mico (aleat�rio).
	 */

	public static float randfn()
	{
		return random.nextInt(2) == 0 ? random.nextFloat() : -random.nextFloat();
	}

	/**
	 * A matriz de proje��o � usada para indicar a �rea e dist�ncia para visualizar o espa�o na tela.
	 * @return aquisi��o da matriz de proje��o global usada pelo engine.
	 */

	public static Matrix4f getProjectMatrix()
	{
		ProjectionMatrix projectionMatrix = ProjectionMatrix.getInstance();
		Matrix4f matrix = projectionMatrix.getMatrix();

		return matrix;
	}

	/**
	 * Permite definir alguns atributos da matriz de proje��o e atualizar o mesmo ap�s.
	 * @param fieldOfView campo de vis�o indica o quanto ser� vis�vel pela c�mera.
	 * @param nearPlane indica onde ser� iniciado o campo de vis�o respectivo a c�mera.
	 * @param farPlane indica onde ser� terminado o campo de vis�o respectivo a c�mera.
	 */

	public static void setProjection(float fieldOfView, float nearPlane, float farPlane)
	{
		ProjectionMatrix projectionMatrix = ProjectionMatrix.getInstance();
		projectionMatrix.updateWith(fieldOfView, nearPlane, farPlane);
	}

	/**
	 * Passa os valores de um vetor de 3 pontos flutuantes para um vetor de 3 pontos inteiros.
	 * @param vector refer�ncia do vetor contendo os 3 pontos flutuantes.
	 * @return vetor de 3 pontos especificados como X, Y e Z do tipo inteiro.
	 */

	public static Vector3i convert3f3i(Vector3f vector)
	{
		return new Vector3i((int) vector.x, (int) vector.y, (int) vector.z);
	}

	/**
	 * Passa os valores de um vetor de 3 pontos inteiros para um vetor de 3 pontos flutuantes.
	 * @param vector refer�ncia do vetor contendo os 3 pontos inteiros.
	 * @return vetor de 3 pontos especificados como X, Y e Z do tipo flutuante.
	 */

	public static Vector3f convert3i3f(Vector3i vector)
	{
		return new Vector3f(vector.x, vector.y, vector.z);
	}
}
