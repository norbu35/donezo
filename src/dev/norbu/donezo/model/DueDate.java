package dev.norbu.donezo.model;

import java.time.ZonedDateTime;

public record DueDate(ZonedDateTime value) {

  public DueDate {
    if (value.isBefore(ZonedDateTime.now())) {
      throw new IllegalArgumentException("Due date needs to be in the future.");
    }
  }
}
