package com.erakin.common;

import org.diverproject.util.UtilException;

/**
 * <h1>Exce��o Erakin</h1>
 *
 * <p>Todas as exce��es que possam ser geradas durante a utiliza��o da biblioteca do engine ser�o deste tipo.
 * O motivo da utiliza��o deste � permitir uma f�cil utiliza��o de try catch onde todas exce��es s�o iguais.
 * Para alguns pontos ser� determinado alguns outros tipos de exce��es se assim for necess�rio para tal.</p>
 *
 * <p>Possui diversos construtores, que permitem uma melhor forma de determinar como ser� a mensagem.
 * Utiliza m�todos que permitem usar formata��o para definir a mensagem ou ent�o usar outra exce��o.
 * Podendo ainda usar uma combina��o dos dois tipos, usando uma mensagem formatada com uma exce��es.</p>
 *
 * <p>Esse tipo de exce��o ser� gerada por toda parte do sistema que n�o seja muito muito longa/grande.
 * Como por exemplos as partes principais do sistema apesar de grandes s�o �nicas e n�o possui tipos.
 * J� outros casos como recursos gr�ficos, sonoros ou modelagens possuem v�rias classes.</p>
 *
 * @see UtilException
 *
 * @author Andrew Mello
 */

public class ErakinException extends UtilException
{
	/**
	 * Serializa��o para identifica��o da classe.
	 */
	private static final long serialVersionUID = 2926428005702877844L;

	/**
	 * Constr�i uma nova exce��o de g�nero gen�rico, sendo necess�rio definir a mensagem de causa.
	 * @param message mensagem contendo informa��es sobre o que ocasionou a exce��o.
	 */

	public ErakinException(String message)
	{
		super(message);
	}

	/**
	 * Constr�i uma nova exce��o de g�nero gen�rico, sendo necess�rio definir a mensagem de causa.
	 * @param format string contendo uma formata��o de uma mensagem sobre o que ocasionou a exce��o.
	 * @param args argumentos respectivos a formata��o da mensagem para serem exibidos.
	 */

	public ErakinException(String format, Object... args)
	{
		super(format, args);
	}

	/**
	 * Constr�i uma nova exce��o de g�nero gen�rico, sendo necess�rio definir a mensagem de causa.
	 * Para esse caso em quest�o � necess�rio utilizar uma outra exce��o e usar sua mensagem.
	 * Mostra o nome da exce��o (classe) ao final da mensagem apenas se for outro tipo.
	 * @param e exce��o do qual dever� ser copiado a mensagem para essa nova exce��o.
	 */

	public ErakinException(Exception e)
	{
		super(e);
	}

	/**
	 * Constr�i uma nova exce��o de g�nero gen�rico, sendo necess�rio definir a mensagem de causa.
	 * Para esse caso em quest�o � necess�rio utilizar uma outra exce��o e usar sua mensagem.
	 * Mostra o nome da exce��o (classe) ao final da mensagem apenas se for outro tipo.
	 * @param e exce��o do qual dever� ser copiado a mensagem para essa nova exce��o.
	 * @param format string contendo uma formata��o de uma mensagem sobre o que ocasionou a exce��o.
	 * @param args argumentos respectivos a formata��o da mensagem para serem exibidos.
	 */

	public ErakinException(Exception e, String format, Object... args)
	{
		super(e, format, args);
	}
}
