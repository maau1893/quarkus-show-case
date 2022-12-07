package com.exxeta.showcase.todo.model

import org.mapstruct.Mapper

@Mapper(componentModel = "cdi")
interface TodoMapper {

    fun toResponseDto(entity: Todo): TodoResponseDto

    fun toEntity(requestDto: TodoRequestDto): Todo
}
