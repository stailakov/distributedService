package ru.example.netty.dto;

/**
 * @author TaylakovSA
 */
public class ResponseVoteDto {

    private Integer id;
    private Long term;
    private boolean voteGranted;
    private String statusCode;

    public ResponseVoteDto() {
    }

    public ResponseVoteDto(Integer id, Long term,boolean voteGranted) {
        this.id = id;
        this.term = term;
        this.voteGranted = voteGranted;
        this.statusCode = "OK";
    }

    public ResponseVoteDto(Integer id, String statusCode) {
        this.id = id;
        this.statusCode = statusCode;
        term = null;
        voteGranted = false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getTerm() {
        return term;
    }

    public void setTerm(Long term) {
        this.term = term;
    }

    public boolean isVoteGranted() {
        return voteGranted;
    }

    public void setVoteGranted(boolean voteGranted) {
        this.voteGranted = voteGranted;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "ResponseVoteDto{" +
                "id=" + id +
                ", term=" + term +
                ", voteGranted=" + voteGranted +
                ", statusCode='" + statusCode + '\'' +
                '}';
    }
}
