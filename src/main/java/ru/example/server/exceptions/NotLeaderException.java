package ru.example.server.exceptions;

public class NotLeaderException extends RuntimeException {

    public NotLeaderException() {
        super("Node is not a leader");
    }
}
