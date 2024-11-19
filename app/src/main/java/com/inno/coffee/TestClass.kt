package com.inno.coffee

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main() {
    val localDateTime = LocalDateTime.now()
    val parse = LocalDateTime.parse(localDateTime.toString())
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    // Format the LocalDateTime
    val formattedDateTime = parse.format(formatter)
    println("localDateTime = $localDateTime, parse = $formattedDateTime")
}
