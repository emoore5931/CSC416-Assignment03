package image.spatial.filtering.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final String CONFIG_PATH = "config.properties";

    private static Properties properties = new Properties();

    static {
        System.out.println("Reading config...");
        try (FileInputStream fileIn = new FileInputStream(CONFIG_PATH)) {
            properties.load(fileIn);
        } catch (IOException e) {
            System.err.println("Unable to read config file: \n" + e.getMessage() + "\n Exiting...");
            System.exit(1);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key, null);
    }
}
