package com.erakin.engine.resource.textures;

import java.io.FileInputStream;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import com.erakin.common.buffer.Buffer;
import com.erakin.common.buffer.BufferInput;
import com.erakin.engine.resource.textures.pixel.PixelFormat;

/**
 * <h1>Carregador de Textura Padr�o</h1>
 *
 * <p>Carregador de modelo � a implementa��o de um carregador de dados para modelo.
 * Implementando todos os getters e definindo os atributos que s�o em comum.
 * Assim, todos os tipos de carregadores de modelos ser�o implementados facilmente.</p>
 *
 * @see TextureReader
 *
 * @author Andrew
 */

public abstract class TextureReaderDefault implements TextureReader
{
	/**
	 * Em qual formato os dados ser�o carregados.
	 */
	private PixelFormat inputFormat;

	/**
	 * Em qual formato os dados dever�o ser armazenados.
	 */
	private PixelFormat outputFormat;

	/**
	 * Cria um novo leitor para texturas sendo necess�rio definir o formato dos pixels padr�o.
	 * Cada tipo de imagem possui um formato de pixels padr�o para o mesmo, como RGB ou RGBA.
	 * @param defaultFormat qual ser� o formato padr�o que ser� assumido se nenhum for definido.
	 */

	public TextureReaderDefault(PixelFormat defaultFormat)
	{
		outputFormat = defaultFormat;
		inputFormat = defaultFormat;
	}

	/**
	 * Formato de entrada ir� indicar qual o formato de pixels que ser� aceito como entrada.
	 * Isso � usado ainda pra prevenir que formatos inadequados ou inexistentes sejam usados.
	 * @return aquisi��o do formato de pixels que ser� aceito durante a entrada de dados.
	 */

	public PixelFormat getInputFormat()
	{
		return inputFormat;
	}

	/**
	 * Permite definir qual ser� o formato em que os pixels estar�o armazenados em sua leitura.
	 * Isso permite a um leitor de textura ampliar seu leque de imagens dispon�veis para leitura.
	 * @param format refer�ncia do formato de pixels que ser� aceito durante a entrada de dados.
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
	 * O mais comum de se encontrar s�o depth de 24bits e 32bits representados normalmente por RGB ou RGBA.
	 * @return aquisi��o de quantos bits s�o necess�rios para formar um �nico pixel da imagem.
	 */

	protected abstract int getDepth();

	/**
	 * Chamado internamente para indicar o tamanho da imagem em pixels e calcular o tamanho do buffer.
	 * O buffer em quest�o � para armazenamento exclusivo para dados dos pixels puros.
	 * @return aquisi��o do tamanho da imagem no eixo da largura em pixels.
	 */

	protected abstract int getWidth();

	/**
	 * Chamado internamente para indicar o tamanho da imagem em pixels e calcular o tamanho do buffer.
	 * O buffer em quest�o � para armazenamento exclusivo para dados dos pixels em seu formato cru.
	 * @return aquisi��o do tamanho da imagem no eixo da altura em pixels.
	 */

	protected abstract int getHeight();

	/**
	 * Ser� chamado ap�s fazer a cria��o do buffer que ir� conter todos os bytes da textura.
	 * Esses bytes dever�o ser analisados e convertidos de forma adequada para TextureData.
	 * @param buffer refer�ncia do buffer do qual vai conter todos os bytes da textura.
	 * @return objeto contendo as informa��es necess�rias para se criar uma textura.
	 */

	protected abstract void parseBuffer(Buffer buffer);

	/**
	 * Chamado internamente somente ap�s fazer a leitura dos dados b�sicos necess�rios da imagem.
	 * Dever� garantir que os dados carregas da imagem sejam convertidos em pixels puros.
	 * No caso, pixels em formato puro significa que n�o deve ter regras ou compacta��o.
	 * O tamanho do buffer criado ser� de acordo com a largura, altura e formato de an�lise.
	 * @param buffer refer�ncia do buffer criado com o tamanho limite que poder� possuir.
	 * @param output formato em que os pixels dever�o ser armazenados na an�lise da textura.
	 */

	protected abstract void parsePixels(ByteBuffer buffer, PixelFormat output);
}
