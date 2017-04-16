package com.erakin.engine;

/**
 * <h1>Adaptador de Escuta para Engine</h1>
 *
 * <p>Esses adaptadores ir�o apenas implementar a Escuta para Engine de forma oca, sem opera��es.
 * Eles devem ser usados pelos desenvolvedores afim de facilitar as codifica��es das escutas.</p>
 *
 * <p>Assim n�o ser� preciso criar uma nova classe para preencher os m�todos ou ent�o instanciar
 * no momento da utiliza��o tendo que deixar todos os m�todos exibidos no meio do c�digo.
 * Por tanto, basta sobrescrever apenas aqueles m�todos que realmente forem utilizados.</p>
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
