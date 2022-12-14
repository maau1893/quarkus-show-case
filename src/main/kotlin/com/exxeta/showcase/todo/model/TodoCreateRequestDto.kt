package com.exxeta.showcase.todo.model

import javax.validation.constraints.NotBlank

data class TodoCreateRequestDto(@NotBlank val description: String = "")
