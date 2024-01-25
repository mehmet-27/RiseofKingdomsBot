package dev.mehmet27.rokbot;

import dev.mehmet27.rokbot.frames.MainFrame;
import dev.mehmet27.rokbot.managers.ConfigManager;
import dev.mehmet27.rokbot.tasks.AllianceHelpTask;
import dev.mehmet27.rokbot.tasks.CollectVillagesTask;
import dev.mehmet27.rokbot.tasks.ScoutFogTask;
import dev.mehmet27.rokbot.tasks.Task;
import org.opencv.core.Core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vidstige.jadb.JadbConnection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Main {

    private static Main instance;
    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(Main.class);
    private final ConfigManager configManager;

    private final Settings settings;

    private MainFrame mainFrame;

    private final ScheduledExecutorService taskExecutorService = Executors.newScheduledThreadPool(1);

    private ScheduledFuture<?> scoutFogTask = null;

    private JadbConnection jadbConnection;

    private List<Task> taskList = new ArrayList<>();

    private boolean isTaskRunning = false;

    public static void main(String[] args) {
        try {
            new Main();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Main() throws Exception {
        System.out.println("Loading Native OpenCv...");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Loaded successfully.");
        instance = this;
        configManager = new ConfigManager();
        settings = new Settings();

        mainFrame = new MainFrame(this);


        //cacheManager = new CacheManager();
        //consoleCommandManager = new ConsoleCommandManager();
        /*if (!isRokRunning()) {
            JDialog dialog = new JDialog(mainFrame, "Hata!");
            JLabel label = new JLabel("Lütfen oyunu başlatın.");
            dialog.add(label);
            dialog.setSize(50, 150);
            dialog.setVisible(true);
        }*/

        jadbConnection = new JadbConnection();
        jadbConnection.getDevices().forEach(System.out::println);
        taskList.add(new AllianceHelpTask());
        taskList.add(new ScoutFogTask());
        taskList.add(new CollectVillagesTask());

    }

    public void scoutFog() {
        if (scoutFogTask != null) {
            scoutFogTask.cancel(true);
            scoutFogTask = null;
        } else {
            scoutFogTask = getTaskExecutorService().schedule(() -> {

            }, 1, TimeUnit.SECONDS);
        }
    }

    public JadbConnection getAdb() {
        return jadbConnection;
    }

    public void setScoutFogTask(ScheduledFuture<?> scoutFogTask) {
        this.scoutFogTask = scoutFogTask;
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

    public boolean isRokRunning() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("tasklist.exe");
            Process process = processBuilder.start();
            Scanner scanner = new Scanner(process.getInputStream(), StandardCharsets.UTF_8).useDelimiter("\\A");
            String tasksList = scanner.hasNext() ? scanner.next() : "";
            scanner.close();
            return tasksList.contains("MASS");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ScheduledExecutorService getTaskExecutorService() {
        return taskExecutorService;
    }

    public List<Task> getTaskList() {
        return taskList;
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
}
