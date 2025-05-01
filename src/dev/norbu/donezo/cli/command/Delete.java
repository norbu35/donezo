package dev.norbu.donezo.cli.command;

import dev.norbu.donezo.cli.exception.InvalidInputException;
import dev.norbu.donezo.service.TaskService;

import java.util.List;

public class Delete
        implements Command {

    private final TaskService taskService;

    public Delete(final TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void execute(final List<String> args) {
        if (args.isEmpty()) {
            throw new InvalidInputException("No task ID provided.");
        }
        final String id = args.getFirst();
        taskService.deleteById(id);
        System.out.printf("Task '%s' deleted.", id);
    }

    @Override
    public String description() {
        return "Delete a task: delete <id>";
    }
}
