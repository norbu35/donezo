package dev.norbu.donezo.cli.command;

import dev.norbu.donezo.service.TaskManager;

public class ListTasks
        implements Command {

    private final TaskManager taskManager;

    public ListTasks(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void execute(java.util.List<String> args) {
        System.out.println("Your tasks:\n");
        taskManager
                .listTasks()
                .forEach(System.out::println);
    }

    @Override
    public String description() {
        return "List all tasks: list";
    }
}
