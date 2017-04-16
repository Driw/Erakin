package com.erakin.engine.resource.textures.pixel;

import org.diverproject.util.lang.ByteUtil;

public class RGBA
{
	private byte red;
	private byte green;
	private byte blue;
	private byte alpha;

	public RGBA(byte red, byte green, byte blue)
	{
		this(red, green, blue, (byte) 0);
	}

	public RGBA(byte red, byte green, byte blue, byte alpha)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	public RGBA(int red, int green, int blue)
	{
		this(red, green, blue, 0);
	}

	public RGBA(int red, int green, int blue, int alpha)
	{
		this.red = ByteUtil.putInt(red);
		this.green = ByteUtil.putInt(green);
		this.blue = ByteUtil.putInt(blue);
		this.alpha = ByteUtil.putInt(alpha);
	}

	public byte getRed()
	{
		return red;
	}

	public void setRed(byte red)
	{
		this.red = red;
	}

	public byte getGreen()
	{
		return green;
	}

	public void setGreen(byte green)
	{
		this.green = green;
	}

	public byte getBlue()
	{
		return blue;
	}

	public void setBlue(byte blue)
	{
		this.blue = blue;
	}

	public byte getAlpha()
	{
		return alpha;
	}

	public void setAlpha(byte alpha)
	{
		this.alpha = alpha;
	}
}
