package dev.mehmet27.rokbot.tasks;

import dev.mehmet27.rokbot.IMAGEPATHS;
import dev.mehmet27.rokbot.LocXY;
import dev.mehmet27.rokbot.Main;
import dev.mehmet27.rokbot.MatchResult;
import dev.mehmet27.rokbot.configuration.file.FileConfiguration;
import dev.mehmet27.rokbot.utils.AdbUtils;
import dev.mehmet27.rokbot.utils.GuiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;

public class Task {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FileConfiguration config = Main.getInstance().getConfigManager().getConfig();

    public ScheduledExecutorService getScheduler() {
        return Main.getInstance().getTaskExecutorService();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public Logger getLogger() {
        return logger;
    }

    public void run() {
    }

    public void openScoutCamp() {
        getLogger().info("scout camp x: " + getConfig().getInt("scout.scoutCampPos.x", 0));
        getLogger().info("scout camp y: " + getConfig().getInt("scout.scoutCampPos.y", 0));
        int x = getConfig().getInt("scout.scoutCampPos.x", 0);
        int y = getConfig().getInt("scout.scoutCampPos.y", 0);
        AdbUtils.tap(new LocXY(x, y));
        getLogger().info("clicked scout camp");
        while (true) {
            if (GuiUtils.checkIsPath(IMAGEPATHS.SCOUT_BUTTON).isMatch()) break;
        }
        MatchResult result = GuiUtils.checkIsPath(IMAGEPATHS.SCOUT_BUTTON);
        if (result.isMatch()) {
            AdbUtils.tap(result.getLoc());
            getLogger().info("scout camp opened");
            while (true) {
                if (GuiUtils.checkIsPath(IMAGEPATHS.SCOUT_MANAGEMENT).isMatch()) break;
            }
        }
    }

    public void backToHome(){
        MatchResult result = GuiUtils.checkIsHome();
        if (!result.isMatch()) {
            result = GuiUtils.checkIsMap();
            if (result.isMatch()) {
                LocXY loc = result.getLoc();
                getLogger().info(loc.toString());
                AdbUtils.tap(loc);
                getLogger().info("Backing to home");
                while (true) {
                    if (GuiUtils.checkIsHome().isMatch()) break;
                }
            }
        } else {
            getLogger().info("Already on home");
        }
    }
}
