package dev.mehmet27.rokbot.tasks;

import dev.mehmet27.rokbot.LocXY;
import dev.mehmet27.rokbot.Main;
import dev.mehmet27.rokbot.MatchResult;
import dev.mehmet27.rokbot.configuration.file.FileConfiguration;
import dev.mehmet27.rokbot.utils.AdbUtils;
import dev.mehmet27.rokbot.utils.GuiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vidstige.jadb.JadbDevice;

import java.util.Random;
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

	private Task next;

	public final JadbDevice device;

	public Task(JadbDevice device) {
		this.device = device;
	}

	public void run(JadbDevice device) {
	}

	public void backToHome() {
		MatchResult result = GuiUtils.checkIsHome(device);
		if (!result.isMatch()) {
			result = GuiUtils.checkIsMap(device);
			if (result.isMatch()) {
				LocXY loc = result.getLoc();
				getLogger().info(loc.toString());
				AdbUtils.tap(loc);
				getLogger().info("Backing to home");
				while (true) {
					if (GuiUtils.checkIsHome(device).isMatch()) break;
				}
			}
		} else {
			getLogger().info("Already on home");
		}
	}

	public void backToMap() {
		MatchResult result = GuiUtils.checkIsMap(device);
		if (!result.isMatch()) {
			result = GuiUtils.checkIsHome(device);
			if (result.isMatch()) {
				LocXY loc = result.getLoc();
				getLogger().info(loc.toString());
				AdbUtils.tap(loc);
				getLogger().info("Backing to map");
				while (true) {
					if (GuiUtils.checkIsMap(device).isMatch()) break;
				}
			}
		} else {
			getLogger().info("Already on Map");
		}
	}

	public void sleepUntil(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void sleepUntil(long min, long max) {
		try {
			long wait = new Random().nextInt((int) (max - min)) + min;
			Thread.sleep(wait);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public Task getNextTask() {
		return next;
	}

	public void setNextTask(Task next) {
		this.next = next;
	}
}
