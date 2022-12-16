package com.exxeta.showcase

import io.quarkus.runtime.ShutdownEvent
import io.quarkus.runtime.StartupEvent
import liquibase.LabelExpression
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.exception.LiquibaseException
import liquibase.resource.ClassLoaderResourceAccessor
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@ApplicationScoped
class ApplicationLifecycle(
    @ConfigProperty(name = "liquibase.url")
    private val datasourceUrl: String,
    @ConfigProperty(name = "quarkus.datasource.username")
    private val datasourceUsername: String,
    @ConfigProperty(name = "quarkus.datasource.password")
    private val datasourcePassword: String,
    @ConfigProperty(name = "quarkus.liquibase.change-log")
    private val changeLogLocation: String
) {

    private val logger: Logger = LoggerFactory.getLogger(ApplicationLifecycle::class.simpleName)

    fun onStart(@Observes ev: StartupEvent) {
        logger.info("The application is starting...")
        runLiquibaseMigration()
    }

    fun onStop(@Observes ev: ShutdownEvent) {
        logger.info("The application is stopping...")
    }

    @Throws(LiquibaseException::class)
    private fun runLiquibaseMigration() {
        logger.info("Running database migration script...")
        val resourceAccessor = ClassLoaderResourceAccessor(Thread.currentThread().contextClassLoader)
        val conn = DatabaseFactory.getInstance()
            .openConnection(datasourceUrl, datasourceUsername, datasourcePassword, null, resourceAccessor)
        Liquibase(changeLogLocation, resourceAccessor, conn).use { liquibase ->
            liquibase.update(null, LabelExpression())
        }
    }
}
