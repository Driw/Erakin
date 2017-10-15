package org.erakin.api.input;

/**
 * <h1>Ação de Tecla</h1>
 *
 * <p>Essa classe é utilizada para criar objetos contendo informações sobre a ação ocorrida em uma tecla.
 * A ideia é permitir que o teclado virtual possa remover as ações ocorrida das listas sempre a cada tick.
 * Porém se isso for feito a ação de um clique pode não funcionar sempre, portanto precisa ter uma duração.</p>
 *
 * <p>Esse objeto quando instanciado define o tempo de inicio e quando obtido o valor do ta tecla,
 * irá determinar este como consumido. Enquanto o clique não for consumido ou não houver passado
 * um tempo determinado (padrão: 5 milissegundos) este não será removido da lista de cliques.</p>
 *
 * @author Andrew
 */

public class KeyAction
{
	/**
	 * Determina se o clique já foi consumido.
	 */
	private boolean consume;

	/**
	 * Código do botão/tecla que foi clicado (criou este clique).
	 */
	private final int key;

	/**
	 * Horário em que este objeto foi criado para efetuar duração limite.
	 */
	private final long create;

	/**
	 * Constrói um novo clique para botão sendo necessário passar o código do botão clicado.
	 * @param key código do botão que foi clicado para verificar cliques.
	 */

	public KeyAction(int key)
	{
		this.key = key;
		this.create = System.currentTimeMillis();
	}

	/**
	 * Verifica se a tecla dessa ação é igual ao código de uma tecla especificada.
	 * @param key código da tecla do qual está tentando verificar a se é igual.
	 * @return true se for igual e irá consumir ou false caso contrário.
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
	 * Verifica se o tempo de duração deste clique do mouse/teclado já se esgotou.
	 * O tempo se esgota quando o clique for consumido ou passar 5 milissegundos.
	 * @return true se já tiver se esgotado ou false caso esteja válido.
	 */

	public boolean isOver()
	{
		return consume || System.currentTimeMillis() - create > 200;
	}
}
