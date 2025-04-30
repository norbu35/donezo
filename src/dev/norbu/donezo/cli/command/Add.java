package dev.norbu.donezo.cli.command;

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

    public Add(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void execute(List<String> args) {
        try {
            var argParser = new ArgParser(args);

            Title title = Title.of(argParser.getFirst());
            Description taskDescription = argParser
                    .getValue(Constants.DESCRIPTION_FLAG)
                    .map(Description::new)
                    .orElse(Description.of(""));
            Task.Priority priority = argParser
                    .getValue(Constants.PRIORITY_FLAG)
                    .map(Task.Priority::fromString)
                    .orElse(Task.Priority.MEDIUM);
            DueDate dueDate = argParser
                    .getValue(Constants.DUE_FLAG)
                    .map(ArgParser::parseDueDate)
                    .orElse(DueDate.inDays(3));
            Task.Status status = argParser
                    .getValue(Constants.STATUS_FLAG)
                    .map(Task.Status::fromString)
                    .orElse(Task.Status.PENDING);

            Task task =
                    Task.builder()
                            .title(title)
                            .description(taskDescription)
                            .priority(priority)
                            .dueDate(dueDate)
                            .status(status)
                            .build();
            taskService.save(task);
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("Invalid input: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date input: " + e.getMessage() + "\nUsage: yyyy-MM-dd.");
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
