package dev.mehmet27.rokbot.managers;

import dev.mehmet27.rokbot.Main;
import org.opencv.core.Mat;

public class CacheManager {

	private final Main main;

	private Mat lastScreenShot;

	public CacheManager(Main main) {
		this.main = main;
	}

	public Mat getLastScreenShot() {
		return lastScreenShot;
	}

	public void setLastScreenShot(Mat lastScreenShot) {
		this.lastScreenShot = lastScreenShot;
	}
}
