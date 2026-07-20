package com.taskmanager.service;

import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskSummaryResponse;
import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.repository.TaskRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * Encapsulates all business logic for Task management.
 * Keeping this logic out of the controller keeps the API layer thin
 * and makes the rules independently unit-testable.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Seeds a few sample tasks so the app is immediately usable on startup,
     * as required by the assignment ("start with a few sample tasks").
     */
    @PostConstruct
    public void seedSampleData() {
        createTask(new TaskRequest() {
            {
                setName("Set up project repository");
                setDescription("Initialize Git repo and push the base project structure");
            }
        });
        createTask(new TaskRequest() {
            {
                setName("Design REST API contracts");
                setDescription("Define request/response DTOs for the Task Management API");
            }
        });
        createTask(new TaskRequest() {
            {
                setName("Build Angular dashboard");
                setDescription("Create the summary widget showing total/pending/completed tasks");
            }
        });
        completeTask(taskRepository.findAll().get(0).getId());
    }

    public Task createTask(TaskRequest request) {
        Task task = new Task();
        task.setName(request.getName().trim());
        task.setDescription(request.getDescription() == null ? "" : request.getDescription().trim());
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedDate(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll().stream()
                .sorted(Comparator.comparing(Task::getCreatedDate)
                        .thenComparing(Task::getId)
                        .reversed())
                .toList();
    }

    public Task updateStatus(Long id, TaskStatus status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.setStatus(status);
        return taskRepository.save(task);
    }

    public Task completeTask(Long id) {
        return updateStatus(id, TaskStatus.COMPLETED);
    }

    public void deleteTask(Long id) {
        boolean removed = taskRepository.deleteById(id);
        if (!removed) {
            throw new TaskNotFoundException(id);
        }
    }

    public TaskSummaryResponse getSummary() {
        List<Task> all = taskRepository.findAll();
        long total = all.size();
        long completed = all.stream().filter(t -> t.getStatus() == TaskStatus.COMPLETED).count();
        long pending = total - completed;
        return new TaskSummaryResponse(total, pending, completed);
    }
}
