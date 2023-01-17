package com.exxeta.showcase.common.control

import org.slf4j.Logger
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class GlobalExceptionMapper(private val logger: Logger) : ExceptionMapper<Exception> {

    override fun toResponse(exception: Exception): Response {
        if (exception is WebApplicationException) {
            val exceptionResponse = exception.response
            return Response.status(exceptionResponse.status)
                .entity(exception.message ?: exceptionResponse.statusInfo.reasonPhrase).build()
        }
        logger.error("Transforming internal error to 500 error response", exception)
        return Response.serverError().build()
    }
}
