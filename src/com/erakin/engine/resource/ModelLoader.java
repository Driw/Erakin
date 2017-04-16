package com.erakin.engine.resource;

import static com.erakin.engine.resource.Model.ATTRIB_NORMALS;
import static com.erakin.engine.resource.Model.ATTRIB_VERTICES;
import static com.erakin.engine.resource.Model.ATTRIB_TEXTURE_COORDS;
import static org.diverproject.log.LogSystem.logDebug;
import static org.diverproject.log.LogSystem.logWarning;

import java.io.FileInputStream;

import org.diverproject.util.FileUtil;

import com.erakin.engine.lwjgl.VAO;
import com.erakin.engine.resource.model.ModelData;
import com.erakin.engine.resource.model.ModelReaderFactory;
import com.erakin.engine.resource.model.ModelReader;
import com.erakin.engine.resource.model.ModelException;
import com.erakin.engine.resource.model.ModelRuntimeException;

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

public final class ModelLoader extends DefaultLoader
{
	/**
	 * Instância para carregador de modelagem no padrão de projetos Singleton.
	 */
	private static final ModelLoader instance = new ModelLoader();

	/**
	 * Construtor privado para evitar múltiplas instâncias para o carregador de modelagens.
	 * Inicializa o mapeador de recursos definindo o seu nome de acordo com as preferências.
	 * A preferência aqui utilizada é <code>models</code>, pasta para arquivos de modelagem.
	 */

	private ModelLoader()
	{
		super("models");
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

		ResourceRoot resourceRoot = selectResource(name);

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

		int indices[] = data.getIndices();
		float vertices[] = data.getVertices();
		float textures[] = data.getTextureCoords();
		float normals[] = data.getNormals();

		ModelRoot root = new ModelRoot();
		root.fileName = FileUtil.getFileName(path);
		root.filePath = FileUtil.getParentPath(path);
		root.fileExtension = FileUtil.getExtension(path);
		root.vao = new VAO();
		root.vao.setIndices(indices);
		root.vao.setAttribute(ATTRIB_VERTICES, 3, vertices);
		root.vao.setAttribute(ATTRIB_TEXTURE_COORDS, 2, textures);
		root.vao.setAttribute(ATTRIB_NORMALS, 3, normals);

		logDebug("modelagem '%s' lida com êxito (indices: %d, vertices: %d, textures: %d, normals: %d).\n",
				root.fileName, indices.length, vertices.length, textures.length, normals.length);

		if (!insertResource(root))
			logWarning("não foi possível salvar a modelage '%s'.\n", root.fileName);

		return root.genResource();
	}

	/**
	 * Procedimento que permite obter a única instância do carregador de modelagem.
	 * Utiliza o padrão Singleton para evitar a existência de mais instâncias.
	 * @return aquisição da instância para utilização do carregador de modelagem.
	 */

	public static ModelLoader getIntance()
	{
		return instance;
	}
}
