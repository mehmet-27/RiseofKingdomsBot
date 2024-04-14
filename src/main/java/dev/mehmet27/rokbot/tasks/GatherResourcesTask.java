package dev.mehmet27.rokbot.tasks;

import dev.mehmet27.rokbot.*;
import dev.mehmet27.rokbot.utils.AdbUtils;
import dev.mehmet27.rokbot.utils.GuiUtils;
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
import java.util.Map;

public class GatherResourcesTask extends Task {

	private JadbDevice device = null;

	private final LocXY magnifierPos = new LocXY(60, 540);
	private LocXY lastResourcePos = null;
	private boolean shouldDecreaseLevel = false;
	private LocXYRandom foodPos = new LocXYRandom(410, 470, 595, 665);
	private LocXYRandom woodPos = new LocXYRandom(595, 670, 595, 665);
	private LocXYRandom stonePos = new LocXYRandom(785, 860, 595, 665);
	private LocXYRandom goldPos = new LocXYRandom(985, 1055, 595, 665);

	public GatherResourcesTask(JadbDevice device) {
		super(device);
	}

	@Override
	public void run(JadbDevice device) {
		this.device = device;

		if (Integer.parseInt((String) Main.getInstance().getMainForm().getFoodBox().getSelectedItem()) == 0 &&
				Integer.parseInt((String) Main.getInstance().getMainForm().getWoodBox().getSelectedItem()) == 0 &&
				Integer.parseInt((String) Main.getInstance().getMainForm().getStoneBox().getSelectedItem()) == 0 &&
				Integer.parseInt((String) Main.getInstance().getMainForm().getGoldBox().getSelectedItem()) == 0) {
			Main.getInstance().getMainForm().appendToTextArea("No resource selected to be collect!");
			return;
		}
		String lastResourcePos = "";
		MatchResult result = GuiUtils.checkIsMap(device);
		if (!result.isMatch()) {
			getLogger().info("not on map");
			result = GuiUtils.checkIsHome(device);
			if (result.isMatch()) {
				getLogger().info("on home");
				LocXY loc = result.getLoc();
				getLogger().info(loc.toString());
				AdbUtils.tap(loc);
				getLogger().info("back to map");
				while (true) {
					if (GuiUtils.checkIsHome(device).isMatch()) break;
				}
			}
		}
		Integer[] querySpace = checkQuerySpace();
		if (querySpace[0] == 0 || querySpace[1] == 0) {
			Main.getInstance().getMainForm().appendToTextArea("Can't check empty query spaces!");
			return;
		}
		int emptyQuerySpace = (querySpace[1] - querySpace[0]);
		if (emptyQuerySpace <= 1) {
			Main.getInstance().getMainForm().appendToTextArea("There is no empty query space!");
			return;
		} else {
			Main.getInstance().getMainForm().appendToTextArea(String.format("There is here %s empty query space!", emptyQuerySpace));
		}

		Main.getInstance().getMainForm().appendToTextArea("Trying to get resource amounts!");
		ResourceAmount resourceAmount = getResourceAmount();
		Main.getInstance().getMainForm().appendToTextArea(String.format("Food: %s", resourceAmount.getFood()));
		Main.getInstance().getMainForm().appendToTextArea(String.format("Wood: %s", resourceAmount.getWood()));
		Main.getInstance().getMainForm().appendToTextArea(String.format("Stone: %s", resourceAmount.getStone()));
		Main.getInstance().getMainForm().appendToTextArea(String.format("Gold: %s", resourceAmount.getGold()));

		for (int i = 0; i < emptyQuerySpace; i++) {
			AdbUtils.tap(magnifierPos);
			sleepUntil(1000);
			resourceAmount.getAsSortedMap().forEach((resourceType, integer) -> System.out.println(resourceType + "" + integer));
			for (Map.Entry<ResourceAmount.ResourceType, Integer> entry : resourceAmount.getAsSortedMap().entrySet()) {
				if (entry.getKey().equals(ResourceAmount.ResourceType.FOOD)) {
					if (Integer.parseInt((String) Main.getInstance().getMainForm().getFoodBox().getSelectedItem()) != 0) {
						Main.getInstance().getMainForm().appendToTextArea("Searching food...");
						AdbUtils.tap(foodPos);
						break;
					}
				}
				if (entry.getKey().equals(ResourceAmount.ResourceType.WOOD)) {
					if (Integer.parseInt((String) Main.getInstance().getMainForm().getWoodBox().getSelectedItem()) != 0) {
						Main.getInstance().getMainForm().appendToTextArea("Searching wood...");
						AdbUtils.tap(woodPos);
						break;
					}
				}
				if (entry.getKey().equals(ResourceAmount.ResourceType.STONE)) {
					if (Integer.parseInt((String) Main.getInstance().getMainForm().getStoneBox().getSelectedItem()) != 0) {
						Main.getInstance().getMainForm().appendToTextArea("Searching stone...");
						AdbUtils.tap(stonePos);
						break;
					}
				}
				if (entry.getKey().equals(ResourceAmount.ResourceType.GOLD)) {
					if (Integer.parseInt((String) Main.getInstance().getMainForm().getGoldBox().getSelectedItem()) != 0) {
						Main.getInstance().getMainForm().appendToTextArea("Searching gold...");
						AdbUtils.tap(goldPos);
						break;
					}
				}
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			result = GuiUtils.checkIsPath(IMAGEPATHS.RESOURCE_SEARCH_BUTTON, device);
			if (!result.isMatch()) {
				return;
			}
			AdbUtils.tap(result.getLoc());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			AdbUtils.tap(new LocXY(640, 360));
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String newResourceLoc = getResourceLocation();
			if (newResourceLoc == null) {
				Main.getInstance().getMainForm().appendToTextArea("Resource point can't found!");
				return;
			}
			if (lastResourcePos.contains(newResourceLoc)) {
				Main.getInstance().getMainForm().appendToTextArea("Resource point is already in match!");
				return;
			}
			result = GuiUtils.checkIsPath(IMAGEPATHS.RESOURCE_GATHER_BUTTON, device);
			if (result.isMatch()) {
				Main.getInstance().getMainForm().appendToTextArea("Resource point found");
				AdbUtils.tap(result.getLoc());
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// New March Location
				AdbUtils.tapRandom(950, 1080, 125, 170);
				try {
					Thread.sleep(1750);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (!Main.getInstance().getMainForm().getUseSecondaryCommanderCheckBox().isSelected()) {
					// Remove secondary commander location
					AdbUtils.tapRandom(465, 475, 462, 472);
				}
				try {
					Thread.sleep(1600);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				AdbUtils.tapRandom(810, 1030, 610, 660);
				lastResourcePos = newResourceLoc;
				Main.getInstance().getMainForm().appendToTextArea("Troops sent successfully.");
			} else {
				Main.getInstance().getMainForm().appendToTextArea("Stone not found");
				return;
			}
		}
	}

	private ResourceAmount getResourceAmount() {
		AdbUtils.tap(new LocXYRandom(675, 775, 12, 32));
		Mat screenshot = AdbUtils.getScreenShot(device);
		Mat foodMat = screenshot.submat(10, 34, 695, 770);
		String food = removeAbbreviation(TesseractUtils.imageToString(GuiUtils.matToImage(foodMat)));
		Mat woodMat = screenshot.submat(10, 34, 820, 890);
		String wood = removeAbbreviation(TesseractUtils.imageToString(GuiUtils.matToImage(woodMat)));
		Mat stoneMat = screenshot.submat(10, 34, 943, 1015);
		String stone = removeAbbreviation(TesseractUtils.imageToString(GuiUtils.matToImage(stoneMat)));
		Mat goldMat = screenshot.submat(10, 34, 1065, 1140);
		String gold = removeAbbreviation(TesseractUtils.imageToString(GuiUtils.matToImage(goldMat)));
		return new ResourceAmount(Integer.parseInt(food), Integer.parseInt(wood), Integer.parseInt(stone), Integer.parseInt(gold));
	}

	private String removeAbbreviation(String string) {
		return string.replace(".", "")
				.replace("B", "00000000")
				.replace("M", "00000")
				.replace("K", "00");
	}

	private Integer[] checkQuerySpace() {
		MatchResult result = GuiUtils.checkIsPath(IMAGEPATHS.HAS_MATCH_QUERY, device);
		if (!result.isMatch()) {
			return new Integer[]{1, 6};
		}
		Mat screenshot = AdbUtils.getScreenShot(device);
		Mat cropped = screenshot.submat(162, 179, 1211, 1242);

		MatOfByte mob = new MatOfByte();
		Imgproc.cvtColor(cropped, cropped, Imgproc.COLOR_BGR2GRAY);
		Imgproc.threshold(cropped, cropped, 128, 255, Imgproc.THRESH_BINARY);
		Imgproc.resize(cropped, cropped, new Size(cropped.width() * 4.0, cropped.height() * 4.0));
		Imgcodecs.imwrite(Main.getInstance().getConfigManager().getDataFolder().resolve("cropped.png").toString(), cropped);
		Imgcodecs.imencode(".png", cropped, mob);
		byte[] byteArray = mob.toArray();
		try {
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(byteArray));
			String resultStr = TesseractUtils.imageToString(image);
			if (resultStr != null) {
				return new Integer[]{Integer.parseInt(resultStr.split("/")[0]), Integer.parseInt(resultStr.split("/")[1])};
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Integer[]{0, 0};
	}

	private String getResourceLocation() {
		Mat screenshot = AdbUtils.getScreenShot(device);
		Mat cropped = screenshot.submat(188, 208, 885, 1035);
		MatOfByte mob = new MatOfByte();
		Imgproc.cvtColor(cropped, cropped, Imgproc.COLOR_BGR2GRAY);
		Imgproc.threshold(cropped, cropped, 215, 255, Imgproc.THRESH_BINARY);
		Imgproc.resize(cropped, cropped, new Size(cropped.width() * 4.0, cropped.height() * 4.0));
		Imgcodecs.imwrite(Main.getInstance().getConfigManager().getDataFolder().resolve("cropped.png").toString(), cropped);
		Imgcodecs.imencode(".png", cropped, mob);
		byte[] byteArray = mob.toArray();
		try {
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(byteArray));
			String resultStr = TesseractUtils.imageToString(image);
			if (resultStr != null) {
				return resultStr;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
