package com.erakin.engine;

import static com.erakin.api.ErakinAPIUtil.fatalError;
import static org.diverproject.log.LogSystem.logNotice;
import static org.diverproject.util.Util.nameOf;

import org.diverproject.util.collection.Node;

/**
 * <h1>Lista de Tarefas para Engine</h1>
 *
 * <p>Classe responsável por alinhar todas as tarefas que serão executadas pela {@link Engine}.
 * As tarefas são ordenadas na mesma ordem em que foram adicionadas a esta lista.
 * Uma tarefa será removida da lista quando encerrada ou um erro for originado ({@link Exception}).</p>
 *
 * <p>A finalidade das tarefas é executar procedimentos extras além dos mínimos necessários.
 * Por serem processadas dentro da {@link Engine}, estão sendo executados na Thread principal,
 * isso significa que elas podem executar ações que tenham comuniocação com o OpengGL.</p>
 *
 * @see Task
 *
 * @author Andrew
 */

public class EngineTaskList
{
	/**
	 * Nó que armazena a primeira tarefa que é nula (apenas para não perder a raíz).
	 */
	private Node<Task> taskRoot;

	/**
	 * Cria uma nova instância de uma lista que armazene as tarefas para a engine.
	 * Deve inicializar o nó de tarefas para que este possa ser processado.
	 */

	EngineTaskList()
	{
		taskRoot = new Node<Task>(null);
	}

	/**
	 * Chamado pela {@link Engine} para que atualize itere cada uma das tarefas e repasse o chamado de tick.
	 * @param delay quantidade de milissegundos que se passou desde o último quadro processado.
	 */

	void tick(long delay)
	{
		Node<Task> node = taskRoot;

		while (node != null)
		{
			Task task = node.get();

			if (task == null)
			{
				node = node.getNext();
				continue;
			}

			if (task.isOver())
			{
				removeNode(node);
				continue;
			}

			try {

				task.tick(delay);

			} catch (Exception e) {

				removeNode(node);
				fatalError(e, "Falha durante a execução da tarefa '%s'.", nameOf(task));
			}

			node = node.getNext();
		}
	}

	/**
	 * Procedimento usado internamente para remover um nó de tarefa do qual acabou ou resultou em erro.
	 * @param node referência do nó de tarefa do qual deve ser removida da lista de tarefas.
	 */

	private void removeNode(Node<Task> node)
	{
		if (node.getPrev() != null)
			node.getPrev().setNext(node.getNext());

		if (node.getNext() != null)
			node.getNext().setPrev(node.getPrev());
	}

	/**
	 * Tarefas permitem uma forma extra de manter partes da aplicação funcionando além das necessárias.
	 * Se a tarefa for inválida, ou seja, nula ou então tiver sido terminada não será adicionada.
	 * @param task tarefa do qual deseja enfileirar para ser processado.
	 */

	public void addTask(Task task)
	{
		synchronized (taskRoot)
		{
			taskRoot.set(task);

			Node<Task> node = new Node<Task>(null);
			Node.attach(node, taskRoot);

			taskRoot = node;

			logNotice("tarefa adicionada ao engine (%s).\n", nameOf(task));
		}		
	}

	/**
	 * Remove forçadamente uma tarefa mesmo que esta não tenha acabado.
	 * Uma vez que a tarefa seja removida está não será mais processada.
	 * @param task referência da tarefa do qual deseja não processar mais.
	 */

	public void removeTask(Task task)
	{
		synchronized (taskRoot)
		{
			Node<Task> node = taskRoot;

			if (node != null)
				do {

					if (node.get().equals(task))
					{
						removeNode(node);
						break;
					}

				} while ((node = node.getNext()) != null);
		}
	}
}
