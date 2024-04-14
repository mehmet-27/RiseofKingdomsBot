package dev.mehmet27.rokbot;

import com.formdev.flatlaf.FlatDarculaLaf;
import dev.mehmet27.rokbot.frames.MainForm;
import dev.mehmet27.rokbot.managers.*;
import org.opencv.core.Core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Main {

	private static Main instance;
	public static final org.slf4j.Logger logger = LoggerFactory.getLogger(Main.class);
	private final ConfigManager configManager;

	private final Settings settings;

	private final StorageManager storageManager;
	private final CacheManager cacheManager;
	private final TaskManager taskManager;
	private final AdbManager adbManager;

	private MainForm mainForm;

	private final ScheduledExecutorService taskExecutorService = Executors.newScheduledThreadPool(1);

	private ScheduledFuture<?> runningTask = null;

	private boolean isTaskRunning = false;

	public static void main(String[] args) {
		try {
			new Main();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Main() throws Exception {
		getLogger().info("Loading Native OpenCV...");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		getLogger().info("OpenCV loaded successfully.");
		instance = this;
		configManager = new ConfigManager();
		settings = new Settings();
		storageManager = new StorageManager(this);
		cacheManager = new CacheManager(this);
		taskManager = new TaskManager(this);
		adbManager = new AdbManager(this);

		//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		UIManager.setLookAndFeel(new FlatDarculaLaf());
		SwingUtilities.invokeLater(() -> {
			mainForm = new MainForm();
			mainForm.setVisible(true);
		});

		//cacheManager = new CacheManager();
	}

	public void scoutFog() {
		if (runningTask != null) {
			runningTask.cancel(true);
			runningTask = null;
		} else {
			runningTask = getTaskExecutorService().schedule(() -> {

			}, 1, TimeUnit.SECONDS);
		}
	}

	public MainForm getMainForm() {
		return mainForm;
	}

	public void setRunningTask(ScheduledFuture<?> runningTask) {
		this.runningTask = runningTask;
	}

	public static Main getInstance() {
		return instance;
	}

	public static Logger getLogger() {
		return logger;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public StorageManager getStorageManager() {
		return storageManager;
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public ScheduledExecutorService getTaskExecutorService() {
		return taskExecutorService;
	}

	public boolean isTaskRunning() {
		return isTaskRunning;
	}

	public void setTaskRunning(boolean taskRunning) {
		isTaskRunning = taskRunning;
	}

	public Settings getSettings() {
		return settings;
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public AdbManager getAdbManager() {
		return adbManager;
	}
}
