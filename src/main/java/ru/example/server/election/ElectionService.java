package ru.example.server.election;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.example.netty.client.HttpClient;
import ru.example.netty.dto.RequestVoteDto;
import ru.example.netty.dto.ResponseVoteDto;
import ru.example.server.context.Context;
import ru.example.server.context.ContextImpl;
import ru.example.server.network.Service;
import ru.example.server.network.ServicesProperties;
import ru.example.server.node.Peer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static ru.example.netty.client.ApiUri.ELECTION;
import static ru.example.server.node.State.CANDIDATE;
import static ru.example.server.node.State.FOLLOWER;
import static ru.example.server.node.State.LEADER;

/**
 * @author TaylakovSA
 */
public class ElectionService {
    private static final int VOTE_RETRY_DELAY = 2000;
    private final Logger log = LoggerFactory.getLogger(ElectionService.class);
    private Context context;
    private HttpClient<RequestVoteDto, ResponseVoteDto> httpClient;
    private ServicesProperties servicesProperties;

    public ElectionService() {
        context = ContextImpl.getInstance();
        httpClient = new HttpClient<>();
        servicesProperties = new ServicesProperties();
    }

    public void processElection(){
        if (context.getState().equals(LEADER) || !context.getActive()) {
            return;
        }

        log.info("Peer #{} Start election", context.getId());

        context.setState(CANDIDATE);
        Long term = context.incCurrentTerm();
        context.setVotedFor(context.getId());

        List<Integer> peersIds = context.getPeers().stream().map(Peer::getId).collect(Collectors.toList());
        long voteGrantedCount = 1L;
        long voteRevokedCount = 0L;

        //while didn't get heartbeat from leader or new election started
        while (checkCurrentElectionStatus(term)) {
            List<ResponseVoteDto> answers = getVoteFromAllPeers(term, peersIds);
            peersIds = new ArrayList<>();
            for (ResponseVoteDto answer : answers) {
                if (answer.getStatusCode().equals("OK")) {
                    if (answer.getTerm() > context.getCurrentTerm()) {
                        //If response contains term T > currentTerm: set currentTerm = T, convert to follower
                        context.setTermGreaterThenCurrent(answer.getTerm());
                        return;
                    }
                    if (answer.isVoteGranted()) {
                        log.info("Peer #{} Vote granted from {}", context.getId(), answer.getId());
                        context.getPeer(answer.getId()).setVoteGranted(true);
                        voteGrantedCount++;
                    } else
                        log.info("Peer #{} Vote revoked from {}", context.getId(), answer.getId());
                    voteRevokedCount++;
                } else {
                    log.info("Peer #{} No vote answer from {}", context.getId(), answer.getId());
                    peersIds.add(answer.getId());
                }
            }
            if (voteGrantedCount >= context.getQuorum()) {
                winElection(term);
                return;
            } else if (voteRevokedCount >= context.getQuorum()) {
                loseElection(term);
                return;
            } //else retry
            delay();
        }
    }

    private List<ResponseVoteDto> getVoteFromAllPeers(Long term,
                                                    List<Integer> peers) {
        log.debug("Peer #{} Forward vote request to peers. Term {}. Peers count: {}", context.getId(), term,
                peers.size());
        List<CompletableFuture<ResponseVoteDto>> answerFutureList =
                peers.stream()
                        .map(i -> getVoteFromOnePeer(i, term))
                        .collect(Collectors.toList());

        if (checkCurrentElectionStatus(term)) {
            return CompletableFuture.allOf(
                    answerFutureList.toArray(new CompletableFuture[0])
            ).thenApply(v ->
                    answerFutureList.stream().map(CompletableFuture::join).collect(Collectors.toList())
            ).join();
        } else
            return new ArrayList<>();
    }

    private CompletableFuture<ResponseVoteDto> getVoteFromOnePeer(Integer id,
                                                                  Long term) {
        return CompletableFuture.supplyAsync(() -> {
            if (!checkCurrentElectionStatus(term))
                return new ResponseVoteDto(id, "NO_CONTENT");
            try {
                log.info("Peer #{} Send vote request to {}", context.getId(), id);

                RequestVoteDto requestVoteDTO = new RequestVoteDto(term, context.getId());
                Service service = servicesProperties.getByName(String.valueOf(id));
                ResponseVoteDto send = httpClient.send(requestVoteDTO, ResponseVoteDto.class,
                        service.getHost(), service.getPort(), ELECTION);

                return Optional.ofNullable(send).
                        orElse(new ResponseVoteDto(id, "NO_CONTENT"));
            } catch (Exception e) {
                log.error("Peer #{} Vote request error for {}. {} {} ", context.getId(), id, e.getClass(),
                        e.getMessage());
                return new ResponseVoteDto(id, "INTERNAL_SERVER_ERROR");
            }
        });
    }

    public ResponseVoteDto vote(RequestVoteDto dto) {
        context.cancelIfNotActive();
        log.info("Peer #{} Get vote request from {} with term {}. Current term: {}. Voted for: {}", context.getId(),
                dto.getCandidateId(),
                dto.getTerm(),
                context.getCurrentTerm(),
                context.getVotedFor());


//        1. Reply false if term < currentTerm
//        2. If votedFor is null or candidateId, and candidate’s operations is at
//        least as up-to-date as receiver’s operations, grant vote

        boolean voteGranted;
        if (dto.getTerm() < context.getCurrentTerm())
            return new ResponseVoteDto(context.getId(), context.getCurrentTerm(), false);
        else if (dto.getTerm().equals(context.getCurrentTerm())) {
            voteGranted = (context.getVotedFor() == null || context.getVotedFor().equals(dto.getCandidateId()));
        } else {
            //If response contains term T > currentTerm: set currentTerm = T, convert to follower
            voteGranted = true;
            context.setTermGreaterThenCurrent(dto.getTerm());
        }

        if (voteGranted) {
            context.setVotedFor(dto.getCandidateId());
            log.info("Peer #{} Give vote for {}", context.getId(), dto.getCandidateId());
        } else {
            log.info("Peer #{} Reject vote for {} Current term {}, Candidate term {} ", context.getId(),
                    dto.getCandidateId(),
                    context.getCurrentTerm(), dto.getTerm());
        }
        return new ResponseVoteDto(context.getId(), context.getCurrentTerm(), voteGranted);
    }

    private void delay() {
        try {
            log.info("Peer #{} Preparing to retry vote request", context.getId());
            Thread.sleep(VOTE_RETRY_DELAY);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    private boolean checkCurrentElectionStatus(Long term) {
        return term.equals(context.getCurrentTerm()) && context.getState().equals(CANDIDATE);
    }

    private void winElection(Long term) {
        if (checkCurrentElectionStatus(term)) {
            log.info("Peer #{} has WON the election!", context.getId());
            context.setState(LEADER);
        }
    }

    private void loseElection(Long term) {
        if (checkCurrentElectionStatus(term)) {
            log.info("Peer #{} has LOSE the election!", context.getId());
            context.setState(FOLLOWER);
        }
    }
}
