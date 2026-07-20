package com.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request payload for creating a new Task.
 * Task name is mandatory as per business rules.
 */
public class TaskRequest {

    @NotBlank(message = "Task name is mandatory")
    @Size(max = 100, message = "Task name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    public TaskRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
