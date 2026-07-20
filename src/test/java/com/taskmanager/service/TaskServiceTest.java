package com.taskmanager.service;

import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskSummaryResponse;
import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        // Fresh repository + service per test (no @PostConstruct seeding here,
        // so each test controls its own data).
        taskService = new TaskService(new TaskRepository());
    }

    @Test
    void createTask_setsPendingStatusAndCreatedDate() {
        TaskRequest request = new TaskRequest();
        request.setName("Write unit tests");
        request.setDescription("Cover the service layer");

        Task created = taskService.createTask(request);

        assertNotNull(created.getId());
        assertEquals("Write unit tests", created.getName());
        assertEquals(TaskStatus.PENDING, created.getStatus());
        assertNotNull(created.getCreatedDate());
    }

    @Test
    void getAllTasks_returnsNewestFirst() {
        taskService.createTask(taskRequest("First task"));
        taskService.createTask(taskRequest("Second task"));

        var tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        assertEquals("Second task", tasks.get(0).getName());
    }

    @Test
    void completeTask_marksStatusAsCompleted() {
        Task task = taskService.createTask(taskRequest("Finish report"));

        Task completed = taskService.completeTask(task.getId());

        assertEquals(TaskStatus.COMPLETED, completed.getStatus());
    }

    @Test
    void updateStatus_withUnknownId_throwsTaskNotFoundException() {
        assertThrows(TaskNotFoundException.class, () -> taskService.updateStatus(999L, TaskStatus.COMPLETED));
    }

    @Test
    void deleteTask_removesTaskFromRepository() {
        Task task = taskService.createTask(taskRequest("Temporary task"));

        taskService.deleteTask(task.getId());

        assertTrue(taskService.getAllTasks().isEmpty());
    }

    @Test
    void deleteTask_withUnknownId_throwsTaskNotFoundException() {
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(999L));
    }

    @Test
    void getSummary_countsPendingAndCompletedCorrectly() {
        Task t1 = taskService.createTask(taskRequest("Task 1"));
        taskService.createTask(taskRequest("Task 2"));
        taskService.completeTask(t1.getId());

        TaskSummaryResponse summary = taskService.getSummary();

        assertEquals(2, summary.getTotalTasks());
        assertEquals(1, summary.getPendingTasks());
        assertEquals(1, summary.getCompletedTasks());
    }

    private TaskRequest taskRequest(String name) {
        TaskRequest request = new TaskRequest();
        request.setName(name);
        request.setDescription("Description for " + name);
        return request;
    }
}
