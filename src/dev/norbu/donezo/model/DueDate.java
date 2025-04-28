package dev.norbu.donezo.model;

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
}
