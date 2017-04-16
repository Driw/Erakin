package com.erakin.common;

import org.diverproject.util.UtilException;

/**
 * <h1>Exceção Erakin</h1>
 *
 * <p>Todas as exceções que possam ser geradas durante a utilização da biblioteca do engine serão deste tipo.
 * O motivo da utilização deste é permitir uma fácil utilização de try catch onde todas exceções são iguais.
 * Para alguns pontos será determinado alguns outros tipos de exceções se assim for necessário para tal.</p>
 *
 * <p>Possui diversos construtores, que permitem uma melhor forma de determinar como será a mensagem.
 * Utiliza métodos que permitem usar formatação para definir a mensagem ou então usar outra exceção.
 * Podendo ainda usar uma combinação dos dois tipos, usando uma mensagem formatada com uma exceções.</p>
 *
 * <p>Esse tipo de exceção será gerada por toda parte do sistema que não seja muito muito longa/grande.
 * Como por exemplos as partes principais do sistema apesar de grandes são únicas e não possui tipos.
 * Já outros casos como recursos gráficos, sonoros ou modelagens possuem várias classes.</p>
 *
 * @see UtilException
 *
 * @author Andrew Mello
 */

public class ErakinException extends UtilException
{
	/**
	 * Serialização para identificação da classe.
	 */
	private static final long serialVersionUID = 2926428005702877844L;

	/**
	 * Constrói uma nova exceção de gênero genérico, sendo necessário definir a mensagem de causa.
	 * @param message mensagem contendo informações sobre o que ocasionou a exceção.
	 */

	public ErakinException(String message)
	{
		super(message);
	}

	/**
	 * Constrói uma nova exceção de gênero genérico, sendo necessário definir a mensagem de causa.
	 * @param format string contendo uma formatação de uma mensagem sobre o que ocasionou a exceção.
	 * @param args argumentos respectivos a formatação da mensagem para serem exibidos.
	 */

	public ErakinException(String format, Object... args)
	{
		super(format, args);
	}

	/**
	 * Constrói uma nova exceção de gênero genérico, sendo necessário definir a mensagem de causa.
	 * Para esse caso em questão é necessário utilizar uma outra exceção e usar sua mensagem.
	 * Mostra o nome da exceção (classe) ao final da mensagem apenas se for outro tipo.
	 * @param e exceção do qual deverá ser copiado a mensagem para essa nova exceção.
	 */

	public ErakinException(Exception e)
	{
		super(e);
	}

	/**
	 * Constrói uma nova exceção de gênero genérico, sendo necessário definir a mensagem de causa.
	 * Para esse caso em questão é necessário utilizar uma outra exceção e usar sua mensagem.
	 * Mostra o nome da exceção (classe) ao final da mensagem apenas se for outro tipo.
	 * @param e exceção do qual deverá ser copiado a mensagem para essa nova exceção.
	 * @param format string contendo uma formatação de uma mensagem sobre o que ocasionou a exceção.
	 * @param args argumentos respectivos a formatação da mensagem para serem exibidos.
	 */

	public ErakinException(Exception e, String format, Object... args)
	{
		super(e, format, args);
	}
}
