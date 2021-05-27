package ru.example.server;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.example.server.exceptions.JsonException;

import java.io.IOException;

/**
 * @author TaylakovSA
 */
public class JsonUtil<T> {

    public static <T> T toObject(String json, Class<T> t) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return mapper.readValue(json, t);
        } catch (JsonProcessingException e) {
            throw new JsonException(e.getMessage());
        }
    }

    public static <T> T toObject(byte [] json, Class<T> t) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return mapper.readValue(json, t);
        } catch (IOException e) {
            throw new JsonException(e.getMessage());
        }
    }

    public static <T> String toJsonString(T t) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return mapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new JsonException(e.getMessage());
        }
    }

    public static <T> byte[] toByteArray(T t) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return mapper.writeValueAsBytes(t);
        } catch (JsonProcessingException e) {
            throw new JsonException(e.getMessage());
        }
    }

}
