package com.erakin.engine.resource.textures;

import java.io.FileInputStream;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import com.erakin.common.buffer.Buffer;
import com.erakin.common.buffer.BufferInput;
import com.erakin.engine.resource.textures.pixel.PixelFormat;

/**
 * <h1>Carregador de Textura Padrão</h1>
 *
 * <p>Carregador de modelo é a implementação de um carregador de dados para modelo.
 * Implementando todos os getters e definindo os atributos que são em comum.
 * Assim, todos os tipos de carregadores de modelos serão implementados facilmente.</p>
 *
 * @see TextureReader
 *
 * @author Andrew
 */

public abstract class TextureReaderDefault implements TextureReader
{
	/**
	 * Em qual formato os dados serão carregados.
	 */
	private PixelFormat inputFormat;

	/**
	 * Em qual formato os dados deverão ser armazenados.
	 */
	private PixelFormat outputFormat;

	/**
	 * Cria um novo leitor para texturas sendo necessário definir o formato dos pixels padrão.
	 * Cada tipo de imagem possui um formato de pixels padrão para o mesmo, como RGB ou RGBA.
	 * @param defaultFormat qual será o formato padrão que será assumido se nenhum for definido.
	 */

	public TextureReaderDefault(PixelFormat defaultFormat)
	{
		outputFormat = defaultFormat;
		inputFormat = defaultFormat;
	}

	/**
	 * Formato de entrada irá indicar qual o formato de pixels que será aceito como entrada.
	 * Isso é usado ainda pra prevenir que formatos inadequados ou inexistentes sejam usados.
	 * @return aquisição do formato de pixels que será aceito durante a entrada de dados.
	 */

	public PixelFormat getInputFormat()
	{
		return inputFormat;
	}

	/**
	 * Permite definir qual será o formato em que os pixels estarão armazenados em sua leitura.
	 * Isso permite a um leitor de textura ampliar seu leque de imagens disponíveis para leitura.
	 * @param format referência do formato de pixels que será aceito durante a entrada de dados.
	 */

	public void setInputFormat(PixelFormat format)
	{
		if (format != null)
			this.inputFormat = format;
	}

	@Override
	public PixelFormat getOutputFormat()
	{
		return outputFormat;
	}

	@Override
	public void setOutputFormat(PixelFormat format)
	{
		if (format != null)
			this.outputFormat = format;
	}

	@Override
	public TextureData readTexture(FileInputStream fileInputStream) throws TextureException
	{
		return readTexture(fileInputStream, outputFormat);
	}

	@Override
	public TextureData readTexture(FileInputStream fileInputStream, PixelFormat output) throws TextureException
	{
		setOutputFormat(output);

		Buffer buffer = new BufferInput(fileInputStream);

		if (buffer.length() == 0)
			throw new TextureException("falha na leitura");

		parseBuffer(buffer);

		int size = getWidth() * getHeight() * output.BYTES;

		ByteBuffer pixels = BufferUtils.createByteBuffer(size);
		parsePixels(pixels, output);

		TextureDataDefault data = new TextureDataDefault();
		data.depth = getDepth();
		data.width = getWidth();
		data.height = getHeight();
		data.pixels = pixels;

		return data;
	}

	/**
	 * Chamado internamente para indicar a profundidade da imagem afim de guardar como dado da textura.
	 * O mais comum de se encontrar são depth de 24bits e 32bits representados normalmente por RGB ou RGBA.
	 * @return aquisição de quantos bits são necessários para formar um único pixel da imagem.
	 */

	protected abstract int getDepth();

	/**
	 * Chamado internamente para indicar o tamanho da imagem em pixels e calcular o tamanho do buffer.
	 * O buffer em questão é para armazenamento exclusivo para dados dos pixels puros.
	 * @return aquisição do tamanho da imagem no eixo da largura em pixels.
	 */

	protected abstract int getWidth();

	/**
	 * Chamado internamente para indicar o tamanho da imagem em pixels e calcular o tamanho do buffer.
	 * O buffer em questão é para armazenamento exclusivo para dados dos pixels em seu formato cru.
	 * @return aquisição do tamanho da imagem no eixo da altura em pixels.
	 */

	protected abstract int getHeight();

	/**
	 * Será chamado após fazer a criação do buffer que irá conter todos os bytes da textura.
	 * Esses bytes deverão ser analisados e convertidos de forma adequada para TextureData.
	 * @param buffer referência do buffer do qual vai conter todos os bytes da textura.
	 * @return objeto contendo as informações necessárias para se criar uma textura.
	 */

	protected abstract void parseBuffer(Buffer buffer);

	/**
	 * Chamado internamente somente após fazer a leitura dos dados básicos necessários da imagem.
	 * Deverá garantir que os dados carregas da imagem sejam convertidos em pixels puros.
	 * No caso, pixels em formato puro significa que não deve ter regras ou compactação.
	 * O tamanho do buffer criado será de acordo com a largura, altura e formato de análise.
	 * @param buffer referência do buffer criado com o tamanho limite que poderá possuir.
	 * @param output formato em que os pixels deverão ser armazenados na análise da textura.
	 */

	protected abstract void parsePixels(ByteBuffer buffer, PixelFormat output);
}
