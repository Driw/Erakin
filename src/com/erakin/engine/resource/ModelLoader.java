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

public final class ModelLoader extends DefaultLoader
{
	/**
	 * Inst�ncia para carregador de modelagem no padr�o de projetos Singleton.
	 */
	private static final ModelLoader instance = new ModelLoader();

	/**
	 * Construtor privado para evitar m�ltiplas inst�ncias para o carregador de modelagens.
	 * Inicializa o mapeador de recursos definindo o seu nome de acordo com as prefer�ncias.
	 * A prefer�ncia aqui utilizada � <code>models</code>, pasta para arquivos de modelagem.
	 */

	private ModelLoader()
	{
		super("models");
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

		logDebug("modelagem '%s' lida com �xito (indices: %d, vertices: %d, textures: %d, normals: %d).\n",
				root.fileName, indices.length, vertices.length, textures.length, normals.length);

		if (!insertResource(root))
			logWarning("n�o foi poss�vel salvar a modelage '%s'.\n", root.fileName);

		return root.genResource();
	}

	/**
	 * Procedimento que permite obter a �nica inst�ncia do carregador de modelagem.
	 * Utiliza o padr�o Singleton para evitar a exist�ncia de mais inst�ncias.
	 * @return aquisi��o da inst�ncia para utiliza��o do carregador de modelagem.
	 */

	public static ModelLoader getIntance()
	{
		return instance;
	}
}
