package dev.mehmet27.rokbot.frames;

import dev.mehmet27.rokbot.Main;
import dev.mehmet27.rokbot.configuration.file.FileConfiguration;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class SettingsPanel extends JPanel {

    private final Main main = Main.getInstance();

    private final FileConfiguration config = Main.getInstance().getConfigManager().getConfig();

    private final MainFrame mainFrame;

    public SettingsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        JCheckBox checkBox = new JCheckBox("Auto Start Game");
        checkBox.addItemListener(e -> mainFrame.getMain().getSettings().setAutoStartGame(e.getStateChange() == ItemEvent.SELECTED));
        add(checkBox);
    }
}
