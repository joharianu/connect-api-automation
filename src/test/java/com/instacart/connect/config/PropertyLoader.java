package com.instacart.connect.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;
import java.util.logging.Logger;

public class PropertyLoader {
    private static PropertyLoader propertyLoader;
    private final Properties properties;

    public static PropertyLoader getInstance() {
        if (propertyLoader == null) {
            propertyLoader = new PropertyLoader();
        }
        return propertyLoader;
    }

    private PropertyLoader() {

        //load common properties
        Logger logger = Logger.getLogger(PropertyLoader.class.getName());
        try {
            logger.info("Loading properties from file");
            BufferedReader reader =
                    new BufferedReader(new FileReader("src/test/resources/test.properties"));
            properties = new Properties();
            properties.load(reader);
            reader.close();
            logger.info("properties loaded");
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        //load auth properties
        try {
            String client = properties.getProperty("client");
            logger.info("Loading client properties - " + client);
            BufferedReader reader =
                    new BufferedReader(new FileReader("src/test/resources/" + client + ".properties"));
            properties.load(reader);
            reader.close();
            logger.info("client properties loaded ");
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getTokenApiUri() {
        return properties.getProperty("token_api_uri");
    }

    public String getBaseUrl() {
        return properties.getProperty("base_url");
    }
}
