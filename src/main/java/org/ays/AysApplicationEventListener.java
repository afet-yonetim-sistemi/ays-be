package org.ays;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

/**
 * A listener that logs a message when the AYS application is ready to start.
 */
@Slf4j
@Configuration
class AysApplicationEventListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    BuildProperties buildProperties;

    /**
     * Logs a message when the AYS application is ready to start.
     *
     * @param applicationReadyEvent the event indicating that the application is ready
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("Build number: {}", buildProperties.get("buildNumber"));
        log.info("Application version: {}", buildProperties.getVersion());
        log.info("AYS BE STARTED!");
    }
}
