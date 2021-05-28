package ru.example.server.heartbeat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.example.netty.client.HttpClient;
import ru.example.netty.dto.HeartbeatRequestDto;
import ru.example.netty.dto.HeartbeatResponseDto;
import ru.example.server.context.Context;
import ru.example.server.context.ContextImpl;
import ru.example.server.election.ElectionTimer;
import ru.example.server.network.Service;
import ru.example.server.network.ServicesProperties;
import ru.example.server.node.Peer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static ru.example.netty.client.ApiUri.HEARTBEAT;
import static ru.example.server.node.State.FOLLOWER;

/**
 * @author TaylakovSA
 */
public class HeartbeatService {

    Logger log = LoggerFactory.getLogger(HeartbeatService.class);

    private Context context;
    private ElectionTimer electionTimer;
    private HttpClient<HeartbeatRequestDto, HeartbeatResponseDto> httpClient;
    ServicesProperties servicesProperties;

    public HeartbeatService() {
        context = ContextImpl.getInstance();
        electionTimer = ElectionTimer.getInstance();
        httpClient = new HttpClient<>();
        servicesProperties = new ServicesProperties();
    }

    public void processHeartbeat(){
        log.debug("Peer #{} Sending request to peers", context.getId());
        List<Integer> peersIds = context.getPeers().stream().map(Peer::getId).collect(Collectors.toList());

        while (peersIds.size() > 0) {//retry
            List<HeartbeatResponseDto> answers = sendAppendToAllPeers(peersIds);
            peersIds = new ArrayList<>();
            for (HeartbeatResponseDto answer : answers) {
                if (answer.getStatusCode().equals("OK")) {
                    if (answer.getTerm() > context.getCurrentTerm()) {
                        //If response contains term T > currentTerm: set currentTerm = T, convert to follower
                        context.setTermGreaterThenCurrent(answer.getTerm());
                        return;
                    }
                    Peer peer = context.getPeer(answer.getId());
                    if (answer.getSuccess()) {
                        log.debug("Peer #{} Get \"request success\"  from {}", context.getId(), answer.getId());
                    } else {
                        //If fails - retry
                        log.debug("Peer #{} Get request rejected from {}",
                                context.getId(), answer.getId());
                        peersIds.add(answer.getId());
                    }
                }
            }
        }
    }

    private List<HeartbeatResponseDto> sendAppendToAllPeers(List<Integer> peers) {
        List<CompletableFuture<HeartbeatResponseDto>> answerFutureList =
                peers.stream()
                        .map(this::sendAppendForOnePeer)
                        .collect(Collectors.toList());

        return CompletableFuture.allOf(
                answerFutureList.toArray(new CompletableFuture[0])
        ).thenApply(v ->
                answerFutureList.stream().map(CompletableFuture::join).collect(Collectors.toList())
        ).join();
    }

    private CompletableFuture<HeartbeatResponseDto> sendAppendForOnePeer(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Service service = servicesProperties.getByName(String.valueOf(id));
                HeartbeatResponseDto responseDto = httpClient.send(createRequest(), HeartbeatResponseDto.class,
                        service.getHost(), service.getPort(), HEARTBEAT);

                return Optional.ofNullable(responseDto).
                        orElse(new HeartbeatResponseDto(id, "NO_CONTENT"));
            } catch (Exception e) {
                log.error("Peer #{} {} request error for Heartbeat. {} {} ", context.getId(), id, e.getClass(),e.getMessage());
                return new HeartbeatResponseDto(id, "SERVICE_UNAVAILABLE");
            }
        });
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

    private HeartbeatRequestDto createRequest(){
        return new HeartbeatRequestDto(
                context.getCurrentTerm(),
                context.getId(),
                context.getCommitIndex()
        );
    }
}
