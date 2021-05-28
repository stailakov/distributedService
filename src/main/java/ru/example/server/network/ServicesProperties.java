package ru.example.server.network;

import ru.example.utils.JsonUtil;
import ru.example.server.config.PropertiesLoader;

import java.util.List;

/**
 * @author TaylakovSA
 */
public class ServicesProperties {

    private List<Service> services;

    public ServicesProperties() {
        PropertiesLoader propertiesLoader = new PropertiesLoader();
        String json = propertiesLoader.getString("services");
        this.services = JsonUtil.toList(json, Service.class);

    }

    public List<Service> getServices() {
        return services;
    }

    public Service getByName(String name){
        return services.stream().
                filter(service -> service.getName().equals(name)).
                findFirst().
                orElseThrow(() -> new RuntimeException("Unsupported service name "+name));

    }
}
