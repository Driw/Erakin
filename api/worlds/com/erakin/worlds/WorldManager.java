package com.erakin.worlds;

import com.erakin.api.resources.world.WorldReaderFactory;
import com.erakin.worlds.wds.WorldReaderWDS;
import com.erakin.worlds.xml.WorldReaderXML;

public class WorldManager
{
	private WorldManager()
	{
		
	}

	public static void addAllNativeFormats()
	{
		addWDSFormat();
		addXMLFormat();
	}

	public static void addWDSFormat()
	{
		WorldReaderFactory.getInstance().addMapReader(WorldReaderWDS.FILE_EXTENSION, new WorldReaderWDS());
	}

	public static void addXMLFormat()
	{
		WorldReaderFactory.getInstance().addMapReader(WorldReaderXML.FILE_EXTENSION, new WorldReaderXML());
	}
}
