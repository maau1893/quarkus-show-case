package com.exxeta.showcase.common.model

object ApiDescriptions {

    const val GET_TODOS = "Gets all Todos present in the database."
    const val GET_TODO = "Gets the Todo for the given id."
    const val DELETE_TODO = "Deletes the Todo for the given id."
    const val PUT_TODO = "Creates a new Todo and returns the database entity, including id and timestamps."
    const val POST_TODO = "Updates a Todo for the given id and request body."

    const val POST_FROM_DUMMY = "Simply logs the received message from the dummy service."
    const val POST_TO_DUMMY = "Sends the received message either via Kafka or REST to the dummy service."
}
