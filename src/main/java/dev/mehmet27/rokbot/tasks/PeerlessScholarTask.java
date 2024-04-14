package dev.mehmet27.rokbot.tasks;

import dev.mehmet27.rokbot.LocXYRandom;
import dev.mehmet27.rokbot.Main;
import dev.mehmet27.rokbot.peerlessscholar.QuestionNotFoundException;
import dev.mehmet27.rokbot.utils.AdbUtils;
import dev.mehmet27.rokbot.utils.TesseractUtils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import se.vidstige.jadb.JadbDevice;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class PeerlessScholarTask extends Task {

	private JadbDevice device = null;

	private LocXYRandom foodPos = new LocXYRandom(410, 470, 595, 665);
	private LocXYRandom woodPos = new LocXYRandom(595, 670, 595, 665);
	private LocXYRandom stonePos = new LocXYRandom(785, 860, 595, 665);
	private LocXYRandom goldPos = new LocXYRandom(985, 1055, 595, 665);

	public PeerlessScholarTask(JadbDevice device) {
		super(device);
	}

	@Override
	public void run(JadbDevice device) {
		this.device = device;

		Mat screenshot = AdbUtils.getScreenShot(device);
		int questNumber = 1;//Integer.parseInt(imageToString(screenshot.submat(177, 219, 284, 332), true));
		int questCount = 11 - questNumber;
		Main.getInstance().getMainForm().appendToTextArea("Total questions: " + questCount);
		for (int i = 0; i < questCount; i++) {
			sleepUntil(1500);
			screenshot = AdbUtils.getScreenShot(device);
			String question = fixSymbols(imageToString(screenshot.submat(185, 290, 335, 1140)));
			System.out.println(question);
			String answer;
			try {
				answer = Main.getInstance().getStorageManager().getQuestionAnswer(question);
			} catch (QuestionNotFoundException e) {
				Main.getInstance().getMainForm().appendToTextArea("Stuck, because can't find answer for question: " + question);
				Main.getInstance().getMainForm().appendToTextArea("We will add answer for this question.");
				//Main.getInstance().getStorageManager().insertQuestion(question);
				e.printStackTrace();
				continue;
			}
			String answerA = imageToString(screenshot.submat(294, 366, 319, 701));
			System.out.printf("Checking answer/A: %s / %s%n", answer, answerA);
			if (answer.equalsIgnoreCase(answerA)) {
				AdbUtils.tap(new LocXYRandom(319, 701, 294, 366));
				System.out.println("Question is solved.");
				Main.getInstance().getMainForm().appendToTextArea(String.format("Question %s is solved.", questNumber));
				continue;
			}
			String answerB = imageToString(screenshot.submat(294, 366, 760, 1143));
			System.out.printf("Checking answer/B: %s / %s%n", answer, answerB);
			Main.getInstance().getMainForm().appendToTextArea(String.format("%s/%s", answer, answerB));
			if (answer.equalsIgnoreCase(answerB)) {
				AdbUtils.tap(new LocXYRandom(760, 1143, 294, 366));
				System.out.println("Question is solved.");
				Main.getInstance().getMainForm().appendToTextArea(String.format("Question %s is solved.", questNumber));
				continue;
			}
			String answerC = imageToString(screenshot.submat(391, 465, 319, 701));
			System.out.printf("Checking answer/C: %s / %s%n", answer, answerC);
			if (answer.equalsIgnoreCase(answerC)) {
				AdbUtils.tap(new LocXYRandom(319, 701, 391, 465));
				System.out.println("Question is solved.");
				Main.getInstance().getMainForm().appendToTextArea(String.format("Question %s is solved.", questNumber));
				continue;
			}
			String answerD = imageToString(screenshot.submat(391, 465, 760, 1143));
			System.out.printf("Checking answer/D: %s / %s%n", answer, answerD);
			if (answer.equalsIgnoreCase(answerD)) {
				AdbUtils.tap(new LocXYRandom(760, 1143, 391, 465));
				System.out.println("Question is solved.");
				Main.getInstance().getMainForm().appendToTextArea(String.format("Question %s is solved.", questNumber));
				continue;
			}
		}
	}

	private String imageToString(Mat mat) {
		return imageToString(mat, false);
	}

	private String imageToString(Mat mat, boolean onlyDigits) {
		MatOfByte mob = new MatOfByte();
		if (onlyDigits){
			Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
			Imgproc.threshold(mat, mat, 128, 255, Imgproc.THRESH_BINARY);
		}
		Mat resized = new Mat();
		Imgproc.resize(mat, resized, new Size(mat.width() * 4.0, mat.height() * 4.0), 0, 0, Imgproc.INTER_CUBIC);
		Imgcodecs.imwrite(Main.getInstance().getConfigManager().getDataFolder().resolve("aaaa.png").toString(), resized);
		Imgcodecs.imencode(".png", resized, mob);
		byte[] byteArray = mob.toArray();
		try {
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(byteArray));
			String resultStr;
			if (onlyDigits) {
				resultStr = TesseractUtils.imageToString(image, onlyDigits);
			} else {
				resultStr = TesseractUtils.imageToString(image);
			}
			if (resultStr != null) {
				return resultStr;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String fixSymbols(String string) {
		return string.replace("”", "\"").replace("’", "'");
	}
}