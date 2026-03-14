package com.old_animation;

import net.fabricmc.loader.api.FabricLoader;
import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

public class AnimationConfig {
    public static boolean autoMode = false;
    public static boolean swordBlock = true;
    public static boolean useSwing = true;
    public static boolean isChinese = false;
    public static double range = 3.0;

    public enum AnimMode { MODE_1_7, MODE_PUSH, MODE_1_7_PLUS }

    public static AnimMode animationMode = AnimMode.MODE_1_7;
    public static float offsetX = 0.0f;
    public static float offsetY = 0.0f;
    public static float offsetZ = 0.0f;

    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("old_animation.properties");

    public static void load() {
        if (!CONFIG_FILE.toFile().exists()) {
            save();
            return;
        }
        try (InputStream is = new FileInputStream(CONFIG_FILE.toFile())) {
            Properties prop = new Properties();
            prop.load(is);
            autoMode = Boolean.parseBoolean(prop.getProperty("autoMode", "false"));
            swordBlock = Boolean.parseBoolean(prop.getProperty("swordBlock", "true"));
            useSwing = Boolean.parseBoolean(prop.getProperty("useSwing", "true"));
            isChinese = Boolean.parseBoolean(prop.getProperty("isChinese", "false"));
            range = Double.parseDouble(prop.getProperty("range", "3.0"));
            offsetX = Float.parseFloat(prop.getProperty("offsetX", "0.0"));
            offsetY = Float.parseFloat(prop.getProperty("offsetY", "0.0"));
            offsetZ = Float.parseFloat(prop.getProperty("offsetZ", "0.0"));
            try {
                animationMode = AnimMode.valueOf(prop.getProperty("animationMode", "MODE_1_7"));
            } catch (IllegalArgumentException e) {
                animationMode = AnimMode.MODE_1_7;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try (OutputStream os = new FileOutputStream(CONFIG_FILE.toFile())) {
            Properties prop = new Properties();
            prop.setProperty("autoMode", String.valueOf(autoMode));
            prop.setProperty("swordBlock", String.valueOf(swordBlock));
            prop.setProperty("useSwing", String.valueOf(useSwing));
            prop.setProperty("isChinese", String.valueOf(isChinese));
            prop.setProperty("range", String.valueOf(range));
            prop.setProperty("animationMode", animationMode.name());
            prop.setProperty("offsetX", String.valueOf(offsetX));
            prop.setProperty("offsetY", String.valueOf(offsetY));
            prop.setProperty("offsetZ", String.valueOf(offsetZ));
            prop.store(os, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}