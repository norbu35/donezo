package dev.norbu.donezo.test;

import dev.norbu.donezo.model.Description;
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
    Task task1 = new Task.Builder()
            .title("test task")
            .build();
    var addedTask =
            taskManager.addTask(task1);

    assert (taskManager.getTask(addedTask.getId())
            .orElseThrow()
            .equals(addedTask));

    // Update task
    Task task2 = new Task.Builder()
            .title("Test title")
            .description(new Description("test desc"))
            .build();
    taskManager.updateTask(task2);
    assert (taskManager.getTask(task2.getId())
            .orElseThrow()
            .getPriority()
            .equals(Task.Priority.MEDIUM));

    // Delete task
    taskManager.deleteTask(task1.getId());
    assert (taskManager.getTask(task1.getId())
            .isEmpty());
  }
}
