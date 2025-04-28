package dev.norbu.donezo.model;

public record Description(String value) {

  public Description {
    if (value.isBlank()) {
      value = "";
    } else {
      value = value.strip();
      if (value.length() > 255) {
        throw new IllegalArgumentException("Description too long (max 255 characters).");
      }
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
