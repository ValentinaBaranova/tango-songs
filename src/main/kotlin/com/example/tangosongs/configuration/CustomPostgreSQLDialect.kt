package com.example.tangosongs.configuration

import org.hibernate.boot.model.FunctionContributions
import org.hibernate.dialect.PostgreSQLDialect


class CustomPostgreSQLDialect: PostgreSQLDialect() {

    override fun initializeFunctionRegistry(functionContributions: FunctionContributions) {
        super.initializeFunctionRegistry(functionContributions)
        val functionRegistry = functionContributions.functionRegistry
        functionRegistry.registerPattern(
            "tsvector_match",
            "(?1 @@ ?2)"
        )
    }
}