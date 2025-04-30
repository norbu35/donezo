package dev.norbu.donezo.cli.command;

import dev.norbu.donezo.service.TaskService;

import java.util.List;
import java.util.NoSuchElementException;

public class Complete
        implements Command {

    private final TaskService taskService;

    public Complete(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void execute(List<String> args) {
        String id = args.getFirst();
        try {
            if (!taskService.markAsCompleted(id)) {
                throw new RuntimeException("Marking as complete failed.");
            }
        } catch (IllegalArgumentException _) {
            System.err.printf("Invalid task ID: %s", id);
        } catch (NoSuchElementException _) {
            System.err.printf("No task with ID: %s found.", id);
        } catch (Exception e) {
            System.err.printf("Unexpected error while marking task as complete: %s",
                              e.getMessage());
        }
    }

    @Override
    public String description() {
        return "Mark a task as completed: complete <id>";
    }
}
