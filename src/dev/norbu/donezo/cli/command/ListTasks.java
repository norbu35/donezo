package dev.norbu.donezo.cli.command;

import dev.norbu.donezo.service.TaskService;

public class ListTasks
        implements Command {

    private final TaskService taskService;

    public ListTasks(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void execute(java.util.List<String> args) {
        System.out.println("Your tasks:\n");
        taskService.findAll()
                .forEach(System.out::println);
    }

    @Override
    public String description() {
        return "List all tasks: list";
    }
}
