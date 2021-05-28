package ru.example.server.exceptions;


public class NotActiveException extends RuntimeException {

    public NotActiveException() {
        super("Node is not active");
    }
}
