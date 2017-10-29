package com.erakin.api.resources;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.collection.FolderElement;
import org.diverproject.util.collection.Node;

/**
 * <h1>Recurso Raíz</h1>
 *
 * <p>Para cada tipo de recurso existente para ser trabalhado no engine,
 * irá existir um recurso raíz para essa, sendo especificado o que deverá armazenar.
 * As raízes devem guardar informações diretas do recurso do qual irá apontar.</p>
 *
 * <p>A ideia na utilização da raíz para os recursos, é garantir que esse recurso
 * fique na memória apenas em quanto esse estiver sendo utilizado pelo engine.
 * No momento em que não houver mais referências do mesmo, esse será excluído.</p>
 *
 * <p>A referência funciona da seguinte forma, toda raíz permite criar uma instância de
 * um recurso utilizável, quando esse recurso para de ser usado e liberado do sistema,
 * a raíz tem como função verificar se há outras referências (recursos usando essa raíz).</p>
 *
 * <p>Inicialmente não irá possuir nenhuma referência, portanto irá viver na memória
 * durante um determinado tempo, se após um tempo não houver nenhuma referência criada,
 * será dito ao gerenciador de recursos para remover esse recurso por inutilizável.</p>
 *
 * <p>Em quanto houver ao menos uma referência em memória a raíz nunca será removida.
 * Para toda raíz há uma código de identificação que irá permitir localizar o mesmo.
 * Assim, é possível localizar o recurso no sistema para gerenciamento dos recursos.</p>
 *
 * @author Andrew
 */

public abstract class ResourceRoot<T extends Resource<?>> implements FolderElement
{
	/**
	 * Quantos milissegundos um recurso raíz deve ficar vivo no sistema.
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
	 * Caminho do recurso em disco sem considerar o diretório para recursos.
	 */
	String filePath;

	/**
	 * Sistema para identificação de operações com recursos.
	 */
	ResourceListener listener;

	/**
	 * Lista que deverá guardar todas as referências dos recursos instanciados.
	 */
	private Node<T> references;

	/**
	 * Quantidade de referências que já foram criadas.
	 */
	private int referenceCount;

	/**
	 * Quando foi a última vez que esse recurso foi verificado.
	 */
	private long last;

	/**
	 * Constrói um novo recurso raíz para que recursos possam ser instanciados.
	 * @param filePath referência do arquivo em disco que será considerado.
	 */

	ResourceRoot()
	{
		
	}

	/**
	 * Caminho do arquivo determina sua localização em disco contendo os dados do recurso.
	 * @return aquisição do caminho sem considerar o diretório para recursos.
	 */

	public String getFilePath()
	{
		if (fileExtension.isEmpty())
			return String.format("%s/%s", filePath, fileName);

		return String.format("%s/%s.%s", filePath, fileName, fileExtension);
	}

	/**
	 * Adiciona uma nova referência de recurso instanciado pra essa raíz.
	 * @param resource referência do recurso do qual foi instanciado.
	 * @return true se conseguir adicionar ou false se já tiver sido adicionado.
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
	 * Exclui uma referência a partir de um determinado recurso especificado.
	 * Caso não haja mais referências esse recurso será removido do sistema.
	 * @param resource referência do recurso para remover uma referência.
	 * @return true se conseguir remover ou false caso contrário.
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
	 * Contagem da referência dos recursos é usado para garantir que recursos sejam liberados.
	 * Essa liberação é feita apenas quando uma raíz não estiver sendo mais utilizada.
	 * Assim quando não estiver mais sendo usado, será liberado da memória.
	 * @return aquisição da quantidade de recursos que utilizam essa raíz.
	 */

	public int getReferenceCount()
	{
		return referenceCount;
	}

	/**
	 * Permite gerar um novo recurso que terá como fonte esse recurso raíz.
	 * @return aquisição do recurso gerado a partir dessa raíz.
	 */

	public abstract T genResource();

	/**
	 * Faz a liberação do recurso raíz, chamando todas as referências para serem liberadas.
	 * Esse método só deve ser chamado quando o recurso não for mais utilizado.
	 * Pois uma vez que ele seja liberado, para ser usado terá de ser iniciado novamente.
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
	 * @param delay quantos milissegundos se passaram desde a última atualização.
	 */

	void update(long delay)
	{
		if (getReferenceCount() > 0)
			last = 0;

		else
			last += delay;
	}

	/**
	 * Verifica se esse recurso raíz ainda está dentro do seu tempo de vida limite.
	 * Chamado pelo gerenciador de recursos para verificar se o recurso deve ou não
	 * ser removido da lista de seus recursos por tempo de inatividade no sistema.
	 * @return true se estiver dentro do tempo ou false caso contrário.
	 */

	boolean isAlive()
	{
		return last < RESOURCE_LIVE_TIME;
	}

	/**
	 * Procedimento chamado por toString a fim de preencher adequadamente o mesmo.
	 * O posicionamento do conteúdo é sempre feito apenas ao final da descrição.
	 * @param description referência da descrição desse objeto que será usado.
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
