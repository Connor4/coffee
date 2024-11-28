package com.inno.coffee

import java.util.Locale

fun main() {
//    val zdt = LocalDateTime.now()
//    println("d : $zdt")
//    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//    println(formatter.format(zdt));
//
//    var zhFormatter = DateTimeFormatter.ofPattern("yyyy MMM dd EE HH:mm", Locale.CHINA);
//    println(zhFormatter.format(zdt));
//
//    var usFormatter = DateTimeFormatter.ofPattern("E, MMMM/dd/yyyy HH:mm", Locale.US);
//    println(usFormatter.format(zdt));

    val chinese = Locale.TRADITIONAL_CHINESE
    val tag = Locale.forLanguageTag(chinese.language)
    println("chinese : $chinese language ${chinese.language} tag $tag")
}




