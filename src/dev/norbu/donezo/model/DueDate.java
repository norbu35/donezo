package dev.norbu.donezo.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

public record DueDate(ZonedDateTime value) {

    public DueDate {
        Objects.requireNonNull(value, "Date value must not be null.");
        if (value.isBefore(ZonedDateTime.now())) {
            throw new IllegalArgumentException("Due date needs to be in the future.");
        }
    }

    public static DueDate of(final ZonedDateTime value) {
        return new DueDate(value);
    }

    public static DueDate of(final LocalDate localDate) {
        final LocalTime localTime = LocalTime.now();
        final ZoneId zoneId = ZoneId.systemDefault();
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(localDate, localTime, zoneId);

        return new DueDate(zonedDateTime);
    }

    public static DueDate of(final String value) {
        final LocalDate parsedLocalDate = LocalDate.parse(value);
        final LocalTime localTime = LocalTime.now();
        final ZoneId zoneId = ZoneId.systemDefault();
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(parsedLocalDate, localTime, zoneId);

        return new DueDate(zonedDateTime);
    }

    public static DueDate inDays(final int days) {
        final ZonedDateTime zonedDateTime = ZonedDateTime.now().plusDays(days);

        return new DueDate(zonedDateTime);
    }
}
