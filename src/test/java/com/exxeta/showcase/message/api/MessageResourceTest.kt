package com.exxeta.showcase.message.api

import com.exxeta.showcase.message.MessageType
import com.exxeta.showcase.message.control.MessageService
import com.exxeta.showcase.message.model.MessageRequestDto
import io.mockk.every
import io.mockk.verify
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.common.http.TestHTTPEndpoint
import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import org.junit.jupiter.api.Test
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@QuarkusTest
@TestHTTPEndpoint(MessageResource::class)
@Suppress("ReactiveStreamsUnusedPublisher")
internal class MessageResourceTest {

    @InjectMock
    private lateinit var messageService: MessageService

    @Test
    fun logMessage() {
        val dto = MessageRequestDto("Test content", MessageType.REST)

        val response = Uni.createFrom().voidItem()

        every { messageService.handleIncomingMessage(dto) } returns response

        Given {
            body(dto)
            contentType(MediaType.APPLICATION_JSON)
        } When {
            post("/from-dummy")
        } Then {
            statusCode(Response.Status.NO_CONTENT.statusCode)
        }

        val subscriber = response.subscribe().withSubscriber(UniAssertSubscriber.create())

        subscriber.assertCompleted()

        verify { messageService.handleIncomingMessage(dto) }
    }

    @Test
    fun sendMessageToDummyService() {
        val dto = MessageRequestDto("Test content", MessageType.REST)

        val response = Uni.createFrom().voidItem()

        every { messageService.sendOutgoingMessage(dto) } returns response

        Given {
            body(dto)
            contentType(MediaType.APPLICATION_JSON)
        } When {
            post("/to-dummy")
        } Then {
            statusCode(Response.Status.NO_CONTENT.statusCode)
        }

        val subscriber = response.subscribe().withSubscriber(UniAssertSubscriber.create())

        subscriber.assertCompleted()

        verify { messageService.sendOutgoingMessage(dto) }
    }
}
