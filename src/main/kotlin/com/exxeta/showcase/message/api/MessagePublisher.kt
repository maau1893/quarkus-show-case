package com.exxeta.showcase.message.api

import com.exxeta.showcase.message.model.MessageRequestDto
import io.smallrye.mutiny.Uni
import io.smallrye.reactive.messaging.MutinyEmitter
import org.eclipse.microprofile.reactive.messaging.Channel
import org.slf4j.Logger
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class MessagePublisher(
    @Channel("show-case-to-dummy")
    private val messageEmitter: MutinyEmitter<MessageRequestDto>,
    private val logger: Logger,
) {

    fun sendMessage(dto: MessageRequestDto): Uni<Void> {
        logger.info("Sending message ${dto.content} via Kafka")
        return messageEmitter.send(dto)
    }
}
