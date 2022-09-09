package de.codecentric.todo.core.impl.persistence.microstream;

import de.codecentric.todo.core.impl.business.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class TodoList extends ReadWriteLocked {

    private final List<Todo> todoList = new ArrayList<>();

    public TodoList() {
    }

    public List<Todo> all() {
        return read(() -> this.todoList);
    }

    public List<Todo> byUser(UUID userId) {
        return read(() -> this.todoList.stream()
                                       .filter(todo -> todo.getUserId() != null && todo.getUserId().equals(userId))
                                       .collect(Collectors.toList()));
    }

    public Optional<Todo> byId(UUID todoId) {
        return read(() -> this.todoList.stream().filter(todo -> todo.getId().equals(todoId)).findFirst());
    }

    public void remove(UUID todoId) {
        Optional<Todo> existing = byId(todoId);
        existing.ifPresent(todo -> write(() -> {
            this.todoList.remove(todo);
            StorageManagerAccessor.getInstance().getStorageManager().store(this.todoList);
        }));
    }

    public UUID add(Todo todo) {
        write(() -> {
            this.todoList.add(todo);
            StorageManagerAccessor.getInstance().getStorageManager().store(this.todoList);
        });
        return todo.getId();
    }

    public UUID update(Todo todo) {
        write(() -> StorageManagerAccessor.getInstance().getStorageManager().store(this.todoList));
        return todo.getId();
    }
}
