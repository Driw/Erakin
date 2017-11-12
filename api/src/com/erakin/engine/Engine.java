package com.erakin.engine;

import static com.erakin.api.ErakinAPIUtil.fatalError;
import static com.erakin.api.ErakinAPIUtil.nameOf;
import static org.diverproject.log.LogSystem.logException;
import static org.diverproject.log.LogSystem.logNotice;
import static org.diverproject.util.MessageUtil.showException;
import static org.diverproject.util.service.SystemBase.PROPERTIE_USE_LOG;

import javax.swing.UIManager;

import org.diverproject.jni.input.InputException;
import org.diverproject.jni.input.InputSystem;
import org.diverproject.log.LogPreferences;
import org.diverproject.log.LogSystem;
import org.diverproject.util.ObjectDescription;
import org.diverproject.util.TimerTick;
import org.diverproject.util.UtilException;
import org.diverproject.util.collection.Node;
import org.diverproject.util.service.LibrarySystem;
import org.diverproject.util.service.ServiceSystem;
import org.lwjgl.opengl.Display;

import com.erakin.api.ErakinException;
import com.erakin.api.input.InputManager;
import com.erakin.api.resources.model.ModelLoader;
import com.erakin.api.resources.texture.TextureLoader;
import com.erakin.api.resources.world.WorldLoader;
import com.erakin.engine.render.RendererManager;
import com.erakin.engine.scene.SceneManager;

/**
 * <h1>Engine</h1>
 *
 * <p>Núcleo do engine, faz a inicialização de diversos serviços dos sistema e bibliotecas.
 * Garante que toda a base do sistema seja carregada e iniciada adequadamente para ser usado.
 * Responsável principalmente por processar os ticks e determinar o intervalo entre eles.</p>
 *
 * <p>Nele também é onde pode ser definido alguns gerenciadores como o de cenários e renderização.
 * Permite adicionar tarefas para serem executadas a cada tick que for processado com o delay.
 * Utiliza algumas bibliotecas e inicializa estas: JavaUtil Primitive, JavaUtil Log e LWJGL.</p>
 *
 * @see Tickable
 * @see SceneManager
 * @see RendererManager
 * @see Task
 *
 * @author Andrew Mello
 */

public class Engine implements Tickable
{
	/**
	 * Instância para núcleo do engine no padrão de projetos Singleton.
	 */
	private static final Engine INSTANCE = new Engine();


	/**
	 * Título dado par a aplicação.
	 */
	private String title;

	/**
	 * Determina se o engine está rodando.
	 */
	private boolean running;

	/**
	 * Usado para obter o intervalo entre cada quadro.
	 */
	private TimerTick timerTick;

	/**
	 * Gerenciador de cenários usado para chamar atualizações e renderizações.
	 */
	private SceneManager sceneManager;

	/**
	 * Gerenciador de renderização para chamar as renderizações necessárias.
	 */
	private RendererManager rendererManager;

	/**
	 * Listener para permitir dinâmica em relação a execução da engine.
	 */
	private EngineListener listener;

	/**
	 * Nó que armazena a primeira tarefa que é nula (apenas para não perder a raíz).
	 */
	private Node<Task> taskRoot;

	/**
	 * Construtor privado para atender ao padrão de projetos singleton.
	 * Primeiramente define o estilo gráfico da janela com o sistema operacional.
	 * Depois deve garantir o carregamento das bibliotecas necessárias adequadamente.
	 * Inicialização do sistema para gerenciamento de serviços e o de entrada.
	 */

