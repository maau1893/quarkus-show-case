package com.exxeta.showcase.message.control

import com.exxeta.showcase.message.MessageType
import com.exxeta.showcase.message.api.MessagePublisher
import com.exxeta.showcase.message.api.MessageClient
import com.exxeta.showcase.message.model.MessageRequestDto
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.slf4j.Logger
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class MessageService(
    @RestClient private val messageClient: MessageClient,
    private val messagePublisher: MessagePublisher,
    private val logger: Logger,
) {

    fun handleIncomingMessage(dto: MessageRequestDto): Uni<Void> {
        logger.info("Received message of type ${dto.messageType} and content ${dto.content}")
        return Uni.createFrom().voidItem()
    }

    fun sendOutgoingMessage(dto: MessageRequestDto): Uni<Void> {
        val messageType = dto.messageType
        val content = dto.content
        logger.info("Sending message of type $messageType and content $content to the dummy service")
        return when (messageType) {
            MessageType.REST -> {
                logger.info("Sending REST message $content to dummy service")
                messageClient.sendMessage(dto)
            }
            MessageType.KAFKA -> messagePublisher.sendMessage(dto)
        }
    }
}
