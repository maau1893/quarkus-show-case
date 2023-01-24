package com.exxeta.showcase.common.control

import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.groups.UniOnNull
import org.slf4j.Logger

fun <T> UniOnNull<T>.logAndFailWith(logger: Logger, throwable: Throwable): Uni<T> {
    return this.failWith {
        logger.error(throwable.message)
        throwable
    }
}
