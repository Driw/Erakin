package com.erakin.api.resources;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.collection.FolderElement;
import org.diverproject.util.collection.Node;

/**
 * <h1>Recurso Ra�z</h1>
 *
 * <p>Para cada tipo de recurso existente para ser trabalhado no engine,
 * ir� existir um recurso ra�z para essa, sendo especificado o que dever� armazenar.
 * As ra�zes devem guardar informa��es diretas do recurso do qual ir� apontar.</p>
 *
 * <p>A ideia na utiliza��o da ra�z para os recursos, � garantir que esse recurso
 * fique na mem�ria apenas em quanto esse estiver sendo utilizado pelo engine.
 * No momento em que n�o houver mais refer�ncias do mesmo, esse ser� exclu�do.</p>
 *
 * <p>A refer�ncia funciona da seguinte forma, toda ra�z permite criar uma inst�ncia de
 * um recurso utiliz�vel, quando esse recurso para de ser usado e liberado do sistema,
 * a ra�z tem como fun��o verificar se h� outras refer�ncias (recursos usando essa ra�z).</p>
 *
 * <p>Inicialmente n�o ir� possuir nenhuma refer�ncia, portanto ir� viver na mem�ria
 * durante um determinado tempo, se ap�s um tempo n�o houver nenhuma refer�ncia criada,
 * ser� dito ao gerenciador de recursos para remover esse recurso por inutiliz�vel.</p>
 *
 * <p>Em quanto houver ao menos uma refer�ncia em mem�ria a ra�z nunca ser� removida.
 * Para toda ra�z h� uma c�digo de identifica��o que ir� permitir localizar o mesmo.
 * Assim, � poss�vel localizar o recurso no sistema para gerenciamento dos recursos.</p>
 *
 * @author Andrew
 */

public abstract class ResourceRoot<T extends Resource<?>> implements FolderElement
{
	/**
	 * Quantos milissegundos um recurso ra�z deve ficar vivo no sistema.
	 */
	public static final int RESOURCE_LIVE_TIME = 120000;

	/**
	 * Tipo do arquivo.
	 */
	String fileExtension;

	/**
	 * Nome do recurso do qual foi carregado.
	 */
	String fileName;

	/**
	 * Caminho do recurso em disco sem considerar o diret�rio para recursos.
	 */
	String filePath;

	/**
	 * Sistema para identifica��o de opera��es com recursos.
	 */
	ResourceListener listener;

	/**
	 * Lista que dever� guardar todas as refer�ncias dos recursos instanciados.
	 */
	private Node<T> references;

	/**
	 * Quantidade de refer�ncias que j� foram criadas.
	 */
	private int referenceCount;

	/**
	 * Quando foi a �ltima vez que esse recurso foi verificado.
	 */
	private long last;

	/**
	 * Constr�i um novo recurso ra�z para que recursos possam ser instanciados.
	 * @param filePath refer�ncia do arquivo em disco que ser� considerado.
	 */

	ResourceRoot()
	{
		
	}

	/**
	 * Caminho do arquivo determina sua localiza��o em disco contendo os dados do recurso.
	 * @return aquisi��o do caminho sem considerar o diret�rio para recursos.
	 */

	public String getFilePath()
	{
		if (fileExtension.isEmpty())
			return String.format("%s/%s", filePath, fileName);

		return String.format("%s/%s.%s", filePath, fileName, fileExtension);
	}

	/**
	 * Adiciona uma nova refer�ncia de recurso instanciado pra essa ra�z.
	 * @param resource refer�ncia do recurso do qual foi instanciado.
	 * @return true se conseguir adicionar ou false se j� tiver sido adicionado.
	 */

	boolean addReference(T resource)
	{
		if (resource.root != this)
			return false;

		if (references == null)
			references = new Node<T>(resource);

		else
		{
			Node<T> node = new Node<T>(resource);
			Node.attach(node, references);

			references = node;
		}

		return true;
	}

	/**
	 * Exclui uma refer�ncia a partir de um determinado recurso especificado.
	 * Caso n�o haja mais refer�ncias esse recurso ser� removido do sistema.
	 * @param resource refer�ncia do recurso para remover uma refer�ncia.
	 * @return true se conseguir remover ou false caso contr�rio.
	 */

	boolean delReference(T resource)
	{
		if (references == null)
			return false;

		if (references.get() == resource)
		{
			references = null;
			return true;
		}

		Node<T> node = references;

		while (node.get() != resource)
			node = node.getNext();

		if (node.get() == resource)
			return Node.attach(node.getPrev(), node.getNext());

		return false;
	}

	/**
	 * Contagem da refer�ncia dos recursos � usado para garantir que recursos sejam liberados.
	 * Essa libera��o � feita apenas quando uma ra�z n�o estiver sendo mais utilizada.
	 * Assim quando n�o estiver mais sendo usado, ser� liberado da mem�ria.
	 * @return aquisi��o da quantidade de recursos que utilizam essa ra�z.
	 */

	public int getReferenceCount()
	{
		return referenceCount;
	}

	/**
	 * Permite gerar um novo recurso que ter� como fonte esse recurso ra�z.
	 * @return aquisi��o do recurso gerado a partir dessa ra�z.
	 */

	public abstract T genResource();

	/**
	 * Faz a libera��o do recurso ra�z, chamando todas as refer�ncias para serem liberadas.
	 * Esse m�todo s� deve ser chamado quando o recurso n�o for mais utilizado.
	 * Pois uma vez que ele seja liberado, para ser usado ter� de ser iniciado novamente.
	 */

	public void release()
	{
		if (listener != null)
			listener.resourceRelease();

		if (references != null)
			for (T reference : references)
				reference.release();
	}

	/**
	 * Procedimento chamado pelo gerenciador de recursos para garantir o tempo de vida.
	 * @param delay quantos milissegundos se passaram desde a �ltima atualiza��o.
	 */

	void update(long delay)
	{
		if (getReferenceCount() > 0)
			last = 0;

		else
			last += delay;
	}

	/**
	 * Verifica se esse recurso ra�z ainda est� dentro do seu tempo de vida limite.
	 * Chamado pelo gerenciador de recursos para verificar se o recurso deve ou n�o
	 * ser removido da lista de seus recursos por tempo de inatividade no sistema.
	 * @return true se estiver dentro do tempo ou false caso contr�rio.
	 */

	boolean isAlive()
	{
		return last < RESOURCE_LIVE_TIME;
	}

	/**
	 * Procedimento chamado por toString a fim de preencher adequadamente o mesmo.
	 * O posicionamento do conte�do � sempre feito apenas ao final da descri��o.
	 * @param description refer�ncia da descri��o desse objeto que ser� usado.
	 */

	public abstract void toString(ObjectDescription description);

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("path", String.format("%s/%s.%s", filePath, fileName, fileExtension));
		description.append("references", getReferenceCount());

		toString(description);

		return description.toString();
	}
}
