package dev.mehmet27.rokbot;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.W32APIOptions;

public interface User32 extends W32APIOptions {

    User32 INSTANCE = Native.load("user32", User32.class,
            DEFAULT_OPTIONS);


    boolean ShowWindow(Pointer hWnd, int nCmdShow);

    boolean SetForegroundWindow(Pointer hWnd);

    Pointer FindWindow(String winClass, String title);

    void mouse_event(int dwFlags, int dx, int dy, int dwData, Object o);

    void keybd_event(byte bVk, byte bScan, int dwFlags, int dwExtraInfo);

    boolean SetCursorPos(int x, int y);

    boolean SetWindowPos(Pointer hWnd, Pointer hWndInsertAfter, int X, int Y, int cx, int cy, int uFlags);

    int SW_SHOW = 1;
}

