package com.exxeta.showcase.common.model

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.enterprise.context.Dependent
import javax.enterprise.inject.Produces
import javax.enterprise.inject.spi.InjectionPoint

@Dependent
class LoggerConfiguration {

    @Produces
    fun createLogger(point: InjectionPoint): Logger {
        return LoggerFactory.getLogger(point.member.declaringClass)
    }
}
