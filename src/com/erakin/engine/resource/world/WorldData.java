package com.erakin.engine.resource.world;

import com.erakin.engine.resource.TerrainLoader;

/**
 * <h1>Dados do Mundo</h1>
 *
 * <p>Interface usada internamente pelo carregador de mundos para tornar o carregamento dinâmico.
 * Os leitores de mundos deverão sempre retornar um objeto do qual possua essa interface em sua classe.
 * Assim, todas as informações básicas para se criar um novo mundo poderão ser obtidas por completo.</p>
 *
 * <p>As informações vão desde as descritivas como nome e pré-fixo de reconhecimento do mundo.
 * Como algumas mais de configuração: tamanho que cada terreno deverá ter e quantos terrenos terá.
 * E por últimas as que irão definir o terreno de fato, como unidade de cada célula e carregador.</p>
 *
 * @see TerrainLoader
 *
 * @author Andrew
 */

public interface WorldData
{
	/**
	 * Nome do mapa é usada de forma visual para que seja possível ao usuário identificar o mundo.
	 * O nome do mundo aparece como informação adicional nos logs e pode ser usado na interface de usuário.
	 * @return aquisição do nome do mundo do qual os dados irão pertencer.
	 */

	String getName();

	/**
	 * Pré-fixo é usado mais internamente para facilitar a programação do jogo com os mundos.
	 * De modo que não seja necessário decorar nomes grandes (se for o caso), e sim alguns caracteres.
	 * @return aquisição do pré-fixo do mundo do qual os dados irão pertencer.
	 */

	String getPrefix();

	/**
	 * A pasta irá indicar onde poderá ser encontrado os arquivos dos terrenos quando necessário carregar.
	 * Essa pasta pode ser relativa ao arquivo de configuração ou uma definição direta do caminho em disco.
	 * @return aquisição do caminho da pasta para leitura dos terrenos usado pelo carregador de terrenos.
	 */

	String getFolder();

	/**
	 * Carregador é usado para que quando um determinado terreno for solicitado possa ser carregado.
	 * Para aliviar o peso em memória RAM, nem todos os terrenos são carregados junto com o mundo.
	 * Assim é possível carregar partes do mundo conforme necessário, e descartá-los da mesma forma.
	 * @return aquisição do objeto que possui a interface para carregamento dos terrenos.
	 */

	TerrainLoader getTerrainLoader();

	/**
	 * Mundos são formados por terrenos de um único tamanho, assim é possível organizá-los em grade.
	 * Isso facilita o mundo também a gerenciar os terrenos através de uma matriz relativa a X e Y.
	 * @return aquisição da quantidade de terrenos que podem ser alocados no eixo da longitude.
	 */

	int getWidth();

	/**
	 * Mundos são formados por terrenos de um único tamanho, assim é possível organizá-los em grade.
	 * Isso facilita o mundo também a gerenciar os terrenos através de uma matriz relativa a X e Y.
	 * @return aquisição da quantidade de terrenos que podem ser alocados no eixo da latitude.
	 */

	int getLength();

	/**
	 * Mundos são formados por terrenos de um único tamanho, para isso é necessário um definido.
	 * Todos os terrenos carregados devem possuir esse tamanho para evitar problemas de renderização.
	 * @return aquisição de quantas células um terreno terá no eixo da longitude.
	 */

	int getTerrainWidth();

	/**
	 * Mundos são formados por terrenos de um único tamanho, para isso é necessário um definido.
	 * Todos os terrenos carregados devem possuir esse tamanho para evitar problemas de renderização.
	 * @return aquisição de quantas células um terreno terá no eixo da latitude.
	 */

	int getTerrainLength();

	/**
	 * Unidade irá definir qual o tamanho que um terreno irá possuir para cada célula.
	 * O padrão a ser usado deve ser 1.0f que representa 100% do tamanho normal.
	 * @return aquisição do tamanho de cada unidade (célula) do terreno.
	 */

	float getUnit();
}
