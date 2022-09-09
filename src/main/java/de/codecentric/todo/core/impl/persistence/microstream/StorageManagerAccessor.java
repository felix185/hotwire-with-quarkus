package de.codecentric.todo.core.impl.persistence.microstream;

import one.microstream.storage.embedded.configuration.types.EmbeddedStorageConfiguration;
import one.microstream.storage.embedded.types.EmbeddedStorageFoundation;
import one.microstream.storage.embedded.types.EmbeddedStorageManager;

/**
 * Accessor implementation for the microstream storage manager.
 *
 * @author Felix Riess, codecentric AG
 * @since 09 Sep 2022
 */
public final class StorageManagerAccessor {

    private static StorageManagerAccessor INSTANCE;

    private volatile EmbeddedStorageManager storageManager;

    private StorageManagerAccessor() {
        final EmbeddedStorageFoundation<?> foundation = EmbeddedStorageConfiguration.Builder()
                .setStorageDirectory("data/storage")
                .setChannelCount(Math.max(
                        1, // minimum one channel, if only 1 core is available
                        Integer.highestOneBit(Runtime.getRuntime().availableProcessors() - 1)
                ))
                .createEmbeddedStorageFoundation();
        this.storageManager = foundation.createEmbeddedStorageManager(new DataRoot()).start();
        this.storageManager.storeRoot();
    }

    /**
     * Initialize the {@link EmbeddedStorageManager}.
     */
    public synchronized static void init() {
        if (INSTANCE == null) {
            INSTANCE = new StorageManagerAccessor();
        }
    }

    /**
     * Get the singleton instance of this {@link StorageManagerAccessor}.
     *
     * @return the only instance of {@link StorageManagerAccessor}.
     */
    public synchronized static StorageManagerAccessor getInstance() {
        init();
        return INSTANCE;
    }

    /**
     * Gracefully shutdown the storage manager if not yet done.
     */
    public void shutdown() {
        if (this.storageManager != null) {
            this.storageManager.shutdown();
            this.storageManager = null;
        }
    }

    public EmbeddedStorageManager getStorageManager() {
        return this.storageManager;
    }

    public TodoList getTodoList() {
        DataRoot root = (DataRoot) this.storageManager.root();
        return root.getTodoList();
    }
}
