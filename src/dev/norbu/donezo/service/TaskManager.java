package dev.norbu.donezo.service;

import dev.norbu.donezo.model.Description;
import dev.norbu.donezo.model.DueDate;
import dev.norbu.donezo.model.Task;
import dev.norbu.donezo.model.Title;
import dev.norbu.donezo.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

public class TaskManager {

    private final TaskRepository taskRepository;

    public TaskManager(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void addTask(Task task) {
        addTask(task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getDueDate(),
                task.getStatus());
    }

    public void addTask(String id,
                        Title title,
                        Description description,
                        Task.Priority priority,
                        DueDate dueDate,
                        Task.Status status) {
        taskRepository.save(Task.from(id, title, description, priority, dueDate, status));
    }

    public void addTask(Title title,
                        Description description,
                        Task.Priority priority,
                        DueDate dueDate,
                        Task.Status status) {
        taskRepository.save(Task.from(title, description, priority, dueDate, status));
    }

    public Task addTask(Title title, Description description) {
        Task task = new Task.Builder().title(title).description(description).build();
        taskRepository.save(task);
        return task;
    }

    public void updateTask(Task task) {
        taskRepository.update(task);
    }

    public void updateTask(Title title,
                           Description description,
                           Task.Priority priority,
                           DueDate dueDate,
                           Task.Status status) {
        taskRepository.update(new Task.Builder().title(title)
                                      .description(description)
                                      .priority(priority)
                                      .dueDate(dueDate)
                                      .status(status)
                                      .build());
    }

    public boolean markAsCompleted(String id) {
        return taskRepository.findById(id)
                .map(task -> {
                    taskRepository.update(task.markAsCompleted());
                    return true;
                })
                .orElse(false);
    }

    public void deleteTask(String id) {
        taskRepository.deleteById(id);
    }

    public Optional<Task> getTask(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be empty.");
        }
        if (id.length() < 4) {
            throw new IllegalArgumentException(
                    "Provide at least the first 4 characters of the ID" + ".");
        }

        String matched = null;

        for (Task task : taskRepository.findAll()) {
            if (task.getId().startsWith(id)) {
                if (matched != null) {
                    throw new IllegalArgumentException(
                            "Several matches found. Provide a more specific ID.");
                }
                matched = task.getId();
            }
        }

        return matched != null
                ? taskRepository.findById(matched)
                : Optional.empty();
    }

    public List<Task> listTasks() {
        return taskRepository.findAll();
    }

    public void saveAll(List<Task> tasks) {
        taskRepository.saveAll(tasks);
    }
}
