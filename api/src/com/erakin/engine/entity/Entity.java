package com.erakin.engine.entity;

import org.lwjgl.util.vector.Vector3f;

import com.erakin.api.resources.model.Model;
import com.erakin.engine.Updatable;

/**
 * <h1>Entidade</h1>
 *
 * <p>As entidades representam diversos tipos de coisas como objetos de decora��o, constru��es, jogadores, monstros e npcs.
 * Sendo uma interface, ir� possuir alguns m�todos b�sicos que todas as entidades dever�o possuir na sua implementa��o.
 * Esses m�todos s�o usados principalmente pelos renderizadores para que possam obter dados dessas entidades.</p>
 *
 * <p>Os dados obtidos s�o referentes a propriedades/atributos que causam interfer�ncia durante a sua renderiza��o.
 * Como por exemplo os atributos de posicionamento no espa�o, sua rota��o se houver ou o seu tamanho atrav�s da escala.
 * Outro ponto importante � o modelo usado pela entidade, para determinar como ser� sua apar�ncia ao ser renderizado.</p>
 *
 * @see Updatable
 * @see Model
 *
 * @author Andrew Mello
 */

public interface Entity extends Updatable
{
	/**
	 * Modelagem tri-dimensional � usada para determinar como ser� a apar�ncia da entidade ao ser vista.
	 * Todas as entidades devem possuir uma modelagem definida, caso contr�rio ir� comprometer o sistema.
	 * @return aquisi��o da modelagem tri-dimensional para visualiza��o da entidade na tela.
	 */

	Model getModel();

	/**
	 * Posi��o indica em que lugar no espa�o tri-dimensional estar� sendo posicionado a entidade.
	 * Para a posi��o ser� considerado latitude, longitude e altura respectivos a X, Y e Z.
	 * @return aquisi��o do vetor contendo as posi��es respectivas aos eixos X, Y e Z.
	 */

	Vector3f getPosition();

	/**
	 * Rota��o pode determinar uma inclina��o como tamb�m indicar que foi girado para um lado.
	 * A rota��o ser� trabalhada em float e deve corresponder sempre a valores de 0 a 360.
	 * @return aquisi��o do vetor contendo as rota��es respectivas aos eixos X, Y e Z.
	 */

	Vector3f getRotation();

	/**
	 * Escala � usada para determinar o quanto ser� dimensionado em rela��o ao tamanho original.
	 * Todas escalas s�o definidas em float e tem como tamanho original sempre os <b>1.0f</b>.
	 * @return aquisi��o do vetor contendo as escalas respectivas aos eixos X, Y e Z.
	 */

	Vector3f getScale();
}
