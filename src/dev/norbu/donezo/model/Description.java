package dev.norbu.donezo.model;

public record Description(String value) {

    public Description {
        value = value.strip();
        if (value.length() > 255) {
            throw new IllegalArgumentException("Description too long (max 255 characters).");
        }
    }

    public static Description of(String value) {
        return new Description(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
