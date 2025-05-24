package com.example.todoapp.dto;

import com.example.todoapp.enums.TaskStatus;  // << THIS IMPORT IS REQUIRED

public class StatusUpdateRequest {

    private TaskStatus status;

    public StatusUpdateRequest() {
    }

    public StatusUpdateRequest(TaskStatus status) {
        this.status = status;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
