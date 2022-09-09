package de.codecentric.todo.core.impl.persistence.microstream;

/**
 * Functional interface for an operation which returns a value. Used for {@link ReadWriteLocked}.
 *
 * @param <T> the type which should be returned by the operation.
 * @author Felix Riess, codecentric AG
 * @since 09 Sep 2022
 * @see <a href="https://github.com/microstream-one/bookstore-demo/blob/master/src/main/java/one/microstream/demo/bookstore/util/concurrent/ValueOperation.java">Microstream demo</a>
 */
@FunctionalInterface
public interface ValueOperation<T> {

    T execute();
}
