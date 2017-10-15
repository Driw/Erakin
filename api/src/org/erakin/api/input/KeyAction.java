package org.erakin.api.input;

/**
 * <h1>A��o de Tecla</h1>
 *
 * <p>Essa classe � utilizada para criar objetos contendo informa��es sobre a a��o ocorrida em uma tecla.
 * A ideia � permitir que o teclado virtual possa remover as a��es ocorrida das listas sempre a cada tick.
 * Por�m se isso for feito a a��o de um clique pode n�o funcionar sempre, portanto precisa ter uma dura��o.</p>
 *
 * <p>Esse objeto quando instanciado define o tempo de inicio e quando obtido o valor do ta tecla,
 * ir� determinar este como consumido. Enquanto o clique n�o for consumido ou n�o houver passado
 * um tempo determinado (padr�o: 5 milissegundos) este n�o ser� removido da lista de cliques.</p>
 *
 * @author Andrew
 */

public class KeyAction
{
	/**
	 * Determina se o clique j� foi consumido.
	 */
	private boolean consume;

	/**
	 * C�digo do bot�o/tecla que foi clicado (criou este clique).
	 */
	private final int key;

	/**
	 * Hor�rio em que este objeto foi criado para efetuar dura��o limite.
	 */
	private final long create;

	/**
	 * Constr�i um novo clique para bot�o sendo necess�rio passar o c�digo do bot�o clicado.
	 * @param key c�digo do bot�o que foi clicado para verificar cliques.
	 */

	public KeyAction(int key)
	{
		this.key = key;
		this.create = System.currentTimeMillis();
	}

	/**
	 * Verifica se a tecla dessa a��o � igual ao c�digo de uma tecla especificada.
	 * @param key c�digo da tecla do qual est� tentando verificar a se � igual.
	 * @return true se for igual e ir� consumir ou false caso contr�rio.
	 */

	public boolean isKey(int key)
	{
		if (this.key == key)
		{
			consume = true;
			return true;
		}

		return false;
	}

	/**
	 * Verifica se o tempo de dura��o deste clique do mouse/teclado j� se esgotou.
	 * O tempo se esgota quando o clique for consumido ou passar 5 milissegundos.
	 * @return true se j� tiver se esgotado ou false caso esteja v�lido.
	 */

	public boolean isOver()
	{
		return consume || System.currentTimeMillis() - create > 200;
	}
}
