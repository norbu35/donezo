package dev.norbu.donezo.service;

import dev.norbu.donezo.cli.exception.TaskNotFoundException;
import dev.norbu.donezo.model.Task;
import dev.norbu.donezo.repository.TaskRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(final TaskRepository taskRepository) {
        Objects.requireNonNull(taskRepository, "taskRepository cannot be null.");
        this.taskRepository = taskRepository;
    }

    public void save(final Task task) {
        taskRepository.save(task);
    }

    public void markAsCompleted(final String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Task ID cannot be null or blank.");
        }
        final Task task =
                taskRepository.findById(id)
                        .orElseThrow(() -> new TaskNotFoundException(id));

        if (task.getStatus() == Task.Status.COMPLETED) {
            System.out.printf("Task '%s' is already marked completed.", task.getId());
        } else {
            taskRepository.save(task.markAsCompleted());
        }
    }

    public void deleteById(final String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be null or blank.");
        }
        taskRepository.deleteById(id);
    }

    public Optional<Task> getById(final String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be null or blank.");
        }
        return taskRepository.findById(id);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public void saveAll(final List<Task> tasks) {
        taskRepository.saveAll(tasks);
    }
}
