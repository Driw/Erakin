package com.erakin.engine;

import static org.diverproject.log.LogSystem.logException;
import static org.diverproject.log.LogSystem.logWarning;

import java.io.File;
import java.util.Iterator;

import org.diverproject.ini.JIni;
import org.diverproject.ini.JIniException;
import org.diverproject.util.FileUtil;
import org.diverproject.util.collection.Map;
import org.diverproject.util.collection.Map.MapItem;
import org.diverproject.util.collection.abstraction.DynamicMap;

/**
 * <h1>Padr�o de Prefer�ncias</h1>
 *
 * <p>Implementa��o b�sica para o funcionamento de constru��es de objetos contendo prefer�ncias.
 * Utiliza uma mapeamento din�mico para facilitar a busca das prefer�ncias armazenadas.
 * Garante que os setters definam um valor novo ou atualize se j� existir a prefer�ncia.</p>
 *
 * @see Preferences
 *
 * @author Andrew Mello
 */

public class PreferencesDefault implements Preferences
{
	/**
	 * Mapeamento das prefer�ncias que j� foram definidas.
	 */
	protected final Map<String, Object> options;

	/**
	 * Inicialize um mapeamento por caracteres din�mico para armazenamento das prefer�ncias.
	 * Tamb�m utiliza <code>getOptions</code> para determinar algumas op��es iniciais.
	 */

	public PreferencesDefault()
	{
		options = new DynamicMap<String, Object>();

		for (Option option : getOptions())
			if (option != null)
				if (!options.add(option.key, option.value))
					logWarning("falha ao carregar opa��o (key: %s, value: %s)", option.key, option.value);
	}

	/**
	 * Garante a inicializa��o das prefer�ncias a partir da leitura do arquivo.
	 * Deve fazer a leitura do arquivo que cont�m as defini��es de prefer�ncias,
	 * a forma como o armazenamento/leitura deve ser especificado pelo tipo.
	 */

	public void initiate()
	{
		File file = new File(getPreferencesFilePath());

		if (file.exists())
		{
			switch (FileUtil.getExtension(file.getAbsolutePath()))
			{
				case "ini":
					try {

						JIni ini = new JIni(file);
						ini.load();
						load(ini);

					} catch (JIniException e) {
						logWarning("falha ao inicializar prefer�ncias (arquivo: %s)", file.getPath());
						logException(e);
					}
					break;
			}
		}
	}

	/**
	 * Garante a termina��o das prefer�ncias a partir do salvamento do arquivo.
	 * Deve fazer a escrita do arquivo contendo o valor das prefer�ncias de forma
	 * que seja poss�vel ser lida de acordo com o modo de inicializa��o.
	 */

	public void termiante()
	{
		File file = new File(getPreferencesFilePath());

		if (file.exists())
		{
			switch (FileUtil.getExtension(file.getAbsolutePath()))
			{
				case "ini":
					try {

						JIni ini = new JIni(file);
						save(ini);
						ini.save();

					} catch (JIniException e) {
						logWarning("falha ao inicializar prefer�ncias (arquivo: %s)", file.getPath());
						logException(e);
					}
					break;
			}
		}
	}

	/**
	 * Carrega as prefer�ncias existentes lidas de um arquivo em formato INI.
	 * Ir� iterar todas as prefer�ncias existentes e atualizar seu valor se carregado.
	 * Caso o a prefer�ncia n�o exista ir� manter o valor anterior a leitura do INI.
	 * @param ini refer�ncia do objecto que carrega as configura��es INI carregadas.
	 */

	protected void load(JIni ini)
	{
		Iterator<MapItem<String, Object>> iterator = options.iteratorItems();

		while (iterator.hasNext())
		{
			MapItem<String, Object> item = iterator.next();

			if (item.value instanceof Integer && ini.contains(item.key))
				options.update(item.key, ini.getInt(item.key));

			else if (item.value instanceof Float && ini.contains(item.key))
				options.update(item.key, ini.getFloat(item.key));

			else if (item.value instanceof Boolean && ini.contains(item.key))
				options.update(item.key, ini.getBoolean(item.key));

			else if (item.value instanceof String && ini.contains(item.key))
				options.update(item.key, ini.getString(item.key));
		}
	}

