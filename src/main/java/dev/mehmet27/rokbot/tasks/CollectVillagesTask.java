package dev.mehmet27.rokbot.tasks;

import dev.mehmet27.rokbot.IMAGEPATHS;
import dev.mehmet27.rokbot.LocXY;
import dev.mehmet27.rokbot.MatchResult;
import dev.mehmet27.rokbot.utils.AdbUtils;
import dev.mehmet27.rokbot.utils.DeviceSize;
import dev.mehmet27.rokbot.utils.GuiUtils;
import se.vidstige.jadb.JadbDevice;

public class CollectVillagesTask extends Task {

    public CollectVillagesTask(JadbDevice device) {
        super(device);
    }

    @Override
    public void run(JadbDevice device) {
        if (getConfig().getBoolean("scout.investigation", true)) {
            // Check main screen is home if not go to home
            MatchResult result = GuiUtils.checkIsHome(device);
            if (!result.isMatch()) {
                getLogger().info("not on home");
                result = GuiUtils.checkIsMap(device);
                if (result.isMatch()) {
                    getLogger().info("on map");
                    LocXY loc = result.getLoc();
                    getLogger().info(loc.toString());
                    AdbUtils.tap(loc);
                    getLogger().info("back to home");
                    while (true) {
                        if (GuiUtils.checkIsHome(device).isMatch()) break;
                    }
                }
            } else {
                getLogger().info("on home");
            }
            openScoutCamp();

            result = GuiUtils.checkIsPath(IMAGEPATHS.VILLAGES_BUTTON, device);
            if (result.isMatch()) {
                // Scouter is available
                AdbUtils.tap(result.getLoc());
                getLogger().info("clicked villages button");
                while (true) {
                    if (GuiUtils.checkIsPath(IMAGEPATHS.VILLAGES_GO_BUTTON, device).isMatch()) break;
                }
                result = GuiUtils.checkIsPath(IMAGEPATHS.VILLAGES_GO_BUTTON, device);
                if (result.isMatch()) {
                    AdbUtils.tap(result.getLoc());
                    getLogger().info("clicked village go button");
                    DeviceSize deviceSize = AdbUtils.getDeviceSize(device);
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

    public void openScoutCamp() {
        getLogger().info("scout camp x: " + getConfig().getInt("scout.scoutCampPos.x", 0));
        getLogger().info("scout camp y: " + getConfig().getInt("scout.scoutCampPos.y", 0));
        int x = getConfig().getInt("scout.scoutCampPos.x", 0);
        int y = getConfig().getInt("scout.scoutCampPos.y", 0);
        AdbUtils.tap(new LocXY(x, y));
        getLogger().info("clicked scout camp");
        while (true) {
            if (GuiUtils.checkIsPath(IMAGEPATHS.SCOUT_BUTTON, device).isMatch()) break;
        }
        MatchResult result = GuiUtils.checkIsPath(IMAGEPATHS.SCOUT_BUTTON, device);
        if (result.isMatch()) {
            AdbUtils.tap(result.getLoc());
            getLogger().info("scout camp opened");
            while (true) {
                if (GuiUtils.checkIsPath(IMAGEPATHS.SCOUT_MANAGEMENT, device).isMatch()) break;
            }
        }
    }
}
