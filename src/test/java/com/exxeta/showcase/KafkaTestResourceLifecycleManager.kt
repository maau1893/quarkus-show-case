package com.exxeta.showcase

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import io.smallrye.reactive.messaging.providers.connectors.InMemoryConnector

class KafkaTestResourceLifecycleManager : QuarkusTestResourceLifecycleManager {

    override fun start(): MutableMap<String, String> {
        val variables = mutableMapOf<String, String>()
        variables.putAll(InMemoryConnector.switchOutgoingChannelsToInMemory("show-case-to-dummy"))
        return variables
    }

    override fun stop() {
        InMemoryConnector.clear()
    }
}
