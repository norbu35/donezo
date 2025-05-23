package dev.norbu.donezo.cli;

import dev.norbu.donezo.cli.command.Add;
import dev.norbu.donezo.cli.command.Command;
import dev.norbu.donezo.cli.command.Complete;
import dev.norbu.donezo.cli.command.Delete;
import dev.norbu.donezo.cli.command.Help;
import dev.norbu.donezo.cli.command.ListTasks;
import dev.norbu.donezo.cli.command.Parser;
import dev.norbu.donezo.cli.command.Update;
import dev.norbu.donezo.cli.exception.InvalidInputException;
import dev.norbu.donezo.cli.exception.TaskNotFoundException;
import dev.norbu.donezo.service.TaskService;
import dev.norbu.donezo.storage.InMemoryTaskRepository;

import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.SequencedMap;

public final class CLI {

    private static final TaskService taskService = new TaskService(new InMemoryTaskRepository());
    private static final Scanner scanner = new Scanner(System.in);
    private static final SequencedMap<String, Command> commands = new LinkedHashMap<>();

    private CLI() {
    }

    public static void init(String[] args) {
        registerCommands();

        System.out.println("Donezo CLI");
        System.out.println("Type 'help' to see commands.\n");

        if (args.length > 1) {
            handleCommand(String.join(" ", args));
        }

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();

            if (line.isBlank()) continue;
            if (line.equalsIgnoreCase("exit")) break;

            try {
                handleCommand(line);
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }
            System.out.println("Run another command or type 'exit' to quit.");
        }
        scanner.close();
    }

    private static void registerCommands() {
        commands.put(Constants.HELP_COMMAND, new Help(commands));
        commands.put(Constants.LIST_COMMAND, new ListTasks(taskService));
        commands.put(Constants.ADD_COMMAND, new Add(taskService));
        commands.put(Constants.COMPLETE_COMMAND, new Complete(taskService));
        commands.put(Constants.UPDATE_COMMAND, new Update(taskService));
        commands.put(Constants.DELETE_COMMAND, new Delete(taskService));
    }

    private static void handleCommand(final String input) {
        try {
            final Parser.Result parsedCommand = Parser.parse(input);

            if (parsedCommand.name().isEmpty() && parsedCommand.args().isEmpty()) {
                System.out.println("Blank commands given.");
            }

            final Command command = commands.get(parsedCommand.name());

            if (command != null) {
                commands.get(parsedCommand.name()).execute(parsedCommand.args());
            } else {
                System.err.println("Invalid command. Type 'help' to see available commands.");
            }
        } catch (InvalidInputException | TaskNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
        }
    }
}
