package com.inno.coffee

import java.util.Locale

fun main() {
    val name = Locale("zh").getDisplayName(Locale.US)
    println(name)
}