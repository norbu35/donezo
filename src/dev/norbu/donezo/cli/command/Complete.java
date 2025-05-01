package dev.norbu.donezo.cli.command;

import dev.norbu.donezo.cli.exception.InvalidInputException;
import dev.norbu.donezo.service.TaskService;

import java.util.List;

public class Complete
        implements Command {

    private final TaskService taskService;

    public Complete(final TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void execute(final List<String> args) {
        if (args.isEmpty()) {
            throw new InvalidInputException("No task ID provided.");
        }
        final String id = args.getFirst();
        taskService.markAsCompleted(id);
        System.out.printf("Task '%s' marked as completed.", id);
    }

    @Override
    public String description() {
        return "Mark a task as completed: complete <id>";
    }
}
