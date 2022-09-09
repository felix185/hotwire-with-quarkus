package de.codecentric.todo.core.impl.persistence.microstream;

import de.codecentric.common.logging.ILogger;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

/**
 * Bean to initialize and shutdown Microstream storage manager
 *
 * @author Felix Riess, codecentric AG
 * @since 09 Sep 2022
 */
@ApplicationScoped
public class StorageManagerController {

    private static final ILogger LOG = ILogger.getLogger(StorageManagerController.class);

    /**
     * Initialize storage manager on quarkus startup.
     *
     * @param startupEvent quarkus startup event.
     */
    public void onStartup(@Observes StartupEvent startupEvent) {
        LOG.info("Initializing storage manager");
        StorageManagerAccessor.init();
    }

    /**
     * Shutdown storage manager on quarkus shutdown.
     *
     * @param shutdownEvent quarkus shutdown event.
     */
    public void onShutdown(@Observes ShutdownEvent shutdownEvent) {
        LOG.info("Shutting down storage manager");
        StorageManagerAccessor.getInstance().shutdown();
        LOG.info("Successfully shutdown storage manager");
    }
}
