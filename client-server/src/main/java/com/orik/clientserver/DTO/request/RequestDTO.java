package com.orik.clientserver.DTO.request;

import java.util.Objects;

public class RequestDTO {

    private Long id;

    private Integer request;

    private String status;


    public RequestDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRequest() {
        return request;
    }

    public void setRequest(Integer request) {
        this.request = request;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestDTO that = (RequestDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(request, that.request) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, request, status);
    }
}
