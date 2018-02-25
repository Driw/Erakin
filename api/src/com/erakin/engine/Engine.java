package com.erakin.engine;

import static org.diverproject.log.LogSystem.logException;
import static org.diverproject.log.LogSystem.logNotice;
import static org.diverproject.util.MessageUtil.showException;
import static org.diverproject.util.Util.nameOf;
import static org.diverproject.util.Util.sleep;
import static org.diverproject.util.service.SystemBase.PROPERTIE_USE_LOG;

import javax.swing.UIManager;

import org.diverproject.jni.input.InputException;
import org.diverproject.jni.input.InputSystem;
import org.diverproject.log.LogPreferences;
import org.diverproject.log.LogSystem;
import org.diverproject.util.ObjectDescription;
import org.diverproject.util.TimerTick;
import org.diverproject.util.UtilException;
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
 * <p>N�cleo do engine, faz a inicializa��o de diversos servi�os dos sistema e bibliotecas.
 * Garante que toda a base do sistema seja carregada e iniciada adequadamente para ser usado.
 * Respons�vel principalmente por processar os ticks e determinar o intervalo entre eles.</p>
 *
 * <p>Nele tamb�m � onde pode ser definido alguns gerenciadores como o de cen�rios e renderiza��o.
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
	 * Inst�ncia para n�cleo do engine no padr�o de projetos Singleton.
	 */
	private static final Engine INSTANCE = new Engine();


	/**
	 * T�tulo dado par a aplica��o.
	 */
	private String title;

	/**
	 * Determina se o sistema est� rodando.
	 */
	private boolean running;

	/**
	 * Determina se o sistema est� parada.
	 */
	private boolean paused;

	/**
	 * Usado para obter o intervalo entre cada quadro.
	 */
	private TimerTick timerTick;

	/**
	 * Gerenciador de cen�rios usado para chamar atualiza��es e renderiza��es.
	 */
	private SceneManager sceneManager;

	/**
	 * Gerenciador de renderiza��o para chamar as renderiza��es necess�rias.
	 */
	private RendererManager rendererManager;

	/**
	 * Listener para permitir din�mica em rela��o a execu��o da engine.
	 */
	private EngineListener listener;

	/**
	 * Tarefas � serem executadas na engine.
	 */
	private EngineTaskList tasks;

	/**
	 * Construtor privado para atender ao padr�o de projetos singleton.
	 * Primeiramente define o estilo gr�fico da janela com o sistema operacional.
	 * Depois deve garantir o carregamento das bibliotecas necess�rias adequadamente.
	 * Inicializa��o do sistema para gerenciamento de servi�os e o de entrada.
	 */

	private Engine()
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			logException(e);
		}

		tasks = new EngineTaskList();

		loadLogSystem();

		LibrarySystem librarySystem = LibrarySystem.getInstance();
		librarySystem.setPropertie(PROPERTIE_USE_LOG, true);
		librarySystem.setDirectory("system/libraries/native");
		librarySystem.bind("lwjgl", "org.lwjgl.librarypath");
		librarySystem.bind("jniInput", "org.diverproject.librarypath");

		ServiceSystem serviceSystem = ServiceSystem.getInstance();
		serviceSystem.setPropertie(PROPERTIE_USE_LOG, true);
	}

	/**
	 * Carregamento e inicializa��o do servi�o para registro de eventos no sistema.
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
	 * Deve inicializar o n�cleo do engine para manter o mesmo funcionando.
	 * A partir daqui o loop ser� feito, calculando taxa de quadros por segundo,
	 * chamando atualiza��es, renderiza��es, exibir a janela e iniciar servi�os.
	 * @throws ErakinException gerenciadores n�o definidos, falha na exibi��o,
	 * inicializa��o do servi�o inv�lida ou problema durante a execu��o da aplica��o.
	 */

	public void initiate() throws ErakinException
	{
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
	 * Ao ser chamado carrega as configura��es de exibi��es a partir das prefer�ncias de v�deo.
	 * As prefer�ncias de v�deo podem ser encontradas na classe de configura��es para prefer�ncias.
	 * @throws ErakinException apenas se houver falha na inicializa��o da exibi��o da tela.
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
	 * A partir desse momento, o n�cleo do engine estar� ativo e em funcionamento iniciando o seu loop.
	 * Determina a aplica��o como rodando, come�a a contagem de quadros por segundo, atualiza��es e renderiza��es.
	 */

	private void initiateLoop()
	{
		paused = false;
		running = true;
		timerTick = new TimerTick(1);
		listener.onInitiate();

		DisplayManager display = DisplayManager.getInstance();

		while (running && !Display.isCloseRequested())
		{
			try {

				display.waitTick();
				display.update();

				if (!paused)
					tick();
				else
					sleep(100);

			} catch (Exception e) {

				logException(e);
				showException(e);
			}

		}

		shutdown();
		listener.onClosed();
	}

	/**
	 * Chamado em quanto a aplica��o estiver em execu��o, representa um �nico quadro executado por segundo.
	 * Dever� garantir que todas as tarefas como os servi�os e objetos existentes sejam atualizados e renderizados.
	 */

	private void tick()
	{
		long delay = timerTick.getTicks();

		if (listener != null)
			listener.tick(delay);

		update(delay);
		render(delay);

		tasks.tick(delay);
	}

	@Override
	public void update(long delay)
	{
		ServiceSystem.getInstance().update(delay);
		TextureLoader.getInstance().update(delay);
		ModelLoader.getInstance().update(delay);
		WorldLoader.getInstance().update(delay);
		ProjectionMatrix.getInstance().update();

		if (sceneManager != null)
			sceneManager.update(delay);

		if (rendererManager != null && !rendererManager.isStoped())
		{
			if (!rendererManager.isInitiate())
				rendererManager.initiate();

			rendererManager.update(delay);
		}
	}

	@Override
	public void render(long delay)
	{
		if (sceneManager != null)
			sceneManager.render(delay);

		if (rendererManager != null && rendererManager.isInitiate() && !rendererManager.isStoped())
			rendererManager.render(delay);
	}

	/**
	 * Uma vez que seja chamado, desencadeia diversos procedimentos seguidos para desligar servi�os.
	 * Al�m de desligar os servi�os, salva os dados necess�rios e desliga a aplica��o como deve ser.
	 */

	private void shutdown()
	{
		listener.onShutdown();
		rendererManager.cleanup();

		DisplayManager.getInstance().close();
		ServiceSystem.getInstance().shutdown();

		try {
			InputSystem.getInstance().shutdown();
		} catch (InputException e) {
			logException(e);
		}
	}

	/**
	 * Uma vez chamado pede para que o cliente para de rodar, isso significa que ele deve ser fechado.
	 */

	public void requestToClose()
	{
		running = false;
	}

	/**
	 * Tarefas permitem uma forma extra de manter partes da aplica��o funcionando al�m das necess�rias.
	 * @return aquisi��o do objeto que permite adicionar e remover tarefas da {@link Engine}.
	 */

	public EngineTaskList getTaskList()
	{
		return tasks;
	}

	/**
	 * Define um listener para que determinados procedimentos sejam executados durante a engine.
	 * Por exemplo, � necess�rio executar procedimentos ap�s o OpenGL iniciar mas antes de renderizar.
	 * @param listener refer�ncia do objeto que implementa o listener com os procedimentos a executar.
	 */

	public void setListener(EngineListener listener)
	{
		this.listener = listener;
	}

	/**
	 * A aplica��o ser� considerada inicializada quando o temporizador de ticks for iniciado.
	 * @return true se a aplica��o tiver sido iniciada ou false caso contr�rio.
	 */

	public boolean isInitialized()
	{
		return timerTick != null;
	}

	/**
	 * T�tulo � usado para determinar o texto que ser� exibido na barra superior da janela.
	 * @return aquisi��o do t�tulo da janela atualmente utilizado.
	 */

	public String getTitle()
	{
		return title;
	}

	/**
	 * Permite definir qual deve ser o novo texto a ser exibido na barra superior da janela.
	 * Se a janela j� tiver sido exibida, apenas atualiza o nome caso contr�rio aguarda.
	 * @param title novo t�tulo da janela do qual dever� ser utilizado.
	 */

	public void setTitle(String title)
	{
		this.title = title;

		if (isInitialized())
			Display.setTitle(title);
	}

	/**
	 * Gerenciador de cen�rios � usado para que garanta aos cen�rios serem atualizados e renderizados.
	 * Isso tamb�m permite uma varia��o na forma como os cen�rios podem ser utilizados.
	 * @return aquisi��o do gerenciador de cen�rios atualmente utilizado.
	 */

	public SceneManager getSceneManager()
	{
		return sceneManager;
	}

	/**
	 * Toda aplica��o deve definir um gerenciador de cen�rios, o engine oferece um padr�o.
	 * � recomend�vel que seja utilizado o padr�o com modifica��es de acordo com a aplica��o.
	 * @param sceneManager refer�ncia do novo gerenciador de cen�rios do qual deve ser usado.
	 */

	public void setSceneManager(SceneManager sceneManager)
	{
		this.sceneManager = sceneManager;
	}

	/**
	 * Gerenciador de renderiza��o � usado para determinar como e o que ser� renderizado na tela.
	 * Isso tamb�m permite ua varia��o na forma como elas ser�o feitas ou o que ser� exibido.
	 * @return aquisi��o do gerenciador de renderiza��o atualmente utilizado.
	 */

	public RendererManager getRendererManger()
	{
		return rendererManager;
	}

	/**
	 * Toda aplica��o deve definir um gerenciador de renderiza��o, o engine oferece um padr�o.
	 * � recomend�vel que seja utilizado o padr�o com modifica��es de acordo com a aplica��o.
	 * @param renderManger refer�ncia do novo gerenciador de renderiza��o do qual deve ser usado.
	 */

	public void setRenderManger(RendererManager renderManger)
	{
		this.rendererManager = renderManger;
	}

	/**
	 * Quando a engine entra em modo de espera (por que est� parada) nada ser� atualizado ou renderizado.
	 * Isso pode ser utilizado em quanto nenhum tipo de {@link RendererManager} ou {@link SceneManager} foi definido.
	 * Desta forma, � poss�vel renderizar o sistema de diferentes jeitos sem a necessidade de reiniciar o sistema.
	 */

	public void pause()
	{
		paused = true;
	}

	/**
	 * Ao resumir a espera voltando a ser atualizado e renderizado, o delay n�o ter� sido afetado pela pausa.
	 * Isso pode ser utilizado em quanto nenhum tipo de {@link RendererManager} ou {@link SceneManager} foi definido.
	 * Desta forma, � poss�vel renderizar o sistema de diferentes jeitos sem a necessidade de reiniciar o sistema.
	 */

	public void resume()
	{
		paused = false;
	}

	/**
	 * Uma quantidade de ticks (unidade de tempo) � calculada para cada quadro processado.
	 * Esse valor � repassado por diversos objetos para atualizar os dados e renderizar o jogo.
	 * Sempre que o valor � calculado, ele � armazenado em uma contagem acumulativa de ticks.
	 * @return aquisi��o do tempo em que a Engine est� rodando em ticks (milissegundos).
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
		description.append("paused", paused);
		description.append("initialized", isInitialized());
		description.append("sceneManager", nameOf(sceneManager));
		description.append("renderManager", nameOf(rendererManager));

		return description.toString();
	}

	/**
	 * Procedimento que permite obter a �nica inst�ncia do n�cleo do engine.
	 * Utiliza o padr�o Singleton para evitar a exist�ncia de mais inst�ncias.
	 * @return aquisi��o da inst�ncia para utiliza��o do n�cleo do engine.
	 */

	public static Engine getInstance()
	{
		return INSTANCE;
	}
}
