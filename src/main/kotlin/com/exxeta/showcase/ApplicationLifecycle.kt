package com.exxeta.showcase

import io.quarkus.runtime.Startup
import io.quarkus.runtime.StartupEvent
import io.quarkus.runtime.configuration.ProfileManager
import org.slf4j.LoggerFactory
import javax.annotation.PostConstruct
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@Startup
@ApplicationScoped
@Suppress("unused")
class ApplicationLifecycle {

    private val logger = LoggerFactory.getLogger(ApplicationLifecycle::class.simpleName)

    @PostConstruct
    fun init() {
        fun onStart(@Observes event: StartupEvent) {
            logger.info("The application starting with Profile " + ProfileManager.getActiveProfile())
        }
    }
}
