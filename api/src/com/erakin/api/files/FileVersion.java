package com.erakin.api.files;

import static org.diverproject.util.Util.b;
import static org.diverproject.util.Util.format;

import org.diverproject.util.lang.Bits;
import org.diverproject.util.stream.Input;
import org.diverproject.util.stream.Output;

public class FileVersion
{
	private byte major;
	private byte minor;

	public FileVersion()
	{
		
	}

	public FileVersion(int major)
	{
		setMajor(b(major));
	}

	public FileVersion(int major, int minor)
	{
		setMajor(major);
		setMinor(major);
	}

	public void read(Input input)
	{
		this.major = input.getByte();
		this.minor = input.getByte();
	}

	public void write(Output output)
	{
		output.putBytes(major, minor);
	}

	public byte getMajor()
	{
		return major;
	}

	public void setMajor(int major)
	{
		this.major = b(major);
	}

	public byte getMinor()
	{
		return minor;
	}

	public void setMinor(int minor)
	{
		this.minor = b(minor);
	}

	public short get()
	{
		return Bits.makeShort(major, minor);
	}

	@Override
	public String toString()
	{
		return format("%d.%d", major, minor);
	}
}
