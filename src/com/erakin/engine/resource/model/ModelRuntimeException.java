package com.erakin.engine.resource.model;

import com.erakin.common.ErakinRuntimeException;

/**
 * <h1>Exceção de Modelagem</h1>
 *
 * <p>Todas as exceções que possam ser geradas durante a utilização de modelagens no engine serão deste tipo.
 * Esse tipo de exceção por parte da biblioteca Erakin será gerada apenas em <code>com.eraking.resource.model</code>.</p>
 *
 * <p>Possui diversos construtores, que permitem uma melhor forma de determinar como será a mensagem.
 * Utiliza métodos que permitem usar formatação para definir a mensagem ou então usar outra exceção.
 * Podendo ainda usar uma combinação dos dois tipos, usando uma mensagem formatada com uma exceções.</p>
 *
 * @see ErakinRuntimeException
 *
 * @author Andrew Mello
 */

public class ModelRuntimeException extends ErakinRuntimeException
{
	/**
	 * Serialização para identificação da classe.
	 */
	private static final long serialVersionUID = 2926428005702877844L;

	/**
	 * Constrói uma nova exceção para modelagem, sendo necessário definir a mensagem de causa.
	 * @param message mensagem contendo informações sobre o que ocasionou a exceção.
	 */

	public ModelRuntimeException(String message)
	{
		super(message);
	}

	/**
	 * Constrói uma nova exceção para modelagem, sendo necessário definir a mensagem de causa.
	 * @param format string contendo uma formatação de uma mensagem sobre o que ocasionou a exceção.
	 * @param args argumentos respectivos a formatação da mensagem para serem exibidos.
	 */

	public ModelRuntimeException(String format, Object... args)
	{
		super(format, args);
	}

	/**
	 * Constrói uma nova exceção para modelagem, sendo necessário definir a mensagem de causa.
	 * Para esse caso em questão é necessário utilizar uma outra exceção e usar sua mensagem.
	 * Mostra o nome da exceção (classe) ao final da mensagem apenas se for outro tipo.
	 * @param e exceção do qual deverá ser copiado a mensagem para essa nova exceção.
	 */

	public ModelRuntimeException(Exception e)
	{
		super(e);
	}

	/**
	 * Constrói uma nova exceção para modelagem, sendo necessário definir a mensagem de causa.
	 * Para esse caso em questão é necessário utilizar uma outra exceção e usar sua mensagem.
	 * Mostra o nome da exceção (classe) ao final da mensagem apenas se for outro tipo.
	 * @param e exceção do qual deverá ser copiado a mensagem para essa nova exceção.
	 * @param format string contendo uma formatação de uma mensagem sobre o que ocasionou a exceção.
	 * @param args argumentos respectivos a formatação da mensagem para serem exibidos.
	 */

	public ModelRuntimeException(Exception e, String format, Object... args)
	{
		super(e, format, args);
	}
}
