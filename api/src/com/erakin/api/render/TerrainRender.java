package com.erakin.api.render;

import com.erakin.api.resources.Texture;

/**
 * <h1>Terreno Renderizável</h1>
 *
 * <p>Objetos que implementem esta interface determina que o mesmo possui conteúdo suficiente para renderizar um terreno.
 * Deverá implementar alguns métodos básicos que é utilizado por um renderizador de terrenos padrão da Engine.
 * Caso o renderizador não seja de base padrão essa interface pode não ser suficiente para renderizar o terreno.</p>
 *
 * @see ModelRender
 * @see Texture
 *
 * @author Andrew
 */

public interface TerrainRender
{
	/**
	 * Terrenos são formados através de uma modelagem tri-dimensiona e usados para serem visualizados.
	 * @return aquisição da modelagem tri-dimensional renderizável respectiva a esse terreno.
	 */

	ModelRender getModel();

	/**
	 * Um mundo é formado por uma grande de terrenos, onde todos os terrenos possuem o mesmo tamanho.
	 * Coordenadas de um terreno permitem saber em que posição da grade de terrenos ele se encontra.
	 * @return aquisição da coordenada na longitude do terreno na grade de terrenos do mundo.
	 */

	int getX();

	/**
	 * Um mundo é formado por uma grande de terrenos, onde todos os terrenos possuem o mesmo tamanho.
	 * Coordenadas de um terreno permitem saber em que posição da grade de terrenos ele se encontra.
	 * @return aquisição da coordenada na latitude do terreno na grade de terrenos do mundo.
	 */

	int getZ();

	/**
	 * Terrenos são formados por diversas células (geralmente quadrados) formados por duas faces.
	 * @return aquisição do tamanho do terreno em células no eixo da longitude.
	 */

	int getWidth();

	/**
	 * Terrenos são formados por diversas células (geralmente quadrados) formados por duas faces.
	 * @return aquisição do tamanho do terreno em células no eixo da latitude.
	 */

	int getLength();

	/**
	 * Terrenos podem usar uma textura única que permite visualizá-lo com cores que define sua altitude.
	 * Por exemplo: o nível mais baixo terá um tom azul em quanto o mais alto um tom vermelho.
	 * Logo todos os níveis do terreno terão uma cor relativa ao seu nível em degradê de azul e vermelho.
	 * @return aquisição da textura que irá representar o nivelamento do terreno em degradê.
	 */

	Texture getHeightTexture();
}
