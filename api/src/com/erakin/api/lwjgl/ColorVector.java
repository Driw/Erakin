package com.erakin.api.lwjgl;

import org.diverproject.util.ObjectDescription;
import org.diverproject.util.lang.FloatUtil;
import org.diverproject.util.lang.IntUtil;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class ColorVector
{
	public static final int MIN_INT_COLOR = 0;
	public static final int MAX_INT_COLOR = 255;
	public static final float MIN_FLOAT_COLOR = 0f;
	public static final float MAX_FLOAT_COLOR = 1f;

	private float red;
	private float green;
	private float blue;
	private float alpha;

	public ColorVector()
	{
		
	}

	public ColorVector(int red, int green, int blue)
	{
		this(red, green, blue, MAX_INT_COLOR);
	}

	public ColorVector(int red, int green, int blue, int alpha)
	{
		setRed(red);
		setGreen(green);
		setBlue(blue);
		setAlpha(alpha);
	}

	public ColorVector(float red, float green, float blue)
	{
		this(red, green, blue, MAX_FLOAT_COLOR);
	}

	public ColorVector(float red, float green, float blue, float alpha)
	{
		setRed(red);
		setGreen(green);
		setBlue(blue);
		setAlpha(alpha);
	}

	public float getRed()
	{
		return red;
	}

	public void setRed(float red)
	{
		this.red = FloatUtil.limit(red, MIN_FLOAT_COLOR, MAX_FLOAT_COLOR);
	}

	public float getGreen()
	{
		return green;
	}

	public void setGreen(float green)
	{
		this.green = FloatUtil.limit(green, MIN_FLOAT_COLOR, MAX_FLOAT_COLOR);
	}

	public float getBlue()
	{
		return blue;
	}

	public void setBlue(float blue)
	{
		this.blue = FloatUtil.limit(blue, MIN_FLOAT_COLOR, MAX_FLOAT_COLOR);
	}

	public float getAlpha()
	{
		return alpha;
	}

	public void setAlpha(float alpha)
	{
		this.alpha = FloatUtil.limit(alpha, MIN_FLOAT_COLOR, MAX_FLOAT_COLOR);
	}

	public int getRedi()
	{
		return (int) (red * MAX_INT_COLOR);
	}

	public void setRed(int red)
	{
		this.red = IntUtil.limit(red, MIN_INT_COLOR, MAX_INT_COLOR) / MAX_INT_COLOR;
	}

	public int getGreeni()
	{
		return (int) (green * MAX_INT_COLOR);
	}

	public void setGreen(int green)
	{
		this.green = IntUtil.limit(green, MIN_INT_COLOR, MAX_INT_COLOR) / MAX_INT_COLOR;
	}

	public int getBluei()
	{
		return (int) (blue * MAX_INT_COLOR);
	}

	public void setBlue(int blue)
	{
		this.blue = IntUtil.limit(blue, MIN_INT_COLOR, MAX_INT_COLOR) / MAX_INT_COLOR;
	}

	public int getAlphai()
	{
		return (int) (alpha * MAX_INT_COLOR);
	}

	public void setAlpha(int alpha)
	{
		this.alpha = IntUtil.limit(alpha, MIN_INT_COLOR, MAX_INT_COLOR) / MAX_INT_COLOR;
	}

	public Vector3f toVector3f()
	{
		return new Vector3f(getRed(), getGreen(), getBlue());
	}

	public Vector4f toVector4f()
	{
		return new Vector4f(getRed(), getGreen(), getBlue(), getAlpha());
	}

	@Override
	public String toString()
	{
		ObjectDescription description = new ObjectDescription(getClass());

		description.append("red", getRedi());
		description.append("green", getGreeni());
		description.append("blue", getBluei());
		description.append("alpha", getAlphai());

		return description.toString();
	}
}
