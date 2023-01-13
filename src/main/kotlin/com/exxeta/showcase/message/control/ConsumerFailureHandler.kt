package com.exxeta.showcase.message.control

import io.smallrye.common.annotation.Identifier
import io.smallrye.reactive.messaging.kafka.DeserializationFailureHandler
import org.apache.kafka.common.header.Headers
import org.slf4j.Logger
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
@Identifier("consumer-failure-handler")
class ConsumerFailureHandler(private val logger: Logger) : DeserializationFailureHandler<Any> {

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
