package ru.example.server.node;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author TaylakovSA
 */
public class Peer {

    final Integer id;

    private final AtomicInteger nextIndex= new AtomicInteger(0);
    private final AtomicInteger matchIndex= new AtomicInteger(-1);
    private final AtomicBoolean voteGranted=new AtomicBoolean(false);

    public Peer(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public Integer getNextIndex() {
        return this.nextIndex.get();
    }

    public void setNextIndex(Integer nextIndex) {
        this.nextIndex.set(nextIndex);
    }

    public void decNextIndex() {
        this.nextIndex.decrementAndGet();
    }

    public Integer getMatchIndex() {
        return this.matchIndex.get();
    }

    public Boolean getVoteGranted() {
        return this.voteGranted.get();
    }

    public void setMatchIndex(Integer matchIndex) {
        this.matchIndex.set(matchIndex);
    }

    public void setVoteGranted(Boolean voteGranted) {
        this.voteGranted.set(voteGranted);
    }
}
