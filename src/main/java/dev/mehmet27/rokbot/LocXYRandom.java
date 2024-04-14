package dev.mehmet27.rokbot;

public class LocXYRandom {

	private final int xMin;
	private final int xMax;
	private final int yMin;
	private final int yMax;

	public LocXYRandom(int xMin, int xMax, int yMin, int yMax) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
	}

	public int getxMin() {
		return xMin;
	}

	public int getxMax() {
		return xMax;
	}

	public int getyMin() {
		return yMin;
	}

	public int getyMax() {
		return yMax;
	}

	@Override
	public String toString() {
		return "LocXYRandom{" +
				"xMin=" + xMin +
				", xMax=" + xMax +
				", yMin=" + yMin +
				", yMax=" + yMax +
				'}';
	}
}