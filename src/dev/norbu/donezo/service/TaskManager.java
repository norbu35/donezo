package dev.norbu.donezo.service;

import dev.norbu.donezo.model.DueDate;
import dev.norbu.donezo.model.Task;
import dev.norbu.donezo.repository.TaskRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TaskManager {

  private final TaskRepository taskRepository;

  public TaskManager(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  public Task addTask(String title, String description) {
    return addTask(title,
                   description,
                   Task.Priority.MEDIUM,
                   new DueDate(ZonedDateTime.now()
                                       .plusDays(3)),
                   Task.Status.PENDING);
  }

  public Task addTask(String title,
                      String description,
                      Task.Priority priority,
                      DueDate dueDate,
                      Task.Status status) {
    var task = Task.of(title, description, priority, dueDate, status);
    taskRepository.save(task);
    return task;
  }

  public Task addTask(String title, String description, Task.Priority priority) {
    return addTask(title,
                   description,
                   priority,
                   new DueDate(ZonedDateTime.now()
                                       .plusDays(3)),
                   Task.Status.PENDING);
  }

  public Task addTask(String title,
                      String description,
                      Task.Priority priority,
                      DueDate dueDate) {
    return addTask(title, description, priority, dueDate, Task.Status.PENDING);
  }

  public List<Task> listTasks() {
    return taskRepository.findAll();
  }

  public void updateTask(Task task) {
    taskRepository.update(task);
  }

  public boolean markAsCompleted(UUID id) {
    return taskRepository.findById(id)
            .map(task -> {
              task.markAsCompleted();
              taskRepository.update(task);
              return true;
            })
            .orElse(false);
  }

  public boolean deleteTask(UUID id) {
    return taskRepository.findById(id)
            .map(_ -> {
              taskRepository.deleteById(id);
              return true;
            })
            .orElse(false);
  }

  public Optional<Task> getTask(UUID id) {
    return taskRepository.findById(id);
  }

  public void saveAll(List<Task> tasks) {
    taskRepository.saveAll(tasks);
  }
}
