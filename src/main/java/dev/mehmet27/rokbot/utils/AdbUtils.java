package dev.mehmet27.rokbot.utils;

import dev.mehmet27.rokbot.LocXY;
import dev.mehmet27.rokbot.Main;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import se.vidstige.jadb.JadbException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class AdbUtils {

    public static void runRok() {
        String command = "am start -n com.lilithgame.roc.gp/com.harry.engine.MainActivity";
        try {
            Main.getInstance().getAdb().getDevices().get(0).executeShell(command);
        } catch (IOException | JadbException e) {
            e.printStackTrace();
        }
    }

    public static void stopRok() {
        String command = "am force-stop com.lilithgame.roc.gp";
        try {
            Main.getInstance().getAdb().getDevices().get(0).executeShell(command);
        } catch (IOException | JadbException e) {
            e.printStackTrace();
        }
    }

    public static Mat getScreenShot() {
        try {
            Process process = Runtime.getRuntime().exec("adb\\adb exec-out screencap -p");
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
            return Imgcodecs.imread(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static DeviceSize getDeviceSize() {
        try {
            Process process = Runtime.getRuntime().exec("adb\\adb shell wm size");
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
        try {
            String command = String.format("input tap %s %s", loc.getX(), loc.getY());
            Main.getInstance().getAdb().getDevices().get(0).executeShell(command);
        } catch (IOException | JadbException e) {
            e.printStackTrace();
        }
    }
}
