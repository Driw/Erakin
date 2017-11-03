package com.erakin.api.resources.texture;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TextureCubeFiles
{
	public static final int CUBE_RIGHT_FACE = 0;
	public static final int CUBE_LEFT_FACE = 1;
	public static final int CUBE_TOP_FACE = 2;
	public static final int CUBE_BOTTOM_FACE = 3;
	public static final int CUBE_BACK_FACE = 4;
	public static final int CUBE_FRONT_FACE = 5;
	public static final int CUBE_TEXTURE_COUNT = 6;

	private String rightFace;
	private String leftFace;
	private String topFace;
	private String bottomFace;
	private String backFace;
	private String frontFace;

	public TextureCubeFiles()
	{
		
	}

	public String getRightFace()
	{
		return rightFace;
	}

	public void setRightFace(String rightFace)
	{
		this.rightFace = rightFace;
	}

	public String getLeftFace()
	{
		return leftFace;
	}

	public void setLeftFace(String leftFace)
	{
		this.leftFace = leftFace;
	}

	public String getTopFace()
	{
		return topFace;
	}

	public void setTopFace(String topFace)
	{
		this.topFace = topFace;
	}

	public String getBottomFace()
	{
		return bottomFace;
	}

	public void setBottomFace(String bottomFace)
	{
		this.bottomFace = bottomFace;
	}

	public String getBackFace()
	{
		return backFace;
	}

	public void setBackFace(String backFace)
	{
		this.backFace = backFace;
	}

	public String getFrontFace()
	{
		return frontFace;
	}

	public void setFrontFace(String frontFace)
	{
		this.frontFace = frontFace;
	}

	public TextureData[] load() throws TextureException
	{
		try {

			String filenames[] = new String[] { rightFace, leftFace, topFace, bottomFace, backFace, frontFace };
			TextureData textureDatas[] = new TextureData[filenames.length];

			for (int i = 0; i < filenames.length; i++)
			{
				String filepath = TextureLoader.getIntance().getPathname() + filenames[i];
				TextureReaderFactory factory = TextureReaderFactory.getInstance();
				TextureReader reader = factory.getTextureReaderOf(filepath);
				TextureData textureData = reader.readTexture(new FileInputStream(filepath));
				textureDatas[i] = textureData;

				if (i > 0)
				{
					TextureData firstTextureData = textureDatas[0];

					if (textureData.getDepth() != firstTextureData.getDepth() ||
						textureData.getWidth() != firstTextureData.getWidth() ||
						textureData.getHeight() != firstTextureData.getHeight())
						throw new TextureException("a textura '%s' não acompanha o formato das outras", filepath);
				}
			}

			return textureDatas;

		} catch (FileNotFoundException e) {
			throw new TextureException(e);
		}
	}
}
