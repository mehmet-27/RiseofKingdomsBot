package dev.mehmet27.rokbot.utils;

import dev.mehmet27.rokbot.IMAGEPATHS;
import dev.mehmet27.rokbot.LocXY;
import dev.mehmet27.rokbot.Main;
import dev.mehmet27.rokbot.MatchResult;
import dev.mehmet27.rokbot.managers.ConfigManager;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import se.vidstige.jadb.JadbDevice;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class GuiUtils {

    public GuiUtils() {

    }

    private static final ConfigManager configManager = Main.getInstance().getConfigManager();

    public static String getCurrentGuiName() {
        return "";
    }

    public static MatchResult checkIsPath(IMAGEPATHS imagepaths, JadbDevice device) {
        Mat mainImage = AdbUtils.getScreenShot(device);
        Mat template = Imgcodecs.imread(configManager.resolveResourcePath(imagepaths.getFileName()));

        DeviceSize deviceSize = AdbUtils.getDeviceSize(device);
        double ratio = (((double) deviceSize.getHeight() / 1280) + ((double) deviceSize.getWidth() / 720)) / 2;
        Imgproc.resize(template, template, new Size(template.size().width * ratio, template.size().height * ratio));

        // / Create the result matrix
        int result_cols = mainImage.cols() - template.cols() + 1;
        int result_rows = mainImage.rows() - template.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

        // / Do the Matching and Normalize
        Imgproc.matchTemplate(mainImage, template, result, Imgproc.TM_CCOEFF_NORMED);
        //Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());

        // / Localizing the best match with minMaxLoc
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);

        // Save images
        Imgproc.rectangle(mainImage, mmr.maxLoc, new Point(mmr.maxLoc.x + template.cols(), mmr.maxLoc.y + template.rows()), new Scalar(255, 0, 0));
        Imgcodecs.imwrite(configManager.getDataFolder().resolve(imagepaths.getFileName()).toString(), mainImage);
        //Imgcodecs.imwrite(configManager.getDataFolder().resolve("template.png").toString(), template);

        Point matchLoc;
        matchLoc = mmr.maxLoc;
        double minMatchQuality = 0.65;
        System.out.println("max val: " + mmr.maxVal);
        if (mmr.maxVal >= minMatchQuality) {
            System.out.println("bulundu: " + imagepaths.getFileName());
            LocXY loc = new LocXY((int) (matchLoc.x + (template.size().width / 2)), (int) (matchLoc.y + (template.size().height / 2)));
            return new MatchResult(true, loc);
        } else {
            System.out.println("bulunmadı: " + imagepaths.getFileName());
            return new MatchResult(false, new LocXY(0, 0));
        }
    }

    public static MatchResult checkIsHome(JadbDevice device) {
        return checkIsPath(IMAGEPATHS.MAP_BUTTON, device);
    }

    public static MatchResult checkIsMap(JadbDevice device) {
        return checkIsPath(IMAGEPATHS.HOME_BUTTON, device);
    }

    public static BufferedImage matToImage(Mat mat){
        MatOfByte matOfByte = new MatOfByte();
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(mat, mat, 128, 255, Imgproc.THRESH_BINARY);
        Imgproc.resize(mat, mat, new Size(mat.width() * 4.0, mat.height() * 4.0));
        //Imgcodecs.imwrite(Main.getInstance().getConfigManager().getDataFolder().resolve("aaaaaaa.png").toString(), mat);
        Imgcodecs.imencode(".png", mat, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        try {
            return ImageIO.read(new ByteArrayInputStream(byteArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
