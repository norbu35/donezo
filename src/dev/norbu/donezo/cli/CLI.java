package dev.norbu.donezo.cli;

import dev.norbu.donezo.model.DueDate;
import dev.norbu.donezo.model.Task;
import dev.norbu.donezo.service.TaskManager;
import dev.norbu.donezo.storage.InMemoryTaskRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Scanner;

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
      System.out.print(">");

      String line = scanner.nextLine()
              .trim();
      if (line.isEmpty()) continue;
      if (line.equalsIgnoreCase("exit")) break;

      handleCommand(line);
      System.out.println("Run another command or type 'exit' to quit.");
    }

    scanner.close();
  }

  private static void handleCommand(String input) {
    Command command = CommandParser.parse(input);
    switch (command.name()) {
      case "help" -> showHelp();
      case "add" -> handleAdd(command.args());
      case "list" -> handleList();
      case "complete" -> handleComplete(command.args());
      case "delete" -> handleDelete(command.args());
      default -> System.out.println("Invalid command.");
    }
  }

  private static void showHelp() {
    System.out.format("""
                              The available commands are:
                              'help': This list.
                              'add': Create a new task. <title> [-d <description>] [-due <yyyy-MM-dd>] [-p <priority: LOW | MEDIUM | HIGH>]
                              'list': List all tasks.
                              'complete': Mark a task as complete. [name]
                              'delete': Delete a task. [name]
                              %n
                              """);
  }

  private static void handleAdd(List<String> args) {
    if (args.isEmpty()) {
      System.out.println(
              "Usage: add <title> [-d <desc>] [-p <LOW|MEDIUM|HIGH>] [-due " + "<yyyy-MM-dd>]");
      return;
    }

    String title = args.getFirst();
    String description = "";
    Task.Priority priority;
    priority = Task.Priority.MEDIUM;
    DueDate dueDate = null;

    for (int i = 1; i < args.size(); i++) {
      String flag = args.get(i);
      switch (flag) {
        case "-d":
          if (i + 1 < args.size()) {
            description = args.get(++i);
          } else {
            System.out.println("Warning: Missing value for flag '-d'.");
          }
          break;
        case "-p":
          if (i + 1 < args.size()) {
            priority = getPriority(args.get(++i));
          } else {
            System.out.println("Warning: Missing value for flag '-p'. Using default MEDIUM.");
            priority = Task.Priority.MEDIUM;
          }
          break;
        case "-due":
          if (i + 1 < args.size()) {
            dueDate = new DueDate(getDueDate(args.get(++i)));
          } else {
            System.out.println("Warning: Missing value for flag '-due'. Using default in 3 days");
            dueDate = new DueDate(ZonedDateTime.now()
                                          .plusDays(3));
          }
          break;
        default:
          if (flag.startsWith("-")) {
            System.out.println("Warning: Unknown flag '" + flag + "' ignored.");
          }
          break;
      }
    }

    try {
      Task task = taskManager.addTask(title, description, priority, dueDate);
      System.out.printf("Task added with ID: %s%n", task.getId());
    } catch (Exception e) {
      System.err.println("Error adding task: " + e.getMessage());
    }
  }

  private static void handleList() {
    System.out.println("Your tasks:");
    var tasks = taskManager.listTasks();
    tasks.forEach(System.out::println);
  }

  private static void handleComplete(List<String> args) {
    if (args.isEmpty()) {
      System.out.println("Usage: complete <id>");
    }
    String id = args.getFirst();
    try {
      var count = taskManager.listTasks()
              .stream()
              .filter(task -> task.getTitle()
                      .startsWith(id))
              .map(task -> taskManager.markAsCompleted(task.getId()))
              .count();

      if (count > 0) {
        System.out.format("%d task(s) marked as completed.", count);
      } else {
        System.out.format("No tasks starting with the ID %s found.", id);
      }
    } catch (Exception e) {
      System.err.printf("Error completing task with ID '%s': %s%n", id, e.getMessage());
    }
  }

  private static void handleDelete(List<String> args) {
    String id = args.getFirst();
    var count = taskManager.listTasks()
            .stream()
            .filter(task -> task.getTitle()
                    .startsWith(id))
            .map(task -> taskManager.deleteTask(task.getId()))
            .count();

    if (count > 0) {
      System.out.format("%d task(s) deleted.%n", count);
    } else {
      System.out.format("No tasks starting with the ID %s found.%n", id);
    }
  }

  private static Task.Priority getPriority(String name) {
    try {
      return Task.Priority.valueOf(name);
    } catch (IllegalArgumentException e) {
      System.out.println(
              "Invalid priority. Use 'LOW', 'MEDIUM' or 'HIGH'. Setting to " + "default 'MEDIUM'");
      return Task.Priority.MEDIUM;
    }
  }

  private static ZonedDateTime getDueDate(String date) {
    try {
      return ZonedDateTime.parse(date);
    } catch (Exception e) {
      System.out.println(
              "Invalid date format. Use yyyy-MM-dd. Setting default date in " + "3 days.%n");
      return ZonedDateTime.now()
              .plusDays(3);
    }
  }
}
