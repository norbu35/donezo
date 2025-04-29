package dev.norbu.donezo.cli.command;

import dev.norbu.donezo.service.TaskManager;

import java.util.List;
import java.util.NoSuchElementException;

public class Delete
        implements Command {

    private final TaskManager taskManager;

    public Delete(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void execute(List<String> args) {
        try {
            String id = args.getFirst();
            taskManager.deleteTask(id);
        } catch (IllegalArgumentException e) {
            System.err.printf("Invalid task ID: %s", args.getFirst());
        } catch (NoSuchElementException e) {
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
