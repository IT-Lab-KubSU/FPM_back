package ru.kubsu.fktpm.fpmbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FpmBackendApplication

fun main(args: Array<String>) {
	runApplication<FpmBackendApplication>(*args)
}
