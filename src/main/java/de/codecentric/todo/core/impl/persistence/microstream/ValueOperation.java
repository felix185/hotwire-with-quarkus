package de.codecentric.todo.core.impl.persistence.microstream;

@FunctionalInterface
public interface ValueOperation<T> {

    T execute();
}
