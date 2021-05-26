package ru.example.server.election;

import ru.example.server.timer.ServerTimer;

import static ru.example.server.node.State.LEADER;

/**
 * @author TaylakovSA
 */
public class ElectionTimer extends ServerTimer {

    private final ElectionService electionService;

    public ElectionTimer() {
        this.electionService = new ElectionService();
    }

    @Override
    protected Integer getTimeout() {
        return nodeProperties.getElectionTimeout();
    }

    @Override
    protected String getActionName() {
        return "vote";
    }


    @Override
    protected Runnable getAction() {
        return electionService::processElection;
    }

    @Override
    protected boolean isRun() {
        return nodeProperties.getActive() && !nodeProperties.getState().equals(LEADER);
    }

}
