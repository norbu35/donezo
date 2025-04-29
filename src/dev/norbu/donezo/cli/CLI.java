package dev.norbu.donezo.cli;

import dev.norbu.donezo.cli.command.Add;
import dev.norbu.donezo.cli.command.Command;
import dev.norbu.donezo.cli.command.Complete;
import dev.norbu.donezo.cli.command.Delete;
import dev.norbu.donezo.cli.command.Help;
import dev.norbu.donezo.cli.command.ListTasks;
import dev.norbu.donezo.cli.command.Parser;
import dev.norbu.donezo.cli.command.Update;
import dev.norbu.donezo.service.TaskManager;
import dev.norbu.donezo.storage.InMemoryTaskRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CLI {

    private static final TaskManager taskManager = new TaskManager(new InMemoryTaskRepository());
    private static final Scanner scanner = new Scanner(System.in);
    private static final Map<String, Command> commands = new HashMap<>();

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
            String line = scanner
                    .nextLine()
                    .trim();

            if (line.isEmpty()) continue;
            if (line.equalsIgnoreCase("exit")) break;

            handleCommand(line);
            System.out.println("Run another command or type 'exit' to quit.");
        }

        scanner.close();
    }

    private static void registerCommands() {
        commands.put(Constants.HELP_COMMAND, new Help(commands));
        commands.put(Constants.LIST_COMMAND, new ListTasks(taskManager));
        commands.put(Constants.ADD_COMMAND, new Add(taskManager));
        commands.put(Constants.COMPLETE_COMMAND, new Complete(taskManager));
        commands.put(Constants.UPDATE_COMMAND, new Update(taskManager));
        commands.put(Constants.DELETE_COMMAND, new Delete(taskManager));
    }

    private static void handleCommand(String input) {
        Parser.Result parsedCommand = Parser.parse(input);
        Command command = commands.get(parsedCommand.name());
        if (command != null) {
            commands
                    .get(parsedCommand.name())
                    .execute(parsedCommand.args());
        } else {
            System.err.println("Invalid command. Type 'help' to see available commands.");
        }
    }
}
