package com.erakin.textures;

import com.erakin.api.resources.texture.TextureReaderFactory;
import com.erakin.textures.bmp.TextureReaderBMP;
import com.erakin.textures.png.TextureReaderPNG;

public class TextureManager
{
	private TextureManager()
	{
		
	}

	public static void addAllNativeFormats()
	{
		addBMPFormat();
		addPNGFormat();
	}

	public static void addBMPFormat()
	{
		TextureReaderFactory.getInstance().addTextureReader(TextureReaderBMP.FILE_EXTENSION, new TextureReaderBMP());
	}

	public static void addPNGFormat()
	{
		TextureReaderFactory.getInstance().addTextureReader(TextureReaderPNG.FILE_EXTENSION, new TextureReaderPNG());
	}
}
