package com.erakin.engine;

/**
 * <h1>Preferências</h1>
 *
 * <p>As preferências são utilizadas para que seja possível determinar algumas configurações preferências.
 * Essas configurações preferências são importantes para o usuário devido a seus recursos de hardware.</p>
 *
 * <p>As vezes deixar o jogo com menor qualidade aumentando a performance pode ser viável para o mesmo.
 * Não só esse tipo de preferências, mas como de jogabilidade, tamanho da tela, tela cheia e outros.
 * Para não determinar como de fato uma preferência deve ser, essa interface é usada como intermédio.</p>
 *
 * <p>Possui getters e setters de acordo com o tipo de propriedade que será definido como preferência.
 * Assim é possível que no engine seja possível obter uma preferência em quanto no cliente uma definição.</p>
 *
 * @author Andrew Mello
 */

public interface Preferences
{
	/**
	 * Obtém o valor de uma determinada preferência a partir do nome da mesma.
	 * @param key nome da preferência do qual deseja obter o valor em inteiro.
	 * @return valor respectivo a preferência desejada ou zero se inválida.
	 */

	int getOptionInt(String key);

	/**
	 * Permite definir o valor de uma determinada preferência a partir do nome da mesma.
	 * @param key nome da preferência do qual deseja definir o valor em int.
	 * @param value novo valor do qual a preferência acima deverá assumir.
	 */

	void setOptionInt(String key, int value);

	/**
	 * Obtém o valor de uma determinada preferência a partir do nome da mesma.
	 * @param key nome da preferência do qual deseja obter o valor em flutuante.
	 * @return valor respectivo a preferência desejada ou zero se inválida.
	 */

	float getOptionFloat(String key);

	/**
	 * Permite definir o valor de uma determinada preferência a partir do nome da mesma.
	 * @param key nome da preferência do qual deseja definir o valor em float.
	 * @param value novo valor do qual a preferência acima deverá assumir.
	 */

	void setOptionFloat(String key, float value);

	/**
	 * Obtém o valor de uma determinada preferência a partir do nome da mesma.
	 * @param key nome da preferência do qual deseja obter o valor em boolean.
	 * @return valor respectivo a preferência desejada ou zero se inválida.
	 */

	boolean getOptionBoolean(String key);

	/**
	 * Permite definir o valor de uma determinada preferência a partir do nome da mesma.
	 * @param key nome da preferência do qual deseja definir o valor em boolean.
	 * @param value novo valor do qual a preferência acima deverá assumir.
	 */

	void setOptionBoolean(String key, boolean value);

	/**
	 * Obtém o valor de uma determinada preferência a partir do nome da mesma.
	 * @param key nome da preferência do qual deseja obter o valor em string.
	 * @return valor respectivo a preferência desejada ou false se inválida.
	 */

	String getOptionString(String key);

	/**
	 * Permite definir o valor de uma determinada preferência a partir do nome da mesma.
	 * @param key nome da preferência do qual deseja definir o valor em string.
	 * @param value novo valor do qual a preferência acima deverá assumir.
	 */

	void setOptionString(String key, String value);
}
