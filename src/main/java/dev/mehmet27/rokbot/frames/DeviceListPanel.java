package dev.mehmet27.rokbot.frames;

import dev.mehmet27.rokbot.Main;
import dev.mehmet27.rokbot.configuration.file.FileConfiguration;
import dev.mehmet27.rokbot.tasks.Task;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class DeviceListPanel extends JPanel {

    private final FileConfiguration config = Main.getInstance().getConfigManager().getConfig();

    private final MainFrame mainFrame;

    public DeviceListPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setPreferredSize(new Dimension(800, 500));
        JButton button = new JButton("Auto Fog");
        button.setBounds(0, 0, 20, 5);
        button.addActionListener(event -> {
            Main.getInstance().setScoutFogTask(Main.getInstance().getExecutorService().scheduleAtFixedRate(() -> {
                for (Task task : Main.getInstance().getTaskList()) {
                    task.run();
                }
            }, 0, 10, TimeUnit.SECONDS));
        });
        add(button);
    }

    public void backToMap() {
        String guiName;

    }
}
