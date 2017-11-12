package com.erakin.models;

import com.erakin.api.resources.model.ModelReaderFactory;
import com.erakin.models.mdl.ModelReaderMDL;
import com.erakin.models.obj.ModelReaderOBJ;

public class ModelManager
{
	private ModelManager()
	{
		
	}

	public static void addAllNativeFormats()
	{
		addMDLExtension();
		addOBJExtension();
	}

	public static void addMDLExtension()
	{
		ModelReaderFactory.getInstance().addModelReader(ModelReaderMDL.FILE_EXTENSION, new ModelReaderMDL());
	}

	public static void addOBJExtension()
	{
		ModelReaderFactory.getInstance().addModelReader(ModelReaderOBJ.FILE_EXTENSION, new ModelReaderOBJ());
	}
}
