package org.erakin.api.lwjgl.math;

import java.nio.IntBuffer;

import org.diverproject.util.ObjectDescription;

public class Vector3i
{
	public int x;
	public int y;
	public int z;

	public Vector3i()
	{
	}

	public Vector3i(int x, int y, int z)
	{
		set(x, y, z);
	}

	public void set(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public void set(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int lengthSquared()
	{
		return (this.x * this.x + this.y * this.y + this.z * this.z);
	}

	public Vector3i translate(int x, int y, int z)
	{
		this.x += x;
		this.y += y;
		this.z += z;

		return this;
	}

	public static Vector3i add(Vector3i left, Vector3i right, Vector3i dest)
	{
		if (dest == null)
			return new Vector3i(left.x + right.x, left.y + right.y, left.z + right.z);

		dest.set(left.x + right.x, left.y + right.y, left.z + right.z);

		return dest;
	}

	public static Vector3i sub(Vector3i left, Vector3i right, Vector3i dest)
	{
		if (dest == null)
			return new Vector3i(left.x - right.x, left.y - right.y, left.z - right.z);

		dest.set(left.x - right.x, left.y - right.y, left.z - right.z);

		return dest;
	}

	public static Vector3i cross(Vector3i left, Vector3i right, Vector3i dest)
	{
		if (dest == null)
			dest = new Vector3i();

		dest.set(left.y * right.z - (left.z * right.y), right.x * left.z - (right.z * left.x), left.x * right.y - (left.y * right.x));

		return dest;
	}

	public Vector3i negate()
	{
		this.x = (-this.x);
		this.y = (-this.y);
		this.z = (-this.z);

		return this;
	}

	public Vector3i negate(Vector3i dest)
	{
		if (dest == null)
			dest = new Vector3i();

		dest.x = (-this.x);
		dest.y = (-this.y);
		dest.z = (-this.z);

		return dest;
	}

	public Vector3i normalise(Vector3i dest)
	{
		int l = length();

		if (dest == null)
			dest = new Vector3i(this.x / l, this.y / l, this.z / l);
		else
			dest.set(this.x / l, this.y / l, this.z / l);

		return dest;
	}

	public static int dot(Vector3i left, Vector3i right)
	{
		return (left.x * right.x + left.y * right.y + left.z * right.z);
	}

	public static int angle(Vector3i a, Vector3i b)
	{
		int dls = dot(a, b) / a.length() * b.length();

		if (dls < -1)
			dls = -1;
		else if (dls > 1)
			dls = 1;

		return (int) Math.acos(dls);
	}

	public Vector3i load(IntBuffer buf)
	{
		this.x = buf.get();
		this.y = buf.get();
		this.z = buf.get();

		return this;
	}

	public Vector3i scale(int scale)
	{
		this.x *= scale;
		this.y *= scale;
		this.z *= scale;

		return this;
	}

	public Vector3i store(IntBuffer buf)
	{
		buf.put(this.x);
		buf.put(this.y);
		buf.put(this.z);

		return this;
	}

	public final int getX()
	{
		return this.x;
	}

	public final void setX(int x)
	{
		this.x = x;
	}

	public final int getY()
	{
		return this.y;
	}

	public final void setY(int y)
	{
		this.y = y;
	}

	public int getZ()
	{
		return this.z;
	}

	public void setZ(int z)
	{
		this.z = z;
	}

	public final int length()
	{
		return (int) Math.sqrt(lengthSquared());
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (!(obj instanceof Vector3i))
			return false;

		Vector3i other = (Vector3i) obj;

		return ((this.x == other.x) && (this.y == other.y) && (this.z == other.z));
	}

	@Override
	public Vector3i clone()
	{
		return new Vector3i(x, y, z);
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append(x);
		description.append(y);
		description.append(z);

		return description.toString();
	}
}