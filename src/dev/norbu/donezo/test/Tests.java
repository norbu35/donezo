package dev.norbu.donezo.test;

import dev.norbu.donezo.model.Task;
import dev.norbu.donezo.model.Title;
import dev.norbu.donezo.repository.TaskRepository;
import dev.norbu.donezo.service.TaskService;
import dev.norbu.donezo.storage.InMemoryTaskRepository;

public class Tests {

    private Tests() {
    }

    public static void run() {
        TaskRepository taskRepository = new InMemoryTaskRepository();
        TaskService taskService = new TaskService(taskRepository);

        // Check created task
        var task1 = Task.builder()
                .title("Task 1")
                .description("Test task")
                .status(Task.Status.PENDING)
                .build();

        taskService.save(task1);

        var fetchedTask = taskService.getById(task1.getId()).orElseThrow();
        assert fetchedTask.equals(task1);

        // Update task
        var task2 = Task.of(task1.getId(),
                            Title.of("Updated task"),
                            task1.getDescription(),
                            task1.getPriority(),
                            task1.getDueDate(),
                            task1.getStatus());

        taskService.save(task2);

        fetchedTask = taskService.getById(task1.getId()).orElseThrow();
        assert fetchedTask.getTitle().value().equals("Updated task");

        // List tasks
        var tasks = taskRepository.findAll();
        tasks.forEach(System.out::println);

        // Mark task as complete
        var task3 = Task.builder().title("test task 3").status(Task.Status.PENDING).build();

        taskService.save(task3);
        taskService.markAsCompleted(task3.getId());

        assert taskService.getById(task3.getId())
                .orElseThrow()
                .getStatus()
                .equals(Task.Status.COMPLETED);

        // Delete task
        boolean success = taskService.deleteById(task3.getId());
        assert success && taskService.getById(task3.getId()).isEmpty();
    }
}
