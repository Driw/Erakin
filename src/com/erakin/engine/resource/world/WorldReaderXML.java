package com.erakin.engine.resource.world;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.diverproject.util.lang.FloatUtil;
import org.diverproject.util.lang.IntUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.erakin.engine.resource.TerrainLoader;

/**
 * Leitor de Mundos em XML
 *
 * Mundos podem ser definidos através de arquivos XML desde que use um padrão especifico.
 * O arquivo deverá iniciar com um elemento de tag <b>xml</b> contendo um único elemento <b>Setting</b>.
 * Caso haja mais de um elemento com esse nome será considerando sempre o primeiro encontrado.
 *
 * <p>O elemento <b>Setting</b> deverá possuir todos os atributos necessários para se definir um mundo:<br>
 * <b>name:</b> nome usado para exibição em interfaces de usuário ou em logs;<br>
 * <b>prefix:</b> nome de identificação do mapa para facilitar o desenvolvimento;<br>
 * <b>width:</b> quantos terrenos poderão existir no eixo da longitude;<br>
 * <b>length:</b> quantos terrenos poderão existir no eixo da latitude;<br>
 * <b>terrainWidth:</b> quantas células serão necessárias para se formar um terreno na vertical;<br>
 * <b>terrainLength:</b> quantas células serão necessárias para se formar um terreno na horizontal;<br>
 * <b>unit:</b> tamanho em float que cada célula do terreno irá possuir, padrão é 1.0f para 100%;<br>
 * <b>path:</b> caminho parcial ou completo da pasta que irá conter os arquivos dos terrenos do mundo;<br>
 * <b>loader:</b> caminho completo da classe usada para carregar os terrenos implementando TerrainLoader.</p>
 *
 * @author Andrew
 */

public class WorldReaderXML implements WorldReader
{
	@Override
	public WorldData readWorld(File file) throws WorldException
	{
		try {

			WorldSettings settings = loadWorldSettings(file);
			Object object = settings.getLoader().newInstance();

			if (!(object instanceof TerrainLoader))
				throw new WorldException("TerrainLoader inválido");

			TerrainLoader loader = (TerrainLoader) object;

			WorldDataDefault data = new WorldDataDefault(settings.getWidth(), settings.getLength(), settings.getUnit());
			data.setTerrainSize(settings.getTerrainWidth(), settings.getTerrainLength());
			data.setName(settings.getName());
			data.setPrefix(settings.getPrefix());
			data.setFolder(settings.getPath());
			data.setLoader(loader);

			return data;

		} catch (IOException e) {
			throw new WorldException(e, "falha ao ler mapa em XML");
		} catch (ParserConfigurationException e) {
			throw new WorldException(e, "arquivo XML corrompido ou inválido");
		} catch (SAXException e) {
			throw new WorldException(e, "não foi possível ler o mapa em XML");
		} catch (ClassNotFoundException e) {
			throw new WorldException(e, "observer não encontrado");
		} catch (InstantiationException e) {
			throw new WorldException(e, "observer não pode ser instanciado");
		} catch (IllegalAccessException e) {
			throw new WorldException(e, "observer não pode ser acessado");
		}
	}

	/**
	 * Carrega todas as configurações existentes em um arquivo XML de definições para um mundo.
	 * @param file referência do arquivo do qual contém as definições do mundo em XML.
	 * @return objeto contendo todas definições do mundo do qual está sendo carregado.
	 * @throws ParserConfigurationException quando houver problema com o builder.
	 * @throws SAXException quando houver problema em analisar o arquivo XML lido.
	 * @throws IOException quando houver problema na leitura do arquivo em si.
	 * @throws ClassNotFoundException quanto um carregador de terrenos for inválido.
	 */

	private WorldSettings loadWorldSettings(File file) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{
		WorldSettings ws = new WorldSettings();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(file);

		Element root = document.getDocumentElement();
		NodeList settings = root.getElementsByTagName("Setting");
		Element setting = (Element) settings.item(0);

		ws.setName(setting.getAttribute("name"));
		ws.setPrefix(setting.getAttribute("prefix"));
		ws.setWidth(IntUtil.parse(setting.getAttribute("width")));
		ws.setLength(IntUtil.parse(setting.getAttribute("length")));
		ws.setChunkWidth(IntUtil.parse(setting.getAttribute("terrainWidth")));
		ws.setChunkLength(IntUtil.parse(setting.getAttribute("terrainLength")));
		ws.setUnit(FloatUtil.parse(setting.getAttribute("unit")));
		ws.setLoader(setting.getAttribute("loader"));
		ws.setPath(setting.getAttribute("path"));

		return ws;
	}
}
