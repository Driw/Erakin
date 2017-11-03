package com.erakin.api.resources.model;

import static com.erakin.api.resources.model.ModelAttribute.ATTRIB_VERTEX;
import static org.diverproject.log.LogSystem.logDebug;
import static org.diverproject.log.LogSystem.logWarning;

import java.io.FileInputStream;

import com.erakin.api.lwjgl.VAO;
import com.erakin.api.resources.DefaultLoader;
import com.erakin.api.resources.ResourceMap;
import com.erakin.api.resources.ResourceRoot;

/**
 * <h1>Carregador de Modelagem</h1>
 *
 * <p>Usada para fazer um gerenciamento avan�ado das modelagens tri-dimensionais carregadas a partir de arquivos.
 * Al�m de carregar, salva as informa��es em uma pasta virtual em mem�ria para que n�o seja carregada duas vezes.</p>
 *
 * <p>Por exemplo, um modelo chamado <b>casa.obj</b> ir� verificar se este objeto j� foi carregado no sistema,
 * caso ele j� tenha sido carregado, ir� usar as informa��es deste objeto ao inv�s de ler os dados do arquivo.
 * Esse sistema permite al�m de economizar tempo de processamento evitar consumo de mem�ria desnecess�rio.</p>
 *
 * <p>Por padr�o os objetos ser�o considerados no formato <b>mdl</b> que � o utilizado pela biblioteca Erakin.
 * Por�m � poss�vel tamb�m utilizar outras formata��es como <b>obj</b> ou ainda adicionar a sua � lista.
 * Sendo poss�vel aumentar a diversidade de formas para trabalhar com os dados de objetos tri-dimensionais.</p>
 *
 * @see ResourceMap
 * @see ModelReader
 * @see ModelReaderFactory
 *
 * @author Andrew Mello
 */

public final class ModelLoader extends DefaultLoader<Model>
{
	/**
	 * Inst�ncia para carregador de modelagem no padr�o de projetos Singleton.
	 */
	private static final ModelLoader instance = new ModelLoader();

	/**
	 * Nome padr�o para mapeamento de mundos.
	 */
	public static final String DEFAULT_PATH = "models";

	/**
	 * Construtor privado para evitar m�ltiplas inst�ncias para o carregador de modelagens.
	 * Inicializa o mapeador de recursos definindo o seu nome por padr�o <code>DEFAULT_PATH</code>.
	 */

	private ModelLoader()
	{
		super(DEFAULT_PATH);
	}

	/**
	 * Permite obter uma determinada modelagem j� carregada ou ent�o for�ar o carregamento desta.
	 * Se a modelagem existir ir� retornar uma modelagem tempor�ria dessa ra�z caso contr�rio ir�
	 * criar uma nova ra�z carregando as informa��es do arquivo de acordo com o nome da modelagem.
	 * Utiliza as propriedades de prefer�ncias que definem o caminho para arquivos de modelagem.
	 * @param name nome do qual foi dado a modelagem, em outras palavras o nome do arquivo,
	 * caso n�o seja definido nenhuma extens�o para esse, ser� considerado <b>mdl</b> por padr�o.
	 * @return aquisi��o do objeto de modelagem tempor�rio gerado da ra�z de acordo com o nome.
	 * @throws ModelException falha durante a leitura do arquivo ou arquivo com dados corrompidos.
	 */

	public Model getModel(String name) throws ModelException
	{
		if (!name.contains("."))
			name += ".mdl";

		ResourceRoot<Model> resourceRoot = selectResource(name);

		if (resourceRoot != null)
		{
			ModelRoot modelRoot = (ModelRoot) resourceRoot;
			return modelRoot.genResource();
		}

		String path = getPathname() + name;
		ModelReaderFactory factory = ModelReaderFactory.getInstance();
		ModelReader reader = factory.getModelReaderOf(path);

		try {

			ModelData data = reader.readModel(new FileInputStream(path));
			Model model = createModel(path, data);

			return model;

		} catch (Exception e) {
			throw new ModelException(e);
		}
	}

	/**
	 * Permite construir uma nova modelagem tri-dimensional a partir das informa��es abaixo.
	 * Caso o caminho de aloca��o j� esteja sendo usado por outra n�o ser� poss�vel criar.
	 * Se for poss�vel criar, a modelagem ra�z ser� armazenada e gerada uma tempor�ria.
	 * @param path caminho onde foi localizado a modelagem, onde deve ser alocada.
	 * @param data objeto contendo os dados da modelagem do qual ser� criada.
	 * @return aquisi��o de uma modelagem tri-dimensional de uso tempor�rio.
	 */

	public Model createModel(String path, ModelData data)
	{
		if (path == null)
			throw new ModelRuntimeException("caminho n�o definido");

		if (data == null)
			throw new ModelRuntimeException("dados n�o definido");

		if (!path.startsWith(getResourceName()))
		{
			if (path.contains(getResourceName()+ "/"))
				path = path.substring(path.indexOf(getResourceName()), path.length());
			else
				path = String.format("%s/%s", getResourceName(), path);
		}

		if (containResource(path))
			throw new ModelRuntimeException("modelagem j� existente (%s)", path);

		ModelRoot root = new ModelRoot(path);
		root.vao = new VAO();
		root.vao.bind();
		{
			root.attributes = new int[data.getAttributes().length];

			if (data.getIndices() != null)
				data.getIndices().storeInVAO(root.vao);
			else
				for (ModelAttribute attribute : data.getAttributes())
					if (attribute.getIndex() == ATTRIB_VERTEX)
					{
						root.vao.setVertexCount(attribute.length());
						break;
					}

			for (int i = 0; i < data.getAttributes().length; i++)
			{
				ModelAttribute attribute = data.getAttributes()[i];
				attribute.storeInVAO(root.vao);
				root.attributes[i] = attribute.getIndex();
			}
		}
		root.vao.unbind();

		logDebug("modelagem '%s' lida com �xito (%s).\n", root.getFileName(), data.toStringDetails());

		if (!insertResource(root))
			logWarning("n�o foi poss�vel salvar a modelagem '%s'.\n", root.getFileName());

		return root.genResource();
	}

	/**
	 * Procedimento que permite obter a �nica inst�ncia do carregador de modelagem.
	 * Utiliza o padr�o Singleton para evitar a exist�ncia de mais inst�ncias.
	 * @return aquisi��o da inst�ncia para utiliza��o do carregador de modelagem.
	 */

	public static ModelLoader getInstance()
	{
		return instance;
	}
}
