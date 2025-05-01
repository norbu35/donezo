package dev.norbu.donezo.model;

public record Description(String value) {

    private static final Description EMPTY = new Description("");

    public Description {
        value = value.strip();
        if (value.length() > 255) {
            throw new IllegalArgumentException("Description too long (max 255 characters).");
        }
    }

    public static Description of(final String value) {
        if (value == null || value.isBlank()) {
            return EMPTY;
        }
        return new Description(value);
    }

    public static Description empty() {
        return EMPTY;
    }
}
