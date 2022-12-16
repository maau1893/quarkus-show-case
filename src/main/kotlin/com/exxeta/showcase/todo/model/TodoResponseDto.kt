package com.exxeta.showcase.todo.model

import java.time.LocalDateTime

data class TodoResponseDto(
    val id: String,
    val description: String,
    val isDone: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
