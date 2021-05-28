package ru.example.server.election;

import ru.example.server.timer.ServerTimer;

import static ru.example.server.node.State.LEADER;

/**
 * @author TaylakovSA
 */
public final class ElectionTimer extends ServerTimer {

    private final ElectionService electionService;

    private static ElectionTimer instance;

    public static synchronized ElectionTimer getInstance() {
        if (instance == null) {
            instance = new ElectionTimer(new ElectionService());
        }
        return instance;
    }

    private ElectionTimer(ElectionService electionService) {
        this.electionService = electionService;
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
