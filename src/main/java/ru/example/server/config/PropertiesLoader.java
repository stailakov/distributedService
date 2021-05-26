package ru.example.server.config;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.util.List;

/**
 * @author TaylakovSA
 */
public class PropertiesLoader {

    private final static String COMMON_PROPERTIES_FILENAME = "application.properties";

    PropertiesConfiguration config;

    public PropertiesLoader() {
        try {

        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                new FileBasedConfigurationBuilder<PropertiesConfiguration>(
                        PropertiesConfiguration.class).configure(params.fileBased()
                        .setListDelimiterHandler(new DefaultListDelimiterHandler(','))
                        .setFile(new File(COMMON_PROPERTIES_FILENAME)));
        config = builder.getConfiguration();

        } catch (ConfigurationException e) {

        }

    }

    public String getString(String key) {
        return config.getString(key);
    }

    public Integer getInt(String key) {
        return config.getInt(key);
    }

    public List<String> getList(String key) {
        return config.getList(String.class, key);
    }
}
