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
 * Mundos podem ser definidos atrav�s de arquivos XML desde que use um padr�o especifico.
 * O arquivo dever� iniciar com um elemento de tag <b>xml</b> contendo um �nico elemento <b>Setting</b>.
 * Caso haja mais de um elemento com esse nome ser� considerando sempre o primeiro encontrado.
 *
 * <p>O elemento <b>Setting</b> dever� possuir todos os atributos necess�rios para se definir um mundo:<br>
 * <b>name:</b> nome usado para exibi��o em interfaces de usu�rio ou em logs;<br>
 * <b>prefix:</b> nome de identifica��o do mapa para facilitar o desenvolvimento;<br>
 * <b>width:</b> quantos terrenos poder�o existir no eixo da longitude;<br>
 * <b>length:</b> quantos terrenos poder�o existir no eixo da latitude;<br>
 * <b>terrainWidth:</b> quantas c�lulas ser�o necess�rias para se formar um terreno na vertical;<br>
 * <b>terrainLength:</b> quantas c�lulas ser�o necess�rias para se formar um terreno na horizontal;<br>
 * <b>unit:</b> tamanho em float que cada c�lula do terreno ir� possuir, padr�o � 1.0f para 100%;<br>
 * <b>path:</b> caminho parcial ou completo da pasta que ir� conter os arquivos dos terrenos do mundo;<br>
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
				throw new WorldException("TerrainLoader inv�lido");

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
			throw new WorldException(e, "arquivo XML corrompido ou inv�lido");
		} catch (SAXException e) {
			throw new WorldException(e, "n�o foi poss�vel ler o mapa em XML");
		} catch (ClassNotFoundException e) {
			throw new WorldException(e, "observer n�o encontrado");
		} catch (InstantiationException e) {
			throw new WorldException(e, "observer n�o pode ser instanciado");
		} catch (IllegalAccessException e) {
			throw new WorldException(e, "observer n�o pode ser acessado");
		}
	}

	/**
	 * Carrega todas as configura��es existentes em um arquivo XML de defini��es para um mundo.
	 * @param file refer�ncia do arquivo do qual cont�m as defini��es do mundo em XML.
	 * @return objeto contendo todas defini��es do mundo do qual est� sendo carregado.
	 * @throws ParserConfigurationException quando houver problema com o builder.
	 * @throws SAXException quando houver problema em analisar o arquivo XML lido.
	 * @throws IOException quando houver problema na leitura do arquivo em si.
	 * @throws ClassNotFoundException quanto um carregador de terrenos for inv�lido.
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
