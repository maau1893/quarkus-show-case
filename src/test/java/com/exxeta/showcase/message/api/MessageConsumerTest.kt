package com.exxeta.showcase.message.api

import com.exxeta.showcase.message.MessageType
import com.exxeta.showcase.message.control.MessageManager
import com.exxeta.showcase.message.model.MessageRequestDto
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

@Suppress("ReactiveStreamsUnusedPublisher")
internal class MessageConsumerTest {

    private lateinit var messageConsumer: MessageConsumer

    private lateinit var messageManagerMock: MessageManager

    @BeforeEach
    fun setUp() {
        messageManagerMock = mockkClass(MessageManager::class)
        messageConsumer = MessageConsumer(messageManagerMock, LoggerFactory.getLogger(MessageConsumer::class.java))
    }

    @Test
    fun consumeMessage() {
        val dto = MessageRequestDto("Test content", MessageType.KAFKA)

        val response = Uni.createFrom().voidItem()

        every { messageManagerMock.handleIncomingMessage(dto) } returns response

        val subscriber = messageConsumer.consumeMessage(dto).subscribe().withSubscriber(UniAssertSubscriber.create())

        verify { messageManagerMock.handleIncomingMessage(dto) }

        subscriber.assertCompleted()
    }
}
