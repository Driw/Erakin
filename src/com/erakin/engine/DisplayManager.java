package com.erakin.engine;

import static com.erakin.api.Constants.NANOSECOND;
import static org.diverproject.log.LogSystem.logNotice;
import static org.lwjgl.opengl.GL11.glViewport;

import java.awt.Toolkit;

import org.diverproject.util.lang.IntUtil;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import com.erakin.api.ErakinException;

/**
 * <h1>Gerenciador de exibição</h1>
 *
 * <p>Esse gerenciador funciona como um intermédio entre o engine e a biblioteca LWJGL (Display).
 * Possui alguns procedimentos básicos para fazer o gerenciamento do Display com o OpenGL.
 * Como também a contagem de quadros por segundos para informar o jogador o andamento gráfico.</p>
 *
 * <p>Aqui é onde é feito a criação da tela de acordo com as configurações iniciais passadas.
 * Sendo possível ainda atualizar essas configurações mais a frente se assim for desejável.
 * Alterar o tamanho da tela, modo tela cheia e janela como a taxa de quadros por segundos.</p>
 *
 * <p>Alguns métodos possui uma interação com as preferências de vídeo utilizadas no engine.
 * Isso é feito para permitir que a aplicação possa salvar as preferências usadas pelo usuário,
 * de modo que quando a aplicação for fechada possam ser carregadas quando aberto novamente.</p>
 *
 * @see Display
 * @see DisplaySettings
 * @see PreferencesSettings
 *
 * @author Andrew Mello
 */

public class DisplayManager
{
	/**
	 * Instância para gerenciador de exibição no padrão de projetos Singleton.
	 */
	private static final DisplayManager INSTANCE = new DisplayManager();


	/**
	 * Tamanho mínimo permitido definir para a largura da tela.
	 */
	public static final int MIN_WIDTH = 720;

	/**
	 * Tamanho mínimo permitido definir para a altura da tela.
	 */
	public static final int MIN_HEIGHT = 480;

	/**
	 * Tamanho máximo permitido da altura na tela, usando propriedades do monitor.
	 */
	public static final int MAX_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;

	/**
	 * Tamanho máximo permitido da largura na tela, usando propriedades do monitor.
	 */
	public static final int MAX_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;


	/**
	 * Taxa de quadros por segundos do último segundo.
	 */
	private int frameRate;

	/**
	 * Quantidade de quadros passados no último segundo.
	 */
	private int frameCount;

	/**
	 * Duração que um único quadro deve possuir.
	 */
	private int frameDuration;

	/**
	 * Momento em que foi atualizado o último quadro.
	 */
	private long lastTick;

	/**
	 * Última vez que foi atualizada a taxa de quadros por segundos.
	 */
	private long lastFrame;

	/**
	 * Construtor privado para evitar múltiplas instâncias do gerenciador de exibição.
	 */

	private DisplayManager()
	{
		
	}

	/**
	 * Chamado apenas internamente pelo núcleo do engine para manter a tela atualizada.
	 * Nesse procedimento será feito a contagem dos quadros por segundos,
	 * tendo efeito no tempo de espera do próximo quadro se assim for necessário.
	 */

	void update()
	{
		Display.update();

		frameCount++;
		lastTick = System.nanoTime();

		if (System.currentTimeMillis() - lastFrame >= 1000)
		{
			lastFrame = System.currentTimeMillis();
			frameRate = frameCount;
			frameCount = 0;
		}
	}

	/**
	 * Chamado internamente pelo núcleo do engine quando e deve criar a janela da aplicação.
	 * Utiliza algumas configurações iniciais para que a janela possa de fato ser criada.
	 * @param settings objeto contendo alguma configurações iniciais para definir a tela.
	 * @throws ErakinException configurações nula ou inválidas ou falha na criação.
	 */

	void create(DisplaySettings settings) throws ErakinException
	{
		ContextAttribs attribs = new ContextAttribs(3, 2);
		attribs.withForwardCompatible(true);
		attribs.withProfileCore(true);

		try {

			setDisplayMode(settings);

			Display.setTitle(settings.getTitle());
			Display.create(new PixelFormat(), attribs);

		} catch (LWJGLException e) {
			throw new ErakinException(e);
		} catch (ErakinException e) {
			throw e;
		}

		glViewport(0, 0, settings.getWidth(), settings.getHeight());
	}

	/**
	 * Permite alterar algumas configurações de como deve ser o modo de exibição da tela.
	 * As configurações aqui necessárias é tamanho da tela, tela cheia, fps e título.
	 * @param settings objeto contendo as configurações do qual a tela deverá assumir.
	 * @throws LWJGLException durante a obtenção das formas de exibição disponíveis.
	 * @throws ErakinException configuração nula ou configurações inválidas.
	 * @see Preferences
	 */

