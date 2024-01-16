package dev.mehmet27.rokbot.managers;

import dev.mehmet27.rokbot.Main;
import dev.mehmet27.rokbot.configuration.InvalidConfigurationException;
import dev.mehmet27.rokbot.configuration.file.FileConfiguration;
import dev.mehmet27.rokbot.configuration.file.YamlConfiguration;
import dev.mehmet27.rokbot.utils.FileUtils;
import dev.mehmet27.rokbot.utils.Utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConfigManager {

    private FileConfiguration config;
    private final Map<Locale, FileConfiguration> locales = new HashMap<>();
    private Locale defaultLocale;
    private Path dataFolder;
    private File configFile;

    public ConfigManager() {
        setup();
    }

    public void setup() {
        try {
            dataFolder = Paths.get(Main.getInstance().getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        reloadConfig();
        defaultLocale = Utils.stringToLocale(getConfig().getString("language", "en_US"));
        Main.getLogger().info("Using language: " + defaultLocale.toString());
        copyFilesFromFolder("locales");
        copyFilesFromFolder("resources");
        for (Map.Entry<Locale, Object> entry : getLocales().entrySet()) {
            locales.put(entry.getKey(), (FileConfiguration) entry.getValue());
        }
    }

    public Map<Locale, Object> getLocales() {
        Map<Locale, Object> locales = new HashMap<>();
        for (File file : getLocaleFiles()) {
            Locale locale = Utils.stringToLocale(file.getName().split("\\.")[0]);
            FileConfiguration configuration = null;
            try {
                configuration = new YamlConfiguration();
                configuration.load(file);
            } catch (InvalidConfigurationException | IOException e) {
                Main.getLogger().error(String.format("An error occurred while loading file: %s", file.getName()), e);
            }
            locales.put(locale, configuration);
        }
        Main.getLogger().info("Found " + locales.size() + " language files.");
        return locales;
    }

    public List<Locale> getAvailableLocales() {
        List<Locale> locales = new ArrayList<>();
        for (Map.Entry<Locale, FileConfiguration> locale : this.locales.entrySet()) {
            locales.add(locale.getKey());
        }
        return locales;
    }

    public List<File> getLocaleFiles() {
        List<File> files = new ArrayList<>();
        File directoryPath = new File(getDataFolder() + File.separator + "locales");
        FilenameFilter ymlFilter = (dir, name) -> {
            String lowercaseName = name.toLowerCase();
            return lowercaseName.endsWith(".yml");
        };
        File[] filesList = directoryPath.listFiles(ymlFilter);
        Objects.requireNonNull(filesList, "Locales folder not found!");
        Collections.addAll(files, filesList);
        return files;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void copyFileFromResources(File file, Path target) {
        if (!target.toFile().getParentFile().exists()) {
            target.toFile().getParentFile().mkdirs();
        }
        if (!target.toFile().exists() && !target.toFile().isDirectory()) {
            try {
                InputStream inputStream = Main.getInstance().getClass().getClassLoader().getResourceAsStream(file.toString());
                if (inputStream == null) {
                    Main.getLogger().error(String.format("Error while trying to load file %s.", file.getName()));
                    throw new RuntimeException(String.format("Error while trying to load file %s.", file.getName()));
                }
                Files.copy(inputStream, target);
            } catch (Exception e) {
                Main.getLogger().error(String.format("Error while trying to load file %s.", file.getName()));
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void copyFilesFromFolder(String folder) {
        Predicate<? super Path> filter = entry -> {
            String path = entry.getFileName().toString();
            if (folder.equals("locales")) {
                return path.endsWith(".yml");
            }
            if (folder.equals("resources")) {
                return path.endsWith(".png");
            }
            return false;
        };
        FileUtils.getFilesIn(folder, filter).forEach(file -> {
            File destination = new File(getDataFolder() + File.separator + folder + File.separator + file.getName());
            if (!destination.getParentFile().exists()) {
                destination.getParentFile().mkdirs();
            }
            if (!destination.exists() && !destination.isDirectory()) {
                try {
                    String fileString = folder + "/" + file.getName();
                    InputStream inputStream = Main.getInstance().getClass().getClassLoader().getResourceAsStream(fileString);
                    if (inputStream == null) {
                        Main.getLogger().error(String.format("Error while trying to load file %s.", file.getName()));
                        throw new RuntimeException(String.format("Error while trying to load file %s.", file.getName()));
                    }
                    Files.copy(inputStream, destination.toPath());
                } catch (IOException e) {
                    Main.getLogger().error(String.format("Error while trying to load file %s.", file.getName()));
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void saveConfig() {
        try {
            getConfig().save(configFile);
            reloadConfig();
        } catch (IOException e) {
            Main.getLogger().error("An error occurred while saving file config.yml", e);
        }
    }

    public void reloadConfig() {
        configFile = new File(getDataFolder() + File.separator + "config.yml");
        copyFileFromResources(new File("config.yml"), configFile.toPath());
        try {
            config = new YamlConfiguration();
            config.load(configFile);

        } catch (IOException | InvalidConfigurationException e) {
            Main.getLogger().error("An error occurred while reloading file config.yml", e);
        }
    }

    public Path getDataFolder() {
        return dataFolder;
    }

    public String getMessage(String path) {
        if (locales.containsKey(defaultLocale)) {
            String msg = locales.get(defaultLocale).getString(path);
            if (msg != null && msg.length() != 0) {
                return msg.replace("\\n", "\n");
            }
        }
        Main.getLogger().warn("The searched value was not found in the language file and the default language file: " + path);
        return path;
    }

    public String getMessage(String path, Function<String, String> placeholders) {
        String message = getMessage(path);
        return placeholders.apply(message);
    }

    public String resolveResourcePath(String resource) {
        return dataFolder.resolve("resources").resolve(resource).toString();
    }

    public FileConfiguration getConfig() {
        return config;
    }
}