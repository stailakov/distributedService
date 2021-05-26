package ru.example.server.exceptions;

/**
 * Ошибка преобразования json
 *
 * @author TaylakovSA
 */
public class JsonException extends RuntimeException {

    public JsonException(String message) {
        super(message);
    }

}
