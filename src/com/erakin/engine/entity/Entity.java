package com.erakin.engine.entity;

import org.lwjgl.util.vector.Vector3f;

import com.erakin.api.resources.model.Model;
import com.erakin.engine.Updatable;

/**
 * <h1>Entidade</h1>
 *
 * <p>As entidades representam diversos tipos de coisas como objetos de decoração, construções, jogadores, monstros e npcs.
 * Sendo uma interface, irá possuir alguns métodos básicos que todas as entidades deverão possuir na sua implementação.
 * Esses métodos são usados principalmente pelos renderizadores para que possam obter dados dessas entidades.</p>
 *
 * <p>Os dados obtidos são referentes a propriedades/atributos que causam interferência durante a sua renderização.
 * Como por exemplo os atributos de posicionamento no espaço, sua rotação se houver ou o seu tamanho através da escala.
 * Outro ponto importante é o modelo usado pela entidade, para determinar como será sua aparência ao ser renderizado.</p>
 *
 * @see Updatable
 * @see Model
 *
 * @author Andrew Mello
 */

public interface Entity extends Updatable
{
	/**
	 * Modelagem tri-dimensional é usada para determinar como será a aparência da entidade ao ser vista.
	 * Todas as entidades devem possuir uma modelagem definida, caso contrário irá comprometer o sistema.
	 * @return aquisição da modelagem tri-dimensional para visualização da entidade na tela.
	 */

	Model getModel();

	/**
	 * Posição indica em que lugar no espaço tri-dimensional estará sendo posicionado a entidade.
	 * Para a posição será considerado latitude, longitude e altura respectivos a X, Y e Z.
	 * @return aquisição do vetor contendo as posições respectivas aos eixos X, Y e Z.
	 */

	Vector3f getPosition();

	/**
	 * Rotação pode determinar uma inclinação como também indicar que foi girado para um lado.
	 * A rotação será trabalhada em float e deve corresponder sempre a valores de 0 a 360.
	 * @return aquisição do vetor contendo as rotações respectivas aos eixos X, Y e Z.
	 */

	Vector3f getRotation();

	/**
	 * Escala é usada para determinar o quanto será dimensionado em relação ao tamanho original.
	 * Todas escalas são definidas em float e tem como tamanho original sempre os <b>1.0f</b>.
	 * @return aquisição do vetor contendo as escalas respectivas aos eixos X, Y e Z.
	 */

	Vector3f getScale();
}
