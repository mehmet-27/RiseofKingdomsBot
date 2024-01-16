package dev.mehmet27.rokbot.utils;

import dev.mehmet27.rokbot.LocXY;
import dev.mehmet27.rokbot.User32;

import java.awt.*;

public class KeyUtils {

    public static void PressKey(int key) {
        try {
            Robot robot = new Robot();
            robot.keyPress(key);
            robot.keyRelease(key);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void mouseClick(LocXY locXY) {
        mouseClick(locXY.getX(), locXY.getY());
    }


    public static void mouseClick(int x, int y) {
        User32 user32 = User32.INSTANCE;
        // İstenilen koordinatlarda sol fare tuşuna basıp bırakmak için
        final int MOUSEEVENTF_LEFTDOWN = 0x0002;
        final int MOUSEEVENTF_LEFTUP = 0x0004;
        user32.SetCursorPos(x, y);
        user32.mouse_event(MOUSEEVENTF_LEFTDOWN, x, y, 0, null);
        user32.mouse_event(MOUSEEVENTF_LEFTUP, x, y, 0, null);
    }

    public static void keyClick(byte key) {
        User32 user32 = User32.INSTANCE;
        // İstenilen koordinatlarda sol fare tuşuna basıp bırakmak için
        final int KEYEVENTF_KEYDOWN = 0x0001;
        final int KEYEVENTF_KEYUP = 0x0002;
        user32.keybd_event(key, (byte) 0, 0, 0);
        user32.keybd_event(key, (byte) 0, KEYEVENTF_KEYUP, 0);
    }
}
