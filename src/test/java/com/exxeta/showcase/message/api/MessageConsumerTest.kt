package com.exxeta.showcase.message.api

import com.exxeta.showcase.message.MessageType
import com.exxeta.showcase.message.control.MessageService
import com.exxeta.showcase.message.model.MessageRequestDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

@Suppress("ReactiveStreamsUnusedPublisher")
internal class MessageConsumerTest {

    private lateinit var messageConsumer: MessageConsumer

    private lateinit var messageServiceMock: MessageService

    @BeforeEach
    fun setUp() {
        messageServiceMock = mockk()
        messageConsumer = MessageConsumer(messageServiceMock, LoggerFactory.getLogger(MessageConsumer::class.java))
    }

    @Test
    fun consumeMessage() {
        val dto = MessageRequestDto("Test content", MessageType.KAFKA)

        val response = Uni.createFrom().voidItem()

        every { messageServiceMock.handleIncomingMessage(dto) } returns response

        val subscriber = messageConsumer.consumeMessage(dto).subscribe().withSubscriber(UniAssertSubscriber.create())

        verify { messageServiceMock.handleIncomingMessage(dto) }

        subscriber.assertCompleted()
    }
}
