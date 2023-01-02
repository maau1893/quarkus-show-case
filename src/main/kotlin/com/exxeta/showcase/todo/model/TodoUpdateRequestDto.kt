package com.exxeta.showcase.todo.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank

data class TodoUpdateRequestDto(
    @NotBlank
    val description: String,
    @NotBlank
    @field:JsonProperty("isDone")
    val isDone: Boolean,
)
