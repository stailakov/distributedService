package ru.example.server.heartbeat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.example.server.timer.ServerTimer;

import static ru.example.server.node.State.LEADER;

/**
 * @author TaylakovSA
 */
public class HeartbeatTimer extends ServerTimer {

    Logger log = LoggerFactory.getLogger(HeartbeatTimer.class);

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
        log.info("Current state: {}", nodeProperties.getState());
        return nodeProperties.getActive() && nodeProperties.getState().equals(LEADER);
    }
}
