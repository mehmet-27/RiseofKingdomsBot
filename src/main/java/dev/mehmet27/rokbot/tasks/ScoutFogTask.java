package dev.mehmet27.rokbot.tasks;

import dev.mehmet27.rokbot.IMAGEPATHS;
import dev.mehmet27.rokbot.LocXY;
import dev.mehmet27.rokbot.MatchResult;
import dev.mehmet27.rokbot.utils.AdbUtils;
import dev.mehmet27.rokbot.utils.GuiUtils;
import se.vidstige.jadb.JadbDevice;

public class ScoutFogTask extends Task {


    public ScoutFogTask(JadbDevice device) {
        super(device);
    }

    @Override
    public void run(JadbDevice device) {
        if (getConfig().getBoolean("scout.investigation", true)) {
            // Check main screen is home if not go to home
            MatchResult result = GuiUtils.checkIsHome( device);
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
            }
            // Check main screen is scout management if not go to scout management
            openScoutCamp();
            result = GuiUtils.checkIsPath(IMAGEPATHS.EXPLORE_BUTTON, device);
            if (result.isMatch()) {
                // One of scouter is available
                AdbUtils.tap(result.getLoc());
                getLogger().info("clicked scout button");
                while (true) {
                    if (GuiUtils.checkIsPath(IMAGEPATHS.EXPLORE_BUTTON_2, device).isMatch()) break;
                }
                result = GuiUtils.checkIsPath(IMAGEPATHS.EXPLORE_BUTTON_2, device);
                if (result.isMatch()) {
                    // Haritada sise tıkla
                    AdbUtils.tap(result.getLoc());
                    getLogger().info("clicked fog in map");
                    while (true) {
                        if (GuiUtils.checkIsPath(IMAGEPATHS.SCOUT_SEND_BUTTON, device).isMatch()) break;
                    }
                    result = GuiUtils.checkIsPath(IMAGEPATHS.SCOUT_SEND_BUTTON, device);
                    if (result.isMatch()) {
                        AdbUtils.tap(result.getLoc());
                        getLogger().info("Scouter was send successfully.");
                        backToHome();
                    }
                }
            } else {
                // All scouters is busy
                result = GuiUtils.checkIsPath(IMAGEPATHS.SCOUT_CAMP_EXIT_BUTTON, device);
                if (result.isMatch()) {
                    AdbUtils.tap(result.getLoc());
                    getLogger().info("Scout management menu is closed");
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
