package com.erakin.engine;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Configurações de Exibição</h1>
 *
 * <p>Usado para facilitar a definição das configurações iniciais para exibição da tela da aplicação.
 * Possui setters para definir quais as configurações a ser utilizadas como preferências ao iniciar,
 * e os getters para que o engine possa saber quais foram as configurações utilizadas como iniciais.</p>
 *
 * <p>Permite ainda trabalhar com Preferences de modo que seja possível obter os valores definidos
 * em um objeto de preferências ou ainda então salvar essas configurações iniciais utilizadas.</p>
 *
 * @author Andrew Mello
 */

public class DisplaySettings
{
	/**
	 * Título que será exibido na barra superior da aplicação.
	 */
	private String title;

	/**
	 * Largura em pixels da janela.
	 */
	private int width;

	/**
	 * Altura em pixels da janela.
	 */
	private int height;

	/**
	 * Taxa de quadros por segundo.
	 */
	private int fps;

	/**
	 * Sincronização vertical.
	 */
	private boolean vsync;

	/**
	 * Modo tela cheia ou janela.
	 */
	private boolean fullscreen;

	/**
	 * Título é o texto que será exibido na barra superior da aplicação.
	 * @return aquisição do título como configuração de exibição.
	 */

	public String getTitle()
	{
		return title;
	}

	/**
	 * Permite determinar qual será o título exibido na barra superior da aplicação.
	 * @param title novo título para a configuração de exibição.
	 */

	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * Largura determina a quantidade de pixels que terá a tela da aplicação.
	 * @return aquisição do tamanho da tela em pixels na largura.
	 */

	public int getWidth()
	{
		return width;
	}

	/**
	 * Permite determinar quantos pixels terá a tela no tamanho em largura.
	 * @param width novo tamanho da tela em pixels na largura.
	 */

	public void setWidth(int width)
	{
		this.width = width;
	}

	/**
	 * Altura determina a quantidade de pixels que terá a tela da aplicação.
	 * @return aquisição do tamanho da tela em pixels na altura.
	 */

	public int getHeight()
	{
		return height;
	}

	/**
	 * Permite determinar quantos pixels terá a tela no tamanho em altura.
	 * @param height novo tamanho da tela em pixels na altura.
	 */

	public void setHeight(int height)
	{
		this.height = height;
	}

	/**
	 * FPS determina quantos ticks a aplicação deve tentar manter por segundo.
	 * @return aquisição da taxa de fps que deve ser mantida na exibição.
	 */

	public int getFPS()
	{
		return fps;
	}

	/**
	 * Permite determinar quantos quadros devem ser exibidos por segundo na tela.
	 * @param fps nova taxa de quadros por segundos para tentar manter.
	 */

	public void setFPS(int fps)
	{
		this.fps = fps;
	}

	/**
	 * Sincronização vertical evita que alguns quadros não deixem de ser atualizados, mantendo
	 * informações de um quadro anterior, aumenta o consumo do processador gráfico da máquina.
	 * @return true se a sincronização vertical estiver habilitada ou false caso contrário.
	 */

	public boolean isVerticalSyncronize()
	{
		return vsync;
	}

	/**
	 * Permite definir se a sincronização vertical deve ou não ser habilitada.
	 * @param enable true para habilitar ou false para desabilitar.
	 */

	public void setVerticalSyncronize(boolean enable)
	{
		this.vsync = enable;
	}

	/**
	 * Verifica se deve ser usado a exibição da tela no modo tela cheia ou modo janela.
	 * Melhora a performance da aplicação quanto o modo tela cheia é habilitado.
	 * @return true se estiver sendo usado tela cheia ou false para modo janela.
	 */

	public boolean isFullscreen()
	{
		return fullscreen;
	}

	/**
	 * Permite definir se será usado o modo tela cheia ou modo janela.
	 * @param enable true para habilitar tela cheia ou false para janela.
	 */

	public void setFullscreen(boolean enable)
	{
		this.fullscreen = enable;
	}

	/**
	 * Define todas a configurações de exibição (exceto o título) através de um objeto de preferências.
	 * As propriedades que serão obtidas das preferências são as seguintes:<br>
	 * width (int), height (int), fps (int), fullscreen (boolean) e vsync (boolean).
	 * @param preferences objeto contendo as preferências para exibição de vídeo.
	 */

	public void setPreferences(Preferences preferences)
	{
		setFPS(preferences.getOptionInt("fps"));
		setWidth(preferences.getOptionInt("width"));
		setHeight(preferences.getOptionInt("height"));
		setFullscreen(preferences.getOptionBoolean("fullscreen"));
		setVerticalSyncronize(preferences.getOptionBoolean("vsync"));
	}

	/**
	 * Permite passar as configurações de exibição aqui armazenadas para um objeto do tipo preferências.
	 * Utiliza todas as propriedades (exceto título) e as salvam da seguinte maneira:<br>
	 * width (int), height (int), fps (int), fullscreen (boolean) e vsync (boolean).
	 * @param preferences objeto de preferências que será usado para salvar as configurações.
	 */

	public void getPreferences(Preferences preferences)
	{
		if (preferences == null)
			return;

		preferences.setOptionInt("fps", fps);
		preferences.setOptionInt("width", width);
		preferences.setOptionInt("height", height);
		preferences.setOptionBoolean("fullscreen", fullscreen);
		preferences.setOptionBoolean("vsync", vsync);
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("width", width);
		description.append("height", height);
		description.append("fullscreen", fullscreen);
		description.append("vsync", vsync);
		description.append("fps", fps);

		return description.toString();
	}
}
