package dev.norbu.donezo.cli.command;

import dev.norbu.donezo.cli.exception.InvalidInputException;
import dev.norbu.donezo.cli.exception.TaskNotFoundException;
import dev.norbu.donezo.model.Description;
import dev.norbu.donezo.model.DueDate;
import dev.norbu.donezo.model.Task;
import dev.norbu.donezo.model.Title;
import dev.norbu.donezo.service.TaskService;

import java.time.format.DateTimeParseException;
import java.util.List;

public class Update
        implements Command {

    private final TaskService taskService;

    public Update(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void execute(List<String> args) {
        if (args.isEmpty()) {
            throw new InvalidInputException("No task ID provided.");
        }

        String taskId = args.getFirst();
        var argParser = new ArgParser(args.subList(1, args.size()));
        try {
            var oldTask = taskService.getById(taskId)
                    .orElseThrow(() -> new TaskNotFoundException(taskId));
            Title title = argParser.getValue("-t")
                    .map(Title::new)
                    .orElse(oldTask.getTitle());
            Description description = argParser.getValue("-d")
                    .map(Description::new)
                    .orElse(oldTask.getDescription());
            Task.Priority priority = argParser.getValue("-p")
                    .map(Task.Priority::fromString)
                    .orElse(oldTask.getPriority());
            DueDate dueDate = argParser.getValue("-due")
                    .map(ArgParser::parseDueDate)
                    .orElse(oldTask.getDueDate());
            Task.Status status = argParser.getValue("-s")
                    .map(Task.Status::fromString)
                    .orElse(oldTask.getStatus());

            Task updatedTask =
                    Task.of(oldTask.getId(), title, description, priority, dueDate, status);

            if (!updatedTask.equals(oldTask)) {
                taskService.save(updatedTask);
                System.out.printf("Task '%s' updated.%n", updatedTask.getId());
            } else {
                System.out.printf("Task '%s' not changed. No updates performed.", taskId);
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidInputException("Invalid input provided for one of the fields: ", e);
        } catch (DateTimeParseException e) {
            String failedDateString = e.getParsedString();
            throw new InvalidInputException(String.format(
                    "Invalid date format for: '%s'. Provide yyyy-MM-dd. Reason: %s",
                    failedDateString,
                    e));
        }
    }

    @Override
    public String description() {
        return "Update a task: update <id> [-t <title>] [-d <desc>] [-p <priority>] [-due " +
                "<yyyy-MM-dd>] " + "[-s <status>]";
    }
}
