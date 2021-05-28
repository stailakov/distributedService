package ru.example.server.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.example.server.node.NodeProperties;
import ru.example.server.node.NodePropertiesImpl;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author TaylakovSA
 */
public abstract class ServerTimer {

    Logger log = LoggerFactory.getLogger(ServerTimer.class);

    protected final NodeProperties nodeProperties;
    private final Timer timer = new Timer();

    abstract protected Integer getTimeout();
    abstract protected String getActionName();
    abstract protected Runnable getAction();
    abstract protected boolean isRun();

    private final AtomicInteger counter = new AtomicInteger(0);

    public AtomicInteger getCounter() {
        return counter;
    }

    protected ServerTimer() {
        this.nodeProperties = NodePropertiesImpl.getInstance();
        start();
    }

    public void reset() {
        counter.set(0);
    }

    private void start() {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isRun()) {
                    counter.incrementAndGet();
                    log.debug("Peer #{} Time to next {}: {} sec", nodeProperties.getId(), getActionName(), getTimeout() - counter.get());
                    if (counter.get() >= getTimeout()) {
                        counter.set(0);
                        getAction().run();
                    }
                }else
                    counter.set(0);
            }
        }, 0, 1000);

    }
}
