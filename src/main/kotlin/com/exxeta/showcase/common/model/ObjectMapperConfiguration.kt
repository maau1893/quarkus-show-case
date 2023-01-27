package com.exxeta.showcase.common.model

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.quarkus.jackson.ObjectMapperCustomizer
import javax.enterprise.inject.Instance
import javax.enterprise.inject.Produces
import javax.inject.Singleton

class ObjectMapperConfiguration {

    @Produces
    @Singleton
    fun provideObjectMapper(customizers: Instance<ObjectMapperCustomizer>): ObjectMapper {
        return jacksonObjectMapper().registerModule(JavaTimeModule())
            .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            .also { mapper ->
                customizers.forEach { customizer ->
                    customizer.customize(mapper)
                }
            }
    }
}
