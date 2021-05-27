package ru.example.netty.dto;

/**
 * @author TaylakovSA
 */
public class HeartbeatResponseDto  {

    private Integer id;

    private Long term;

    private Boolean success;


    private String statusCode;

    public HeartbeatResponseDto() {
    }

    public HeartbeatResponseDto(Integer id, Long term, Boolean success)
    {
        this.id = id;
        this.term = term;
        this.success = success;
        this.statusCode = "OK";
    }

    public HeartbeatResponseDto(Integer id, String statusCode) {
        this.id = id;
        this.statusCode = statusCode;
        this.term = null;
        this.success = false;
    }

    public Integer getId() {
        return id;
    }

    public Long getTerm() {
        return term;
    }

    public Boolean getSuccess() {
        return success;
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

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "HeartbeatResponseDto{" +
                "id=" + id +
                ", term=" + term +
                ", success=" + success +
                ", statusCode=" + statusCode +
                '}';
    }
}
