package ru.example.server.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.example.server.node.NodeProperties;
import ru.example.server.node.NodePropertiesImpl;
import ru.example.server.node.Peer;
import ru.example.server.node.Peers;
import ru.example.server.node.State;
import ru.example.server.node.Term;

import java.util.List;

import static ru.example.server.node.State.FOLLOWER;

/**
 * @author TaylakovSA
 */
public class ContextImpl implements Context {

    Logger log = LoggerFactory.getLogger(ContextImpl.class);

    private Peers peers;
    private Term term;
    private NodeProperties nodeProperties;

    public ContextImpl() {
        this.peers = new Peers();
        this.nodeProperties = new NodePropertiesImpl();
        this.term = new Term(nodeProperties);
    }

    @Override
    public Integer getId() {
        return nodeProperties.getId();
    }

    @Override
    public void setActive(Boolean active) {
        nodeProperties.setActive(active);
    }

    @Override
    public Boolean getActive() {
        return nodeProperties.getActive();
    }

    @Override
    public void cancelIfNotActive() {
        nodeProperties.cancelIfNotActive();
    }

    @Override
    public State getState() {
        return nodeProperties.getState();
    }

    @Override
    public void setState(State state) {
        nodeProperties.setState(state);
    }

    @Override
    public Long getCurrentTerm() {
        return term.getCurrentTerm();
    }

    @Override
    public Integer getCommitIndex() {
        return nodeProperties.getCommitIndex();
    }

    @Override
    public void setCommitIndex(Integer commitIndex) {
        nodeProperties.setCommitIndex(commitIndex);
    }

    @Override
    public Integer getLastApplied() {
        return nodeProperties.getLastApplied();
    }

    @Override
    public Integer getElectionTimeout() {
        return nodeProperties.getElectionTimeout();
    }

    @Override
    public Integer getHeartBeatTimeout() {
        return nodeProperties.getHeartBeatTimeout();
    }

    @Override
    public List<Peer> getPeers() {
        return peers.getPeers();
    }

    @Override
    public Peer getPeer(Integer id)  {
        return  peers.get(id);
    }

    @Override
    public Long incCurrentTerm() {
        return term.incCurrentTerm();
    }

    @Override
    public void setCurrentTerm(Long term) {
        this.term.setCurrentTerm(term);
    }

    @Override
    public Integer getQuorum() {
        return peers.getQuorum();
    }

    @Override
    public Integer getVotedFor() {
        return nodeProperties.getVotedFor();
    }

    @Override
    public void setVotedFor(Integer votedFor) {
        nodeProperties.setVotedFor(votedFor);
    }

    @Override
    public void setTermGreaterThenCurrent(Long term) {
        log.info("Peer #{} Get term {} greater then current. The current term is {}", getId(), term, getCurrentTerm());
        setState(FOLLOWER);
        setCurrentTerm(term);
        setVotedFor(null);
    }

}
