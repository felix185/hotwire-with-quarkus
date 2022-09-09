package de.codecentric.todo.core.impl.persistence;

import de.codecentric.todo.core.impl.business.Todo;
import de.codecentric.todo.core.impl.persistence.microstream.StorageManagerAccessor;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Microstream
@ApplicationScoped
public class MicrostreamTodoRepositoryAdapter implements TodoRepository {

    @Override
    public List<Todo> findAll() {
        return StorageManagerAccessor.getInstance().getTodoList().all();
    }

    @Override
    public List<Todo> findByUserId(UUID userId) {
        return StorageManagerAccessor.getInstance().getTodoList().byUser(userId);
    }

    @Override
    public Optional<Todo> findById(UUID id) {
        return StorageManagerAccessor.getInstance().getTodoList().byId(id);
    }

    @Override
    public void remove(UUID id) {
        StorageManagerAccessor.getInstance().getTodoList().remove(id);
    }

    @Override
    public UUID add(Todo todo) {
        return StorageManagerAccessor.getInstance().getTodoList().add(todo);
    }

    @Override
    public UUID update(Todo todo) {
        return StorageManagerAccessor.getInstance().getTodoList().update(todo);
    }
}
