package com.exxeta.showcase.todo.control

import com.exxeta.showcase.todo.model.Todo
import io.quarkus.test.TestReactiveTransaction
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.vertx.UniAsserter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import java.util.*
import javax.inject.Inject

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class TodoRepositoryTest {

    @Inject
    private lateinit var todoRepository: TodoRepository

    @TestReactiveTransaction
    @Test
    @Order(0)
    fun find(asserter: UniAsserter) {
        asserter.assertThat({
            todoRepository.persist(Todo().also { it.description = "Hello World" })
                .chain { res -> todoRepository.findById(res.id) }
        }) { result: Todo ->
            Assertions.assertEquals("Hello World", result.description)
            Assertions.assertEquals(false, result.isDone)
        }

    }

    @TestReactiveTransaction
    @Test
    @Order(1)
    fun findAllSize1(asserter: UniAsserter) {
        asserter.assertThat({
            todoRepository.persist(Todo().also { it.description = "Hello World" }).replaceWith(todoRepository.listAll())
        }) { result ->
            Assertions.assertEquals(1, result.size)
        }
    }

    @TestReactiveTransaction
    @Test
    @Order(2)
    fun findAllSize1Again(asserter: UniAsserter) {
        asserter.assertThat({
            todoRepository.persist(Todo().also { it.description = "Hello World" }).replaceWith(todoRepository.listAll())
        }) { result ->
            Assertions.assertEquals(1, result.size)
        }
    }
}