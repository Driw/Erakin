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
 * <p>Usada para fazer um gerenciamento avançado das modelagens tri-dimensionais carregadas a partir de arquivos.
 * Além de carregar, salva as informações em uma pasta virtual em memória para que não seja carregada duas vezes.</p>
 *
 * <p>Por exemplo, um modelo chamado <b>casa.obj</b> irá verificar se este objeto já foi carregado no sistema,
 * caso ele já tenha sido carregado, irá usar as informações deste objeto ao invés de ler os dados do arquivo.
 * Esse sistema permite além de economizar tempo de processamento evitar consumo de memória desnecessário.</p>
 *
 * <p>Por padrão os objetos serão considerados no formato <b>mdl</b> que é o utilizado pela biblioteca Erakin.
 * Porém é possível também utilizar outras formatações como <b>obj</b> ou ainda adicionar a sua à lista.
 * Sendo possível aumentar a diversidade de formas para trabalhar com os dados de objetos tri-dimensionais.</p>
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
	 * Instância para carregador de modelagem no padrão de projetos Singleton.
	 */
	private static final ModelLoader instance = new ModelLoader();

	/**
	 * Nome padrão para mapeamento de mundos.
	 */
	public static final String DEFAULT_PATH = "models";

	/**
	 * Construtor privado para evitar múltiplas instâncias para o carregador de modelagens.
	 * Inicializa o mapeador de recursos definindo o seu nome por padrão <code>DEFAULT_PATH</code>.
	 */

	private ModelLoader()
	{
		super(DEFAULT_PATH);
	}

	/**
	 * Permite obter uma determinada modelagem já carregada ou então forçar o carregamento desta.
	 * Se a modelagem existir irá retornar uma modelagem temporária dessa raíz caso contrário irá
	 * criar uma nova raíz carregando as informações do arquivo de acordo com o nome da modelagem.
	 * Utiliza as propriedades de preferências que definem o caminho para arquivos de modelagem.
	 * @param name nome do qual foi dado a modelagem, em outras palavras o nome do arquivo,
	 * caso não seja definido nenhuma extensão para esse, será considerado <b>mdl</b> por padrão.
	 * @return aquisição do objeto de modelagem temporário gerado da raíz de acordo com o nome.
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
	 * Permite construir uma nova modelagem tri-dimensional a partir das informações abaixo.
	 * Caso o caminho de alocação já esteja sendo usado por outra não será possível criar.
	 * Se for possível criar, a modelagem raíz será armazenada e gerada uma temporária.
	 * @param path caminho onde foi localizado a modelagem, onde deve ser alocada.
	 * @param data objeto contendo os dados da modelagem do qual será criada.
	 * @return aquisição de uma modelagem tri-dimensional de uso temporário.
	 */

	public Model createModel(String path, ModelData data)
	{
		if (path == null)
			throw new ModelRuntimeException("caminho não definido");

		if (data == null)
			throw new ModelRuntimeException("dados não definido");

		if (!path.startsWith(getResourceName()))
		{
			if (path.contains(getResourceName()+ "/"))
				path = path.substring(path.indexOf(getResourceName()), path.length());
			else
				path = String.format("%s/%s", getResourceName(), path);
		}

		if (containResource(path))
			throw new ModelRuntimeException("modelagem já existente (%s)", path);

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

		logDebug("modelagem '%s' lida com êxito (%s).\n", root.getFileName(), data.toStringDetails());

		if (!insertResource(root))
			logWarning("não foi possível salvar a modelagem '%s'.\n", root.getFileName());

		return root.genResource();
	}

	/**
	 * Procedimento que permite obter a única instância do carregador de modelagem.
	 * Utiliza o padrão Singleton para evitar a existência de mais instâncias.
	 * @return aquisição da instância para utilização do carregador de modelagem.
	 */

	public static ModelLoader getInstance()
	{
		return instance;
	}
}
