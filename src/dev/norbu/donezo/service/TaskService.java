package dev.norbu.donezo.service;

import dev.norbu.donezo.model.Task;
import dev.norbu.donezo.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void save(Task task) {
        taskRepository.save(task);
    }

    public boolean markAsCompleted(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be empty.");
        }
        return taskRepository.findById(id)
                .map(task -> {
                    taskRepository.save(task.markAsCompleted());
                    return true;
                })
                .orElse(false);
    }

    public boolean deleteById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be empty.");
        }
        return taskRepository.deleteById(id);
    }

    public Optional<Task> getById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be empty.");
        }
        return taskRepository.findById(id);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public void saveAll(List<Task> tasks) {
        taskRepository.saveAll(tasks);
    }
}
