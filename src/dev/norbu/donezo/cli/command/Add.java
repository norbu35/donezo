package dev.norbu.donezo.cli.command;

import dev.norbu.donezo.model.Description;
import dev.norbu.donezo.model.DueDate;
import dev.norbu.donezo.model.Task;
import dev.norbu.donezo.model.Title;
import dev.norbu.donezo.service.TaskManager;

import java.time.ZonedDateTime;
import java.util.List;

public class Add
        implements Command {

    private final TaskManager taskManager;

    public Add(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void execute(List<String> args) {
        try {
            var argParser = new ArgParser(args);

            Title title;
            title = new Title(argParser.getFirst());
            Description taskDescription = argParser
                    .getValue(Constants.DESCRIPTION_FLAG)
                    .map(Description::new)
                    .orElse(new Description(""));
            Task.Priority priority = argParser
                    .getValue(Constants.PRIORITY_FLAG)
                    .map(Task.Priority::fromString)
                    .orElse(Task.Priority.MEDIUM);
            DueDate dueDate = argParser
                    .getValue(Constants.DUE_FLAG)
                    .map(ArgParser::parseDueDate)
                    .orElse(new DueDate(ZonedDateTime
                                                .now()
                                                .plusDays(3)));
            Task.Status status = argParser
                    .getValue(Constants.STATUS_FLAG)
                    .map(Task.Status::fromString)
                    .orElse(Task.Status.PENDING);

            taskManager.addTask(title, taskDescription, priority, dueDate, status);
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            System.err.printf("Unexpected error while adding task: %s.", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String description() {
        return "Add a new task: add <title> [-d <description>] [-due <yyyy-MM-dd>] [-p " +
                "<priority>] [-s <status>]";
    }
}
