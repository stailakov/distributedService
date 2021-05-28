package ru.example.server.config;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.util.List;

/**
 * @author TaylakovSA
 */
public class PropertiesLoader {

    private final static String COMMON_PROPERTIES_FILENAME = "application.properties";

    private static PropertiesLoader instance;

    PropertiesConfiguration config;

    public static synchronized PropertiesLoader getInstance() {
        if (instance == null) {
            instance = new PropertiesLoader();
        }
        return instance;
    }

    private PropertiesLoader() {
        try {

        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                new FileBasedConfigurationBuilder<PropertiesConfiguration>(
                        PropertiesConfiguration.class).configure(params.fileBased()
                        .setFile(new File(COMMON_PROPERTIES_FILENAME)));
        config = builder.getConfiguration();

        } catch (ConfigurationException e) {

        }

    }

    public String getString(String key) {
        return config.getString(key);
    }

    public void setProperty(String key, Object value) {
        config.setProperty(key, value);
    }

    public Integer getInt(String key) {
        return config.getInt(key);
    }

    public List<String> getList(String key) {
        return config.getList(String.class, key);
    }
}
