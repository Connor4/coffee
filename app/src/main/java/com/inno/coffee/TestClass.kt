package com.inno.coffee

fun main() {
//    val zdt = LocalDateTime.now()
//    println("d : $zdt")
//    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//    println(formatter.format(zdt))
//
//    var zhFormatter = DateTimeFormatter.ofPattern("yyyy MMM dd EE HH:mm", Locale.CHINA);
//    println(zhFormatter.format(zdt));
//
//    var usFormatter = DateTimeFormatter.ofPattern("E, MMMM/dd/yyyy HH:mm", Locale.US);
//    println(usFormatter.format(zdt));

//    val time = LocalDateTime.parse("2024-01-01T00:00")
//    println("time : $time")
//    val epochMilli = time.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
//    val now = System.currentTimeMillis()
//    println("milli : $epochMilli  now $now")

    val time = 100
    val min = time / 60
    val second = time % 60
    println("time $time min $min second $second")
}




