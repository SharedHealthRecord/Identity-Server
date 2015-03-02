package org.freeshr.identity.repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
    protected Properties loadProperties(String propFileName) {
        Properties properties = new Properties();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        try {
            if (inputStream != null)
                properties.load(inputStream);
        } catch (IOException e) {
            System.out.println("property file '" + propFileName + "' not found in the classpath");
        }
        return properties;
    }
}
