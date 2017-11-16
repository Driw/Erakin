package com.erakin.api.input;

import org.diverproject.jni.input.KeyEvent;
import org.diverproject.jni.input.KeyboardDispatcher;
import org.diverproject.jni.input.enums.EnumKEY;
import org.diverproject.util.ObjectDescription;
import org.diverproject.util.collection.List;
import org.diverproject.util.collection.abstraction.DynamicList;
import org.diverproject.util.lang.IntUtil;
import org.lwjgl.opengl.Display;

/**
 * <h1>Teclado Virtual</h1>
 *
 * <p>JNI Input é uma das bibliotecas utilizadas pelo engine, permite detectar ações para mouse e teclado.
 * Nesse caso será considerado apenas as funcionalidades de teclado na classe, que funciona como despachante.</p>
 * 
 * <p>A biblioteca JNI deverá detectar as ações do teclado e repassar para o sistema de entradas e repassar.
 * Quando repassado, o sistema irá verificar qual o despachante de teclado usado e repassar para este,
 * determinar o que será feito com o evento que foi construído a partir da detecção da ação acionada.</p>
 *
 * <p>A finalidade do despachante de teclado é receber os eventos detectados e determinar o que fazer.
 * Para cada evento de tecla detectado há um tipo de ação no mesmo e alguns atributos informativos sobre.</p>
 *
 * <p>Essa classe teclado irá despachar adequadamente cada evento para suas ações respectivas.
 * As ações que poderão ser encontradas pelos eventos de teclado são de tecla pressionada, clicada ou solta.
 * Todos os eventos irão possuir informações sobre qual a tecla que foi usada na ação e alguns outros afins.</p>
 *
 * <p>Tem como alta prioridade no sistema do engine, pois garante metade da interação do jogador com o jogo.
 * Pode ser usado para identificar qual ação usar ou funcionalidade que deve ser ativada de acordo com o que
 * foi programado para tal, como por exemplo quando a tecla Escape for pressionada exibir o menu principal.</p>
 *
 * @see Keyboard
 * @see KeyboardDispatcher
 * @see KeyEvent
 * @see KeyTypedListener
 * @see KeyPressedListener
 * @see KeyReleasedListener
 * @see KeyListener
 * @see KeyAction
 * @see EnumKEY
 *
 * @author Andrew Mello
 */

public class VirtualKeyboard extends Keyboard implements Input, KeyboardDispatcher
{
	/**
	 * Instância para teclado virtual no padrão de projetos Singleton.
	 */
	private static final VirtualKeyboard INSTANCE = new VirtualKeyboard();


	/**
	 * Listener para habilitar e desabilitar o teclado virtual.
	 */
	private VirtualKeyBoardActiveListener activeListener;

	/**
	 * Código do estado de serviço que o teclado se encontrada.
	 */
	private int state = SERVICE_UNDEFINID;

	/**
	 * Lista contendo as escutas quando teclas detectarem ações ou caracteres.
	 */
	private List<KeyTypedListener> typedListeners;

	/**
	 * Lista contendo as escutas em quanto teclas estiverem pressionadas.
	 */
	private List<KeyPressedListener> pressedListeners;

	/**
	 * Lista contendo as escutas quando teclas forem liberadas.
	 */
	private List<KeyReleasedListener> releasedListeners;

	/**
	 * Vetor que irá identificar as teclas que estão pressionadas.
	 */
	private boolean keyPresseds[];

	/**
	 * Fila com as teclas que foram recentemente clicadas.
	 */
	private List<KeyAction> keyClickeds;

	@Override
	public void update(long delay)
	{
		while (keyClickeds.size() > 0)
		{
			KeyAction action = keyClickeds.get(0);

			if (action != null && !action.isOver())
				break;

			keyClickeds.remove(0);
		}
	}

	@Override
	public void start()
	{
		state = SERVICE_STARTED;

		if (typedListeners == null)		typedListeners = new DynamicList<>();
		if (pressedListeners == null)	pressedListeners = new DynamicList<>();
		if (releasedListeners == null)	releasedListeners = new DynamicList<>();
		if (keyPresseds == null)		keyPresseds = new boolean[EnumKEY.KEY_MAX];
		if (keyClickeds == null)		keyClickeds = new DynamicList<>();

		state = SERVICE_RUNNING;
	}

	@Override
	public void terminate()
	{
		typedListeners.clear();
		pressedListeners.clear();
		releasedListeners.clear();

		state = SERVICE_TERMINATED;
	}

	@Override
	public void interrupted()
	{
		state = SERVICE_STOPED;
	}

	@Override
	public int getState()
	{
		return state;
	}

	@Override
	public String getIdentificator()
	{
		return "Erakin.Keyboard";
	}

	@Override
	public void dispatch(KeyEvent event)
	{
		if (getState() != SERVICE_RUNNING)
			return;

		if (activeListener == null)
		{
			if (!Display.isCreated() || !Display.isActive())
				return;
		}
		else if (!activeListener.canDispatch())
			return;

		switch (event.getType())
		{
			case KeyEvent.KT_TYPED: dispatchTypedKey(event); break;
			case KeyEvent.KT_PRESSED: dispatchPressedKey(event); break;
			case KeyEvent.KT_RELEASED: dispatchRelasedKey(event); break;
		}
	}

