package de.codecentric.todo.core.impl.persistence.microstream;

import de.codecentric.common.errorhandling.ErrorCode;
import de.codecentric.common.errorhandling.exception.TechnicalException;
import de.codecentric.common.logging.ILogger;
import de.codecentric.common.validation.ArgumentChecker;
import one.microstream.afs.sql.types.SqlConnector;
import one.microstream.afs.sql.types.SqlFileSystem;
import one.microstream.afs.sql.types.SqlProviderPostgres;
import one.microstream.reflect.ClassLoaderProvider;
import one.microstream.storage.embedded.types.EmbeddedStorageFoundation;
import one.microstream.storage.embedded.types.EmbeddedStorageManager;
import one.microstream.storage.types.Storage;
import one.microstream.storage.types.StorageChannelCountProvider;
import one.microstream.storage.types.StorageConfiguration;
import org.postgresql.ds.PGSimpleDataSource;

/**
 * Accessor implementation for the microstream storage manager.
 *
 * @author Felix Riess, codecentric AG
 * @since 09 Sep 2022
 */
public final class StorageManagerAccessor {

    private static final ILogger LOG = ILogger.getLogger(StorageManagerAccessor.class);

    private static StorageManagerAccessor INSTANCE;

    private volatile EmbeddedStorageManager storageManager;

    private StorageManagerAccessor(final String dbUrl, final String dbUser, final String dbPassword) {
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUser(dbUser);
        dataSource.setPassword(dbPassword);

        final SqlFileSystem fileSystem = SqlFileSystem.New(SqlConnector.Caching(SqlProviderPostgres.New(dataSource)));

        final EmbeddedStorageFoundation<?> foundation = EmbeddedStorageFoundation.New().setConfiguration(
                StorageConfiguration.Builder()
                        .setStorageFileProvider(
                                Storage.FileProviderBuilder(fileSystem)
                                        .setDirectory(fileSystem.ensureDirectoryPath("microstream_storage"))
                                        .createFileProvider())
                        .setChannelCountProvider(
                                StorageChannelCountProvider.New(Math.max(1, // minimum one channel, if only 1 core is available
                                        Integer.highestOneBit(Runtime.getRuntime().availableProcessors() - 1))))
                        .createConfiguration()
        );
        // handle changing class definitions at runtime ("hot code replacement" by quarkus by running app in development mode)
        foundation.onConnectionFoundation(connectionFoundation ->
                connectionFoundation.setClassLoaderProvider(ClassLoaderProvider.New(Thread.currentThread()
                                                                                          .getContextClassLoader())));
        this.storageManager = foundation.createEmbeddedStorageManager().start();
        if (this.storageManager.root() == null) {
            LOG.info("Setting root for storage manager");
            this.storageManager.setRoot(new DataRoot());
            this.storageManager.storeRoot();
        }
    }

    /**
     * Initialize the {@link EmbeddedStorageManager}.
     *
     * @param dbUrl url of the postgres database to connect to (not {@code null} or empty).
     * @param dbUser the user to connect to the postgres database (not {@code null} or empty).
     * @param dbPassword the password of the user (not {@code null} or empty).
     */
    public static void init(final String dbUrl, final String dbUser, final String dbPassword) {
        ArgumentChecker.checkNotEmpty(dbUrl, "Database URL");
        ArgumentChecker.checkNotEmpty(dbUser, "Database user");
        ArgumentChecker.checkNotEmpty(dbPassword, "Database password");
        if (INSTANCE == null) {
            INSTANCE = new StorageManagerAccessor(dbUrl, dbUser, dbPassword);
        }
    }

    /**
     * Get the singleton instance of this {@link StorageManagerAccessor}.
     *
     * @return the only instance of {@link StorageManagerAccessor}.
     */
    public synchronized static StorageManagerAccessor getInstance() {
        if (INSTANCE == null) {
            LOG.error("Storage Manager is not yet initialized");
            throw new TechnicalException(ErrorCode.ILLEGAL_ACCESS, "Storage Manager is not yet initialized");
        }
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
