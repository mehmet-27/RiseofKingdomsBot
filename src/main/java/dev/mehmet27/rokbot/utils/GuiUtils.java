package dev.mehmet27.rokbot.utils;

import dev.mehmet27.rokbot.IMAGEPATHS;
import dev.mehmet27.rokbot.LocXY;
import dev.mehmet27.rokbot.Main;
import dev.mehmet27.rokbot.managers.ConfigManager;
import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import se.vidstige.jadb.JadbException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GuiUtils {

    public GuiUtils() {

    }

    private static final ConfigManager configManager = Main.getInstance().getConfigManager();

    public static String getCurrentGuiName() {
        return "";
    }

    public static LocXY getLocXY(IMAGEPATHS imagepaths) {
        Mat mainImage = AdbUtils.getScreenShot();
        Mat template = Imgcodecs.imread(configManager.resolveResourcePath(imagepaths.getFileName()));

        // / Create the result matrix
        int result_cols = mainImage.cols() - template.cols() + 1;
        int result_rows = mainImage.rows() - template.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

        // / Do the Matching and Normalize
        Imgproc.matchTemplate(mainImage, template, result, Imgproc.TM_CCOEFF_NORMED);
        //Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());

        // / Localizing the best match with minMaxLoc
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);

        Point matchLoc;
        matchLoc = mmr.maxLoc;
        double minMatchQuality = 0.8;
        if (mmr.maxVal >= minMatchQuality) {
            return new LocXY((int) (matchLoc.x + (template.size().width / 2)), (int) (matchLoc.y + (template.size().height / 2)));
        } else
            System.out.println("bulunmadı");
        return new LocXY(0, 0);
    }

    public static boolean checkIsPath(IMAGEPATHS imagepaths) {
        Mat mainImage = AdbUtils.getScreenShot();
        Mat template = Imgcodecs.imread(configManager.resolveResourcePath(imagepaths.getFileName()));

        //mainImage = Imgcodecs.imread(configManager.getDataFolder().resolve("screenshot.png").toString());

        // / Create the result matrix
        int result_cols = mainImage.cols() - template.cols() + 1;
        int result_rows = mainImage.rows() - template.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

        // / Do the Matching and Normalize
        Imgproc.matchTemplate(mainImage, template, result, Imgproc.TM_CCOEFF_NORMED);
        //Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());

        // / Localizing the best match with minMaxLoc
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);

        if (imagepaths.equals(IMAGEPATHS.EXPLORE_BUTTON_2)) {
            Imgproc.rectangle(mainImage, mmr.maxLoc, new Point(mmr.maxLoc.x + template.cols(), mmr.maxLoc.y + template.rows()), new Scalar(0, 255, 0));
            Imgcodecs.imwrite(configManager.getDataFolder().resolve(imagepaths.getFileName()).toString(), mainImage);
        }

        Point matchLoc;
        matchLoc = mmr.maxLoc;
        double minMatchQuality = 0.8;
        if (mmr.maxVal >= minMatchQuality) {
            System.out.println("bulundu");
            return true;
        } else
            System.out.println("bulunmadı");
        return false;
    }

    public static boolean checkIsHome() {
        return checkIsPath(IMAGEPATHS.MAP_BUTTON);
    }

    public static boolean checkIsMap() {
        return checkIsPath(IMAGEPATHS.HOME_BUTTON);
    }

    public static String screenShot() {
        try {
            try {
                Robot robot = new Robot();
                Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                BufferedImage screenshot = robot.createScreenCapture(screenRect);
                File outputFile = new File(configManager.getDataFolder().resolve("screenshot.png").toString()); // Kaydedilecek dosyanın yolu ve adı

                ImageIO.write(screenshot, "png", outputFile);
                System.out.println("Screenshot saved: " + outputFile.getPath());
                return outputFile.getPath();
            } catch (AWTException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] toByteArray(InputStream inputStream) throws IOException {
        // Convert InputStream to byte array
        List<Byte> byteList = new ArrayList<>();
        int read;
        while ((read = inputStream.read()) != -1) {
            byteList.add((byte) read);
        }

        byte[] byteArray = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            byteArray[i] = byteList.get(i);
        }

        return byteArray;
    }

    private static Mat byteArrayToMat(byte[] byteArray) {
        // Convert byte array to Mat
        MatOfByte matOfByte = new MatOfByte(byteArray);

        return Imgcodecs.imdecode(matOfByte, Imgcodecs.IMREAD_UNCHANGED);
    }
}
