package com.exxeta.showcase.common.control

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class GlobalExceptionHandler : ExceptionMapper<Exception> {

    private val logger: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.simpleName)

    override fun toResponse(exception: Exception?): Response {
        if (exception is WebApplicationException) {
            val exceptionResponse = exception.response
            return Response.status(exceptionResponse.status)
                .entity(exception.message ?: exceptionResponse.statusInfo.reasonPhrase).build()
        }
        logger.error("Transforming internal error to 500 error response", exception)
        return Response.serverError().build()
    }
}
