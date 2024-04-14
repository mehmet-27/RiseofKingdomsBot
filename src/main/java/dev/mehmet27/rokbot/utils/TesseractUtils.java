package dev.mehmet27.rokbot.utils;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;

public class TesseractUtils {

	public static String imageToString(Mat mat) {
		return imageToString(GuiUtils.matToImage(mat), false);
	}

	public static String imageToString(BufferedImage bufferedImage) {
		return imageToString(bufferedImage, false);
	}

	public static String imageToString(BufferedImage bufferedImage, boolean onlyDigits) {
		ITesseract tesseract = new Tesseract();
		tesseract.setPageSegMode(6);
		tesseract.setDatapath(LoadLibs.extractTessResources("tessdata").getPath());
		if (onlyDigits) {
			tesseract.setTessVariable("tessedit_char_whitelist", "0123456789");
		}
		try {
			String result = tesseract.doOCR(bufferedImage).replace("\n", " ");
			if (result.endsWith(" ")) {
				result = result.substring(0, result.length() - 1);
			}
			return result;
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		return null;
	}
}
