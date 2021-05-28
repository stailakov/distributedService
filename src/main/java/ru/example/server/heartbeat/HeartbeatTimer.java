package ru.example.server.heartbeat;

import ru.example.server.timer.ServerTimer;

import static ru.example.server.node.State.LEADER;

/**
 * @author TaylakovSA
 */
public class HeartbeatTimer extends ServerTimer {

    private HeartbeatService heartbeatService;

    public HeartbeatTimer() {
        this.heartbeatService = new HeartbeatService();
    }

    @Override
    protected Integer getTimeout() {
        return nodeProperties.getHeartBeatTimeout();
    }

    @Override
    protected String getActionName() {
        return "heart beat";
    }

    @Override
    protected Runnable getAction() {
        return heartbeatService::processHeartbeat;
    }

    @Override
    protected boolean isRun() {
        System.out.println(nodeProperties.getState());
        return nodeProperties.getActive() && nodeProperties.getState().equals(LEADER);
    }
}
