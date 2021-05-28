package ru.example.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.example.netty.dto.HeartbeatRequestDto;
import ru.example.netty.dto.HeartbeatResponseDto;
import ru.example.server.context.Context;
import ru.example.server.context.ContextImpl;
import ru.example.server.election.ElectionTimer;

import static ru.example.server.node.State.FOLLOWER;

/**
 * @author TaylakovSA
 */
public class HeartbeatRequestHandler {

    Logger log = LoggerFactory.getLogger(HeartbeatRequestHandler.class);

    private Context context;
    private ElectionTimer electionTimer;

    public HeartbeatRequestHandler() {
        this.context = ContextImpl.getInstance();
        this.electionTimer =  ElectionTimer.getInstance();
    }

    public HeartbeatResponseDto handle(HeartbeatRequestDto dto){
        context.cancelIfNotActive();

        if (dto.getTerm() < context.getCurrentTerm()) {
            log.info("Peer #{} Rejected request from {}. Term {} too small", context.getId(), dto.getLeaderId(),
                    dto.getTerm());
            return new HeartbeatResponseDto(context.getId(), context.getCurrentTerm(), false);
        } else if (dto.getTerm() > context.getCurrentTerm()) {
            context.setCurrentTerm(dto.getTerm());
            context.setVotedFor(null);
        }
        //reset election timer
        electionTimer.reset();
        // convert to follower because just one Leader
        if (!context.getState().equals(FOLLOWER)) {
            context.setState(FOLLOWER);
        }
        return new HeartbeatResponseDto(context.getId(), context.getCurrentTerm(), true);
    }

}
