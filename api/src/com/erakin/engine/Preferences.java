package com.erakin.engine;

/**
 * <h1>Prefer�ncias</h1>
 *
 * <p>As prefer�ncias s�o utilizadas para que seja poss�vel determinar algumas configura��es prefer�ncias.
 * Essas configura��es prefer�ncias s�o importantes para o usu�rio devido a seus recursos de hardware.</p>
 *
 * <p>As vezes deixar o jogo com menor qualidade aumentando a performance pode ser vi�vel para o mesmo.
 * N�o s� esse tipo de prefer�ncias, mas como de jogabilidade, tamanho da tela, tela cheia e outros.
 * Para n�o determinar como de fato uma prefer�ncia deve ser, essa interface � usada como interm�dio.</p>
 *
 * <p>Possui getters e setters de acordo com o tipo de propriedade que ser� definido como prefer�ncia.
 * Assim � poss�vel que no engine seja poss�vel obter uma prefer�ncia em quanto no cliente uma defini��o.</p>
 *
 * @author Andrew Mello
 */

public interface Preferences
{
	/**
	 * Obt�m o valor de uma determinada prefer�ncia a partir do nome da mesma.
	 * @param key nome da prefer�ncia do qual deseja obter o valor em inteiro.
	 * @return valor respectivo a prefer�ncia desejada ou zero se inv�lida.
	 */

	int getOptionInt(String key);

	/**
	 * Permite definir o valor de uma determinada prefer�ncia a partir do nome da mesma.
	 * @param key nome da prefer�ncia do qual deseja definir o valor em int.
	 * @param value novo valor do qual a prefer�ncia acima dever� assumir.
	 */

	void setOptionInt(String key, int value);

	/**
	 * Obt�m o valor de uma determinada prefer�ncia a partir do nome da mesma.
	 * @param key nome da prefer�ncia do qual deseja obter o valor em flutuante.
	 * @return valor respectivo a prefer�ncia desejada ou zero se inv�lida.
	 */

	float getOptionFloat(String key);

	/**
	 * Permite definir o valor de uma determinada prefer�ncia a partir do nome da mesma.
	 * @param key nome da prefer�ncia do qual deseja definir o valor em float.
	 * @param value novo valor do qual a prefer�ncia acima dever� assumir.
	 */

	void setOptionFloat(String key, float value);

	/**
	 * Obt�m o valor de uma determinada prefer�ncia a partir do nome da mesma.
	 * @param key nome da prefer�ncia do qual deseja obter o valor em boolean.
	 * @return valor respectivo a prefer�ncia desejada ou zero se inv�lida.
	 */

	boolean getOptionBoolean(String key);

	/**
	 * Permite definir o valor de uma determinada prefer�ncia a partir do nome da mesma.
	 * @param key nome da prefer�ncia do qual deseja definir o valor em boolean.
	 * @param value novo valor do qual a prefer�ncia acima dever� assumir.
	 */

	void setOptionBoolean(String key, boolean value);

	/**
	 * Obt�m o valor de uma determinada prefer�ncia a partir do nome da mesma.
	 * @param key nome da prefer�ncia do qual deseja obter o valor em string.
	 * @return valor respectivo a prefer�ncia desejada ou false se inv�lida.
	 */

	String getOptionString(String key);

	/**
	 * Permite definir o valor de uma determinada prefer�ncia a partir do nome da mesma.
	 * @param key nome da prefer�ncia do qual deseja definir o valor em string.
	 * @param value novo valor do qual a prefer�ncia acima dever� assumir.
	 */

	void setOptionString(String key, String value);
}
