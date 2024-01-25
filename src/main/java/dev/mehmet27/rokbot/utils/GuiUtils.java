package dev.mehmet27.rokbot.utils;

import dev.mehmet27.rokbot.IMAGEPATHS;
import dev.mehmet27.rokbot.LocXY;
import dev.mehmet27.rokbot.Main;
import dev.mehmet27.rokbot.MatchResult;
import dev.mehmet27.rokbot.managers.ConfigManager;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class GuiUtils {

    public GuiUtils() {

    }

    private static final ConfigManager configManager = Main.getInstance().getConfigManager();

    public static String getCurrentGuiName() {
        return "";
    }

    public static MatchResult checkIsPath(IMAGEPATHS imagepaths) {
        Mat mainImage = AdbUtils.getScreenShot();
        Mat template = Imgcodecs.imread(configManager.resolveResourcePath(imagepaths.getFileName()));

        DeviceSize deviceSize = AdbUtils.getDeviceSize();
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
        double minMatchQuality = 0.8;
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

    public static MatchResult checkIsHome() {
        return checkIsPath(IMAGEPATHS.MAP_BUTTON);
    }

    public static MatchResult checkIsMap() {
        return checkIsPath(IMAGEPATHS.HOME_BUTTON);
    }
}
