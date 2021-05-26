package ru.example.server.network;

import ru.example.server.JsonUtil;
import ru.example.server.config.PropertiesLoader;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TaylakovSA
 */
public class ServicesProperties {

    private List<Service> services;

    public ServicesProperties() {
        PropertiesLoader propertiesLoader = new PropertiesLoader();
        List<String> stringList = propertiesLoader.getList("services");
        this.services = stringList.stream()
                .map(e -> JsonUtil.toObject(e, Service.class))
                .collect(Collectors.toList());

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
