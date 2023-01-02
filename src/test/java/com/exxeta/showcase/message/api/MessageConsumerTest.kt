package com.exxeta.showcase.message.api

import com.exxeta.showcase.message.MessageType
import com.exxeta.showcase.message.control.MessageManager
import com.exxeta.showcase.message.model.MessageRequestDto
import io.mockk.every
import io.mockk.verify
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import org.junit.jupiter.api.Test
import javax.inject.Inject

@QuarkusTest
@Suppress("ReactiveStreamsUnusedPublisher")
internal class MessageConsumerTest {

    @Inject
    private lateinit var messageConsumer: MessageConsumer

    @InjectMock
    private lateinit var messageManager: MessageManager

    @Test
    fun consumeMessage() {
        val dto = MessageRequestDto("Test content", MessageType.KAFKA)

        val response = Uni.createFrom().voidItem()

        every { messageManager.handleIncomingMessage(dto) } returns response

        val subscriber = messageConsumer.consumeMessage(dto).subscribe().withSubscriber(UniAssertSubscriber.create())

        verify { messageManager.handleIncomingMessage(dto) }

        subscriber.assertCompleted()
    }
}
