package com.erakin.tools;

import static org.diverproject.util.MessageUtil.showError;
import static org.diverproject.util.MessageUtil.showException;
import static org.diverproject.util.MessageUtil.showInput;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import org.diverproject.util.SystemUtil;
import org.diverproject.util.lang.IntUtil;

public class SplitImageToChunks
{
	public static void main(String args[])
	{
		SystemUtil.setWindowsInterface();

		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Selecione o Bitmap");
		chooser.setMultiSelectionEnabled(false);
		chooser.setCurrentDirectory(new File("D:/Andrew/Workspace/Java/Asckaryn/trunk/data/worlds"));

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
			open(chooser.getSelectedFile());
	}

	private static void open(File file)
	{
		try {

			BufferedImage bi = ImageIO.read(file);
			int biw = bi.getWidth();
			int bih = bi.getHeight();

			BufferedImage bia = new BufferedImage(biw + 3, bih + 3, BufferedImage.TYPE_BYTE_GRAY);
			int biaw = bia.getWidth();
			int biah = bia.getHeight();

			Graphics g = bia.getGraphics();
			g.setColor(Color.white);
			g.fillRect(0, 0, biaw, biah);

			g.drawImage(bi, 0, 0, 1, 1, 0, 0, 1, 1, null);
			g.drawImage(bi, 1, 1, biw+1, bih+1, 0, 0, biw, bih, null);
			g.drawImage(bi, 1, 0, biw+1, 1, 0, 0, biw, 1, null);
			g.drawImage(bi, biaw-2, 0, biaw, 1, biw-1, 0, biw, 1, null);
			g.drawImage(bi, biaw-2, 1, biaw, biah-2, biw-1, 1, biw, bih-1, null);
			g.drawImage(bi, biaw-2, biah-2, biaw, biah, biw-1, bih-1, biw, bih, null);
			g.drawImage(bi, 1, biah-2, biaw-2, biah, 1, bih-1, biw-1, bih, null);
			g.drawImage(bi, 0, biah-2, 1, biah, 0, bih-1, 1, bih, null);
			g.drawImage(bi, 0, 1, 1, biah-2, 0, 1, 1, bih-1, null);

			int width = 0;
			int length = 0;
			String str = null;

			while (str == null)
			{
				str = showInput("Exportar", "Digite o tamanho de cada chunk, {width} {length}:");

				width = IntUtil.parse(str.split(" ")[0], -1);
				length = IntUtil.parse(str.split(" ")[1], -1);

				if (width == -1 || length == -1)
					str = null;
			}

			int chunkWidth = (bia.getWidth() - 3) / width;
			int chunkLength = (bia.getHeight() - 3) / length;

			BufferedImage chunk = new BufferedImage(width + 3, length + 3, BufferedImage.TYPE_BYTE_GRAY);
			g = chunk.getGraphics();

			for (int x = 0; x < chunkWidth; x++)
				for (int y = 0; y < chunkLength; y++)
				{
					int dx1 = 0;							int dy1 = 0;
					int dx2 = width + 3;					int dy2 = length + 3;

					int sx1 = (x * width);					int sy1 = (y * length);
					int sx2 = ((x + 1) * width) + 3;		int sy2 = ((y + 1) * length) + 3;

					g.drawImage(bia, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
					writeChunk(file, chunk, width, length, x, y);
				}

		} catch (IOException e) {
			showException(e);
		}
	}

	private static void writeChunk(File file, BufferedImage chunk, int width, int length, int x, int y)
	{
		String pathname = String.format("%s\\split\\terrain_%d-%d.bmp", file.getParent(), y, x);
		File output = new File(pathname);
		output.getParentFile().mkdirs();

		try {
			ImageIO.write(chunk, "BMP", output);
			System.out.println(pathname);
		} catch (IOException e) {
			showError("Exportar", "Falha ao exportar: %s", pathname);
			System.exit(0);
		}
	}
}
