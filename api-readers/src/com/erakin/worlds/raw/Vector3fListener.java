package com.erakin.worlds.raw;

/**
 * Listener para Vetor de 3 Floats
 *
 * <p>Interface usada como listener para que possamos saber quando o v�rtice de uma unidade de terreno � alterada.
 * Ser� necess�rio implementar um m�todo que ir� dizer qual o novo valor do v�rtice e a unidade que foi alterada.</p>
 *
 * @see RawTerrainUnit
 *
 * @author Andrew
 */

public interface Vector3fListener
{
	void onSet(RawTerrainUnit source, float x, float y, float z);
}
