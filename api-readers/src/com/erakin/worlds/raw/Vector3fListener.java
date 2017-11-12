package com.erakin.worlds.raw;

/**
 * Listener para Vetor de 3 Floats
 *
 * <p>Interface usada como listener para que possamos saber quando o vértice de uma unidade de terreno é alterada.
 * Será necessário implementar um método que irá dizer qual o novo valor do vértice e a unidade que foi alterada.</p>
 *
 * @see RawTerrainUnit
 *
 * @author Andrew
 */

public interface Vector3fListener
{
	void onSet(RawTerrainUnit source, float x, float y, float z);
}
