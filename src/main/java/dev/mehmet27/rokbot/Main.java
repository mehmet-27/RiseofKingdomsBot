package dev.mehmet27.rokbot;

import dev.mehmet27.rokbot.frames.MainFrame;
import dev.mehmet27.rokbot.managers.ConfigManager;
import org.opencv.core.Core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vidstige.jadb.JadbConnection;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Main {

    private static Main instance;
    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(Main.class);
    private final ConfigManager configManager;

    private MainFrame mainFrame;

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private ScheduledFuture<?> scoutFogTask = null;

    private JadbConnection jadbConnection;

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
    }

    public void scoutFog() {
        if (scoutFogTask != null) {
            scoutFogTask.cancel(true);
            scoutFogTask = null;
        } else {
            scoutFogTask = getExecutorService().schedule(() -> {

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

    public ScheduledExecutorService getExecutorService() {
        return executorService;
    }
}
