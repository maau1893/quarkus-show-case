package com.exxeta.showcase.message.api

import com.exxeta.showcase.common.model.ApiDescriptions
import com.exxeta.showcase.message.control.MessageManager
import com.exxeta.showcase.message.model.MessageRequestDto
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.slf4j.Logger
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.MediaType

@Path("message")
class MessageResource(private val messageManager: MessageManager, private val logger: Logger) {

    @POST
    @Path("/from-dummy")
    @APIResponse(responseCode = "200")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = ApiDescriptions.POST_FROM_DUMMY)
    fun logMessage(dto: MessageRequestDto): Uni<Void> {
        logger.info("Incoming message received")
        return messageManager.handleIncomingMessage(dto)
    }

    @POST
    @Path("/to-dummy")
    @APIResponses(
        value = [
            APIResponse(responseCode = "200"),
            APIResponse(responseCode = "500")
        ]
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = ApiDescriptions.POST_TO_DUMMY)
    fun sendMessageToDummyService(dto: MessageRequestDto): Uni<Void> {
        logger.info("Outgoing message received")
        return messageManager.sendOutgoingMessage(dto)
    }
}
