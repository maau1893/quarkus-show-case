package com.exxeta.showcase.common.control

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException
import javax.ws.rs.InternalServerErrorException
import javax.ws.rs.NotFoundException
import javax.ws.rs.core.Response

internal class GlobalExceptionHandlerTest {

    private lateinit var handler: GlobalExceptionHandler

    @BeforeEach
    fun setUp() {
        handler = GlobalExceptionHandler()
    }

    @Test
    fun `toResponse with WebApplicationException`() {
        val errorMessage = "We were unable to find the entity you were looking for"
        val response = handler.toResponse(NotFoundException(errorMessage))
        Assertions.assertEquals(errorMessage, response.entity)
        Assertions.assertEquals(Response.Status.NOT_FOUND.statusCode, response.status)
    }

    @Test
    fun `toResponse with IOException`() {
        val errorMessage = "We were unable to connect to the database"
        val response = handler.toResponse(IOException(errorMessage))
        Assertions.assertNotEquals(errorMessage, response.entity)
        Assertions.assertNull(response.entity)
        Assertions.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.statusCode, response.status)
    }

    @Test
    fun `toResponse with InternalServerErrorException`() {
        val errorMessage = "The operation could not be executed"
        val response = handler.toResponse(InternalServerErrorException(errorMessage))
        Assertions.assertEquals(errorMessage, response.entity)
        Assertions.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.statusCode, response.status)
    }
}
