package com.exxeta.showcase.message.model

import com.exxeta.showcase.message.MessageType
import javax.validation.constraints.NotBlank

data class MessageRequestDto(@NotBlank val content: String = "", val messageType: MessageType = MessageType.REST)
