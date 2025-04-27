package dev.norbu.donezo.repository;

import dev.norbu.donezo.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {

  void save(Task task);

  Optional<Task> findById(UUID id);

  List<Task> findAll();

  void deleteById(UUID id);

  void update(Task task);

  void saveAll(List<Task> tasks);
}
