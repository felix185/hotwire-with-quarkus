package de.codecentric.todo.core.impl.persistence.microstream;

/**
 * Functional interface for an operation with no return.
 *
 * @author Felix Riess, codecentric AG
 * @since 09 Sep 2022
 * @see <a href="https://github.com/microstream-one/bookstore-demo/blob/master/src/main/java/one/microstream/demo/bookstore/util/concurrent/VoidOperation.java">Microstream demo</a>
 */
@FunctionalInterface
public interface VoidOperation {

    void execute();
}
