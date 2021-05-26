package ru.example.server.node;

/**
 * @author TaylakovSA
 */
public interface NodeProperties {

    Integer getId();

    State getState();

    void setState(State state);

    Integer getCommitIndex();

    void setCommitIndex(Integer commitIndex);

    Integer getLastApplied();

    void incLastApplied();

    Boolean getActive();

    void setActive(Boolean active);

    void setVotedFor(Integer votedFor);
    Integer getVotedFor();

    Integer getElectionTimeout();

    Integer getHeartBeatTimeout();

    void cancelIfNotActive();
}
