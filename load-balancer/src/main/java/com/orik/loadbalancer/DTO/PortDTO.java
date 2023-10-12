package com.orik.loadbalancer.DTO;

public class PortDTO {
    private Integer port;

    public PortDTO(Integer port) {
        this.port = port;
    }

    public PortDTO() {
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
