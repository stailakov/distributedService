package ru.example.server.node;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author TaylakovSA
 */
public class Peer {

    final Integer id;

    private final AtomicBoolean voteGranted=new AtomicBoolean(false);

    public Peer(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public Boolean getVoteGranted() {
        return this.voteGranted.get();
    }

    public void setVoteGranted(Boolean voteGranted) {
        this.voteGranted.set(voteGranted);
    }
}
