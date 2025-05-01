package dev.norbu.donezo.cli.command;

import dev.norbu.donezo.cli.exception.InvalidInputException;
import dev.norbu.donezo.model.Description;
import dev.norbu.donezo.model.DueDate;
import dev.norbu.donezo.model.Task;
import dev.norbu.donezo.model.Title;
import dev.norbu.donezo.service.TaskService;

import java.time.format.DateTimeParseException;
import java.util.List;

public class Add
        implements Command {

    private final TaskService taskService;

    public Add(final TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void execute(final List<String> args) {
        if (args.isEmpty()) {
            throw new InvalidInputException("No fields for a new task provided.");
        }

        final var argParser = new ArgParser(args);

        try {
            final Title title = Title.of(argParser.getFirst());
            final Description taskDescription = argParser.getValue(Constants.DESCRIPTION_FLAG)
                    .map(Description::new)
                    .orElse(Description.empty());
            final Task.Priority priority = argParser.getValue(Constants.PRIORITY_FLAG)
                    .map(Task.Priority::fromString)
                    .orElse(Task.Priority.MEDIUM);
            final DueDate dueDate = argParser.getValue(Constants.DUE_FLAG)
                    .map(ArgParser::parseDueDate)
                    .orElse(DueDate.inDays(3));
            final Task.Status status = argParser.getValue(Constants.STATUS_FLAG)
                    .map(Task.Status::fromString)
                    .orElse(Task.Status.PENDING);

            final Task task = Task.builder(title)
                    .description(taskDescription)
                    .priority(priority)
                    .dueDate(dueDate)
                    .status(status)
                    .build();
            taskService.save(task);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidInputException("Invalid input provided", e);
        } catch (DateTimeParseException e) {
            String failedString = e.getParsedString();
            throw new InvalidInputException(
                    String.format("Invalid date provided: '%s'. Expected " + "YYYY-MM-DD.",
                                  failedString),
                    e);
        }
    }

    @Override
    public String description() {
        return "Add a new task: add <title> [-d <description>] [-due <yyyy-MM-dd>] [-p " +
                "<priority>] [-s <status>]";
    }
}
