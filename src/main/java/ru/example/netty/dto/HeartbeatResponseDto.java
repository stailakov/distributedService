package ru.example.netty.dto;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author TaylakovSA
 */
public class HeartbeatResponseDto {

    private Integer id;

    private Long term;

    private Boolean success;

    private Integer matchIndex;

    private String statusCode;

    public HeartbeatResponseDto() {
    }

//    public HeartbeatResponseDto(Integer id, Long term, Boolean success, Integer matchIndex)
//    {
//        this.id = id;
//        this.term = term;
//        this.success = success;
//        this.matchIndex = matchIndex;
//        this.statusCode = HttpResponseStatus.OK;
//    }
//
//    public HeartbeatResponseDto(Integer id, HttpResponseStatus statusCode) {
//        this.id = id;
//        this.statusCode = statusCode;
//        this.term = null;
//        this.success = false;
//        matchIndex = null;
//    }

    public Integer getId() {
        return id;
    }

    public Long getTerm() {
        return term;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Integer getMatchIndex() {
        return matchIndex;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTerm(Long term) {
        this.term = term;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setMatchIndex(Integer matchIndex) {
        this.matchIndex = matchIndex;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "HeartbeatResponseDto{" +
                "id=" + id +
                ", term=" + term +
                ", success=" + success +
                ", matchIndex=" + matchIndex +
                ", statusCode=" + statusCode +
                '}';
    }
}
