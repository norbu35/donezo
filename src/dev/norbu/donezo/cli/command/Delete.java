package dev.norbu.donezo.cli.command;

import dev.norbu.donezo.service.TaskService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Delete
        implements Command {

    private final TaskService taskService;

    public Delete(final TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void execute(final List<String> args) {
        Objects.requireNonNull(args);
        try {
            final String id = args.getFirst();
            taskService.deleteById(id);
        } catch (IllegalArgumentException _) {
            System.err.printf("Invalid task ID: %s", args.getFirst());
        } catch (NoSuchElementException _) {
            System.err.printf("No task with ID: %s found.", args.getFirst());
        }
    }

    @Override
    public String description() {
        return "Delete a task: delete <id>";
    }
}
