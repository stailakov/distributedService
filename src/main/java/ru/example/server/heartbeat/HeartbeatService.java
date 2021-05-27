package ru.example.server.heartbeat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.example.netty.client.HttpClient;
import ru.example.netty.dto.HeartbeatRequestDto;
import ru.example.netty.dto.HeartbeatResponseDto;
import ru.example.server.context.Context;
import ru.example.server.context.ContextImpl;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author TaylakovSA
 */
public class HeartbeatService {

    Logger log = LoggerFactory.getLogger(HeartbeatService.class);

    private Context context;
    private HttpClient<HeartbeatRequestDto, HeartbeatResponseDto> httpClient;

    public HeartbeatService() {
        context = new ContextImpl();
        httpClient = new HttpClient<>();
    }

    public void processHeartbeat(){
        try {
            CompletableFuture<HeartbeatResponseDto> future = sendAppendForOnePeer(1);
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private CompletableFuture<HeartbeatResponseDto> sendAppendForOnePeer(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HeartbeatResponseDto send = httpClient.send(createRequest(), HeartbeatResponseDto.class, "/heartbeat");

                return Optional.ofNullable(send).
                        orElse(new HeartbeatResponseDto(id, "NO_CONTENT"));
            } catch (Exception e) {
                log.error("Peer #{} {} request error for Heartbeat. {} {} ", context.getId(), id, e.getClass(),e.getMessage());
                return new HeartbeatResponseDto(id, "SERVICE_UNAVAILABLE");
            }
        });
    }

    private HeartbeatRequestDto createRequest(){
        return new HeartbeatRequestDto(
                context.getCurrentTerm(),
                context.getId(),
                context.getCommitIndex()
        );
    }
}
