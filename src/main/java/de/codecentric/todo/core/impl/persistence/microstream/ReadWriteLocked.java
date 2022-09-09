package de.codecentric.todo.core.impl.persistence.microstream;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLocked {

    private transient volatile ReentrantReadWriteLock mutex;

    protected ReadWriteLocked() {
        super();
    }

    private ReentrantReadWriteLock mutex() {
        synchronized (this) {
            if (this.mutex == null) {
                this.mutex = new ReentrantReadWriteLock();
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
