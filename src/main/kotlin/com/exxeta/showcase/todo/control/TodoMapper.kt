package com.exxeta.showcase.todo.control

import com.exxeta.showcase.todo.model.Todo
import com.exxeta.showcase.todo.model.CreateTodoRequestDto
import com.exxeta.showcase.todo.model.TodoResponseDto
import com.exxeta.showcase.todo.model.UpdateTodoRequestDto
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface TodoMapper {

    @Mapping(source = "done", target = "isDone")
    fun toResponseDto(entity: Todo): TodoResponseDto

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "description", target = "description")
    fun toEntity(requestDto: CreateTodoRequestDto): Todo

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "done", target = "done")
    @Mapping(source = "description", target = "description")
    fun toEntity(updateDto: UpdateTodoRequestDto): Todo
}
