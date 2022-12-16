package com.exxeta.showcase.message.api

import com.exxeta.showcase.message.model.MessageRequestDto
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import javax.ws.rs.POST
import javax.ws.rs.Path

@Path("message")
@RegisterRestClient(configKey = "dummy-api")
interface MessageService {

    @POST
    @Path("/from-show-case")
    fun sendMessage(dto: MessageRequestDto): Uni<Void>
}
