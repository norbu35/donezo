package dev.norbu.donezo.model;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

public class Task {

  private final UUID id;
  private final Title title;
  private final Description description;
  private final ZonedDateTime dueDate;
  private final Status status;
  private final Priority priority;

  private Task(Builder builder) {
    this.id          = UUID.randomUUID();
    this.title       = builder.title;
    this.description = builder.description;
    this.dueDate     = builder.dueDate.value();
    this.priority    = builder.priority;
    this.status      = builder.status;
  }

  public static Task of(String title, String description) {
    return new Builder().title(title)
            .description(description)
            .build();
  }

  public static Task of(Title title,
                        Description description,
                        Priority priority,
                        DueDate dueDate,
                        Status status) {
    return new Builder()
            .title(title)
            .description(description)
            .priority(priority)
            .dueDate(dueDate)
            .status(status)
            .build();
  }

  public static Task of(String title,
                        String description,
                        Priority priority,
                        DueDate dueDate,
                        Status status) {
    return new Builder()
            .title(title)
            .description(description)
            .priority(priority)
            .dueDate(dueDate)
            .status(status)
            .build();
  }

  public UUID getId() {
    return id;
  }

  public String getTitle() {
    return title.value();
  }

  public String getDescription() {
    return description.value();
  }

  public Status getStatus() {
    return status;
  }

  public Priority getPriority() {
    return priority;
  }

  public boolean isOverdue() {
    return status == Status.PENDING && dueDate != null &&
            getDueDate().isBefore(ZonedDateTime.now());
  }

  public ZonedDateTime getDueDate() {
    return dueDate;
  }

  public void markAsCompleted() {

  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Task task)) return false;
    return id.equals(task.id);
  }

  @Override
  public String toString() {
    return String.format("""
                                 Title: %s
                                 Description: %s
                                 Priority: %s
                                 Due date: %tF
                                 Status: %s
                                 """, title, description, priority, dueDate, status);
  }

  public enum Status {
    PENDING,
    COMPLETED,
  }

  public enum Priority {
    LOW,
    MEDIUM,
    HIGH,
  }

  public static class Builder {

    private Title title;
    private Description description;
    private Priority priority = Priority.MEDIUM;
    private DueDate dueDate = new DueDate(ZonedDateTime.now()
                                                  .plusDays(3));
    private Status status = Status.PENDING;

    public Builder title(String title) {
      this.title = new Title(title);
      return this;
    }

    public Builder title(Title title) {
      this.title = new Title(title.value());
      return this;
    }

    public Builder description(String description) {
      if (description == null || description.isBlank()) {
        this.description = new Description("");
      }
      this.description = new Description(description);
      return this;
    }

    public Builder description(Description description) {
      this.description = new Description(description.value());
      return this;
    }

    public Builder priority(Priority priority) {
      this.priority = priority;
      return this;
    }

    public Builder priority(String priority) {
      this.priority = Priority.valueOf(priority);
      return this;
    }

    public Builder dueDate(DueDate dueDate) {
      this.dueDate = new DueDate(dueDate.value());
      return this;
    }

    public Builder dueDate(String dueDate) {
      this.dueDate = new DueDate(ZonedDateTime.parse(dueDate));
      return this;
    }

    public Builder status(Status status) {
      this.status = status;
      return this;
    }

    public Builder status(String status) {
      this.status = Status.valueOf(status);
      return this;
    }

    public Task build() {
      return new Task(this);
    }
  }
}
