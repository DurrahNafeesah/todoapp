package com.example.todoapp.dto;

public class StatusUpdateRequest {

    private Long statusId;

    public StatusUpdateRequest() {
    }

    public StatusUpdateRequest(Long statusId) {
        this.statusId = statusId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }
}
