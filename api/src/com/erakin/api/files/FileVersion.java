package com.erakin.api.files;

import static org.diverproject.util.Util.b;
import static org.diverproject.util.Util.format;

import org.diverproject.util.lang.Bits;
import org.diverproject.util.stream.Input;
import org.diverproject.util.stream.Output;

public class FileVersion
{
	public static final long BYTES = (Byte.BYTES * 2);

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
		setMinor(minor);
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
	public FileVersion clone()
	{
		return new FileVersion(major, minor);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof FileVersion)
		{
			FileVersion version = (FileVersion) obj;
			return version.major == major && version.minor == minor;
		}

		else if (obj instanceof Float)
			return get() == (Float) obj;

		return false;
	}

	@Override
	public String toString()
	{
		return format("%d.%d", major, minor);
	}
}
