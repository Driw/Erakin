package com.erakin.engine;

/**
 * <h1>Adaptador de Escuta para Engine</h1>
 *
 * <p>Esses adaptadores irão apenas implementar a Escuta para Engine de forma oca, sem operações.
 * Eles devem ser usados pelos desenvolvedores afim de facilitar as codificações das escutas.</p>
 *
 * <p>Assim não será preciso criar uma nova classe para preencher os métodos ou então instanciar
 * no momento da utilização tendo que deixar todos os métodos exibidos no meio do código.
 * Por tanto, basta sobrescrever apenas aqueles métodos que realmente forem utilizados.</p>
 *
 * @author Andrew
 */

public abstract class EngineAdapter implements EngineListener
{
	@Override
	public void onInitiate()
	{
		
	}

	@Override
	public void onClosed()
	{
		
	}

	@Override
	public void onShutdown()
	{
		
	}
}
