package dev.norbu.donezo.cli;

import dev.norbu.donezo.model.Description;
import dev.norbu.donezo.model.DueDate;
import dev.norbu.donezo.model.Task;
import dev.norbu.donezo.model.Title;
import dev.norbu.donezo.service.TaskManager;
import dev.norbu.donezo.storage.InMemoryTaskRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class CLI {

    private static final TaskManager taskManager = new TaskManager(new InMemoryTaskRepository());
    private static final Scanner scanner = new Scanner(System.in);

    private CLI() {
    }

    public static void init(String[] args) {
        System.out.println("Donezo CLI");
        System.out.println("Type 'help' to see commands.\n");

        if (args.length > 1) {
            handleCommand(String.join(" ", args));
        }

        while (true) {
            System.out.print("> ");
            String line = scanner
                    .nextLine()
                    .trim();

            if (line.isEmpty()) continue;
            if (line.equalsIgnoreCase("exit")) break;

            handleCommand(line);
            System.out.println("Run another command or type 'exit' to quit.");
        }
    }

    private static void handleCommand(String input) {
        Command command = CommandParser.parse(input);
        switch (command
                .name()
                .toLowerCase()) {
            case "help" -> showHelp();
            case "list" -> handleList();
            case "add" -> handleAdd(command.args());
            case "complete" -> handleComplete(command.args());
            case "update" -> handleUpdate(command.args());
            case "delete" -> handleDelete(command.args());
            default -> System.err.println("Invalid command.");
        }
    }

    private static void showHelp() {
        System.out.format("""
                                  Available commands:
                                  'help'      : Show this list
                                  'add'       : Create a task. <title> [-d <description>] [-due <yyyy-MM-dd>] [-p <LOW|MEDIUM|HIGH>]
                                  'list'      : List all tasks
                                  'complete'  : Mark a task as complete. <id>
                                  'delete'    : Delete a task. <id>
                                  'update'    : Update a task. <id> [-t <title>] [-d <desc>] [-p <priority>] [-due <yyyy-MM-dd>] [-s <status>]
                                  """);
    }

    private static void handleList() {
        System.out.println("\nYour tasks:");
        taskManager
                .listTasks()
                .forEach(System.out::println);
    }

    private static void handleAdd(List<String> args) {
        if (args.isEmpty()) {
            printUsageAdd();
            return;
        }

        var argParser = new ArgParser(args);

        Title title;
        try {
            title = new Title(argParser.getFirst());
        } catch (IllegalArgumentException e) {
            System.err.printf("Invalid title: %s%n", e.getMessage());
            return;
        }

        Description description = argParser
                .getValue("-d")
                .map(Description::new)
                .orElse(new Description(""));
        Task.Priority priority = argParser
                .getValue("-p")
                .map(Task.Priority::fromString)
                .orElse(Task.Priority.MEDIUM);
        DueDate dueDate = argParser
                .getValue("-due")
                .map(CLI::parseDueDate)
                .orElse(defaultDueDate());
        Task.Status status = argParser
                .getValue("-s")
                .map(Task.Status::fromString)
                .orElse(Task.Status.PENDING);

        try {
            Task task = taskManager.addTask(title, description, priority, dueDate, status);
            System.out.printf("Task added with ID: %s%n", task.getId());
        } catch (Exception e) {
            System.err.println("Error adding task: " + e.getMessage());
        }
    }

    private static void handleComplete(List<String> args) {
        if (args.isEmpty()) {
            System.err.println("Usage: complete <id>");
            return;
        }

        String id = args.getFirst();
        try {
            UUID taskId = UUID.fromString(id);
            taskManager
                    .getTask(taskId)
                    .ifPresent(task -> taskManager.markAsCompleted(task.getId()));
        } catch (IllegalArgumentException e) {
            System.err.printf("Invalid UUID: %s%n", id);
        }
    }

    private static void handleUpdate(List<String> args) {
        if (args.size() < 2) {
            printUsageUpdate();
            return;
        }

        UUID taskId;
        Task oldTask;
        try {
            taskId  = UUID.fromString(args.getFirst());
            oldTask = taskManager
                    .getTask(taskId)
                    .orElseThrow();
        } catch (IllegalArgumentException e) {
            System.err.printf("Invalid Task ID: %s%n", args.getFirst());
            return;
        } catch (NoSuchElementException e) {
            System.err.printf("Task with ID '%s' not found.%n", args.getFirst());
            return;
        }

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
                .map(CLI::parseDueDate)
                .orElse(oldTask.getDueDate());
        Task.Status status = argParser
                .getValue("-s")
                .map(Task.Status::fromString)
                .orElse(oldTask.getStatus());

        Task updatedTask = Task.from(taskId, title, description, priority, dueDate, status);

        try {
            taskManager.updateTask(updatedTask);
            System.out.printf("Task with ID %s updated.%n", updatedTask.getId());
        } catch (Exception e) {
            System.err.println("Error updating task: " + e.getMessage());
        }
    }

    private static void handleDelete(List<String> args) {
        if (args.isEmpty()) {
            System.err.println("Usage: delete <id>");
            return;
        }

        String id = args.getFirst();
        long count = taskManager
                .listTasks()
                .stream()
                .filter(task -> task
                        .getTitleValue()
                        .startsWith(id))
                .map(task -> taskManager.deleteTask(task.getId()))
                .count();

        if (count > 0) {
            System.out.printf("%d task(s) deleted.%n", count);
        } else {
            System.out.printf("No tasks starting with '%s' found.%n", id);
        }
    }

    private static DueDate parseDueDate(String date) {
        try {
            return new DueDate(LocalDate
                                       .parse(date)
                                       .atStartOfDay(ZoneId.systemDefault()));
        } catch (DateTimeParseException e) {
            System.err.printf("Invalid date format: %s. Defaulting due date in 3 days.%n", date);
            return defaultDueDate();
        }
    }

    private static DueDate defaultDueDate() {
        return new DueDate(ZonedDateTime
                                   .now()
                                   .plusDays(3));
    }

    private static void printUsageAdd() {
        System.out.println(
                "Usage: add <title> [-d <description>] [-due <yyyy-MM-dd>] [-p <priority>] [-s " +
                        "<status>]");
    }

    private static void printUsageUpdate() {
        System.out.println(
                "Usage: update <id> [-t <title>] [-d <desc>] [-p <priority>] [-due <yyyy-MM-dd>] " +
                        "[-s <status>]");
    }

    private static class ArgParser {

        private final Map<String, String> options = new HashMap<>();
        private final String first;

        ArgParser(List<String> args) {
            if (args.isEmpty()) {
                throw new IllegalArgumentException("No arguments provided.");
            }
            first = args.getFirst();

            for (int i = 1; i < args.size(); i++) {
                String key = args.get(i);
                if (key.startsWith("-") && i + 1 < args.size() && !args
                        .get(i + 1)
                        .startsWith("-")) {
                    options.put(key, args.get(i + 1));
                    i++;
                }
            }
        }

        String getFirst() {
            return first;
        }

        Optional<String> getValue(String flag) {
            return Optional.ofNullable(options.get(flag));
        }
    }
}
