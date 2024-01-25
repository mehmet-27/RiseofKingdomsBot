package dev.mehmet27.rokbot.tasks;

import dev.mehmet27.rokbot.IMAGEPATHS;
import dev.mehmet27.rokbot.LocXY;
import dev.mehmet27.rokbot.MatchResult;
import dev.mehmet27.rokbot.utils.AdbUtils;
import dev.mehmet27.rokbot.utils.DeviceSize;
import dev.mehmet27.rokbot.utils.GuiUtils;

public class CollectVillagesTask extends Task {

    @Override
    public void run() {
        if (getConfig().getBoolean("scout.investigation", true)) {
            // Check main screen is home if not go to home
            MatchResult result = GuiUtils.checkIsHome();
            if (!result.isMatch()) {
                getLogger().info("not on home");
                result = GuiUtils.checkIsMap();
                if (result.isMatch()) {
                    getLogger().info("on map");
                    LocXY loc = result.getLoc();
                    getLogger().info(loc.toString());
                    AdbUtils.tap(loc);
                    getLogger().info("back to home");
                    while (true) {
                        if (GuiUtils.checkIsHome().isMatch()) break;
                    }
                }
            } else {
                getLogger().info("on home");
            }
            openScoutCamp();

            result = GuiUtils.checkIsPath(IMAGEPATHS.VILLAGES_BUTTON);
            if (result.isMatch()) {
                // Scouter is available
                AdbUtils.tap(result.getLoc());
                getLogger().info("clicked villages button");
                while (true) {
                    if (GuiUtils.checkIsPath(IMAGEPATHS.VILLAGES_GO_BUTTON).isMatch()) break;
                }
                result = GuiUtils.checkIsPath(IMAGEPATHS.VILLAGES_GO_BUTTON);
                if (result.isMatch()) {
                    AdbUtils.tap(result.getLoc());
                    getLogger().info("clicked village go button");
                    DeviceSize deviceSize = AdbUtils.getDeviceSize();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AdbUtils.tap(new LocXY(deviceSize.getHeight() / 2, deviceSize.getWidth() / 2));
                    getLogger().info("Collect village task is successfully.");
                    backToHome();
                }
            }
        }
    }
}