	private Engine()
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			logException(e);
		}

		taskRoot = new Node<Task>(null);

		loadLogSystem();

		LibrarySystem librarySystem = LibrarySystem.getInstance();
		librarySystem.setPropertie(PROPERTIE_USE_LOG, true);
		librarySystem.setDirectory("system/libraries/native");
		librarySystem.bind("lwjgl", "org.lwjgl.librarypath");
		librarySystem.bind("jniInput", "org.diverproject.librarypath");

		ServiceSystem serviceSystem = ServiceSystem.getInstance();
		serviceSystem.setPropertie(PROPERTIE_USE_LOG, true);

		InputManager.setDefaultProperties();
	}

	/**
	 * Carregamento e inicialização do serviço para registro de eventos no sistema.
	 */

	private void loadLogSystem()
	{
		LogPreferences.setFile("system/log.txt");
		LogPreferences.setUseDebug(true);
		LogPreferences.setUseWarning(true);
		LogPreferences.setUseError(true);
		LogPreferences.setUseException(true);
		LogSystem.initialize();
	}

	/**
	 * Deve inicializar o núcleo do engine para manter o mesmo funcionando.
	 * A partir daqui o loop será feito, calculando taxa de quadros por segundo,
	 * chamando atualizações, renderizações, exibir a janela e iniciar serviços.
	 * @throws ErakinException gerenciadores não definidos, falha na exibição,
	 * inicialização do serviço inválida ou problema durante a execução da aplicação.
	 */

	public void initiate() throws ErakinException
	{
		if (sceneManager == null)
			throw new ErakinException("gerenciador de cenários não definido");

		if (rendererManager == null)
			throw new ErakinException("gerenciador de renderização não definido");

		logNotice("engine iniciado.\n");

		try {

			initiateDisplay();
			InputManager.setDefaultProperties();
			InputManager.initiateInput();
			initiateLoop();

		} catch (UtilException e) {
			throw new ErakinException(e);
		}

		DisplayManager display = DisplayManager.getInstance();
		display.close();

		logNotice("engine encerrado.\n");
	}

	/**
	 * Ao ser chamado carrega as configurações de exibições a partir das preferências de vídeo.
	 * As preferências de vídeo podem ser encontradas na classe de configurações para preferências.
	 * @throws ErakinException apenas se houver falha na inicialização da exibição da tela.
	 * @see PreferencesSettings
	 */

	private void initiateDisplay() throws ErakinException
	{
		Preferences preferences = PreferencesSettings.getVideoPreferences();

		DisplaySettings settings = new DisplaySettings();
		settings.setPreferences(preferences);
		settings.setTitle(title);

		DisplayManager display = DisplayManager.getInstance();
		display.create(settings);
	}

	/**
	 * A partir desse momento, o núcleo do engine estará ativo e em funcionamento iniciando o seu loop.
	 * Determina a aplicação como rodando, começa a contagem de quadros por segundo, atualizações e renderizações.
	 */

	private void initiateLoop()
	{
		running = true;
		timerTick = new TimerTick(1);
		listener.onInitiate();

		DisplayManager display = DisplayManager.getInstance();

		try {

			while (running && !Display.isCloseRequested())
			{
				display.waitTick();
				display.update();

				tick();
			}

		} catch (Exception e) {

			logException(e);
			showException(e);
		}

		try {
			shutdown();
		} catch (InputException ie) {
			showException(ie);
			System.exit(0);
		}

		listener.onClosed();
	}

	/**
	 * Chamado em quanto a aplicação estiver em execução, representa um único quadro executado por segundo.
	 * Deverá garantir que todas as tarefas como os serviços e objetos existentes sejam atualizados e renderizados.
	 */

	private void tick()
	{
		long delay = timerTick.getTicks();

		if (listener != null)
			listener.tick(delay);

		update(delay);
		render(delay);

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
				if (node.getPrev() != null)
					node.getPrev().setNext(node.getNext());

				if (node.getNext() != null)
					node.getNext().setPrev(node.getPrev());

				continue;
			}

			try {

				task.tick(delay);

			} catch (Exception e) {

				if (node.getPrev() != null)
					node.getPrev().setNext(node.getNext());

				if (node.getNext() != null)
					node.getNext().setPrev(node.getPrev());

				fatalError(e, "Falha durante a execução da tarefa '%s'.", nameOf(task));
			}

			node = node.getNext();
		}

		ServiceSystem.getInstance().update(delay);
		TextureLoader.getInstance().update(delay);
		ModelLoader.getInstance().update(delay);
		WorldLoader.getInstance().update(delay);
	}

	@Override
	public void update(long delay)
	{
		try {

			if (!rendererManager.isInitiate())
				rendererManager.initiate();

			ProjectionMatrix.getInstance().update();
			sceneManager.update(delay);
			rendererManager.update(delay);

		} catch (Exception e) {
			fatalError(e, "Falha durante a atualização do engine:");
		}
	}

	@Override
	public void render(long delay)
	{
		try {

			sceneManager.render(delay);
			rendererManager.render(delay);

		} catch (Exception e) {
			fatalError(e, "Falha durante a renderização do engine:");
		}
	}

	/**
	 * Uma vez que seja chamado, desencadeia diversos procedimentos seguidos para desligar serviços.
	 * Além de desligar os serviços, salva os dados necessários e desliga a aplicação como deve ser.
	 * @throws InputException apenas se houver falha na finalização do serviço de entrada.
	 */

	public void shutdown() throws InputException
	{
		DisplayManager.getInstance().close();
		ServiceSystem.getInstance().shutdown();
		InputSystem.getInstance().shutdown();

		rendererManager.cleanup();
		listener.onShutdown();

		System.exit(0);
	}

	/**
	 * Uma vez chamado pede para que o cliente para de rodar, isso significa que ele deve ser fechado.
	 */

	public void requestToClose()
	{
		running = false;
	}

	/**
	 * Tarefas permitem uma forma extra de manter partes da aplicação funcionando além das necessárias.
	 * Se a tarefa for inválida, ou seja, nula ou então tiver sido terminada não será adicioanda.
	 * @param task tarefa do qual deseja enfileirar para ser processado pelo engine.
	 */

	public void addTask(Task task)
	{
		if (task == null || task.isOver())
			return;

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
	 * Define um listener para que determinados procedimentos sejam executados durante a engine.
	 * Por exemplo, é necessário executar procedimentos após o OpenGL iniciar mas antes de renderizar.
	 * @param listener referência do objeto que implementa o listener com os procedimentos a executar.
	 */

	public void setListener(EngineListener listener)
	{
		this.listener = listener;
	}

	/**
	 * A aplicação será considerada inicializada quando o temporizador de ticks for iniciado.
	 * @return true se a aplicação tiver sido iniciada ou false caso contrário.
	 */

	public boolean isInitialized()
	{
		return timerTick != null;
	}

	/**
	 * Título é usado para determinar o texto que será exibido na barra superior da janela.
	 * @return aquisição do título da janela atualmente utilizado.
	 */

	public String getTitle()
	{
		return title;
	}

	/**
	 * Permite definir qual deve ser o novo texto a ser exibido na barra superior da janela.
	 * Se a janela já tiver sido exibida, apenas atualiza o nome caso contrário aguarda.
	 * @param title novo título da janela do qual deverá ser utilizado.
	 */

	public void setTitle(String title)
	{
		this.title = title;

		if (isInitialized())
			Display.setTitle(title);
	}

	/**
	 * Gerenciador de cenários é usado para que garanta aos cenários serem atualizados e renderizados.
	 * Isso também permite uma variação na forma como os cenários podem ser utilizados.
	 * @return aquisição do gerenciador de cenários atualmente utilizado.
	 */

	public SceneManager getSceneManager()
	{
		return sceneManager;
	}

	/**
	 * Toda aplicação deve definir um gerenciador de cenários, o engine oferece um padrão.
	 * É recomendável que seja utilizado o padrão com modificações de acordo com a aplicação.
	 * @param sceneManager referência do novo gerenciador de cenários do qual deve ser usado.
	 */

	public void setSceneManager(SceneManager sceneManager)
	{
		this.sceneManager = sceneManager;
	}

	/**
	 * Gerenciador de renderização é usado para determinar como e o que será renderizado na tela.
	 * Isso também permite ua variação na forma como elas serão feitas ou o que será exibido.
	 * @return aquisição do gerenciador de renderização atualmente utilizado.
	 */

	public RendererManager getRendererManger()
	{
		return rendererManager;
	}

	/**
	 * Toda aplicação deve definir um gerenciador de renderização, o engine oferece um padrão.
	 * É recomendável que seja utilizado o padrão com modificações de acordo com a aplicação.
	 * @param renderManger referência do novo gerenciador de renderização do qual deve ser usado.
	 */

	public void setRenderManger(RendererManager renderManger)
	{
		this.rendererManager = renderManger;
	}

	/**
	 * Uma quantidade de ticks (unidade de tempo) é calculada para cada quadro processado.
	 * Esse valor é repassado por diversos objetos para atualizar os dados e renderizar o jogo.
	 * Sempre que o valor é calculado, ele é armazenado em uma contagem acumulativa de ticks.
	 * @return aquisição do tempo em que a Engine está rodando em ticks (milissegundos).
	 */

	public long getTickCount()
	{
		return timerTick.getTicksCount();
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("running", running);
		description.append("initialized", isInitialized());
		description.append("sceneManager", nameOf(sceneManager));
		description.append("renderManager", nameOf(rendererManager));

		return description.toString();
	}

	/**
	 * Procedimento que permite obter a única instância do núcleo do engine.
	 * Utiliza o padrão Singleton para evitar a existência de mais instâncias.
	 * @return aquisição da instância para utilização do núcleo do engine.
	 */

	public static Engine getInstance()
	{
		return INSTANCE;
	}
}
