package dev.norbu.donezo.cli.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Parser {

    private static final Pattern TOKEN_PATTERN =
            // Matches the content between double quotes or non-quoted words
            Pattern.compile("\"([^\"]*)\"|([^\\s\"]+)");

    public static Result parse(final String input) {
        final Matcher matcher = TOKEN_PATTERN.matcher(input.trim());
        final List<String> tokens = new ArrayList<>();

        while (matcher.find()) {
            String quoted = matcher.group(1);
            String unquoted = matcher.group(2);
            tokens.add(quoted != null
                               ? quoted
                               : unquoted);
        }

        if (tokens.isEmpty()) {
            return new Result("", Collections.emptyList());
        }

        String name = tokens.getFirst();
        return new Result(name.toLowerCase(), tokens.subList(1, tokens.size()));
    }

    public record Result(String name, List<String> args) {

    }
}
