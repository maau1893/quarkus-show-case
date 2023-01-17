package com.exxeta.showcase.todo.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank

data class UpdateTodoRequestDto(
    @NotBlank
    val description: String,
    @NotBlank
    @get:JsonProperty("isDone")
    val isDone: Boolean,
)
