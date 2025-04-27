package dev.norbu.donezo.cli;

import java.util.List;

public record Command(String name, List<String> args) {

}
