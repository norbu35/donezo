package dev.norbu.donezo.model;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Task {

  private final UUID id;
  private final Title title;
  private final Description description;
  private final DueDate dueDate;
  private final Status status;
  private final Priority priority;

  private Task(Builder builder) {
    this(UUID.randomUUID(),
         builder.title,
         builder.description,
         builder.priority,
         builder.dueDate,
         builder.status);
  }

  private Task(UUID id,
               Title title,
               Description description,
               Priority priority,
               DueDate dueDate,
               Status status) {
    this.id          = Objects.requireNonNull(id, "id cannot be null");
    this.title       = Objects.requireNonNull(title, "title cannot be null");
    this.description = Objects.requireNonNull(description, "description cannot be null");
    this.priority    = Objects.requireNonNull(priority, "priority cannot be null");
    this.dueDate     = Objects.requireNonNull(dueDate, "dueDate cannot be null");
    this.status      = Objects.requireNonNull(status, "status cannot be null");
  }

  public Task markAsCompleted() {
    return withStatus(Status.COMPLETED);
  }

  public Task withStatus(Status status) {
    if (this.status == status) {
      return this;
    }

    return new Task(this.id, this.title, this.description, this.priority, this.dueDate, status);
  }

  public Status getStatus() {
    return status;
  }

  public boolean isOverdue() {
    return status == Status.PENDING && dueDate != null && getDueDate()
            .value()
            .isBefore(ZonedDateTime.now());
  }

  public DueDate getDueDate() {
    return dueDate;
  }

  public UUID getId() {
    return id;
  }

  public Title getTitle() {
    return title;
  }

  public String getTitleValue() {
    return title.value();
  }

  public Description getDescription() {
    return description;
  }

  public String getDescriptionValue() {
    return description.value();
  }

  public Priority getPriority() {
    return priority;
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
                                 """, title, description, priority, dueDate.value(), status);
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
    private DueDate dueDate = new DueDate(ZonedDateTime
                                                  .now()
                                                  .plusDays(3));
    private Status status = Status.PENDING;

    public Builder title(Title title) {
      this.title = title;
      return this;
    }

    public Builder title(String title) {
      this.title = new Title(title);
      return this;
    }

    public Builder description(Description description) {
      this.description = description;
      return this;
    }

    public Builder description(String description) {
      this.description = new Description(description);
      return this;
    }

    public Builder priority(Priority priority) {
      this.priority = priority;
      return this;
    }

    public Builder dueDate(DueDate dueDate) {
      this.dueDate = dueDate;
      return this;
    }

    public Builder status(Status status) {
      this.status = status;
      return this;
    }

    public Task build() {
      return new Task(this);
    }
  }
}
