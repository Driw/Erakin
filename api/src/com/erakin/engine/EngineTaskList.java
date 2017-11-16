package com.erakin.engine;

import static com.erakin.api.ErakinAPIUtil.fatalError;
import static org.diverproject.log.LogSystem.logNotice;
import static org.diverproject.util.Util.nameOf;

import org.diverproject.util.collection.Node;

/**
 * <h1>Lista de Tarefas para Engine</h1>
 *
 * <p>Classe respons�vel por alinhar todas as tarefas que ser�o executadas pela {@link Engine}.
 * As tarefas s�o ordenadas na mesma ordem em que foram adicionadas a esta lista.
 * Uma tarefa ser� removida da lista quando encerrada ou um erro for originado ({@link Exception}).</p>
 *
 * <p>A finalidade das tarefas � executar procedimentos extras al�m dos m�nimos necess�rios.
 * Por serem processadas dentro da {@link Engine}, est�o sendo executados na Thread principal,
 * isso significa que elas podem executar a��es que tenham comunioca��o com o OpengGL.</p>
 *
 * @see Task
 *
 * @author Andrew
 */

public class EngineTaskList
{
	/**
	 * N� que armazena a primeira tarefa que � nula (apenas para n�o perder a ra�z).
	 */
	private Node<Task> taskRoot;

	/**
	 * Cria uma nova inst�ncia de uma lista que armazene as tarefas para a engine.
	 * Deve inicializar o n� de tarefas para que este possa ser processado.
	 */

	EngineTaskList()
	{
		taskRoot = new Node<Task>(null);
	}

	/**
	 * Chamado pela {@link Engine} para que atualize itere cada uma das tarefas e repasse o chamado de tick.
	 * @param delay quantidade de milissegundos que se passou desde o �ltimo quadro processado.
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
				fatalError(e, "Falha durante a execu��o da tarefa '%s'.", nameOf(task));
			}

			node = node.getNext();
		}
	}

	/**
	 * Procedimento usado internamente para remover um n� de tarefa do qual acabou ou resultou em erro.
	 * @param node refer�ncia do n� de tarefa do qual deve ser removida da lista de tarefas.
	 */

	private void removeNode(Node<Task> node)
	{
		if (node.getPrev() != null)
			node.getPrev().setNext(node.getNext());

		if (node.getNext() != null)
			node.getNext().setPrev(node.getPrev());
	}

	/**
	 * Tarefas permitem uma forma extra de manter partes da aplica��o funcionando al�m das necess�rias.
	 * Se a tarefa for inv�lida, ou seja, nula ou ent�o tiver sido terminada n�o ser� adicionada.
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
	 * Remove for�adamente uma tarefa mesmo que esta n�o tenha acabado.
	 * Uma vez que a tarefa seja removida est� n�o ser� mais processada.
	 * @param task refer�ncia da tarefa do qual deseja n�o processar mais.
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
