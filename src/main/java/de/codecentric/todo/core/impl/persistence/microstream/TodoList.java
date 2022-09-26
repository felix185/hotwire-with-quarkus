package de.codecentric.todo.core.impl.persistence.microstream;

import de.codecentric.todo.core.impl.business.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Storage accessor for todos which also handles the locking via {@link ReadWriteLocked}.
 *
 * @author Felix Riess, codecentric AG
 * @since 09 Sep 2022
 */
public class TodoList extends ReadWriteLocked {

    private final List<Todo> todoList = new ArrayList<>();

    public TodoList() {
        this.todoList.add(new Todo("Todo"));
        this.todoList.add(new Todo("Another Todo"));
        this.todoList.add(new Todo("Yet another Todo"));
        this.todoList.add(new Todo("Admin Todo", UUID.fromString("042aa75e-a811-42fe-8223-6c62dc079b5b")));
        this.todoList.add(new Todo("Standard User Todo", UUID.fromString("f68e5283-481e-4adc-b474-7980849418df")));
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
        write(() -> StorageManagerAccessor.getInstance().getStorageManager().store(todo));
        return todo.getId();
    }

    @Override
    public String toString() {
        return "TodoList{" +
                "todoList=" + this.todoList +
                '}';
    }
}
