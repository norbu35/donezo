package dev.norbu.donezo.cli.command;

import java.util.List;

public interface Command {

    void execute(List<String> args);

    String description();
}
