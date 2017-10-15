package com.erakin.api.render;

import com.erakin.api.lwjgl.math.enumeration.DrawElement;

/**
 * <h1>Modelo Renderiz�vel</h1>
 *
 * <p>Objetos que implementem esta interface determina que o mesmo possui conte�do suficiente para renderizar um modelo.
 * Dever� implementar alguns m�todos b�sicos que � utilizado por um renderizador de modelos padr�o da Engine.
 * Caso o renderizador n�o seja de base padr�o essa interface pode n�o ser suficiente para renderizar o modelo.</p>
 *
 * @see DrawElement
 *
 * @author Andrew
 */

public interface ModelRender
{
	/**
	 * Deve fazer a vincula��o desse objeto com o OpenGL para ser utilizado.
	 * Uma vez que tenha sido vinculado, todas intera��es no OpenGL ser� com esse objeto.
	 * Funciona de forma diferente para cada objeto, de acordo com a identifica��o e tipo.
	 */

	void bind();

	/**
	 * Quando chamado deve fazer desfazer a vincula��o de qualquer objeto usado.
	 * Uma vez que tenha sido desvinculado, n�o ter� intera��o com o OpenGL.
	 * Funciona da mesma forma que qualquer outro objeto desse tipo.
	 */

	void unbind();

	/**
	 * Ao ser chamado ir� acessar o VAO respectivo a essa modelagem e desenhar esta.
	 * O resultado do desenhado varia de acordo com o tipo de computa��o gr�fica usado.
	 * @param mode em que modo dever� ser feito o desenho dos v�rtices da modelagem.
	 * @see DrawElement
	 */

	void draw(DrawElement mode);

	/**
	 * Refletividade indica o quanto a luz ser� refletida quando atingir o objeto em quest�o (modelagem).
	 * Esse reflexo varia ainda tamb�m com as vari�veis da luz como dist�ncia e sua intensidade (for�a).
	 * @return aquisi��o do n�vel da for�a para refletir ilumina��es atrav�s do brilho.
	 */

	float getReflectivity();

	/**
	 * Redu��o do brilho � usado na computa��o gr�fica para reduzir o efeito da luz ao atingir um objeto.
	 * Essa redu��o possui duas variantes, a dist�ncia da dire��o do reflexo em rela��o com a c�mera,
	 * e o n�vel de redu��o do brilho, quanto maior ambos os valores, menor ser� o brilho refletido visto.
	 * @return aquisi��o do n�vel da redu��o do brilho para ilumina��es refletidas.
	 */

	float getShineDamping();
}
