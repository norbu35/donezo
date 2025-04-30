package dev.norbu.donezo.cli.command;

import dev.norbu.donezo.model.DueDate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

class ArgParser {

    private final Map<String, String> options = new HashMap<>();
    private final String first;

    ArgParser(final List<String> args) {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("No arguments provided.");
        }
        first = args.getFirst();
        Objects.requireNonNull(first, "Command identifier cannot be null.");

        for (int i = 1; i < args.size(); i++) {
            final String key = args.get(i);
            if (key.startsWith("-")
                    && i + 1 < args.size()
                    && !args.get(i + 1).startsWith("-")) {
                options.put(key, args.get(i + 1));
                i++;
            }
        }
    }

    public static DueDate parseDueDate(String date) {
        final LocalTime localTime = LocalTime.now();
        final LocalDate localDate = LocalDate.parse(date);
        final ZoneId zoneId = ZoneId.systemDefault();
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(localDate, localTime, zoneId);

        return new DueDate(zonedDateTime);
    }

    String getFirst() {
        return first;
    }

    Optional<String> getValue(final String flag) {
        return Optional.ofNullable(options.get(flag));
    }
}
