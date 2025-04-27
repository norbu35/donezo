package dev.norbu.donezo.test;

import dev.norbu.donezo.model.Task;
import dev.norbu.donezo.repository.TaskRepository;
import dev.norbu.donezo.service.TaskManager;
import dev.norbu.donezo.storage.InMemoryTaskRepository;

public class Tests {

  public static void run() {
    TaskRepository taskRepository = new InMemoryTaskRepository();
    TaskManager taskManager = new TaskManager(taskRepository);

    // List tasks
    var tasks = taskRepository.findAll();
    tasks.forEach(System.out::println);

    // Check created task
    var addedTask = taskManager.addTask("Test", "Task for testing purposes.");

    assert (taskManager.getTask(addedTask.getId())
            .orElseThrow()
            .equals(addedTask));

    // Update task
    Task task = Task.of("updated task 2", "desc");
    taskManager.updateTask(task);
    assert (taskManager.getTask(task.getId())
            .orElseThrow()
            .getPriority()
            .equals(Task.Priority.MEDIUM));

    // Delete task
    taskManager.deleteTask(task.getId());
    assert (taskManager.getTask(task.getId())
            .isEmpty());
  }
}
