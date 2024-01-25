package dev.mehmet27.rokbot.tasks;

import dev.mehmet27.rokbot.IMAGEPATHS;
import dev.mehmet27.rokbot.LocXY;
import dev.mehmet27.rokbot.MatchResult;
import dev.mehmet27.rokbot.utils.AdbUtils;
import dev.mehmet27.rokbot.utils.GuiUtils;

public class AllianceHelpTask extends Task {

    @Override
    public void run() {
        // Check main screen is home if not go to home
        MatchResult result = GuiUtils.checkIsPath(IMAGEPATHS.ALLIANCE_BUTTON);
        if (result.isMatch()) {
            getLogger().info("clicking alliance");
            LocXY loc = result.getLoc();
            AdbUtils.tap(loc);
            while (true) {
                if (GuiUtils.checkIsPath(IMAGEPATHS.ALLIANCE_HELP).isMatch()) break;
            }
            result = GuiUtils.checkIsPath(IMAGEPATHS.ALLIANCE_HELP);
            if (result.isMatch()) {
                getLogger().info("entering help menu");
                AdbUtils.tap(result.getLoc());
                while (true) {
                    if (GuiUtils.checkIsPath(IMAGEPATHS.ALLIANCE_HELP_BUTTON).isMatch()) break;
                }
                result = GuiUtils.checkIsPath(IMAGEPATHS.ALLIANCE_HELP_BUTTON);
                if (result.isMatch()) {
                    AdbUtils.tap(result.getLoc());
                    getLogger().info("Help success");
                } else {
                    result = GuiUtils.checkIsPath(IMAGEPATHS.SCOUT_CAMP_EXIT_BUTTON);
                    if (result.isMatch()) {
                        AdbUtils.tap(result.getLoc());
                        getLogger().info("Alliance help menu is closed");
                    }
                }

            }
        }
    }
}
