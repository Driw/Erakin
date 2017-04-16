package com.erakin.engine;

import static org.diverproject.log.LogSystem.logWarning;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.diverproject.util.collection.Map;
import org.diverproject.util.collection.Map.MapItem;
import org.diverproject.util.collection.abstraction.DynamicMap;
import org.diverproject.util.stream.Input;
import org.diverproject.util.stream.StreamException;
import org.diverproject.util.stream.StreamRuntimeException;
import org.diverproject.util.stream.implementation.InputBuilder;
import org.diverproject.util.stream.implementation.option.OptionInput;
import org.diverproject.util.stream.implementation.option.OptionListInput;
import org.diverproject.util.stream.implementation.option.OptionOutput;

/**
 * <h1>Padrão de Preferências</h1>
 *
 * <p>Implementação básica para o funcionamento de construções de objetos contendo preferências.
 * Utiliza uma mapeamento dinâmico para facilitar a busca das preferências armazenadas.
 * Garante que os setters definam um valor novo ou atualize se já existir a preferência.</p>
 *
 * @see Preferences
 *
 * @author Andrew Mello
 */

public class PreferencesDefault implements Preferences
{
	/**
	 * Mapeamento das preferências que já foram definidas.
	 */
	protected final Map<String, Object> options;

	/**
	 * Inicialize um mapeamento por caracteres dinâmico para armazenamento das preferências.
	 * Também utiliza <code>getOptions</code> para determinar algumas opções iniciais.
	 */

	public PreferencesDefault()
	{
		options = new DynamicMap<String, Object>();

		for (Option option : getOptions())
			if (option != null)
				if (!options.add(option.key, option.value))
					logWarning("falha ao carregar opação (key: %s, value: %s)", option.key, option.value);
	}

	/**
	 * Garante a inicialização das preferências a partir da leitura do arquivo.
	 * Deve fazer a leitura do arquivo que contém as definições de preferências,
	 * a forma como o armazenamento/leitura deve ser especificado pelo tipo.
	 */

	public void initiate()
	{
		File file = new File(getPreferencesFilePath());

		if (file.exists())
		{
			try {

				InputBuilder builder = new InputBuilder();
				Input input = builder.newInput(file);
				OptionInput optionInput = new OptionListInput(input);

				load(optionInput);

			} catch (IOException e) {
				logWarning("falha ao inicializar preferências (arquivo: %s)", file.getPath());
			}
		}
	}

	/**
	 * Garante a terminação das preferências a partir do salvamento do arquivo.
	 * Deve fazer a escrita do arquivo contendo o valor das preferências de forma
	 * que seja possível ser lida de acordo com o modo de inicialização.
	 */

	public void termiante()
	{
		File file = new File(getPreferencesFilePath());

		try {

//			StreamFactory factory = new DefaultStreamFactory();
//			OptionBuilder builder = factory.newOptionBuilder();
//			OutputOption output = builder.newOutputOption(file);

//			save(output);

		} catch (StreamRuntimeException e) {
			logWarning("falha ao finalizar preferências (arquivo: %s)", file.getPath());
		}
	}

	/**
	 * Carrega as preferências armazenadas em uma stream de opções de acordo com a JavaUtil.
	 * @param input referência da stream do qual contém as opções para preferências.
	 * @throws StreamException apenas quando o formato for inválido ou fim inesperado.
	 * @see InputOption
	 */

	protected void load(OptionInput input)
	{
		Iterator<MapItem<String, Object>> iterator = options.iteratorItems();

		while (iterator.hasNext())
		{
			MapItem<String, Object> item = iterator.next();

			if (item.value instanceof Integer)
				setOptionInt(item.key, (Integer) item.value);

			else if (item.value instanceof Float)
				setOptionFloat(item.key, (Float) item.value);

			else if (item.value instanceof Boolean)
				setOptionBoolean(item.key, (Boolean) item.value);

			else if (item.value instanceof String)
				setOptionString(item.key, (String) item.value);
		}
	}

	/**
	 * Salva as preferências existentes em uma stream de opções de acordo com a JavaUtil.
	 * @param output referência da stream do qual deverá armazenar as opções de preferência.
	 * @throws StreamException apenas quando não for possível escrever ou fim inesperado.
	 */

	protected void save(OptionOutput output)
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
				output.putInt(key, (Integer) value);

			else if (value instanceof Float)
				output.putFloat(key, (Float) value);

			else if (value instanceof Boolean)
				output.putBoolean(key, (Boolean) value);

			else if (value instanceof String)
				output.putString(key, (String) value);
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
	 * Usado internamente para determinar o arquivo que será lido contendo as preferências salvas.
	 * @return aquisição do caminho do arquivo contendo as preferências.
	 */

	protected String getPreferencesFilePath()
	{
		return "preferences.prf";
	}

	/**
	 * Para permitir que mesmo se não encontrar um arquivo com preferências,
	 * as preferências padrões utilizam esse vetor para garantir que estas vão
	 * existir, assumindo valores padrões pré-definidos durante a construção.
	 * @return vetor contendo as opções do qual serão usadas como iniciais.
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
	 * <h1>Opção</h1>
	 *
	 * <p>As opções nada mais são do que items de mapeamento utilizados nas tabelas espalhadas.
	 * Não possuem nada de diferente, apenas utilizado para melhor identificação da codificação.</p>
	 *
	 * @see MapItem
	 *
	 * @author Andrew Mello
	 */

	protected class Option extends MapItem<String, Object>
	{
		/**
		 * Constrói uma nova opção sendo necessário determinar o seu valor e chave.
		 * @param key chave usada para identificar o elemento no mapeador.
		 * @param value valor armazenado na respectiva chave acima.
		 */

		public Option(String key, Object value)
		{
			super(key, value);
		}
	}
}
