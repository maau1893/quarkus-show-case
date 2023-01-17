package com.exxeta.showcase.todo.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.ZonedDateTime
import java.util.UUID

data class TodoResponseDto(
    val id: UUID,
    val description: String,
    @get:JsonProperty("isDone")
    val isDone: Boolean,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
)
