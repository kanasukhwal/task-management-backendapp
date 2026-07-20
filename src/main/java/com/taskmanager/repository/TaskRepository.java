package com.taskmanager.repository;

import com.taskmanager.model.Task;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory data store for Tasks.
 *
 * As required by the assignment, no external database is used. A thread-safe
 * {@link CopyOnWriteArrayList} backs the collection so the app behaves
 * correctly even under concurrent requests, and an {@link AtomicLong}
 * generates unique, monotonically increasing IDs.
 */
@Repository
public class TaskRepository {

    private final List<Task> tasks = new CopyOnWriteArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(idGenerator.incrementAndGet());
            tasks.add(task);
        } else {
            // update existing task in place
            tasks.replaceAll(t -> t.getId().equals(task.getId()) ? task : t);
        }
        return task;
    }

    public List<Task> findAll() {
        return List.copyOf(tasks);
    }

    public Optional<Task> findById(Long id) {
        return tasks.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    public boolean deleteById(Long id) {
        return tasks.removeIf(t -> t.getId().equals(id));
    }
}
