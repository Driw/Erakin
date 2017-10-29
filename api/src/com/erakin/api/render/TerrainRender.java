package com.erakin.api.render;

import org.diverproject.util.collection.List;

import com.erakin.api.resources.Texture;

/**
 * <h1>Terreno Renderiz�vel</h1>
 *
 * <p>Objetos que implementem esta interface determina que o mesmo possui conte�do suficiente para renderizar um terreno.
 * Dever� implementar alguns m�todos b�sicos que � utilizado por um renderizador de terrenos padr�o da Engine.
 * Caso o renderizador n�o seja de base padr�o essa interface pode n�o ser suficiente para renderizar o terreno.</p>
 *
 * @see ModelRender
 * @see Texture
 *
 * @author Andrew
 */

public interface TerrainRender
{
	/**
	 * Terrenos s�o formados atrav�s de uma modelagem tri-dimensiona e usados para serem visualizados.
	 * @return aquisi��o da modelagem tri-dimensional renderiz�vel respectiva a esse terreno.
	 */

	ModelRender getModel();

	/**
	 * Um mundo � formado por uma grande de terrenos, onde todos os terrenos possuem o mesmo tamanho.
	 * Coordenadas de um terreno permitem saber em que posi��o da grade de terrenos ele se encontra.
	 * @return aquisi��o da coordenada na longitude do terreno na grade de terrenos do mundo.
	 */

	int getX();

	/**
	 * Um mundo � formado por uma grande de terrenos, onde todos os terrenos possuem o mesmo tamanho.
	 * Coordenadas de um terreno permitem saber em que posi��o da grade de terrenos ele se encontra.
	 * @return aquisi��o da coordenada na latitude do terreno na grade de terrenos do mundo.
	 */

	int getZ();

	/**
	 * Terrenos s�o formados por diversas c�lulas (geralmente quadrados) formados por duas faces.
	 * @return aquisi��o do tamanho do terreno em c�lulas no eixo da longitude.
	 */

	int getWidth();

	/**
	 * Terrenos s�o formados por diversas c�lulas (geralmente quadrados) formados por duas faces.
	 * @return aquisi��o do tamanho do terreno em c�lulas no eixo da latitude.
	 */

	int getLength();

	/**
	 * Um terreno pode ser renderizado por m�ltiplas texturas e para isso � necess�rio saber quais usar.
	 * @return aquisi��o da lista com todas as texturas dispon�veis para texturiza��o do terreno.
	 */

	List<Texture> getTextures();

	/**
	 * Terrenos podem usar uma textura �nica que permite visualiz�-lo com cores que define sua altitude.
	 * Por exemplo: o n�vel mais baixo ter� um tom azul em quanto o mais alto um tom vermelho.
	 * Logo todos os n�veis do terreno ter�o uma cor relativa ao seu n�vel em degrad� de azul e vermelho.
	 * @return aquisi��o da textura que ir� representar o nivelamento do terreno em degrad�.
	 */

	Texture getHeightTexture();

	/**
	 * Um terreno pode ser renderizado utilizando dois tipos de texturas e para cada modo a l�gica muda.
	 * No primeiro modo apenas uma textura � vinculada e renderizada que � para visualizar a eleva��o do terreno.
	 * Nesse caso, uma textura em degrad� � usada, conforme o n�vel do relevo ser� usado um polo da textura.
	 * No segundo modo, diversas texturas podem ser utilizadas, sendo necess�rio outra forma de renderiza��o.
	 * @return true se for para usar a textura de n�vel de eleva��o do terreno ou false para texturas m�ltiplas.
	 */

	boolean isHeightTexture();
}
