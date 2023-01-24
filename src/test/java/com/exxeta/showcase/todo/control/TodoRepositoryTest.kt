package com.exxeta.showcase.todo.control

import com.exxeta.showcase.todo.model.Todo
import io.quarkus.test.TestReactiveTransaction
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.vertx.UniAsserter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.inject.Inject

@QuarkusTest
class TodoRepositoryTest {

    @Inject
    private lateinit var todoRepository: TodoRepository

    @Test
    @TestReactiveTransaction
    fun find(asserter: UniAsserter) {
        asserter.assertThat({
            todoRepository.persist(Todo().apply { this.description = "Hello World" })
                .chain { res -> todoRepository.findById(res.id) }
        }) { result: Todo ->
            Assertions.assertEquals("Hello World", result.description)
            Assertions.assertEquals(false, result.isDone)
        }
    }

    @Test
    @TestReactiveTransaction
    fun findAllSize1(asserter: UniAsserter) {
        asserter.assertThat({
            todoRepository.persist(Todo().apply { this.description = "Hello World" }).replaceWith(todoRepository.listAll())
        }) { result ->
            Assertions.assertEquals(1, result.size)
        }
    }

    @Test
    @TestReactiveTransaction
    fun findAllSize1Again(asserter: UniAsserter) {
        asserter.assertThat({
            todoRepository.persist(Todo().apply { this.description = "Hello World" }).replaceWith(todoRepository.listAll())
        }) { result ->
            Assertions.assertEquals(1, result.size)
        }
    }
}