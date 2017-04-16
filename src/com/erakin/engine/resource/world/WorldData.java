package com.erakin.engine.resource.world;

import com.erakin.engine.resource.TerrainLoader;

/**
 * <h1>Dados do Mundo</h1>
 *
 * <p>Interface usada internamente pelo carregador de mundos para tornar o carregamento din�mico.
 * Os leitores de mundos dever�o sempre retornar um objeto do qual possua essa interface em sua classe.
 * Assim, todas as informa��es b�sicas para se criar um novo mundo poder�o ser obtidas por completo.</p>
 *
 * <p>As informa��es v�o desde as descritivas como nome e pr�-fixo de reconhecimento do mundo.
 * Como algumas mais de configura��o: tamanho que cada terreno dever� ter e quantos terrenos ter�.
 * E por �ltimas as que ir�o definir o terreno de fato, como unidade de cada c�lula e carregador.</p>
 *
 * @see TerrainLoader
 *
 * @author Andrew
 */

public interface WorldData
{
	/**
	 * Nome do mapa � usada de forma visual para que seja poss�vel ao usu�rio identificar o mundo.
	 * O nome do mundo aparece como informa��o adicional nos logs e pode ser usado na interface de usu�rio.
	 * @return aquisi��o do nome do mundo do qual os dados ir�o pertencer.
	 */

	String getName();

	/**
	 * Pr�-fixo � usado mais internamente para facilitar a programa��o do jogo com os mundos.
	 * De modo que n�o seja necess�rio decorar nomes grandes (se for o caso), e sim alguns caracteres.
	 * @return aquisi��o do pr�-fixo do mundo do qual os dados ir�o pertencer.
	 */

	String getPrefix();

	/**
	 * A pasta ir� indicar onde poder� ser encontrado os arquivos dos terrenos quando necess�rio carregar.
	 * Essa pasta pode ser relativa ao arquivo de configura��o ou uma defini��o direta do caminho em disco.
	 * @return aquisi��o do caminho da pasta para leitura dos terrenos usado pelo carregador de terrenos.
	 */

	String getFolder();

	/**
	 * Carregador � usado para que quando um determinado terreno for solicitado possa ser carregado.
	 * Para aliviar o peso em mem�ria RAM, nem todos os terrenos s�o carregados junto com o mundo.
	 * Assim � poss�vel carregar partes do mundo conforme necess�rio, e descart�-los da mesma forma.
	 * @return aquisi��o do objeto que possui a interface para carregamento dos terrenos.
	 */

	TerrainLoader getTerrainLoader();

	/**
	 * Mundos s�o formados por terrenos de um �nico tamanho, assim � poss�vel organiz�-los em grade.
	 * Isso facilita o mundo tamb�m a gerenciar os terrenos atrav�s de uma matriz relativa a X e Y.
	 * @return aquisi��o da quantidade de terrenos que podem ser alocados no eixo da longitude.
	 */

	int getWidth();

	/**
	 * Mundos s�o formados por terrenos de um �nico tamanho, assim � poss�vel organiz�-los em grade.
	 * Isso facilita o mundo tamb�m a gerenciar os terrenos atrav�s de uma matriz relativa a X e Y.
	 * @return aquisi��o da quantidade de terrenos que podem ser alocados no eixo da latitude.
	 */

	int getLength();

	/**
	 * Mundos s�o formados por terrenos de um �nico tamanho, para isso � necess�rio um definido.
	 * Todos os terrenos carregados devem possuir esse tamanho para evitar problemas de renderiza��o.
	 * @return aquisi��o de quantas c�lulas um terreno ter� no eixo da longitude.
	 */

	int getTerrainWidth();

	/**
	 * Mundos s�o formados por terrenos de um �nico tamanho, para isso � necess�rio um definido.
	 * Todos os terrenos carregados devem possuir esse tamanho para evitar problemas de renderiza��o.
	 * @return aquisi��o de quantas c�lulas um terreno ter� no eixo da latitude.
	 */

	int getTerrainLength();

	/**
	 * Unidade ir� definir qual o tamanho que um terreno ir� possuir para cada c�lula.
	 * O padr�o a ser usado deve ser 1.0f que representa 100% do tamanho normal.
	 * @return aquisi��o do tamanho de cada unidade (c�lula) do terreno.
	 */

	float getUnit();
}
