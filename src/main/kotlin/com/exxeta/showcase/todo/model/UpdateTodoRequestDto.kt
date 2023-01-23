package com.exxeta.showcase.todo.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class UpdateTodoRequestDto(
    @field:NotBlank
    val description: String,
    @field:NotNull
    @get:JsonProperty("isDone")
    val isDone: Boolean,
)
