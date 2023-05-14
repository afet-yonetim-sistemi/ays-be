package com.ays;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

/**
 * A listener that logs a message when the AYS application is ready to start.
 */
@Slf4j
@Configuration
class AysApplicationEventListener implements ApplicationListener<ApplicationReadyEvent> {

    /**
     * Logs a message when the AYS application is ready to start.
     *
     * @param applicationReadyEvent the event indicating that the application is ready
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("AYS BE STARTED!");
    }
}
