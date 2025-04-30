package dev.norbu.donezo.service;

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

    public boolean markAsCompleted(final String id) {
        final var taskOptional = taskRepository.findById(id);

        taskOptional.ifPresent(task -> {
            if (task.getStatus() != Task.Status.COMPLETED) {
                taskRepository.save(task.markAsCompleted());
            }
        });

        return taskOptional.isPresent();
    }

    public boolean deleteById(final String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be empty.");
        }
        return taskRepository.deleteById(id);
    }

    public Optional<Task> getById(final String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be empty.");
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
