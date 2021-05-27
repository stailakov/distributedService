package ru.example.server.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.example.server.config.PropertiesLoader;
import ru.example.server.exceptions.NotActiveException;

import java.util.concurrent.atomic.AtomicInteger;

import static ru.example.server.node.State.FOLLOWER;
import static ru.example.server.node.State.LEADER;

/**
 * @author TaylakovSA
 */
public class NodePropertiesImpl implements NodeProperties {

    final static Logger log = LoggerFactory.getLogger(NodePropertiesImpl.class);

    private Integer id;
    private volatile State state = LEADER;
    private final AtomicInteger commitIndex = new AtomicInteger(-1);
    private final AtomicInteger lastApplied = new AtomicInteger(-1);
    Boolean active = true;
    private volatile Integer votedFor = null;
    Integer electionTimeout;
    Integer heartBeatTimeout;

    public NodePropertiesImpl() {
        PropertiesLoader propertiesLoader = new PropertiesLoader();
        this.id = propertiesLoader.getInt("node.id");
        this.electionTimeout = propertiesLoader.getInt("node.election-timeout");
        this.heartBeatTimeout = propertiesLoader.getInt("node.heartbeat-timeout");
    }

    public Integer getId() {
        return id;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void setState(State state) {
        log.info("Peer #{} Set new state: {}", getId(), state);
        this.state = state;
    }

    @Override
    public Integer getCommitIndex() {
        return commitIndex.get();
    }

    @Override
    public void setCommitIndex(Integer commitIndex) {
        this.commitIndex.set(commitIndex);
        log.info("Peer #{} New commit index: {}", getId(), this.commitIndex.get());
    }

    @Override
    public Integer getLastApplied() {
        return lastApplied.get();
    }

    @Override
    public void incLastApplied() {
        log.info("Peer #{} New applied index: {}", getId(), this.lastApplied.incrementAndGet());
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
        log.info("Peer #{} {}", getId(),active?"STARTED":"STOPPED");
    }

    public Integer getVotedFor() {
        return votedFor;
    }

    @Override
    public void setVotedFor(Integer votedFor) {
        this.votedFor = votedFor;
        log.debug("Peer #{} set voted for {}", getId(),votedFor);
    }

    public Integer getElectionTimeout() {
        return electionTimeout;
    }

    public Integer getHeartBeatTimeout() {
        return heartBeatTimeout;
    }


    @Override
    public void cancelIfNotActive() {
        if (!getActive())
            throw  new NotActiveException();
    }
}
