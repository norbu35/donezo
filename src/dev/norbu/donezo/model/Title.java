package dev.norbu.donezo.model;

public record Title(String value) {

    public Title {
        value = value.strip();
        if (value.isBlank()) {
            throw new IllegalArgumentException("Task title must not be blank.");
        }
    }

    public static Title of(String value) {
        return new Title(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
