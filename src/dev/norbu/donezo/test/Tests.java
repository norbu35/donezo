package dev.norbu.donezo.test;

import dev.norbu.donezo.model.Task;
import dev.norbu.donezo.model.Title;
import dev.norbu.donezo.repository.TaskRepository;
import dev.norbu.donezo.service.TaskManager;
import dev.norbu.donezo.storage.InMemoryTaskRepository;

public class Tests {

    private Tests() {
    }

    public static void run() {
        TaskRepository taskRepository = new InMemoryTaskRepository();
        TaskManager taskManager = new TaskManager(taskRepository);

        // Check created task
        var task1 = new Task.Builder().title("Task 1")
                .description("Test task")
                .status(Task.Status.PENDING)
                .build();

        taskManager.addTask(task1);

        var fetchedTask = taskManager.getTask(task1.getId()).orElseThrow();
        assert fetchedTask.equals(task1);

        // Update task
        var task2 = Task.from(task1.getId(),
                              new Title("Updated task"),
                              task1.getDescription(),
                              task1.getPriority(),
                              task1.getDueDate(),
                              task1.getStatus());

        taskManager.updateTask(task2);

        fetchedTask = taskManager.getTask(task1.getId()).orElseThrow();
        assert fetchedTask.getTitle().value().equals("Updated task");

        // List tasks
        var tasks = taskRepository.findAll();
        tasks.forEach(System.out::println);

        // Mark task as complete
        var task3 = new Task.Builder()
                .title("test task 3")
                .status(Task.Status.PENDING)
                .build();

        taskManager.addTask(task3);
        taskManager.markAsCompleted(task3.getId());

        assert taskManager.getTask(task3.getId())
                .orElseThrow()
                .getStatus()
                .equals(Task.Status.COMPLETED);

        // Delete task
        taskManager.deleteTask(task1.getId());
        assert taskManager.getTask(task1.getId()).isEmpty();
    }
}
