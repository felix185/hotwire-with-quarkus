package de.codecentric.todo.core.impl.persistence.microstream;

import de.codecentric.common.logging.ILogger;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class StorageManagerController {

    private static final ILogger LOG = ILogger.getLogger(StorageManagerController.class);

    public void onStartup(@Observes StartupEvent startupEvent) {
        LOG.info("Initializing storage manager");
        StorageManagerAccessor.init();
    }

    public void onShutdown(@Observes ShutdownEvent shutdownEvent) {
        LOG.info("Shutting down storage manager");
        StorageManagerAccessor.getInstance().shutdown();
        LOG.info("Successfully shutdown storage manager");
    }
}
