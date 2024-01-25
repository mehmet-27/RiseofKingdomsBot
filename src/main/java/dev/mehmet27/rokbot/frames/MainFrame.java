package dev.mehmet27.rokbot.frames;

import dev.mehmet27.rokbot.Main;
import dev.mehmet27.rokbot.configuration.file.FileConfiguration;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final Main main;
    private final FileConfiguration configuration;

    public MainFrame(Main main) {
        this.main = main;
        configuration = main.getConfigManager().getConfig();
        setTitle("Rise of Kingdoms Bot v1.0.0");
        setSize(configuration.getInt("screenSize.width", 470), configuration.getInt("screenSize.height", 850));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(true);

        JTabbedPane tabbedPane;

        tabbedPane = new JTabbedPane();

        JPanel displayDevicePanel = new DisplayDevicePanel();
        JPanel settingsPanel = new SettingsPanel(this);
        JPanel deviceListPanel = new DeviceListPanel(this);
        tabbedPane.addTab("Device List", deviceListPanel);
        tabbedPane.addTab("Display Device", displayDevicePanel);
        tabbedPane.addTab("Settings", settingsPanel);

        add(tabbedPane);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(800, 100));

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public Main getMain() {
        return main;
    }
}
