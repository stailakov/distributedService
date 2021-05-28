package ru.example.netty.dto;


/**
 * @author TaylakovSA
 */
public class RequestVoteDto {

    public RequestVoteDto() {
    }

    public RequestVoteDto(Long term, Integer candidateId) {
        this.term = term;
        this.candidateId = candidateId;
    }

    private Long term;

    private Integer candidateId;

    public Long getTerm() {
        return term;
    }

    public void setTerm(Long term) {
        this.term = term;
    }

    public Integer getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }

    @Override
    public String toString() {
        return "RequestVoteDto{" +
                "term=" + term +
                ", candidateId=" + candidateId +
                '}';
    }
}
