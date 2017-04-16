package com.erakin.engine.resource.world;

import java.io.File;

import com.erakin.common.buffer.Buffer;
import com.erakin.common.buffer.BufferInput;

/**
 * <h1>Leitor de Dados para Mapa Padrão</h1>
 *
 * <p>Esse leitor faz uma implementação padrão de como irá funcionar a leitura de um mundo.
 * No seu método principal implementado através da interface cria um Buffer para ler os dados.
 * Assim, é repassado para o método <b>parse()</b> que deverá retornar os dados do mundo lido.</p>
 *
 * @see WorldReader
 * @see WorldData
 * @see Buffer
 *
 * @author Andrew
 */

public abstract class WorldReaderDefault implements WorldReader
{
	@Override
	public WorldData readWorld(File file) throws WorldException
	{
		Buffer buffer = new BufferInput(file);

		return parse(buffer, file);
	}

	/**
	 * Método de implementação necessária e chamado pelo método principal do leitor.
	 * Deverá garantir que os dados do mundo sejam carregador através do Buffer passado.
	 * @param buffer referência do buffer contendo todos os dados do mundo que foi lido.
	 * @param file referência do arquivo passado no método principal, caso necessário.
	 * @return aquisição de um objeto contendo os dados básicos para se carregar o mundo.
	 * @throws WorldException deve ocorrer apenas por violação de dados (corrompidos ou inválidos).
	 */

	public abstract WorldData parse(Buffer buffer, File file) throws WorldException;
}
