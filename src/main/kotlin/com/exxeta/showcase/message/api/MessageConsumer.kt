package com.exxeta.showcase.message.api

import com.exxeta.showcase.message.control.MessageService
import com.exxeta.showcase.message.model.MessageRequestDto
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.slf4j.Logger
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class MessageConsumer(private val messageService: MessageService, private val logger: Logger) {

    @Incoming("dummy-to-show-case")
    fun consumeMessage(dto: MessageRequestDto): Uni<Void> {
        logger.info("Incoming Kafka message")
        return messageService.handleIncomingMessage(dto)
    }
}
