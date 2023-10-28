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
			BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2D = resizedImage.createGraphics();
			graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
			graphics2D.dispose();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(resizedImage, type, baos);
			newImageData = baos.toByteArray();
			/*
			ImagesService imagesService = ImagesServiceFactory.getImagesService();
			Image oldImage = ImagesServiceFactory.makeImage(oldImageData);
			Transform resize = ImagesServiceFactory.makeResize(256, 256, (double) (0.5),
					(double) (0.5));
			Image newImage = imagesService.applyTransform(resize, oldImage);
			newImageData = newImage.getImageData();
			 */
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
