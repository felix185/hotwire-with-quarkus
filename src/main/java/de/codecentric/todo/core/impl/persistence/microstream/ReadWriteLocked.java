package de.codecentric.todo.core.impl.persistence.microstream;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Simple implementation of a read-write-locking mechanism.
 *
 * @author Felix Riess, codecentric AG
 * @since 09 Sep 2022
 * @see <a href="https://github.com/microstream-one/bookstore-demo/blob/master/src/main/java/one/microstream/demo/bookstore/util/concurrent/ReadWriteLocked.java">Microstream demo</a>
 */
public class ReadWriteLocked {

    private transient volatile ReentrantReadWriteLock mutex;

    protected ReadWriteLocked() {
        super();
    }

    private ReentrantReadWriteLock mutex() {
        if (this.mutex == null) {
            synchronized (this) {
                if (this.mutex == null) {
                    this.mutex = new ReentrantReadWriteLock();
                }
            }
        }
        return this.mutex;
    }

    protected final <T> T read(ValueOperation<T> operation) {
        mutex().readLock().lock();
        try {
            return operation.execute();
        } finally {
            mutex().readLock().unlock();
        }
    }

    protected final <T> T write(ValueOperation<T> operation) {
        mutex().writeLock().lock();
        try {
            return operation.execute();
        } finally {
            mutex().writeLock().unlock();
        }
    }

    protected final void write (VoidOperation operation) {
        mutex().writeLock().lock();
        try {
            operation.execute();
        } finally {
            mutex().writeLock().unlock();
        }
    }
}
