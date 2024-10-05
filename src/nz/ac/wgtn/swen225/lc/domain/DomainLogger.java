package nz.ac.wgtn.swen225.lc.domain;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A logger for domain debugging and monitoring.
 */
public enum DomainLogger {
    LOGGER;

    private final Logger logger;

    DomainLogger() {
        this.logger = Logger.getLogger("Domain");
        this.logger.setLevel(Level.OFF);
    }

    public Logger getLogger() {
        return logger;
    }
}
