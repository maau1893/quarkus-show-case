package com.exxeta.showcase.message.control

import com.exxeta.showcase.message.MessageType
import com.exxeta.showcase.message.model.MessageRequestDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

internal class ConsumerFailureHandlerTest {

    private lateinit var handler: ConsumerFailureHandler

    private val objectMapper: ObjectMapper = ObjectMapper()

    @BeforeEach
    fun setUp() {
        handler = ConsumerFailureHandler(LoggerFactory.getLogger(ConsumerFailureHandler::class.java))
    }

    @Test
    fun handleDeserializationFailure() {
        val dto = MessageRequestDto("Test content", messageType = MessageType.KAFKA)

        val element = objectMapper.writeValueAsString(dto)

        val elementAsBytes = element.encodeToByteArray()

        val result = handler.handleDeserializationFailure(
            "test",
            isKey = false,
            deserializer = null,
            data = elementAsBytes,
            exception = null,
            headers = null
        )

        Assertions.assertNull(result)
    }
}
