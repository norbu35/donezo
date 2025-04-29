package dev.norbu.donezo.cli.command;

import dev.norbu.donezo.service.TaskManager;

import java.util.List;
import java.util.NoSuchElementException;

public class Complete
        implements Command {

    private final TaskManager taskManager;

    public Complete(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    /**
     * @param args A List of passed in arguments.
     */
    @Override
    public void execute(List<String> args) {
        String id = args.getFirst();
        try {
            taskManager.markAsCompleted(id);
        } catch (IllegalArgumentException e) {
            System.err.printf("Invalid task ID: %s", id);
        } catch (NoSuchElementException e) {
            System.err.printf("No task with ID: %s found.", id);
        } catch (Exception e) {
            System.err.printf("Unexpected error while marking task as complete: %s",
                              e.getMessage());
        }
    }

    /**
     * @return A String describing the command.
     */
    @Override
    public String description() {
        return "Mark a task as completed: complete <id>";
    }
}
