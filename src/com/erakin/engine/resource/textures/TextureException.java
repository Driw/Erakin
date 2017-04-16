package com.erakin.engine.resource.textures;

import com.erakin.common.ErakinException;

/**
 * <h1>Exce��o de Textura</h1>
 *
 * <p>Todas as exce��es que possam ser geradas durante a utiliza��o de texturas no engine ser�o deste tipo.
 * Esse tipo de exce��o por parte da biblioteca Erakin ser� gerada apenas em <code>com.eraking.resource.texture</code>.</p>
 *
 * <p>Possui diversos construtores, que permitem uma melhor forma de determinar como ser� a mensagem.
 * Utiliza m�todos que permitem usar formata��o para definir a mensagem ou ent�o usar outra exce��o.
 * Podendo ainda usar uma combina��o dos dois tipos, usando uma mensagem formatada com uma exce��es.</p>
 *
 * @see ErakinException
 *
 * @author Andrew Mello
 */

public class TextureException extends ErakinException
{
	/**
	 * Serializa��o para identifica��o da classe.
	 */
	private static final long serialVersionUID = 2926428005702877844L;

	/**
	 * Constr�i uma nova exce��o para textura, sendo necess�rio definir a mensagem de causa.
	 * @param message mensagem contendo informa��es sobre o que ocasionou a exce��o.
	 */

	public TextureException(String message)
	{
		super(message);
	}

	/**
	 * Constr�i uma nova exce��o para textura, sendo necess�rio definir a mensagem de causa.
	 * @param format string contendo uma formata��o de uma mensagem sobre o que ocasionou a exce��o.
	 * @param args argumentos respectivos a formata��o da mensagem para serem exibidos.
	 */

	public TextureException(String format, Object... args)
	{
		super(format, args);
	}

	/**
	 * Constr�i uma nova exce��o para textura, sendo necess�rio definir a mensagem de causa.
	 * Para esse caso em quest�o � necess�rio utilizar uma outra exce��o e usar sua mensagem.
	 * Mostra o nome da exce��o (classe) ao final da mensagem apenas se for outro tipo.
	 * @param e exce��o do qual dever� ser copiado a mensagem para essa nova exce��o.
	 */

	public TextureException(Exception e)
	{
		super(e);
	}

	/**
	 * Constr�i uma nova exce��o para textura, sendo necess�rio definir a mensagem de causa.
	 * Para esse caso em quest�o � necess�rio utilizar uma outra exce��o e usar sua mensagem.
	 * Mostra o nome da exce��o (classe) ao final da mensagem apenas se for outro tipo.
	 * @param e exce��o do qual dever� ser copiado a mensagem para essa nova exce��o.
	 * @param format string contendo uma formata��o de uma mensagem sobre o que ocasionou a exce��o.
	 * @param args argumentos respectivos a formata��o da mensagem para serem exibidos.
	 */

	public TextureException(Exception e, String format, Object... args)
	{
		super(e, format, args);
	}
}
