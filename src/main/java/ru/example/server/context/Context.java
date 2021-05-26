package ru.example.server.context;

import ru.example.server.node.State;
import ru.example.server.node.Peer;

import java.util.List;


/**
 * @author TaylakovSA
 */
public interface Context {

    Integer getId();

    State getState();

    void setState(State state);

    Integer getCommitIndex();

    void setCommitIndex(Integer commitIndex);

    Integer getLastApplied();

    Boolean getActive();

    void setActive(Boolean active);

    Integer getVotedFor();

    void setVotedFor(Integer votedFor);

    Integer getElectionTimeout();

    Integer getHeartBeatTimeout();

    void cancelIfNotActive();

    Integer getQuorum();

    Long getCurrentTerm();

    List<Peer> getPeers();
    Peer getPeer(Integer id);

    Long incCurrentTerm();

    void setCurrentTerm(Long term);

    void setTermGreaterThenCurrent(Long term);
}
