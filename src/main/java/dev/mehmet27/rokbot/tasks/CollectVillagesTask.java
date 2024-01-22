package dev.mehmet27.rokbot.tasks;

import dev.mehmet27.rokbot.IMAGEPATHS;
import dev.mehmet27.rokbot.LocXY;
import dev.mehmet27.rokbot.MatchResult;
import dev.mehmet27.rokbot.utils.AdbUtils;
import dev.mehmet27.rokbot.utils.GuiUtils;

public class CollectVillagesTask extends Task {

    @Override
    public void run() {
        if (getConfig().getBoolean("scout.investigation", true)) {
            // Check is scouter available
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
                }
            } else {
                result = GuiUtils.checkIsPath(IMAGEPATHS.SCOUT_MANAGEMENT);
                if (!result.isMatch()) {
                    getLogger().info("scout x: " + getConfig().getInt("scout.scoutCampPos.x", 0));
                    getLogger().info("scout y: " + getConfig().getInt("scout.scoutCampPos.y", 0));
                    int x = getConfig().getInt("scout.scoutCampPos.x", 0);
                    int y = getConfig().getInt("scout.scoutCampPos.y", 0);
                    AdbUtils.tap(new LocXY(x, y));
                    getLogger().info("clicked scout camp");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    result = GuiUtils.checkIsPath(IMAGEPATHS.SCOUT_BUTTON);
                    if (result.isMatch()) {
                        AdbUtils.tap(result.getLoc());
                        getLogger().info("scout camp opened");
                    } else {

                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    result = GuiUtils.checkIsPath(IMAGEPATHS.VILLAGES_BUTTON);
                    if (result.isMatch()) {
                        // Scouter is available
                        AdbUtils.tap(result.getLoc());
                        getLogger().info("clicked villages button");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        result = GuiUtils.checkIsPath(IMAGEPATHS.VILLAGES_GO_BUTTON);
                        if (result.isMatch()) {
                            AdbUtils.tap(result.getLoc());
                            getLogger().info("clicked village go button");
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            AdbUtils.tap(new LocXY(635, 340));
                            getLogger().info("Collect village task is successfully.");
                        }
                    } else {
                        // All scouters is busy
                    }
                }
            }
        }
    }
}
