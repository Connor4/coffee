package com.inno.coffee

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main() {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
    val dateTime = LocalDateTime.now()
    val formattedDateTime = dateTime.format(formatter)
    println(formattedDateTime)

}