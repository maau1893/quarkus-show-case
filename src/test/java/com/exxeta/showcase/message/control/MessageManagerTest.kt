package com.exxeta.showcase.message.control

import com.exxeta.showcase.message.MessageType
import com.exxeta.showcase.message.api.MessagePublisher
import com.exxeta.showcase.message.api.MessageService
import com.exxeta.showcase.message.model.MessageRequestDto
import io.mockk.every
import io.mockk.verify
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.junit.jupiter.api.Test
import javax.inject.Inject

@QuarkusTest
@Suppress("ReactiveStreamsUnusedPublisher")
internal class MessageManagerTest {

    @Inject
    private lateinit var messageManager: MessageManager

    @InjectMock
    @RestClient
    private lateinit var messageService: MessageService

    @InjectMock
    private lateinit var messagePublisher: MessagePublisher

    @Test
    fun handleIncomingMessage() {
        val dto = MessageRequestDto(content = "Test content", messageType = MessageType.KAFKA)

        val subscriber =
            messageManager.handleIncomingMessage(dto).subscribe().withSubscriber(UniAssertSubscriber.create())

        subscriber.assertCompleted()
    }

    @Test
    fun `sendOutgoingMessage with REST`() {
        val dto = MessageRequestDto(content = "Test content", messageType = MessageType.REST)

        val expected = Uni.createFrom().voidItem()

        every { messageService.sendMessage(dto) } returns expected

        val subscriber =
            messageManager.sendOutgoingMessage(dto).subscribe().withSubscriber(UniAssertSubscriber.create())

        verify { messageService.sendMessage(dto) }
        verify(inverse = true) { messagePublisher.sendMessage(dto) }

        subscriber.assertCompleted()
    }

    @Test
    fun `sendOutgoingMessage with Kafka`() {
        val dto = MessageRequestDto(content = "Test content", messageType = MessageType.KAFKA)

        val expected = Uni.createFrom().voidItem()

        every { messagePublisher.sendMessage(dto) } returns expected

        val subscriber =
            messageManager.sendOutgoingMessage(dto).subscribe().withSubscriber(UniAssertSubscriber.create())

        verify { messagePublisher.sendMessage(dto) }
        verify(inverse = true) { messageService.sendMessage(dto) }

        subscriber.assertCompleted()
    }
}
