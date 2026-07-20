package com.taskmanager.controller;

import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskStatusUpdateRequest;
import com.taskmanager.dto.TaskSummaryResponse;
import com.taskmanager.model.Task;
import com.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for Task Management.
 *
 * Base path: /api/tasks
 *  POST   /api/tasks             -> create a task
 *  GET    /api/tasks             -> list all tasks (optional search/status filter)
 *  PATCH  /api/tasks/{id}/status -> update a task's status
 *  DELETE /api/tasks/{id}        -> delete a task
 *  GET    /api/tasks/summary     -> dashboard statistics
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskRequest request) {
        Task created = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status) {

        List<Task> tasks = taskService.getAllTasks();

        if (search != null && !search.isBlank()) {
            String needle = search.trim().toLowerCase();
            tasks = tasks.stream()
                    .filter(t -> t.getName().toLowerCase().contains(needle)
                            || (t.getDescription() != null && t.getDescription().toLowerCase().contains(needle)))
                    .toList();
        }

        if (status != null && !status.isBlank() && !status.equalsIgnoreCase("ALL")) {
            tasks = tasks.stream()
                    .filter(t -> t.getStatus().name().equalsIgnoreCase(status))
                    .toList();
        }

        return ResponseEntity.ok(tasks);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateStatus(@PathVariable Long id,
                                              @Valid @RequestBody TaskStatusUpdateRequest request) {
        Task updated = taskService.updateStatus(id, request.getStatus());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    public ResponseEntity<TaskSummaryResponse> getSummary() {
        return ResponseEntity.ok(taskService.getSummary());
    }
}
