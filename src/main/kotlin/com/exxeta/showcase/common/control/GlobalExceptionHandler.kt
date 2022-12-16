package com.exxeta.showcase.common.control

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class GlobalExceptionHandler : ExceptionMapper<Exception> {
    override fun toResponse(exception: Exception?): Response {
        if (exception is WebApplicationException) {
            val exceptionResponse = exception.response
            return Response.status(exceptionResponse.status)
                .entity(exception.message ?: exceptionResponse.statusInfo.reasonPhrase).build()
        }
        return Response.serverError().build()
    }
}
