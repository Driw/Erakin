package com.erakin.textures.jpg;

import static com.erakin.api.resources.texture.PixelFormat.FORMAT_RGB;

import java.nio.ByteBuffer;

import com.erakin.api.buffer.Buffer;
import com.erakin.api.resources.texture.PixelFormat;
import com.erakin.api.resources.texture.TextureReaderDefault;

public class TextureReaderJPG extends TextureReaderDefault
{
	public TextureReaderJPG()
	{
		super(FORMAT_RGB);
	}

	@Override
	protected int getDepth()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getWidth()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getHeight()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void parseBuffer(Buffer buffer)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void parsePixels(ByteBuffer buffer, PixelFormat output)
	{
		// TODO Auto-generated method stub
		
	}
}
