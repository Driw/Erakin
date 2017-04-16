package com.erakin.engine.resource.textures;

import java.io.FileInputStream;

import com.erakin.engine.resource.textures.pixel.PixelFormat;

/**
 * <h1>Carregador de Dados para Textura</h1>
 *
 * <p>Todo carregador de modelos dever� implementar essa interface.
 * Qualquer classe que a implemente poder� carregar uma imagem especifica.
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
	 * Ser� construido um objeto para dados tempor�rios da textura no mesmo.
	 * Esse objeto ser� retornado contendo as informa��es da textura carregada.
	 * @param fileInputStream refer�ncia da stream com entrada de dados da textura.
	 * @return aquisi��o de um objeto contendo os dados da textura lida.
	 * @throws TextureException ocorre por falha na leitura da textura.
	 */

	TextureData readTexture(FileInputStream fileInputStream) throws TextureException;

	/**
	 * Deve carregar os dados de uma determinada stream com entrada de dados.
	 * Ser� construido um objeto para dados tempor�rios da textura no mesmo.
	 * Esse objeto ser� retornado contendo as informa��es do modelo carregado.
	 * @param fileInputStream refer�ncia da stream com entrada de dados da textura.
	 * @param output como os bytes dever�o ser salvos ap�s serem carregados.
	 * @return aquisi��o de um objeto contendo os dados da textura lida.
	 * @throws TextureException ocorre por falha na leitura da textura.
	 */

	TextureData readTexture(FileInputStream fileInputStream, PixelFormat output) throws TextureException;

	/**
	 * Formato dos pixels ir� indicar como os dados analisados da textura ser�o armazenados.
	 * Cada formato possui uma quantidade de bytes por pixel e consecutivamente uma ordem.
	 * @return aquisi��o do atual formato em que as texturas dever�o ser escritas.
	 */

	PixelFormat getOutputFormat();

	/**
	 * Em qual o formato os pixels dever�o ser armazenados ao serem carregados.
	 * @param format refer�ncia do formato em que dever� ser armazenado.
	 */

	void setOutputFormat(PixelFormat format);
}
