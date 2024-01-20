package dev.mehmet27.rokbot.frames;

import dev.mehmet27.rokbot.IMAGEPATHS;
import dev.mehmet27.rokbot.LocXY;
import dev.mehmet27.rokbot.Main;
import dev.mehmet27.rokbot.configuration.file.FileConfiguration;
import dev.mehmet27.rokbot.utils.AdbUtils;
import dev.mehmet27.rokbot.utils.GuiUtils;

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
            if (config.getBoolean("scout.investigation", true)) {
                Main.getInstance().setScoutFogTask(Main.getInstance().getExecutorService().scheduleAtFixedRate(() -> {
                    // Check is scouter available
                    if (!GuiUtils.checkIsHome()) {
                        System.out.println("not on home");
                        if (GuiUtils.checkIsMap()) {
                            System.out.println("on map");
                            LocXY loc = GuiUtils.getLocXY(IMAGEPATHS.HOME_BUTTON);
                            System.out.println(loc);
                            AdbUtils.tap(loc);
                            System.out.println("back to home");
                        }
                    } else {
                        if (!GuiUtils.checkIsPath(IMAGEPATHS.SCOUTER)) {
                            System.out.println("scout x: " + config.getInt("scout.scoutCampPos.x", 0));
                            System.out.println("scout y: " + config.getInt("scout.scoutCampPos.y", 0));
                            int x = config.getInt("scout.scoutCampPos.x", 0);
                            int y = config.getInt("scout.scoutCampPos.y", 0);
                            AdbUtils.tap(new LocXY(x, y));
                            System.out.println("clicked scout camp");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (GuiUtils.checkIsPath(IMAGEPATHS.SCOUT_BUTTON)) {
                                AdbUtils.tap(GuiUtils.getLocXY(IMAGEPATHS.SCOUT_BUTTON));
                                System.out.println("scout camp opened");
                            } else {

                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (GuiUtils.checkIsPath(IMAGEPATHS.EXPLORE_BUTTON)) {
                                // Scouter is available
                                AdbUtils.tap(GuiUtils.getLocXY(IMAGEPATHS.EXPLORE_BUTTON));
                                System.out.println("clicked scout button");
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (GuiUtils.checkIsPath(IMAGEPATHS.EXPLORE_BUTTON_2)) {
                                    // Haritada sise tıkla
                                    AdbUtils.tap(GuiUtils.getLocXY(IMAGEPATHS.EXPLORE_BUTTON_2));
                                    System.out.println("clicked fog in map");
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    if (GuiUtils.checkIsPath(IMAGEPATHS.SCOUT_SEND_BUTTON)) {
                                        System.out.println("scout button cord: " + GuiUtils.getLocXY(IMAGEPATHS.SCOUT_SEND_BUTTON));
                                        AdbUtils.tap(GuiUtils.getLocXY(IMAGEPATHS.SCOUT_SEND_BUTTON));
                                        System.out.println("Scouter was send successfully.");
                                    }
                                }
                            } else {
                                // All scouters is busy
                            }
                        }
                    }
                    if (GuiUtils.checkIsPath(IMAGEPATHS.EXPLORE_BUTTON)) {
                        // Scouter is available
                        AdbUtils.tap(GuiUtils.getLocXY(IMAGEPATHS.EXPLORE_BUTTON));
                        System.out.println("clicked scout button");
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (GuiUtils.checkIsPath(IMAGEPATHS.EXPLORE_BUTTON_2)) {
                            // Haritada sise tıkla
                            AdbUtils.tap(GuiUtils.getLocXY(IMAGEPATHS.EXPLORE_BUTTON_2));
                            System.out.println("clicked fog in map");
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (GuiUtils.checkIsPath(IMAGEPATHS.SCOUT_SEND_BUTTON)) {
                                System.out.println("scout button cord: " + GuiUtils.getLocXY(IMAGEPATHS.SCOUT_SEND_BUTTON));
                                AdbUtils.tap(GuiUtils.getLocXY(IMAGEPATHS.SCOUT_SEND_BUTTON));
                                System.out.println("scouter was send successfully.");
                            }
                        }
                    } else {
                        // All scouters is busy

                    }
                }, 0, 1, TimeUnit.SECONDS));
            }
        });
        add(button);
    }

    public void backToMap() {
        String guiName;

    }
}
