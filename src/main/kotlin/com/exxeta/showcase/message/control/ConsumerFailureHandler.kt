package com.exxeta.showcase.message.control

import io.smallrye.common.annotation.Identifier
import io.smallrye.reactive.messaging.kafka.DeserializationFailureHandler
import org.apache.kafka.common.header.Headers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
@Identifier("consumer-failure-handler")
class ConsumerFailureHandler : DeserializationFailureHandler<Any> {

    private val logger: Logger = LoggerFactory.getLogger(ConsumerFailureHandler::class.simpleName)

    override fun handleDeserializationFailure(
        topic: String?,
        isKey: Boolean,
        deserializer: String?,
        data: ByteArray?,
        exception: Exception?,
        headers: Headers?
    ): Any? {
        val content = data?.decodeToString()
        logger.error("Failed to deserialize incoming update message $content", exception)
        return null
    }
}
