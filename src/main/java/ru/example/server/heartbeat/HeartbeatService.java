package ru.example.server.heartbeat;

import ru.example.netty.client.HttpClient;
import ru.example.netty.dto.HeartbeatRequestDto;
import ru.example.netty.dto.HeartbeatResponseDto;

/**
 * @author TaylakovSA
 */
public class HeartbeatService {

    HttpClient<HeartbeatRequestDto, HeartbeatResponseDto> httpClient = new HttpClient<>();

    public void processHeartbeat(){
        try {
            HeartbeatResponseDto send = httpClient.send(createRequest(), HeartbeatResponseDto.class, "/heartbeat");
            System.out.println(send);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private HeartbeatRequestDto createRequest(){
        HeartbeatRequestDto requestDto = new HeartbeatRequestDto();
        requestDto.setLeaderId(111);
        requestDto.setPrevLogTerm(222L);
        return requestDto;
    }
}
