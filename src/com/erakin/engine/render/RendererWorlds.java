package com.erakin.engine.render;

import org.erakin.api.lwjgl.math.Vector3i;

import com.erakin.engine.resource.World;

/**
 * <h1>Renderizador para Mundos</h1>
 *
 * <p>Essa interface é usada no gerenciador de renderização com as funcionalidades padrões que deve possuir.
 * Assim é possível fazer com que as renderizações possam atuar de formas diferentes para cada aplicação.
 * Podendo ainda melhorar a performance conforme o desenvolvedor deseja ou conforme a aplicação funcione.</p>
 *
 * <p>Possui funcionalidades bem básicas relacionadas sempre a mundos, somente mundos serão trabalhados.
 * Permitindo inserir um mundo no mesmo de modo que seja possível renderizar este na tela se visualizada.</p>
 *
 * @see RendererManager
 *
 * @author Andrew Mello
 */

public interface RendererWorlds extends RendererManager
{
	/**
	 * Irá indicar qual o mundo que será usado pelo renderizador para ser renderizado na tela.
	 * Para que o renderizador funcione, é necessário definir um novo mundo para se renderizar.
	 * @param world referência do mundo do qual será renderizado pelo renderizador de mundos.
	 */

	void setWorld(World world);

	/**
	 * Ao definir a posição de renderização será indicado a partir de que ponto será feito a renderização.
	 * A nova posição será o centro da renderização, tenho <b>n</b> de distância do seu ponto renderizado.
	 * @param position vetor contendo as coordenadas para renderização do novo mundo nos eixos X, Y e Z.
	 */

	void setRenderPosition(Vector3i position);

	/**
	 * Permite definir qual será o alcance de visão durante a renderização do mundo.
	 * Isso irá implicar no qual distante o jogador poderá ver ao seu redor.
	 * @param distance distância do qual poderá ser visualizada na tela.
	 */

	void setRenderRange(int distance);
}