	public void setDisplayMode(DisplaySettings settings) throws LWJGLException, ErakinException
	{
		if (settings == null)
			throw new ErakinException("configurações nula");

		int width = IntUtil.limit(settings.getWidth(), MIN_WIDTH, MAX_WIDTH);
		int height = IntUtil.limit(settings.getHeight(), MIN_HEIGHT, MAX_HEIGHT);
		boolean fullscreen = settings.isFullscreen();

		DisplayMode displayMode = Display.getDisplayMode();

		if (displayMode.getWidth() == width && displayMode.getHeight() == height && Display.isFullscreen() == fullscreen)
			return;

		if (!fullscreen)
			displayMode = new DisplayMode(width, height);

		else
		{
			int frequency = 0;
			DisplayMode[] modes = Display.getAvailableDisplayModes();

			for (DisplayMode mode : modes)
			{
				if (mode.getWidth() == width && mode.getHeight() == height)
				{
					if (mode.getFrequency() == frequency)
					{
						if (mode.getBitsPerPixel() > displayMode.getBitsPerPixel())
						{
							displayMode = mode;
							frequency = displayMode.getFrequency();
						}
					}

					if (mode.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel() && mode.getFrequency() == Display.getDesktopDisplayMode().getFrequency())
					{
						displayMode = mode;
						break;
					}
				}
			}
		}

		setFPS(settings.getFPS());

		Display.setDisplayMode(displayMode);
		Display.setFullscreen(fullscreen);
		Display.setVSyncEnabled(settings.isVerticalSyncronize());

		Preferences preferences = PreferencesSettings.getVideoPreferences();
		settings.getPreferences(preferences);

		logNotice("resolução alterada (%dx%d%s).\n", width, height, (fullscreen ? ", fullscreen" : ""));
	}

	/**
	 * Ao ser chamado deve fazer o fechamento da janela, isso não determina o término da aplicação.
	 * Uma vez que a janela tenha sido fechada ela pode ser aberta novamente se assim for chamado.
	 */

	public void close()
	{
		Display.destroy();
	}

	/**
	 * <p>Quando chamado fica em espera até que tenha dado tempo suficiente de um tick.
	 * Cada tick é equivalente a duração esperada que um quadro por segundo tenha.</p>
	 * <p>Caso o tempo do último tick tenha excedido o tempo de duração do quadro,
	 * ele simplesmente sai do loop e retorna do procedimento, caso contrário será
	 * esperado dentro do procedimento até concluir a duração do quadro por segundo.</p>
	 * <p>Assim é possível garantir uma estabilidade na taxa de quadros por segundo,
	 * caso seja usado uma taxa de quadros ilimitado esse procedimento não terá efeito.</p>
	 */

	public void waitTick()
	{
		while (frameDuration > 0 && (System.nanoTime() - lastTick < frameDuration));

		lastTick = System.nanoTime();
	}

	/**
	 * Taxa de quadros por segundos (frames per second) determina quantas vezes deve ser processado
	 * todo o conteúdo do engine na tela, isso pode influenciar na jogabilidade se for muito baixa.
	 * A partir dos 60 quadros por segundos, o efeito não pode ser muito bem visível a olho nu.
	 * Caso o valor da taxa seja igual a zero significa que a taxa deve ser o maior possível.
	 * @return aquisição da taxa de quadros por segundos do qual está sendo utilizado.
	 */

	public int getFPS()
	{
		return frameRate;
	}

	/**
	 * Permite definir qual deve ser a taxa de quadros por segundos que a tela deve mostrar.
	 * Valores negativos não serão aceitos, em quanto zero irá determinar o máximo que conseguir.
	 * Quando um novo valor for definido, será atualizado a preferências para essa configuração.
	 * @param value nova taxa de quadros por segundos do qual deverá ser assumida na exibição.
	 */

	public void setFPS(int value)
	{
		if (value < 0)
			return;

		Preferences preferences = PreferencesSettings.getVideoPreferences();
		preferences.setOptionInt("fps", value);

		frameDuration = value > 0 ? (int) ((NANOSECOND / value) * 0.975) : 0;

		logNotice("taxa de quadros por segundos atualizada (%d).\n", value);
	}

	/**
	 * A renderização da tela é feita por diversos quadros, onde cada quadro é uma "imagem".
	 * Quando essas imagens são sobrescritas em um intervalo de tempo muito curto cria o efeito da animação.
	 * Através desse método é possível saber quantos quatros já foram processados no último segundo.
	 * @return aquisição da quantidade de quadros que já foram contabilizados.
	 */

	public int getFrameCount()
	{
		return frameCount;
	}

	/**
	 * Procedimento que permite obter a única instância do gerenciador de exibição.
	 * Utiliza o padrão Singleton para evitar a existência de mais instâncias.
	 * @return aquisição da instância para utilização do gerenciador de exibição.
	 */

	public static DisplayManager getInstance()
	{
		return INSTANCE;
	}
}
