package com.example.tangosongs

import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer

@ActiveProfiles("test")
@SpringBootTest
abstract class AbstractTest {

    companion object {

        private var postgreSQLContainer = PostgreSQLContainer("postgres:15-alpine")

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            postgreSQLContainer.start()
        }

        @DynamicPropertySource
        @JvmStatic
        fun propertiesSetting(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { postgreSQLContainer.jdbcUrl }
            registry.add("spring.datasource.password") { postgreSQLContainer.password }
            registry.add("spring.datasource.username") { postgreSQLContainer.username }
        }

    }
}