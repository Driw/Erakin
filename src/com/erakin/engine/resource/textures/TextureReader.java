package com.erakin.engine.resource.textures;

import java.io.FileInputStream;

import com.erakin.engine.resource.textures.pixel.PixelFormat;

/**
 * <h1>Carregador de Dados para Textura</h1>
 *
 * <p>Todo carregador de modelos deverá implementar essa interface.
 * Qualquer classe que a implemente poderá carregar uma imagem especifica.
 * Podendo assim ser utilizada em ResourceManager para carregar o mesmo.</p>
 *
 * @see TextureData
 *
 * @author Andrew
 */

public interface TextureReader
{
	/**
	 * Deve carregar os dados de uma determinada stream com entrada de dados.
	 * Será construido um objeto para dados temporários da textura no mesmo.
	 * Esse objeto será retornado contendo as informações da textura carregada.
	 * @param fileInputStream referência da stream com entrada de dados da textura.
	 * @return aquisição de um objeto contendo os dados da textura lida.
	 * @throws TextureException ocorre por falha na leitura da textura.
	 */

	TextureData readTexture(FileInputStream fileInputStream) throws TextureException;

	/**
	 * Deve carregar os dados de uma determinada stream com entrada de dados.
	 * Será construido um objeto para dados temporários da textura no mesmo.
	 * Esse objeto será retornado contendo as informações do modelo carregado.
	 * @param fileInputStream referência da stream com entrada de dados da textura.
	 * @param output como os bytes deverão ser salvos após serem carregados.
	 * @return aquisição de um objeto contendo os dados da textura lida.
	 * @throws TextureException ocorre por falha na leitura da textura.
	 */

	TextureData readTexture(FileInputStream fileInputStream, PixelFormat output) throws TextureException;

	/**
	 * Formato dos pixels irá indicar como os dados analisados da textura serão armazenados.
	 * Cada formato possui uma quantidade de bytes por pixel e consecutivamente uma ordem.
	 * @return aquisição do atual formato em que as texturas deverão ser escritas.
	 */

	PixelFormat getOutputFormat();

	/**
	 * Em qual o formato os pixels deverão ser armazenados ao serem carregados.
	 * @param format referência do formato em que deverá ser armazenado.
	 */

	void setOutputFormat(PixelFormat format);
}
