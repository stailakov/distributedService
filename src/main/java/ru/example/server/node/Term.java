package ru.example.server.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author TaylakovSA
 */
public class Term {

    Logger log = LoggerFactory.getLogger(Term.class);

    private final AtomicLong currentTerm = new AtomicLong(0L);
    private final NodeProperties nodeProperties;

    public Term(NodeProperties nodeProperties) {
        this.nodeProperties = nodeProperties;
    }

    public Long getCurrentTerm() {
        return currentTerm.get();
    }

    public void setCurrentTerm(long currentTerm) {
        this.currentTerm.set(currentTerm);
        log.info("Peer #{} Set term to {}", nodeProperties.getId(),getCurrentTerm());
    }

    public Long incCurrentTerm() {
        currentTerm.incrementAndGet();
        log.info("Peer #{} Term incremented: {}",nodeProperties.getId(), getCurrentTerm());
        return getCurrentTerm();
    }
}
