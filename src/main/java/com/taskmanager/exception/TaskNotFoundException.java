package com.taskmanager.exception;

/**
 * Thrown when a Task with the given ID does not exist.
 */
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("Task not found with id: " + id);
    }
}
