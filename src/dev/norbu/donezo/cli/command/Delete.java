package dev.norbu.donezo.cli.command;

import dev.norbu.donezo.service.TaskService;

import java.util.List;
import java.util.NoSuchElementException;

public class Delete
        implements Command {

    private final TaskService taskService;

    public Delete(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void execute(List<String> args) {
        try {
            String id = args.getFirst();
            taskService.deleteById(id);
        } catch (IllegalArgumentException _) {
            System.err.printf("Invalid task ID: %s", args.getFirst());
        } catch (NoSuchElementException _) {
            System.err.printf("No task with ID: %s found.", args.getFirst());
        } catch (Exception e) {
            System.err.printf("Unexpected error while deleting task: %s", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String description() {
        return "Delete a task: delete <id>";
    }
}
