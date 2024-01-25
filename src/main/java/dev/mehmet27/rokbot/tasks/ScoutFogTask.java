package dev.mehmet27.rokbot.tasks;

import dev.mehmet27.rokbot.IMAGEPATHS;
import dev.mehmet27.rokbot.LocXY;
import dev.mehmet27.rokbot.MatchResult;
import dev.mehmet27.rokbot.utils.AdbUtils;
import dev.mehmet27.rokbot.utils.GuiUtils;

public class ScoutFogTask extends Task {

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
            }
            // Check main screen is scout management if not go to scout management
            openScoutCamp();
            result = GuiUtils.checkIsPath(IMAGEPATHS.EXPLORE_BUTTON);
            if (result.isMatch()) {
                // One of scouter is available
                AdbUtils.tap(result.getLoc());
                getLogger().info("clicked scout button");
                while (true) {
                    if (GuiUtils.checkIsPath(IMAGEPATHS.EXPLORE_BUTTON_2).isMatch()) break;
                }
                result = GuiUtils.checkIsPath(IMAGEPATHS.EXPLORE_BUTTON_2);
                if (result.isMatch()) {
                    // Haritada sise tıkla
                    AdbUtils.tap(result.getLoc());
                    getLogger().info("clicked fog in map");
                    while (true) {
                        if (GuiUtils.checkIsPath(IMAGEPATHS.SCOUT_SEND_BUTTON).isMatch()) break;
                    }
                    result = GuiUtils.checkIsPath(IMAGEPATHS.SCOUT_SEND_BUTTON);
                    if (result.isMatch()) {
                        AdbUtils.tap(result.getLoc());
                        getLogger().info("Scouter was send successfully.");
                        backToHome();
                    }
                }
            } else {
                // All scouters is busy
                result = GuiUtils.checkIsPath(IMAGEPATHS.SCOUT_CAMP_EXIT_BUTTON);
                if (result.isMatch()) {
                    AdbUtils.tap(result.getLoc());
                    getLogger().info("Scout management menu is closed");
                }
            }
        }
    }
}
