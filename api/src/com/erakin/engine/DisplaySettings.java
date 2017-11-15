package com.erakin.engine;

import org.diverproject.util.ObjectDescription;

/**
 * <h1>Configura��es de Exibi��o</h1>
 *
 * <p>Usado para facilitar a defini��o das configura��es iniciais para exibi��o da tela da aplica��o.
 * Possui setters para definir quais as configura��es a ser utilizadas como prefer�ncias ao iniciar,
 * e os getters para que o engine possa saber quais foram as configura��es utilizadas como iniciais.</p>
 *
 * <p>Permite ainda trabalhar com Preferences de modo que seja poss�vel obter os valores definidos
 * em um objeto de prefer�ncias ou ainda ent�o salvar essas configura��es iniciais utilizadas.</p>
 *
 * @author Andrew Mello
 */

public class DisplaySettings
{
	/**
	 * T�tulo que ser� exibido na barra superior da aplica��o.
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
	 * Sincroniza��o vertical.
	 */
	private boolean vsync;

	/**
	 * Modo tela cheia ou janela.
	 */
	private boolean fullscreen;

	/**
	 * T�tulo � o texto que ser� exibido na barra superior da aplica��o.
	 * @return aquisi��o do t�tulo como configura��o de exibi��o.
	 */

	public String getTitle()
	{
		return title;
	}

	/**
	 * Permite determinar qual ser� o t�tulo exibido na barra superior da aplica��o.
	 * @param title novo t�tulo para a configura��o de exibi��o.
	 */

	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * Largura determina a quantidade de pixels que ter� a tela da aplica��o.
	 * @return aquisi��o do tamanho da tela em pixels na largura.
	 */

	public int getWidth()
	{
		return width;
	}

	/**
	 * Permite determinar quantos pixels ter� a tela no tamanho em largura.
	 * @param width novo tamanho da tela em pixels na largura.
	 */

	public void setWidth(int width)
	{
		this.width = width;
	}

	/**
	 * Altura determina a quantidade de pixels que ter� a tela da aplica��o.
	 * @return aquisi��o do tamanho da tela em pixels na altura.
	 */

	public int getHeight()
	{
		return height;
	}

	/**
	 * Permite determinar quantos pixels ter� a tela no tamanho em altura.
	 * @param height novo tamanho da tela em pixels na altura.
	 */

	public void setHeight(int height)
	{
		this.height = height;
	}

	/**
	 * FPS determina quantos ticks a aplica��o deve tentar manter por segundo.
	 * @return aquisi��o da taxa de fps que deve ser mantida na exibi��o.
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
	 * Sincroniza��o vertical evita que alguns quadros n�o deixem de ser atualizados, mantendo
	 * informa��es de um quadro anterior, aumenta o consumo do processador gr�fico da m�quina.
	 * @return true se a sincroniza��o vertical estiver habilitada ou false caso contr�rio.
	 */

	public boolean isVerticalSyncronize()
	{
		return vsync;
	}

	/**
	 * Permite definir se a sincroniza��o vertical deve ou n�o ser habilitada.
	 * @param enable true para habilitar ou false para desabilitar.
	 */

	public void setVerticalSyncronize(boolean enable)
	{
		this.vsync = enable;
	}

	/**
	 * Verifica se deve ser usado a exibi��o da tela no modo tela cheia ou modo janela.
	 * Melhora a performance da aplica��o quanto o modo tela cheia � habilitado.
	 * @return true se estiver sendo usado tela cheia ou false para modo janela.
	 */

	public boolean isFullscreen()
	{
		return fullscreen;
	}

	/**
	 * Permite definir se ser� usado o modo tela cheia ou modo janela.
	 * @param enable true para habilitar tela cheia ou false para janela.
	 */

	public void setFullscreen(boolean enable)
	{
		this.fullscreen = enable;
	}

	/**
	 * Define todas a configura��es de exibi��o (exceto o t�tulo) atrav�s de um objeto de prefer�ncias.
	 * As propriedades que ser�o obtidas das prefer�ncias s�o as seguintes:<br>
	 * width (int), height (int), fps (int), fullscreen (boolean) e vsync (boolean).
	 * @param preferences objeto contendo as prefer�ncias para exibi��o de v�deo.
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
	 * Permite passar as configura��es de exibi��o aqui armazenadas para um objeto do tipo prefer�ncias.
	 * Utiliza todas as propriedades (exceto t�tulo) e as salvam da seguinte maneira:<br>
	 * width (int), height (int), fps (int), fullscreen (boolean) e vsync (boolean).
	 * @param preferences objeto de prefer�ncias que ser� usado para salvar as configura��es.
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
