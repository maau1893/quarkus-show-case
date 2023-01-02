package com.exxeta.showcase.message.api

import com.exxeta.showcase.KafkaTestResourceLifecycleManager
import com.exxeta.showcase.message.MessageType
import com.exxeta.showcase.message.model.MessageRequestDto
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import io.smallrye.reactive.messaging.providers.connectors.InMemoryConnector
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.inject.Inject
import javax.enterprise.inject.Any

@QuarkusTest
@Suppress("ReactiveStreamsUnusedPublisher")
@QuarkusTestResource(KafkaTestResourceLifecycleManager::class)
internal class MessagePublisherTest {

    @Inject
    private lateinit var messagePublisher: MessagePublisher

    @Any
    @Inject
    private lateinit var connector: InMemoryConnector

    @Test
    fun sendMessage() {
        val showCaseToDummySink = connector.sink<MessageRequestDto>("show-case-to-dummy")

        val dto = MessageRequestDto(content = "Test content", messageType = MessageType.KAFKA)

        val subscriber = messagePublisher.sendMessage(dto).subscribe().withSubscriber(UniAssertSubscriber.create())

        val messages = showCaseToDummySink.received()

        Assertions.assertEquals(1, messages.size)

        val message = messages.first()

        Assertions.assertEquals(dto, message.payload)

        subscriber.assertCompleted()
    }
}
