package dev.mehmet27.rokbot.tasks;

import dev.mehmet27.rokbot.GovernorData;
import dev.mehmet27.rokbot.LocXYRandom;
import dev.mehmet27.rokbot.Main;
import dev.mehmet27.rokbot.utils.AdbUtils;
import dev.mehmet27.rokbot.utils.StringUtils;
import dev.mehmet27.rokbot.utils.TesseractUtils;
import dev.mehmet27.rokbot.utils.WindowsUtils;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.opencv.core.Mat;
import se.vidstige.jadb.JadbDevice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KingdomScannerTask extends Task {

	private List<LocXYRandom> posList = new ArrayList<>();

	public KingdomScannerTask(JadbDevice device) {
		super(device);
		posList.add(new LocXYRandom(250, 1000, 200, 252));
		posList.add(new LocXYRandom(250, 1000, 280, 335));
		posList.add(new LocXYRandom(250, 1000, 360, 415));
		posList.add(new LocXYRandom(250, 1000, 440, 500));
		posList.add(new LocXYRandom(250, 1000, 460, 515));
	}

	@Override
	public void run(JadbDevice device) {
		WindowsUtils.getClipboard();
		long start = System.currentTimeMillis();
		int kingdom = 3402;
		int searchRange = 10;
		long nameCopyWait = 300;
		Path scansPath = Main.getInstance().getConfigManager().getDataFolder().resolve("scans");
		scansPath.toFile().mkdirs();

		List<GovernorData> dataList = new ArrayList<>();
		int pos = 0;
		for (int i = 0; i < searchRange; i++) {
			if (pos == 3) {
				pos = 4;
			}
			AdbUtils.tap(posList.get(pos));
			if (pos < 4) pos += 1;
			sleepUntil(900, 950);
			Mat governorMat = AdbUtils.getScreenShot(device);
			GovernorData data = new GovernorData();
			String governorId = TesseractUtils.imageToString(governorMat.submat(155, 180, 590, 675));
			data.setId(governorId);
			AdbUtils.tap(AdbUtils.LOC.COPY_NAME);
			sleepUntil(nameCopyWait);
			String governorName = WindowsUtils.getClipboard();
			data.setName(governorName);
			String alliance = TesseractUtils.imageToString(governorMat.submat(263, 293, 478, 700));
			data.setAlliance(alliance);
			long power = parseLong(StringUtils.removeCommas(TesseractUtils.imageToString(governorMat.submat(261, 293, 707, 866))));
			data.setPower(power);
			long killPoints = parseLong(StringUtils.removeCommas(TesseractUtils.imageToString(governorMat.submat(263, 293, 907, 1077))));
			data.setKillPoints(killPoints);
			// Kill info
			AdbUtils.tap(AdbUtils.LOC.KILL_INFO);
			sleepUntil(1000);
			Mat killsMat = AdbUtils.getScreenShot(device);
			long t1 = parseLong(StringUtils.removeCommas(TesseractUtils.imageToString(killsMat.submat(344, 364, 689, 786))));
			data.setT1(t1);
			long t2 = parseLong(StringUtils.removeCommas(TesseractUtils.imageToString(killsMat.submat(378, 400, 689, 786))));
			data.setT2(t2);
			long t3 = parseLong(StringUtils.removeCommas(TesseractUtils.imageToString(killsMat.submat(412, 437, 689, 786))));
			data.setT3(t3);
			long t4 = parseLong(StringUtils.removeCommas(TesseractUtils.imageToString(killsMat.submat(448, 472, 689, 786))));
			data.setT4(t4);
			long t5 = parseLong(StringUtils.removeCommas(TesseractUtils.imageToString(killsMat.submat(483, 506, 689, 786))));
			data.setT5(t5);
			long kills = t1 + t2 + t3 + t4 + t5;
			data.setKills(kills);
			long killsT4T5 = t4 + t5;
			data.setKillsT4T5(killsT4T5);
			long rangedPoints = parseLong(StringUtils.removeCommas(TesseractUtils.imageToString(killsMat.submat(566, 590, 941, 1132))));
			data.setRangedPoints(rangedPoints);
			// More info
			AdbUtils.tap(AdbUtils.LOC.MORE_INFO);
			sleepUntil(1000);
			Mat moreInfoMat = AdbUtils.getScreenShot(device);
			long dead = parseLong(StringUtils.removeCommas(TesseractUtils.imageToString(moreInfoMat.submat(358, 385, 895, 1043))));
			data.setDeaths(dead);
			long rssGathered = parseLong(StringUtils.removeCommas(TesseractUtils.imageToString(moreInfoMat.submat(494, 518, 895, 1043))));
			data.setRssGathered(rssGathered);
			long rssAssistance = parseLong(StringUtils.removeCommas(TesseractUtils.imageToString(moreInfoMat.submat(543, 571, 895, 1043))));
			data.setRssAssistance(rssAssistance);
			long helps = parseLong(StringUtils.removeCommas(TesseractUtils.imageToString(moreInfoMat.submat(592, 617, 895, 1043))));
			data.setHelps(helps);
			dataList.add(data);
			AdbUtils.tap(AdbUtils.LOC.CLOSE_MORE_INFO);
			sleepUntil(800, 850);
			AdbUtils.tap(AdbUtils.LOC.CLOSE_PROFILE);
			sleepUntil(800, 850);
		}

		try (Workbook wb = new Workbook(Files.newOutputStream(scansPath.resolve("test.xlsx"),
				StandardOpenOption.CREATE), "MyApp", "04.2024")) {
			Worksheet ws = wb.newWorksheet("Kingdom Scan");
			ws.value(0, 0, "Governor ID");
			ws.value(0, 1, "Governor Name");
			ws.value(0, 2, "Power");
			ws.value(0, 3, "Kill Points");
			ws.value(0, 4, "Deaths");
			ws.value(0, 5, "T1");
			ws.value(0, 6, "T2");
			ws.value(0, 7, "T3");
			ws.value(0, 8, "T4");
			ws.value(0, 9, "T5");
			ws.value(0, 10, "Kills");
			ws.value(0, 11, "Kills (T4+)");
			ws.value(0, 12, "Ranged Points");
			ws.value(0, 13, "RSS Gathered");
			ws.value(0, 14, "RSS Assistance");
			ws.value(0, 15, "Helps");
			ws.value(0, 16, "Alliance");

			for (int i = 0; i < dataList.size(); i++) {
				GovernorData data = dataList.get(i);
				ws.value(i + 1, 0, data.getId());
				ws.value(i + 1, 1, data.getName());
				ws.value(i + 1, 2, data.getPower());
				ws.value(i + 1, 3, data.getKillPoints());
				ws.value(i + 1, 4, data.getDeaths());
				ws.value(i + 1, 5, data.getT1());
				ws.value(i + 1, 6, data.getT2());
				ws.value(i + 1, 7, data.getT3());
				ws.value(i + 1, 8, data.getT4());
				ws.value(i + 1, 9, data.getT5());
				ws.value(i + 1, 10, data.getKills());
				ws.value(i + 1, 11, data.getKillsT4T5());
				ws.value(i + 1, 12, data.getRangedPoints());
				ws.value(i + 1, 13, data.getRssGathered());
				ws.value(i + 1, 14, data.getRssAssistance());
				ws.value(i + 1, 15, data.getHelps());
				ws.value(i + 1, 16, data.getAlliance());
			}
			wb.finish();
			long end = System.currentTimeMillis() - start;
			System.out.println(end + " ms");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private long parseLong(String text) {
		try {
			return Long.parseLong(text);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return -1;
		}
	}
}
