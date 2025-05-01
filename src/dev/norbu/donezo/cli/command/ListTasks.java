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
        var tasks = taskService.findAll();
        if (tasks.isEmpty()) {
            System.out.println("You have no tasks yet.");
        } else {
            System.out.println("Your tasks:\n");
            tasks.forEach(System.out::println);
        }
    }

    @Override
    public String description() {
        return "List all tasks: list";
    }
}