	/**
	 * Procedimento chamado assim que um evento de teclado do tipo typed é detectado.
	 * Eventos typed são desencadeados sempre que um caracter ou ação de tecla ocorrer.
	 * @param event referência do evento contendo as informações da tecla digitada.
	 */

	private void dispatchTypedKey(KeyEvent event)
	{
		for (KeyTypedListener listener : typedListeners)
			listener.keyTyped(event);
	}

	/**
	 * Procedimento chamado assim que um evento de teclado do tipo pressed é detectado.
	 * Eventos typed são desencadeados somente em quanto uma tecla estiver pressionada.
	 * @param event referência do evento contendo as informações da tecla pressionada.
	 */

	private void dispatchPressedKey(KeyEvent event)
	{
		keyPresseds[event.getKey()] = true;

		for (KeyPressedListener listener : pressedListeners)
			listener.keyPressed(event);
	}

	/**
	 * Procedimento chamado assim que um evento de teclado do tipo released é detectado.
	 * Eventos typed são desencadeados somente uma vez após a tecla para de ser pressionada.
	 * @param event referência do evento contendo as informações da tecla liberada.
	 */

	private void dispatchRelasedKey(KeyEvent event)
	{
		keyPresseds[event.getKey()] = false;
		keyClickeds.add(new KeyAction(event.getKey()));

		for (KeyReleasedListener listener : releasedListeners)
			listener.keyReleased(event);
	}

	@Override
	public boolean wasClicked(int... key)
	{
		if (getState() != SERVICE_RUNNING)
			return false;

		for (int i = 0; i < key.length; i++)
		{
			boolean clicked = false;

			for (KeyAction action : keyClickeds)
				if (action != null && action.isKey(key[i]))
				{
					clicked = true;
					break;
				}

			if (!clicked)
				return false;
		}

		return true;
	}

	@Override
	public boolean isPressed(int... key)
	{
		if (getState() != SERVICE_RUNNING)
			return false;

		for (int i = 0; i < key.length; i++)
			if (IntUtil.interval(key[i], 0, keyPresseds.length - 1))
				if (!keyPresseds[key[i]])
					return false;

		return true;
	}

	@Override
	public void addListener(KeyListener listener)
	{
		if (getState() != SERVICE_RUNNING)
			return;

		addTypedListener(listener);
		addPressedListener(listener);
		addReleasedListener(listener);
	}

	@Override
	public void removeListener(KeyListener listener)
	{
		if (getState() != SERVICE_RUNNING)
			return;

		removeTypedListener(listener);
		removePressedListener(listener);
		removeReleasedListener(listener);
	}

	@Override
	public void addTypedListener(KeyTypedListener listener)
	{
		if (getState() != SERVICE_RUNNING)
			return;

		if (!typedListeners.contains(listener))
			typedListeners.add(listener);
	}

	@Override
	public void removeTypedListener(KeyTypedListener listener)
	{
		if (getState() != SERVICE_RUNNING)
			return;

		typedListeners.remove(listener);
	}

	@Override
	public void addPressedListener(KeyPressedListener listener)
	{
		if (getState() != SERVICE_RUNNING)
			return;

		if (!pressedListeners.contains(listener))
			pressedListeners.add(listener);
	}

	@Override
	public void removePressedListener(KeyPressedListener listener)
	{
		if (getState() != SERVICE_RUNNING)
			return;

		pressedListeners.remove(listener);
	}

	@Override
	public void addReleasedListener(KeyReleasedListener listener)
	{
		if (getState() != SERVICE_RUNNING)
			return;

		if (!releasedListeners.contains(listener))
			releasedListeners.add(listener);
	}

	@Override
	public void removeReleasedListener(KeyReleasedListener listener)
	{
		if (getState() != SERVICE_RUNNING)
			return;

		releasedListeners.remove(listener);
	}

	/**
	 * Esse listener é usado para determinar se o {@link VirtualKeyboard} deve ou não despachar eventos engatilhados.
	 * Quando o serviço for desativado nenhum evento será despachado, seja de pressed, released ou typed.
	 * @param activeListener referência do objeto que implementa o listener de teclado ativo.
	 */

	public void setActiveListener(VirtualKeyBoardActiveListener activeListener)
	{
		this.activeListener = activeListener;
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("state", state);
		description.append("keyClickeds", keyClickeds.size());
		description.append("typedListeners", typedListeners.size());
		description.append("pressedListeners", pressedListeners.size());
		description.append("releasedListeners", releasedListeners.size());

		return description.toString();
	}

	/**
	 * Procedimento que permite obter a única instância do teclado virtual.
	 * Utiliza o padrão Singleton para evitar a existência de mais instâncias.
	 * @return aquisição da instância para utilização das preferências de vídeo.
	 */

	public static VirtualKeyboard getInstance()
	{
		return INSTANCE;
	}
}
