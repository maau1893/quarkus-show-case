package com.exxeta.showcase.todo.model

import javax.validation.constraints.NotBlank

data class CreateTodoRequestDto(@NotBlank val description: String)
