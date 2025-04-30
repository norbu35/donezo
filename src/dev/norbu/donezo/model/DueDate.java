package dev.norbu.donezo.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public record DueDate(ZonedDateTime value) {

    public DueDate {
        try {
            if (value.isBefore(ZonedDateTime.now())) {
                throw new IllegalArgumentException("Due date needs to be in the future.");
            }
        } catch (IllegalArgumentException e) {
            System.err.printf("Invalid Date: %s", e.getMessage());
            throw new IllegalArgumentException();
        }
    }

    public static DueDate of(ZonedDateTime value) {
        return new DueDate(value);
    }

    public static DueDate of(LocalDate localDate) {
        ZonedDateTime zonedDateTime;
        var localTime = LocalTime.now();
        var zoneId = ZoneId.systemDefault();
        zonedDateTime = ZonedDateTime.of(localDate, localTime, zoneId);

        return new DueDate(zonedDateTime);
    }

    public static DueDate of(String value) {
        ZonedDateTime zonedDateTime;
        var parsedLocalDate = LocalDate.parse(value);
        var localTime = LocalTime.now();
        var zoneId = ZoneId.systemDefault();
        zonedDateTime = ZonedDateTime.of(parsedLocalDate, localTime, zoneId);

        return new DueDate(zonedDateTime);
    }

    public static DueDate inDays(int days) {
        var zonedDateTime = ZonedDateTime.now().plusDays(days);

        return new DueDate(zonedDateTime);
    }
}
