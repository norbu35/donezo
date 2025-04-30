package dev.norbu.donezo.model;

import java.util.Objects;

public record Title(String value) {

    public Title {
        Objects.requireNonNull(value, "Title value cannot be null.");
        value = value.strip();
        if (value.isBlank()) {
            throw new IllegalArgumentException("Task title must not be blank.");
        }
        if (value.length() > 100) {
            throw new IllegalArgumentException("Title too long (max 100 characters).");
        }
    }

    public static Title of(final String value) {
        return new Title(value);
    }
}
