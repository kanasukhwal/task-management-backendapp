package com.taskmanager.dto;

import com.taskmanager.model.TaskStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Request payload for updating a Task's status.
 */
public class TaskStatusUpdateRequest {

    @NotNull(message = "Status is mandatory")
    private TaskStatus status;

    public TaskStatusUpdateRequest() {
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
