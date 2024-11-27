package com.inno.coffee

fun main() {
    // Set time
    val hour = 15 // 2:00 PM
    val minute = 30

    // Encode time
    val timeValue = encodeTime(hour, minute)
    println("Encoded time: $timeValue") // Example output: 3633

    // Decode time
    val (decodedHour, decodedMinute) = decodeTime(timeValue)
    println("Decoded time: $decodedHour:$decodedMinute") // Example output: 14:45
}

fun encodeTime(hour: Int, minute: Int): Int {
    require(hour in 0..23) { "Hour must be between 0 and 23" }
    require(minute in 0..59) { "Minute must be between 0 and 59" }
    return (hour shl 8) or minute
}

fun decodeTime(timeValue: Int): Pair<Int, Int> {
    val hour = (timeValue shr 8) and 0xFF
    val minute = timeValue and 0xFF
    return Pair(hour, minute)
}



