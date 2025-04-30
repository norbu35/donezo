package dev.norbu.donezo.cli.command;

import dev.norbu.donezo.service.TaskService;

import java.util.List;

public class ListTasks
        implements Command {

    private final TaskService taskService;

    public ListTasks(final TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void execute(final List<String> args) {
        System.out.println("Your tasks:\n");
        taskService.findAll()
                .forEach(System.out::println);
    }

    @Override
    public String description() {
        return "List all tasks: list";
    }
}
