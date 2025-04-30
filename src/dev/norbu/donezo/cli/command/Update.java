package dev.norbu.donezo.cli.command;

import dev.norbu.donezo.model.Description;
import dev.norbu.donezo.model.DueDate;
import dev.norbu.donezo.model.Task;
import dev.norbu.donezo.model.Title;
import dev.norbu.donezo.service.TaskService;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;

public class Update
        implements Command {

    private final TaskService taskService;

    public Update(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void execute(List<String> args) {
        try {
            String id = args.getFirst();
            Task oldTask = taskService
                    .getById(id)
                    .orElseThrow();

            var argParser = new ArgParser(args.subList(1, args.size()));

            Title title = argParser
                    .getValue("-t")
                    .map(Title::new)
                    .orElse(oldTask.getTitle());
            Description description = argParser
                    .getValue("-d")
                    .map(Description::new)
                    .orElse(oldTask.getDescription());
            Task.Priority priority = argParser
                    .getValue("-p")
                    .map(Task.Priority::fromString)
                    .orElse(oldTask.getPriority());
            DueDate dueDate = argParser
                    .getValue("-due")
                    .map(ArgParser::parseDueDate)
                    .orElse(oldTask.getDueDate());
            Task.Status status = argParser
                    .getValue("-s")
                    .map(Task.Status::fromString)
                    .orElse(oldTask.getStatus());

            Task updatedTask =
                    Task.of(oldTask.getId(),
                            title,
                            description,
                            priority,
                            dueDate,
                            status);

            taskService.save(updatedTask);
            System.out.printf("Task with ID %s updated.%n", updatedTask.getId());
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.printf("Invalid input: %s", e.getMessage());
        } catch (NoSuchElementException _) {
            System.err.printf("Task with id %s not found", args.getFirst());
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date input: " + e.getMessage() + "\nUsage: yyyy-MM-dd.");
        }
    }

    @Override
    public String description() {
        return "Update a task: update <id> [-t <title>] [-d <desc>] [-p <priority>] [-due " +
                "<yyyy-MM-dd>] " + "[-s <status>]";
    }
}
