package com.exxeta.showcase.message.api

import com.exxeta.showcase.message.model.MessageRequestDto
import io.smallrye.mutiny.Uni
import io.smallrye.reactive.messaging.MutinyEmitter
import org.eclipse.microprofile.reactive.messaging.Channel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class MessagePublisher @Inject constructor(
    @Channel("show-case-to-dummy")
    private val messageEmitter: MutinyEmitter<MessageRequestDto>,
) {

    private val logger: Logger = LoggerFactory.getLogger(MessagePublisher::class.simpleName)

    fun sendMessage(dto: MessageRequestDto): Uni<Void> {
        logger.info("Sending message ${dto.content} via Kafka")
        return messageEmitter.send(dto)
    }
}
