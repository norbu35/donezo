package dev.norbu.donezo.storage;

import dev.norbu.donezo.model.Task;
import dev.norbu.donezo.repository.TaskRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryTaskRepository
        implements TaskRepository {

  private final Map<UUID, Task> tasks = new LinkedHashMap<>();

  /**
   * @param task
   */
  @Override
  public void save(Task task) {
    tasks.put(UUID.randomUUID(), task);
  }

  /**
   * @param id
   * @return @Link List of Tasks
   */
  @Override
  public Optional<Task> findById(UUID id) {
    return Optional.ofNullable(tasks.get(id));
  }

  /**
   * @return
   */
  @Override
  public List<Task> findAll() {
    return new ArrayList<>(tasks.values());
  }

  /**
   * @param id
   */
  @Override
  public void deleteById(UUID id) {
    tasks.remove(id);
  }

  /**
   * @param task
   */
  @Override
  public void update(Task task) {
    tasks.put(task.getId(), task);
  }

  /**
   * @param taskList List of Tasks
   */
  @Override
  public void saveAll(List<Task> taskList) {
    taskList.forEach(task -> tasks.put(task.getId(), task));
  }
}
