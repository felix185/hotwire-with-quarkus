package de.codecentric.todo.core.api.types;

import java.util.Objects;
import java.util.UUID;

/**
 * DTO to represent the current state of a Todo.
 *
 * @author Felix Riess, codecentric AG
 * @since 18 Aug 2022
 */
public class TodoDTO {

    private final UUID id;
    private final String name;
    private final boolean completed;

    public TodoDTO(UUID id, String name, boolean completed) {
        this.id = id;
        this.name = name;
        this.completed = completed;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoDTO todoDTO = (TodoDTO) o;
        return this.completed == todoDTO.completed && this.id.equals(todoDTO.id) && this.name.equals(todoDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.completed);
    }

    @Override
    public String toString() {
        return "TodoDTO{" +
                "id=" + this.id +
                ", name='" + this.name + '\'' +
                ", completed=" + this.completed +
                '}';
    }
}
