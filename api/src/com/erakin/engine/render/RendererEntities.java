package com.erakin.engine.render;

import com.erakin.engine.entity.Entity;

/**
 * <h1>Renderizador para Entidades</h1>
 *
 * <p>Essa interface � usada no gerenciador de renderiza��o com as funcionalidades padr�es que deve possuir.
 * Assim � poss�vel fazer com que as renderiza��es possam atuar de formas diferentes para cada aplica��o.
 * Podendo ainda melhorar a performance conforme o desenvolvedor deseja ou conforme a aplica��o funcione.</p>
 *
 * <p>Possui funcionalidades bem b�sicas relacionadas sempre a entidades, somente entidades ser�o trabalhadas.
 * Permitindo inserir uma entidade no mesmo de modo que seja poss�vel renderizar esta na tela se visualizada.</p>
 *
 * @see RendererManager
 *
 * @author Andrew Mello
 */

public interface RendererEntities extends RendererManager
{
	/**
	 * Permite inserir uma nova entidade para ser renderizada pelo renderizador de entidades.
	 * A forma como ser� inserido n�o � especificado, pode ser uma lista, fila ou outra.
	 * Al�m disso pode ser feito implementa��o para remov�-los em todos os loops ou mant�-los.
	 * @param entity refer�ncia da entidade do qual deve ser adicionada a cole��o.
	 */

	void insert(Entity entity);
}
