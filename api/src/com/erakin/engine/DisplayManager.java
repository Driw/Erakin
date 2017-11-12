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
 * <h1>Gerenciador de exibi��o</h1>
 *
 * <p>Esse gerenciador funciona como um interm�dio entre o engine e a biblioteca LWJGL (Display).
 * Possui alguns procedimentos b�sicos para fazer o gerenciamento do Display com o OpenGL.
 * Como tamb�m a contagem de quadros por segundos para informar o jogador o andamento gr�fico.</p>
 *
 * <p>Aqui � onde � feito a cria��o da tela de acordo com as configura��es iniciais passadas.
 * Sendo poss�vel ainda atualizar essas configura��es mais a frente se assim for desej�vel.
 * Alterar o tamanho da tela, modo tela cheia e janela como a taxa de quadros por segundos.</p>
 *
 * <p>Alguns m�todos possui uma intera��o com as prefer�ncias de v�deo utilizadas no engine.
 * Isso � feito para permitir que a aplica��o possa salvar as prefer�ncias usadas pelo usu�rio,
 * de modo que quando a aplica��o for fechada possam ser carregadas quando aberto novamente.</p>
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
	 * Inst�ncia para gerenciador de exibi��o no padr�o de projetos Singleton.
	 */
	private static final DisplayManager INSTANCE = new DisplayManager();


	/**
	 * Tamanho m�nimo permitido definir para a largura da tela.
	 */
	public static final int MIN_WIDTH = 720;

	/**
	 * Tamanho m�nimo permitido definir para a altura da tela.
	 */
	public static final int MIN_HEIGHT = 480;

	/**
	 * Tamanho m�ximo permitido da altura na tela, usando propriedades do monitor.
	 */
	public static final int MAX_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;

	/**
	 * Tamanho m�ximo permitido da largura na tela, usando propriedades do monitor.
	 */
	public static final int MAX_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;


	/**
	 * Taxa de quadros por segundos do �ltimo segundo.
	 */
	private int frameRate;

	/**
	 * Quantidade de quadros passados no �ltimo segundo.
	 */
	private int frameCount;

	/**
	 * Dura��o que um �nico quadro deve possuir.
	 */
	private int frameDuration;

	/**
	 * Momento em que foi atualizado o �ltimo quadro.
	 */
	private long lastTick;

	/**
	 * �ltima vez que foi atualizada a taxa de quadros por segundos.
	 */
	private long lastFrame;

	/**
	 * Construtor privado para evitar m�ltiplas inst�ncias do gerenciador de exibi��o.
	 */

	private DisplayManager()
	{
		
	}

	/**
	 * Chamado apenas internamente pelo n�cleo do engine para manter a tela atualizada.
	 * Nesse procedimento ser� feito a contagem dos quadros por segundos,
	 * tendo efeito no tempo de espera do pr�ximo quadro se assim for necess�rio.
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
	 * Chamado internamente pelo n�cleo do engine quando e deve criar a janela da aplica��o.
	 * Utiliza algumas configura��es iniciais para que a janela possa de fato ser criada.
	 * @param settings objeto contendo alguma configura��es iniciais para definir a tela.
	 * @throws ErakinException configura��es nula ou inv�lidas ou falha na cria��o.
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
	 * Permite alterar algumas configura��es de como deve ser o modo de exibi��o da tela.
	 * As configura��es aqui necess�rias � tamanho da tela, tela cheia, fps e t�tulo.
	 * @param settings objeto contendo as configura��es do qual a tela dever� assumir.
	 * @throws LWJGLException durante a obten��o das formas de exibi��o dispon�veis.
	 * @throws ErakinException configura��o nula ou configura��es inv�lidas.
	 * @see Preferences
	 */

	public void setDisplayMode(DisplaySettings settings) throws LWJGLException, ErakinException
	{
		if (settings == null)
			throw new ErakinException("configura��es nula");

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

		logNotice("resolu��o alterada (%dx%d%s).\n", width, height, (fullscreen ? ", fullscreen" : ""));
	}

	/**
	 * Ao ser chamado deve fazer o fechamento da janela, isso n�o determina o t�rmino da aplica��o.
	 * Uma vez que a janela tenha sido fechada ela pode ser aberta novamente se assim for chamado.
	 */

	public void close()
	{
		Display.destroy();
	}

	/**
	 * <p>Quando chamado fica em espera at� que tenha dado tempo suficiente de um tick.
	 * Cada tick � equivalente a dura��o esperada que um quadro por segundo tenha.</p>
	 * <p>Caso o tempo do �ltimo tick tenha excedido o tempo de dura��o do quadro,
	 * ele simplesmente sai do loop e retorna do procedimento, caso contr�rio ser�
	 * esperado dentro do procedimento at� concluir a dura��o do quadro por segundo.</p>
	 * <p>Assim � poss�vel garantir uma estabilidade na taxa de quadros por segundo,
	 * caso seja usado uma taxa de quadros ilimitado esse procedimento n�o ter� efeito.</p>
	 */

	public void waitTick()
	{
		while (frameDuration > 0 && (System.nanoTime() - lastTick < frameDuration));

		lastTick = System.nanoTime();
	}

	/**
	 * Taxa de quadros por segundos (frames per second) determina quantas vezes deve ser processado
	 * todo o conte�do do engine na tela, isso pode influenciar na jogabilidade se for muito baixa.
	 * A partir dos 60 quadros por segundos, o efeito n�o pode ser muito bem vis�vel a olho nu.
	 * Caso o valor da taxa seja igual a zero significa que a taxa deve ser o maior poss�vel.
	 * @return aquisi��o da taxa de quadros por segundos do qual est� sendo utilizado.
	 */

	public int getFPS()
	{
		return frameRate;
	}

	/**
	 * Permite definir qual deve ser a taxa de quadros por segundos que a tela deve mostrar.
	 * Valores negativos n�o ser�o aceitos, em quanto zero ir� determinar o m�ximo que conseguir.
	 * Quando um novo valor for definido, ser� atualizado a prefer�ncias para essa configura��o.
	 * @param value nova taxa de quadros por segundos do qual dever� ser assumida na exibi��o.
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
	 * A renderiza��o da tela � feita por diversos quadros, onde cada quadro � uma "imagem".
	 * Quando essas imagens s�o sobrescritas em um intervalo de tempo muito curto cria o efeito da anima��o.
	 * Atrav�s desse m�todo � poss�vel saber quantos quatros j� foram processados no �ltimo segundo.
	 * @return aquisi��o da quantidade de quadros que j� foram contabilizados.
	 */

	public int getFrameCount()
	{
		return frameCount;
	}

	/**
	 * Procedimento que permite obter a �nica inst�ncia do gerenciador de exibi��o.
	 * Utiliza o padr�o Singleton para evitar a exist�ncia de mais inst�ncias.
	 * @return aquisi��o da inst�ncia para utiliza��o do gerenciador de exibi��o.
	 */

	public static DisplayManager getInstance()
	{
		return INSTANCE;
	}
}
