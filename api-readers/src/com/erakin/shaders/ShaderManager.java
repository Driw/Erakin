package com.erakin.shaders;

import com.erakin.api.resources.shader.ShaderReaderFactory;
import com.erakin.shaders.glsl.ShaderReaderGLSL;

public class ShaderManager
{
	private ShaderManager()
	{
		
	}

	public static void addAllNativeFormats()
	{
		addGLSLFormat();
	}

	private static void addGLSLFormat()
	{
		ShaderReaderFactory.getInstance().addShaderReader(ShaderReaderGLSL.EXTENSION, new ShaderReaderGLSL());
	}
}
