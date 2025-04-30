package dev.norbu.donezo.cli.command;

import java.util.List;
import java.util.Map;

public class Help
        implements Command {

    private final Map<String, Command> commands;

    public Help(final Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public void execute(final List<String> args) {
        System.out.println("Available commands:\n");
        commands
                .values()
                .forEach(command -> System.out.println(command.description()));
    }

    @Override
    public String description() {
        return "See available commands: help";
    }
}
