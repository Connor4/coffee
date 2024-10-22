package com.inno.coffee

import java.util.Locale

fun main() {
    val forLanguageTag = Locale.forLanguageTag("en")
    println("for ${forLanguageTag == Locale.ENGLISH}")
}