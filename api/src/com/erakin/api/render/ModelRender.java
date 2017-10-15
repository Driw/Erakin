package com.erakin.api.render;

import com.erakin.api.lwjgl.math.enumeration.DrawElement;

/**
 * <h1>Modelo Renderizável</h1>
 *
 * <p>Objetos que implementem esta interface determina que o mesmo possui conteúdo suficiente para renderizar um modelo.
 * Deverá implementar alguns métodos básicos que é utilizado por um renderizador de modelos padrão da Engine.
 * Caso o renderizador não seja de base padrão essa interface pode não ser suficiente para renderizar o modelo.</p>
 *
 * @see DrawElement
 *
 * @author Andrew
 */

public interface ModelRender
{
	/**
	 * Deve fazer a vinculação desse objeto com o OpenGL para ser utilizado.
	 * Uma vez que tenha sido vinculado, todas interações no OpenGL será com esse objeto.
	 * Funciona de forma diferente para cada objeto, de acordo com a identificação e tipo.
	 */

	void bind();

	/**
	 * Quando chamado deve fazer desfazer a vinculação de qualquer objeto usado.
	 * Uma vez que tenha sido desvinculado, não terá interação com o OpenGL.
	 * Funciona da mesma forma que qualquer outro objeto desse tipo.
	 */

	void unbind();

	/**
	 * Ao ser chamado irá acessar o VAO respectivo a essa modelagem e desenhar esta.
	 * O resultado do desenhado varia de acordo com o tipo de computação gráfica usado.
	 * @param mode em que modo deverá ser feito o desenho dos vértices da modelagem.
	 * @see DrawElement
	 */

	void draw(DrawElement mode);

	/**
	 * Refletividade indica o quanto a luz será refletida quando atingir o objeto em questão (modelagem).
	 * Esse reflexo varia ainda também com as variáveis da luz como distância e sua intensidade (força).
	 * @return aquisição do nível da força para refletir iluminações através do brilho.
	 */

	float getReflectivity();

	/**
	 * Redução do brilho é usado na computação gráfica para reduzir o efeito da luz ao atingir um objeto.
	 * Essa redução possui duas variantes, a distância da direção do reflexo em relação com a câmera,
	 * e o nível de redução do brilho, quanto maior ambos os valores, menor será o brilho refletido visto.
	 * @return aquisição do nível da redução do brilho para iluminações refletidas.
	 */

	float getShineDamping();
}
