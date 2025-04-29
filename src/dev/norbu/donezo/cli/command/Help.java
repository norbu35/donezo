package dev.norbu.donezo.cli.command;

import java.util.List;
import java.util.Map;

public class Help
        implements Command {

    private final Map<String, Command> commands;

    public Help(Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public void execute(List<String> args) {
        System.out.println("Available commands:\n");
        commands
                .values()
                .forEach(System.out::println);
    }

    @Override
    public String description() {
        return "See available commands: help";
    }
}
