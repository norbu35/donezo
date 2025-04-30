package dev.norbu.donezo.storage;

import dev.norbu.donezo.model.Task;
import dev.norbu.donezo.repository.TaskRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryTaskRepository
        implements TaskRepository {

    private final Map<String, Task> tasks = new LinkedHashMap<>();

    @Override
    public void save(final Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public Optional<Task> findById(final String id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public boolean deleteById(final String id) {
        return tasks.remove(id) != null;
    }

    @Override
    public void saveAll(final List<Task> taskList) {
        taskList.forEach(task -> tasks.put(task.getId(), task));
    }
}