	/**
	 * Salva as prefer�ncias aqui existentes em um arquivo INI atrav�s do seguinte objeto:
	 * @param ini refer�ncia do objeto que permite escrever em formato de arquivo INI.
	 */

	protected void save(JIni ini)
	{
		int i = 0;
		int size = options.size();
		String keys[] = new String[size];
		Object values[] = new Object[size];

		for (Object object : options)
			values[i++] = object;

		Iterator<String> iterator = options.iteratorKey();

		for (i = 0; iterator.hasNext(); i++)
			keys[i] = iterator.next();

		for (i = 0; i < size; i++)
		{
			String key = keys[i];
			Object value = values[i];

			if (value instanceof Integer)
				ini.putInt(key, (Integer) value);

			else if (value instanceof Float)
				ini.putFloat(key, (Float) value);

			else if (value instanceof Boolean)
				ini.putBoolean(key, (Boolean) value);

			else if (value instanceof String)
				ini.putString(key, (String) value);
		}
	}

	@Override
	public int getOptionInt(String key)
	{
		Object object = options.get(key);

		return object instanceof Integer ? (Integer) object : 0;
	}

	@Override
	public void setOptionInt(String key, int value)
	{
		if (options.containsKey(key))
			options.update(key, value);
		else
			options.add(key, value);
	}

	@Override
	public float getOptionFloat(String key)
	{
		Object object = options.get(key);

		return object instanceof Float ? (Float) object : 0f;
	}

	@Override
	public void setOptionFloat(String key, float value)
	{
		if (options.containsKey(key))
			options.update(key, value);
		else
			options.add(key, value);		
	}

	@Override
	public boolean getOptionBoolean(String key)
	{
		Object object = options.get(key);

		return object instanceof Boolean ? (Boolean) object : false;
	}

	@Override
	public void setOptionBoolean(String key, boolean value)
	{
		if (options.containsKey(key))
			options.update(key, value);
		else
			options.add(key, value);		
	}

	@Override
	public String getOptionString(String key)
	{
		Object object = options.get(key);

		return object instanceof String ? (String) object : "";
	}

	@Override
	public void setOptionString(String key, String value)
	{
		if (options.containsKey(key))
			options.update(key, value);
		else
			options.add(key, value);		
	}

	/**
	 * Usado internamente para determinar o arquivo que ser� lido contendo as prefer�ncias salvas.
	 * @return aquisi��o do caminho do arquivo contendo as prefer�ncias.
	 */

	protected String getPreferencesFilePath()
	{
		return "preferences.ini";
	}

	/**
	 * Para permitir que mesmo se n�o encontrar um arquivo com prefer�ncias,
	 * as prefer�ncias padr�es utilizam esse vetor para garantir que estas v�o
	 * existir, assumindo valores padr�es pr�-definidos durante a constru��o.
	 * @return vetor contendo as op��es do qual ser�o usadas como iniciais.
	 */

	protected Option[] getOptions()
	{
		return new Option[] {};
	}

	@Override
	public String toString()
	{
		String options = this.options.toString();
		options = options.substring(11, options.length() - 1);

		return String.format("%s[%s]", getClass().getSimpleName(), options);
	}

	/**
	 * <h1>Op��o</h1>
	 *
	 * <p>As op��es nada mais s�o do que items de mapeamento utilizados nas tabelas espalhadas.
	 * N�o possuem nada de diferente, apenas utilizado para melhor identifica��o da codifica��o.</p>
	 *
	 * @see MapItem
	 *
	 * @author Andrew Mello
	 */

	protected class Option extends MapItem<String, Object>
	{
		/**
		 * Constr�i uma nova op��o sendo necess�rio determinar o seu valor e chave.
		 * @param key chave usada para identificar o elemento no mapeador.
		 * @param value valor armazenado na respectiva chave acima.
		 */

		public Option(String key, Object value)
		{
			super(key, value);
		}
	}
}
