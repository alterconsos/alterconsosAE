package fr.hypertable;

import fr.hypertable.AppTransaction.StatusPhase;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class StorePhoto extends Operation {

	public String line;
	public String column;
	String content;
	String mime;
	String prefix;
	byte[] newImageData;
	Archive archive;

	@Override public String mainLine() {
		return line;
	}

	@Override public StatusPhase phaseFaible() throws AppException {
		column = arg().getS("c");
		line = arg().getS("l");
		content = arg().getS("content");
		mime = arg().getS("mime");
		prefix = "data:" + mime + ";base64,";

		byte[] oldImageData = null;
		newImageData = null;
		int targetWidth = 256;
		int targetHeight = 256;
		try {
			String type = mime.substring(mime.indexOf("/") + 1);
			oldImageData = Base64.getDecoder().decode(content.substring(prefix.length()));
			// oldImageData = Base64.decodeToBytes(content.substring(prefix.length()));
			BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(oldImageData));
			int desiredSize = 256;
			BufferedImage resizedImage = new BufferedImage(desiredSize, desiredSize, BufferedImage.TYPE_INT_RGB);

			Graphics2D g = resizedImage.createGraphics();
			g.setPaint(Color.WHITE);
			g.fillRect(0, 0, desiredSize, desiredSize);

			int tempWidth;
			int tempHeight;
			int y = 0;
			int x = 0;

			if (originalImage.getHeight() < originalImage.getWidth()) {
				tempWidth = desiredSize;
				tempHeight = (int)(((double)originalImage.getHeight()*desiredSize)/originalImage.getWidth());
				y = -(tempHeight - tempWidth)/2;
			}
			else {
				tempHeight = desiredSize;
				tempWidth = (int)(((double)originalImage.getWidth()*desiredSize)/originalImage.getHeight());
				x = -(tempWidth - tempHeight)/2;
			}

			g.drawImage(originalImage.getScaledInstance(tempWidth, tempHeight, Image.SCALE_SMOOTH), x, y, null);

			g.dispose();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(resizedImage, type, baos);
			newImageData = baos.toByteArray();
		} catch (Exception e) {
			throw new AppException(e, MF.PHOTO, mime);
		}
		archive = new Archive(line, column, "PHOTO", mime, newImageData, 0);
		return StatusPhase.transactionSimple;
	}

	@Override public void phaseForte() throws AppException {
		archive.putArchiveInStorage();
	}

}
