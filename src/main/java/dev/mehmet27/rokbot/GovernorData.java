package dev.mehmet27.rokbot;

public class GovernorData {

	private String id;
	private String name;
	private long power;
	private long killPoints;
	private long deaths;
	private long t1;
	private long t2;
	private long t3;
	private long t4;
	private long t5;
	private long kills;
	private long killsT4T5;
	private long rangedPoints;
	private long rssGathered;
	private long rssAssistance;
	private long helps;
	private String alliance;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPower() {
		return power;
	}

	public void setPower(long power) {
		this.power = power;
	}

	public long getKillPoints() {
		return killPoints;
	}

	public void setKillPoints(long killPoints) {
		this.killPoints = killPoints;
	}

	public long getDeaths() {
		return deaths;
	}

	public void setDeaths(long deaths) {
		this.deaths = deaths;
	}

	public long getT1() {
		return t1;
	}

	public void setT1(long t1) {
		this.t1 = t1;
	}

	public long getT2() {
		return t2;
	}

	public void setT2(long t2) {
		this.t2 = t2;
	}

	public long getT3() {
		return t3;
	}

	public void setT3(long t3) {
		this.t3 = t3;
	}

	public long getT4() {
		return t4;
	}

	public void setT4(long t4) {
		this.t4 = t4;
	}

	public long getT5() {
		return t5;
	}

	public void setT5(long t5) {
		this.t5 = t5;
	}

	public long getKills() {
		return kills;
	}

	public void setKills(long kills) {
		this.kills = kills;
	}

	public long getKillsT4T5() {
		return killsT4T5;
	}

	public void setKillsT4T5(long killsT4T5) {
		this.killsT4T5 = killsT4T5;
	}

	public long getRangedPoints() {
		return rangedPoints;
	}

	public void setRangedPoints(long rangedPoints) {
		this.rangedPoints = rangedPoints;
	}

	public long getRssGathered() {
		return rssGathered;
	}

	public void setRssGathered(long rssGathered) {
		this.rssGathered = rssGathered;
	}

	public long getRssAssistance() {
		return rssAssistance;
	}

	public void setRssAssistance(long rssAssistance) {
		this.rssAssistance = rssAssistance;
	}

	public long getHelps() {
		return helps;
	}

	public void setHelps(long helps) {
		this.helps = helps;
	}

	public String getAlliance() {
		return alliance;
	}

	public void setAlliance(String alliance) {
		this.alliance = alliance;
	}

	@Override
	public String toString() {
		return "GovernorData{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", power=" + power +
				", killPoints=" + killPoints +
				", deaths=" + deaths +
				", t1=" + t1 +
				", t2=" + t2 +
				", t3=" + t3 +
				", t4=" + t4 +
				", t5=" + t5 +
				", kills=" + kills +
				", killsT4T5=" + killsT4T5 +
				", rangedPoints=" + rangedPoints +
				", rssGathered=" + rssGathered +
				", rssAssistance=" + rssAssistance +
				", helps=" + helps +
				", alliance='" + alliance + '\'' +
				'}';
	}
}
