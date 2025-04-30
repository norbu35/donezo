package dev.norbu.donezo.model;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Task {

    private final String id;
    private final Title title;
    private final Description description;
    private final DueDate dueDate;
    private final Status status;
    private final Priority priority;

    private Task(final Builder builder) {
        this(UUID.randomUUID().toString(),
             builder.title,
             builder.description,
             builder.priority,
             builder.dueDate,
             builder.status);
    }

    private Task(final String id,
                 final Title title,
                 final Description description,
                 final Priority priority,
                 final DueDate dueDate,
                 final Status status) {
        this.id          = Objects.requireNonNull(id, "id cannot be null");
        this.title       = Objects.requireNonNull(title, "title cannot be null");
        this.description = Objects.requireNonNull(description, "description cannot be null");
        this.priority    = Objects.requireNonNull(priority, "priority cannot be null");
        this.dueDate     = Objects.requireNonNull(dueDate, "dueDate cannot be null");
        this.status      = Objects.requireNonNull(status, "status cannot be null");
    }

    public static Task createSimple(final String title, final String description) {
        return builder(title).description(description).build();
    }

    public static Builder builder(final String title) {
        return new Builder(title);
    }

    public static Builder builder(final Title title) {
        return new Builder(title);
    }

    public static Task of(final String id,
                          final Title title,
                          final Description description,
                          final Priority priority,
                          final DueDate dueDate,
                          final Status status) {
        return new Task(id, title, description, priority, dueDate, status);
    }

    public Task markAsCompleted() {
        return withStatus(Status.COMPLETED);
    }

    public Task withStatus(final Status status) {
        if (this.status == status) {
            return this;
        }

        return new Task(this.id, this.title, this.description, this.priority, this.dueDate, status);
    }

    public boolean isOverdue() {
        return status == Status.PENDING
                && dueDate != null
                && getDueDate().value().isBefore(ZonedDateTime.now());
    }

    public DueDate getDueDate() {
        return dueDate;
    }

    public String getId() {
        return id;
    }

    public Title getTitle() {
        return title;
    }

    public Description getDescription() {
        return description;
    }

    public Priority getPriority() {
        return priority;
    }

    public Status getStatus() {
        return status;
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
                                     Task[
                                         ID: %s
                                         Title: %s
                                         Description: %s
                                         Priority: %s
                                         Due date: %tF
                                         Status: %s
                                     ]""",
                             id,
                             title.value(),
                             description.value(),
                             priority,
                             dueDate.value().toLocalDate(),
                             status);
    }

    public enum Status {
        PENDING,
        COMPLETED;

        public static Status fromString(final String value) {
            Objects.requireNonNull(value, "Status value cannot be null.");
            try {
                return Status.valueOf(value.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status value: '" + value + "'", e);
            }
        }
    }

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH;

        public static Priority fromString(final String value) {
            Objects.requireNonNull(value, "Priority value cannot be null.");
            try {
                return Priority.valueOf(value.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid priority value: '" + value + "'", e);
            }
        }
    }

    public static final class Builder {

        private final Title title;

        private Description description = Description.empty();
        private Priority priority = Priority.MEDIUM;
        private DueDate dueDate = DueDate.inDays(3);
        private Status status = Status.PENDING;

        public Builder(final Title title) {
            this.title = title;
        }

        public Builder(final String title) {
            this.title = Title.of(title);
        }

        public Builder description(final Description description) {
            this.description = description;
            return this;
        }

        public Builder description(final String description) {
            this.description = Description.of(description);
            return this;
        }

        public Builder priority(final Priority priority) {
            this.priority = priority;
            return this;
        }

        public Builder dueDate(final DueDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder status(final Status status) {
            this.status = status;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }
}
