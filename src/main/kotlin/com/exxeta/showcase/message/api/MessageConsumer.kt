package com.exxeta.showcase.message.api

import com.exxeta.showcase.message.control.MessageManager
import com.exxeta.showcase.message.model.MessageRequestDto
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class MessageConsumer @Inject constructor(private val messageManager: MessageManager) {

    private val logger: Logger = LoggerFactory.getLogger(MessageConsumer::class.simpleName)

    @Incoming("dummy-to-show-case")
    fun consumeMessage(dto: MessageRequestDto): Uni<Void> {
        logger.info("Incoming Kafka message")
        return messageManager.handleIncomingMessage(dto)
    }
}
