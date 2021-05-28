package ru.example.netty.dto;

/**
 * @author TaylakovSA
 */
public class HeartbeatRequestDto {

    public HeartbeatRequestDto() {
    }

    public HeartbeatRequestDto(Long term, Integer leaderId, Integer leaderCommit) {
        this.term = term;
        this.leaderId = leaderId;
        this.leaderCommit = leaderCommit;
    }

    private Long term;
    private Integer leaderId;

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

    public Integer getLeaderCommit() {
        return leaderCommit;
    }

    public void setLeaderCommit(Integer leaderCommit) {
        this.leaderCommit = leaderCommit;
    }
}
