package dev.norbu.donezo.service;

import dev.norbu.donezo.model.Description;
import dev.norbu.donezo.model.DueDate;
import dev.norbu.donezo.model.Task;
import dev.norbu.donezo.model.Title;
import dev.norbu.donezo.repository.TaskRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TaskManager {

  private final TaskRepository taskRepository;

  public TaskManager(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  public Task addTask(Task task) {
    addTask(task.getTitle(), task.getDescription(), task.getPriority(), task.getDueDate(),
            task.getStatus());
  }

  public Task addTask(Title title,
                      Description description,
                      Task.Priority priority,
                      DueDate dueDate,
                      Task.Status status) {
    Task task = new Task.Builder().title(title)
            .description(description)
            .dueDate(dueDate)
            .priority(priority)
            .status(status)
            .build();
    taskRepository.save(task);
    return task;
  }

  public Task addTask(Title title, Description description) {
    Task task = new Task.Builder().title(title)
            .description(description)
            .build();
    taskRepository.save(task);
    return task;
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
              taskRepository.update(task.markAsCompleted());
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
