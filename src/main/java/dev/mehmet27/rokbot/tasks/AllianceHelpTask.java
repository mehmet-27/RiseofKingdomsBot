package dev.mehmet27.rokbot.tasks;

import dev.mehmet27.rokbot.IMAGEPATHS;
import dev.mehmet27.rokbot.LocXY;
import dev.mehmet27.rokbot.MatchResult;
import dev.mehmet27.rokbot.utils.AdbUtils;
import dev.mehmet27.rokbot.utils.GuiUtils;
import se.vidstige.jadb.JadbDevice;

public class AllianceHelpTask extends Task {

    public AllianceHelpTask(JadbDevice device) {
        super(device);
    }

    @Override
    public void run(JadbDevice device) {
        // Check main screen is home if not go to home
        MatchResult result = GuiUtils.checkIsPath(IMAGEPATHS.ALLIANCE_BUTTON, device);
        if (result.isMatch()) {
            getLogger().info("clicking alliance");
            LocXY loc = result.getLoc();
            AdbUtils.tap(loc);
            while (true) {
                if (GuiUtils.checkIsPath(IMAGEPATHS.ALLIANCE_HELP, device).isMatch()) break;
            }
            result = GuiUtils.checkIsPath(IMAGEPATHS.ALLIANCE_HELP, device);
            if (result.isMatch()) {
                getLogger().info("entering help menu");
                AdbUtils.tap(result.getLoc());
                while (true) {
                    if (GuiUtils.checkIsPath(IMAGEPATHS.ALLIANCE_HELP_BUTTON, device).isMatch()) break;
                }
                result = GuiUtils.checkIsPath(IMAGEPATHS.ALLIANCE_HELP_BUTTON, device);
                if (result.isMatch()) {
                    AdbUtils.tap(result.getLoc());
                    getLogger().info("Help success");
                    while (true) {
                        if (GuiUtils.checkIsPath(IMAGEPATHS.SCOUT_CAMP_EXIT_BUTTON, device).isMatch()) break;
                    }
                    result = GuiUtils.checkIsPath(IMAGEPATHS.SCOUT_CAMP_EXIT_BUTTON, device);
                    if (result.isMatch()) {
                        AdbUtils.tap(result.getLoc());
                        getLogger().info("Alliance help menu is closed");
                    }
                } else {
                    result = GuiUtils.checkIsPath(IMAGEPATHS.SCOUT_CAMP_EXIT_BUTTON, device);
                    if (result.isMatch()) {
                        AdbUtils.tap(result.getLoc());
                        getLogger().info("Alliance help menu is closed");
                    }
                }

            }
        }
    }
}
