package dev.mehmet27.rokbot.tasks;

import dev.mehmet27.rokbot.Main;
import dev.mehmet27.rokbot.configuration.file.FileConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Task {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FileConfiguration config = Main.getInstance().getConfigManager().getConfig();

    public FileConfiguration getConfig() {
        return config;
    }

    public Logger getLogger() {
        return logger;
    }

    public void run() {
    }

    ;
}
