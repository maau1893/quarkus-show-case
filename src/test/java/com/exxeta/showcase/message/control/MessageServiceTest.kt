package com.exxeta.showcase.message.control

import com.exxeta.showcase.message.MessageType
import com.exxeta.showcase.message.api.MessagePublisher
import com.exxeta.showcase.message.api.MessageClient
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
internal class MessageServiceTest {

    private lateinit var messageService: MessageService

    private lateinit var messageClient: MessageClient

    private lateinit var messagePublisher: MessagePublisher

    @BeforeEach
    fun beforeEach() {
        messageClient = mockk()
        messagePublisher = mockk()
        messageService =
            MessageService(messageClient, messagePublisher, LoggerFactory.getLogger(MessageService::class.java))
    }

    @Test
    fun handleIncomingMessage() {
        val dto = MessageRequestDto(content = "Test content", messageType = MessageType.KAFKA)

        val subscriber =
            messageService.handleIncomingMessage(dto).subscribe().withSubscriber(UniAssertSubscriber.create())

        subscriber.assertCompleted()
    }

    @Test
    fun `sendOutgoingMessage with REST`() {
        val dto = MessageRequestDto(content = "Test content", messageType = MessageType.REST)

        val expected = Uni.createFrom().voidItem()

        every { messageClient.sendMessage(dto) } returns expected

        val subscriber =
            messageService.sendOutgoingMessage(dto).subscribe().withSubscriber(UniAssertSubscriber.create())

        verify { messageClient.sendMessage(dto) }
        verify(inverse = true) { messagePublisher.sendMessage(dto) }

        subscriber.assertCompleted()
    }

    @Test
    fun `sendOutgoingMessage with Kafka`() {
        val dto = MessageRequestDto(content = "Test content", messageType = MessageType.KAFKA)

        val expected = Uni.createFrom().voidItem()

        every { messagePublisher.sendMessage(dto) } returns expected

        val subscriber =
            messageService.sendOutgoingMessage(dto).subscribe().withSubscriber(UniAssertSubscriber.create())

        verify { messagePublisher.sendMessage(dto) }
        verify(inverse = true) { messageClient.sendMessage(dto) }

        subscriber.assertCompleted()
    }
}
