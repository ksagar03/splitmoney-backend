package com.splitmoney

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SplitMoneyApplication

fun main(args: Array<String>) {
	runApplication<SplitMoneyApplication>(*args)
}
