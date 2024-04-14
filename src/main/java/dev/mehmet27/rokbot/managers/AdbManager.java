package dev.mehmet27.rokbot.managers;

import dev.mehmet27.rokbot.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vidstige.jadb.JadbConnection;

import java.io.IOException;

public class AdbManager {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final Main main;

	private JadbConnection jadbConnection;

	public AdbManager(Main main) {
		this.main = main;
		//restartAdbServer();
		try {
			jadbConnection = new JadbConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JadbConnection getJadbConnection() {
		return jadbConnection;
	}

	public void restartAdbServer() {
		logger.info("Starting adb server...");
		try {
			Process process = Runtime.getRuntime().exec("adb\\adb kill-server");
			process.waitFor();
			process.destroy();
			process = Runtime.getRuntime().exec("adb\\adb start-server");
			process.waitFor();
			process.destroy();
			logger.info("Adb server successfully started.");
		} catch (IOException | InterruptedException ex) {
			logger.info("An error occurred while starting the adb server.");
			ex.printStackTrace();
		}
	}
}
