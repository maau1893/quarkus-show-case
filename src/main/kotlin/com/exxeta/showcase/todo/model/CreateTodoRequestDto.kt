package com.exxeta.showcase.todo.model

import javax.validation.constraints.NotBlank

data class CreateTodoRequestDto(@field:NotBlank val description: String)
