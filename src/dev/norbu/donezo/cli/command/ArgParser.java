package dev.norbu.donezo.cli.command;

import dev.norbu.donezo.model.DueDate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class ArgParser {

    private final Map<String, String> options = new HashMap<>();
    private final String first;

    ArgParser(List<String> args) {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("No arguments provided.");
        }
        first = args.getFirst();

        for (int i = 1; i < args.size(); i++) {
            String key = args.get(i);
            if (key.startsWith("-")
                    && i + 1 < args.size()
                    && !args.get(i + 1).startsWith("-")) {
                options.put(key, args.get(i + 1));
                i++;
            }
        }
    }

    public static DueDate parseDueDate(String date) {
        LocalTime localTime = LocalTime.now();
        LocalDate localDate = LocalDate.parse(date);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDate, localTime, zoneId);

        return new DueDate(zonedDateTime);
    }

    String getFirst() {
        return first;
    }

    Optional<String> getValue(String flag) {
        return Optional.ofNullable(options.get(flag));
    }
}
