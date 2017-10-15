package com.erakin.api.resources.world;

/**
 * <h1>Exce��o de Terreno</h1>
 *
 * <p>Todas as exce��es que possam ser geradas durante a utiliza��o de modelagens no engine ser�o deste tipo.
 * Esse tipo de exce��o por parda da biblioteca Erakin ser� gerada apenas em <code>com.eraking.resource.world</code>.</p>
 *
 * <p>Possui diversos construtores, que permitem uma melhor forma de determinar como ser� a mensagem.
 * Utiliza m�todos que permitem usar formata��o para definir a mensagem ou ent�o usar outra exce��o.
 * Podendo ainda usar uma combina��o dos dois tipos, usando uma mensagem formatada com uma exce��es.</p>
 *
 * @see WorldException
 *
 * @author Andrew Mello
 */

public class TerrainException extends WorldException
{
	/**
	 * Serializa��o para identifica��o da classe.
	 */
	private static final long serialVersionUID = 2926428005702877844L;

	/**
	 * Constr�i uma nova exce��o de origem dos terrenos, sendo necess�rio definir a mensagem de causa.
	 * @param message mensagem contendo informa��es sobre o que ocasionou a exce��o.
	 */

	public TerrainException(String message)
	{
		super(message);
	}

	/**
	 * Constr�i uma nova exce��o de origem dos terrenos, sendo necess�rio definir a mensagem de causa.
	 * @param format string contendo uma formata��o de uma mensagem sobre o que ocasionou a exce��o.
	 * @param args argumentos respectivos a formata��o da mensagem para serem exibidos.
	 */

	public TerrainException(String format, Object... args)
	{
		super(format, args);
	}

	/**
	 * Constr�i uma nova exce��o de origem dos terrenos, sendo necess�rio definir a mensagem de causa.
	 * Para esse caso em quest�o � necess�rio utilizar uma outra exce��o e usar sua mensagem.
	 * Mostra o nome da exce��o (classe) ao final da mensagem apenas se for outro tipo.
	 * @param e exce��o do qual dever� ser copiado a mensagem para essa nova exce��o.
	 */

	public TerrainException(Exception e)
	{
		super(e);
	}

	/**
	 * Constr�i uma nova exce��o de origem dos terrenos, sendo necess�rio definir a mensagem de causa.
	 * Para esse caso em quest�o � necess�rio utilizar uma outra exce��o e usar sua mensagem.
	 * Mostra o nome da exce��o (classe) ao final da mensagem apenas se for outro tipo.
	 * @param e exce��o do qual dever� ser copiado a mensagem para essa nova exce��o.
	 * @param format string contendo uma formata��o de uma mensagem sobre o que ocasionou a exce��o.
	 * @param args argumentos respectivos a formata��o da mensagem para serem exibidos.
	 */

	public TerrainException(Exception e, String format, Object... args)
	{
		super(e, format, args);
	}
}
