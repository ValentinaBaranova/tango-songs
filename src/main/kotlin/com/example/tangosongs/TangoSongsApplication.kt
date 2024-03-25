package com.example.tangosongs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@ConfigurationPropertiesScan(basePackages = ["com.example"])
@SpringBootApplication
class TangoSongsApplication

fun main(args: Array<String>) {
	runApplication<TangoSongsApplication>(*args)
}
