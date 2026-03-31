package com.aegis.membersync.config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private static Properties props = new Properties();

    static {
        try (InputStream input = ConfigLoader.class
                .getClassLoader()
                .getResourceAsStream("memberServiceConfiguration.properties")) {

            props.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static String getEnv() {
        return props.getProperty("env");
    }

    public static String getEnvProperty(String key) {
        return props.getProperty(getEnv() + "." + key);
    }
}