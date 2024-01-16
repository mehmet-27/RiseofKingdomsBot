package dev.mehmet27.rokbot.utils;

import java.util.Locale;

public class Utils {
    public static Locale stringToLocale(String loc) {
        String[] localeStr = loc.split("_");
        return new Locale(localeStr[0], localeStr[1]);
    }
}
