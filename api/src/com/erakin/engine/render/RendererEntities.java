package com.erakin.engine.render;

import com.erakin.engine.entity.Entity;

/**
 * <h1>Renderizador para Entidades</h1>
 *
 * <p>Essa interface é usada no gerenciador de renderização com as funcionalidades padrões que deve possuir.
 * Assim é possível fazer com que as renderizações possam atuar de formas diferentes para cada aplicação.
 * Podendo ainda melhorar a performance conforme o desenvolvedor deseja ou conforme a aplicação funcione.</p>
 *
 * <p>Possui funcionalidades bem básicas relacionadas sempre a entidades, somente entidades serão trabalhadas.
 * Permitindo inserir uma entidade no mesmo de modo que seja possível renderizar esta na tela se visualizada.</p>
 *
 * @see RendererManager
 *
 * @author Andrew Mello
 */

public interface RendererEntities extends RendererManager
{
	/**
	 * Permite inserir uma nova entidade para ser renderizada pelo renderizador de entidades.
	 * A forma como será inserido não é especificado, pode ser uma lista, fila ou outra.
	 * Além disso pode ser feito implementação para removê-los em todos os loops ou mantê-los.
	 * @param entity referência da entidade do qual deve ser adicionada a coleção.
	 */

	void insert(Entity entity);
}
