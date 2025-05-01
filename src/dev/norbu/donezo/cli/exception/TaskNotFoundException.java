package dev.norbu.donezo.cli.exception;

public class TaskNotFoundException
        extends TaskManagementException {

    public TaskNotFoundException(String taskId) {
        super(String.format("Task '%s' not found.", taskId));
    }
}
