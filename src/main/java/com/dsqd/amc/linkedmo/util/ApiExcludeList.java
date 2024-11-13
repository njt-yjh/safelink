package com.dsqd.amc.linkedmo.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

public class ApiExcludeList {
    private static final List<Pattern> excludePaths = new ArrayList<>();
    private static final List<Pattern> excludeExtensions = new ArrayList<>();
    
    static {
        loadProperties();
    }

    private static void loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = ApiExcludeList.class.getClassLoader().getResourceAsStream("jwtsecurity.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find jwtsecurity.properties");
                return;
            }
            properties.load(input);

            properties.stringPropertyNames().forEach(key -> {
                if (key.startsWith("exclude.path")) {
                    String paths = properties.getProperty(key);
                    for (String path : paths.split(";")) {
                        addPath(path);
                    }
                } else if (key.startsWith("exclude.extension")) {
                	String exts = properties.getProperty(key);
                    for (String ext : exts.split(";")) {
                        addPath(ext);
                    }
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void addPath(String path) {
        String regex = path.replace("*", ".*");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        excludePaths.add(pattern);
    }

    public static void addExtension(String extension) {
        String regex = ".*\\." + extension;
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        excludeExtensions.add(pattern);
    }

    public static boolean isExcluded(String path) {
        for (Pattern pattern : excludePaths) {
            if (pattern.matcher(path).find()) {
                return true;
            }
        }
        for (Pattern pattern : excludeExtensions) {
            if (pattern.matcher(path).matches()) {
                return true;
            }
        }
        return false;
    }
}
