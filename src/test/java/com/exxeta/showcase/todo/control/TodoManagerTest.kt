package com.exxeta.showcase.todo.control

import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test
import javax.inject.Inject

@QuarkusTest
internal class TodoManagerTest {
    
    @Inject
    private lateinit var todoManager: TodoManager
    
    @InjectMock
    private lateinit var todoRepository: TodoRepository
    
    @InjectMock
    private lateinit var todoMapper: TodoMapper

    @Test
    fun getAll() {
    }

    @Test
    fun getTodoById() {
    }

    @Test
    fun deleteTodoById() {
    }

    @Test
    fun createTodo() {
    }

    @Test
    fun updateTodo() {
    }
}
