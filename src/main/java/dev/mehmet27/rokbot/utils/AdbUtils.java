package dev.mehmet27.rokbot.utils;

import dev.mehmet27.rokbot.LocXY;
import dev.mehmet27.rokbot.LocXYRandom;
import dev.mehmet27.rokbot.Main;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import se.vidstige.jadb.ConnectionToRemoteDeviceException;
import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

public class AdbUtils {

	private static final JadbConnection jadbConnection = Main.getInstance().getAdbManager().getJadbConnection();

	public static void runRok(JadbDevice device) {
		String command = "am start -n com.lilithgame.roc.gp/com.harry.engine.MainActivity";
		try {
			device.executeShell(command);
		} catch (IOException | JadbException e) {
			e.printStackTrace();
		}
	}

	public static void stopRok(JadbDevice device) {
		String command = "am force-stop com.lilithgame.roc.gp";
		try {
			device.executeShell(command);
		} catch (IOException | JadbException e) {
			e.printStackTrace();
		}
	}

	public static Mat getScreenShot(JadbDevice device) {
		try {
			Process process = Runtime.getRuntime().exec(String.format("adb\\adb -s %s exec-out screencap -p", device.getSerial()));
			InputStream stream = process.getInputStream();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = stream.read(buffer)) != -1) {
				baos.write(buffer, 0, bytesRead);
			}

			// Convert bytes to BufferedImage
			byte[] imageData = baos.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
			BufferedImage image = ImageIO.read(bais);
			File file = Main.getInstance().getConfigManager().getDataFolder().resolve("screenshot.png").toFile();
			ImageIO.write(image, "png", file);
			Mat screenshot = Imgcodecs.imread(file.getAbsolutePath());
			Main.getInstance().getCacheManager().setLastScreenShot(screenshot);
			return screenshot;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static DeviceSize getDeviceSize(JadbDevice device) {
		try {
			Process process = Runtime.getRuntime().exec(String.format("adb\\adb -s %s shell wm size", device.getSerial()));
			// İşlem çıktısını almak için bir InputStream kullanın
			InputStream inputStream = process.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder output = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				// Satırları okuyarak çıktıyı oluşturun
				output.append(line).append("\n");
			}
			String[] result = output.toString().replace("Physical size:", "")
					.replace(" ", "")
					.replaceAll("\\r?\\n", "")
					.split("x");
			// Finish process
			process.destroy();
			return new DeviceSize(Integer.parseInt(result[0]), Integer.parseInt(result[1]));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new DeviceSize(0, 0);
	}

	public static void tap(LocXY loc) {
		tap(loc.getX(), loc.getY());
	}

	public static void tap(LOC loc) {
		tap(loc.getLoc());
	}

	public static void tap(LocXYRandom loc) {
		tap(new Random().nextInt(loc.getxMax() - loc.getxMin()) + loc.getxMin(),
				new Random().nextInt(loc.getyMax() - loc.getyMin()) + loc.getyMin());
	}

	public static void tapRandom(int xMin, int xMax, int yMin, int yMax) {
		tap(new Random().nextInt(xMax - xMin) + xMin,
				new Random().nextInt(yMax - yMin) + yMin);
	}

	public static void tap(int x, int y) {
		try {
			String command = String.format("input tap %s %s", x, y);
			jadbConnection.getDevices().get(0).executeShell(command);
		} catch (IOException | JadbException e) {
			e.printStackTrace();
		}
	}

	public static List<JadbDevice> getDevices() throws IOException, JadbException {
		return jadbConnection.getDevices();
	}

	public static String shellToString(JadbDevice device, String command) throws IOException, JadbException {
		InputStream inputStream = device.executeShell(command);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
		}
		return stringBuilder.toString();
	}

	public static boolean isRokRunning(JadbDevice device) {
		try {
			if (AdbUtils.shellToString(device, "dumpsys window windows | grep mCurrentFocus")
					.contains("com.lilithgame.roc.gp/com.harry.engine.MainActivity")) {
				return true;
			}
		} catch (IOException | JadbException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void connectToDevice(int port) {
		try {
			jadbConnection.connectToTcpDevice(new InetSocketAddress("localhost", port));
		} catch (IOException | JadbException | ConnectionToRemoteDeviceException e) {
			e.printStackTrace();
		}
	}

	public static JadbDevice getDevice() {
		return jadbConnection.getAnyDevice();
	}

	public enum LOC {
		KILL_INFO(new LocXYRandom(886, 904, 243, 260)),
		MORE_INFO(new LocXYRandom(250, 315, 565, 621));

		LocXYRandom loc;

		LOC(LocXYRandom locXYRandom) {
			this.loc = locXYRandom;
		}

		public LocXYRandom getLoc() {
			return loc;
		}
	}
}

