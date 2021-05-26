package ru.example.netty.dto;

/**
 * @author TaylakovSA
 */
public class HeartbeatRequestDto {

    public HeartbeatRequestDto() {
    }

    public HeartbeatRequestDto(Long term, Integer leaderId, Integer prevLogIndex, Long prevLogTerm, Integer leaderCommit) {
        this.term = term;
        this.leaderId = leaderId;
        this.prevLogIndex = prevLogIndex;
        this.prevLogTerm = prevLogTerm;
        this.leaderCommit = leaderCommit;
    }

    private Long term;
    private Integer leaderId;

    private Integer prevLogIndex;

    private Long prevLogTerm;

    private Integer leaderCommit;

    public Long getTerm() {
        return term;
    }

    public void setTerm(Long term) {
        this.term = term;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Integer leaderId) {
        this.leaderId = leaderId;
    }

    public Integer getPrevLogIndex() {
        return prevLogIndex;
    }

    public void setPrevLogIndex(Integer prevLogIndex) {
        this.prevLogIndex = prevLogIndex;
    }

    public Long getPrevLogTerm() {
        return prevLogTerm;
    }

    public void setPrevLogTerm(Long prevLogTerm) {
        this.prevLogTerm = prevLogTerm;
    }

    public Integer getLeaderCommit() {
        return leaderCommit;
    }

    public void setLeaderCommit(Integer leaderCommit) {
        this.leaderCommit = leaderCommit;
    }
}
