package com.splitmoney

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.TimeZone

@SpringBootApplication
open class SplitMoneyApplication

fun main(args: Array<String>) {
	// 1. Force the timezone FIRST
	TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"))

	// 2. THEN start the Spring Boot application
	runApplication<SplitMoneyApplication>(*args)
}