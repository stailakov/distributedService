package ru.example.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

/**
 * @author TaylakovSA
 */
public class JsonUtil {

    public static <T> List<T> toList(String json, Class<T> t) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, mapper.getTypeFactory()
                    .constructCollectionType(List.class, t));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
