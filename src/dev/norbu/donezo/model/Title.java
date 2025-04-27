package dev.norbu.donezo.model;

public record Title(String value) {

  public Title {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("Task title must not be blank.");
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
