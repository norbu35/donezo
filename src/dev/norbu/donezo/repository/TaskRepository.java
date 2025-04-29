package dev.norbu.donezo.repository;

import dev.norbu.donezo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    void save(Task task);

    Optional<Task> findById(String id);

    List<Task> findAll();

    void deleteById(String id);

    void update(Task task);

    void saveAll(List<Task> tasks);
}
