package com.erakin.engine.resource.world;

import java.io.File;

import com.erakin.common.buffer.Buffer;
import com.erakin.common.buffer.BufferInput;

/**
 * <h1>Leitor de Dados para Mapa Padr�o</h1>
 *
 * <p>Esse leitor faz uma implementa��o padr�o de como ir� funcionar a leitura de um mundo.
 * No seu m�todo principal implementado atrav�s da interface cria um Buffer para ler os dados.
 * Assim, � repassado para o m�todo <b>parse()</b> que dever� retornar os dados do mundo lido.</p>
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
	 * M�todo de implementa��o necess�ria e chamado pelo m�todo principal do leitor.
	 * Dever� garantir que os dados do mundo sejam carregador atrav�s do Buffer passado.
	 * @param buffer refer�ncia do buffer contendo todos os dados do mundo que foi lido.
	 * @param file refer�ncia do arquivo passado no m�todo principal, caso necess�rio.
	 * @return aquisi��o de um objeto contendo os dados b�sicos para se carregar o mundo.
	 * @throws WorldException deve ocorrer apenas por viola��o de dados (corrompidos ou inv�lidos).
	 */

	public abstract WorldData parse(Buffer buffer, File file) throws WorldException;
}
