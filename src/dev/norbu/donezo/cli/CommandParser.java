package dev.norbu.donezo.cli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommandParser {

  private static final Pattern TOKEN_PATTERN =
          // Matches the content between double quotes or non-quoted words
          Pattern.compile("\"([^\"]*)\"|([^\\s\"]+)");

  private CommandParser() {
  }

  public static Command parse(String input) {
    Matcher matcher = TOKEN_PATTERN.matcher(input.trim());
    List<String> tokens = new ArrayList<>();

    while (matcher.find()) {
      String quoted = matcher.group(1);
      String unquoted = matcher.group(2);
      tokens.add(quoted != null
                         ? quoted
                         : unquoted);
    }

    if (tokens.isEmpty()) {
      return new Command("", Collections.emptyList());
    }

    String name = tokens.getFirst();
    return new Command(name, tokens.subList(1, tokens.size()));
  }
}
