package org.ays;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public abstract class AysLogConfiguration {

    protected final LogTracker logTracker = new LogTracker();

    @BeforeEach
    void setup() {
        Logger logger = (Logger) LoggerFactory.getLogger("org.ays");
        logger.setLevel(Level.TRACE);
        logger.addAppender(logTracker);

        logTracker.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logTracker.start();
    }


    protected static class LogTracker extends ListAppender<ILoggingEvent> {

        public Optional<String> findMessage(Level level, String messageFragment) {

            if (CollectionUtils.isEmpty(this.list)) {
                return Optional.empty();
            }

            return this.list.stream()
                    .filter(log -> log.getLevel().equals(level))
                    .map(ILoggingEvent::getFormattedMessage)
                    .filter(formattedMessage -> formattedMessage.contains(messageFragment))
                    .findFirst();
        }

    }

}
